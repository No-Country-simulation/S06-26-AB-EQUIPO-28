package tech.nocountry.talent.appbitservice.aiassistant.infrastructure.ratelimit;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Bucket4j configuration for rate limiting.
 * Limits requests per IP address for the AI Assistant endpoints.
 */
@Configuration
@Slf4j
public class Bucket4jConfig {

    // Default: 10 requests per minute
    private static final int DEFAULT_CAPACITY = 10;
    private static final Duration DEFAULT_REFILL_DURATION = Duration.ofMinutes(1);
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    /**
     * Creates a rate limiting bucket for a given key (IP address or API key).
     *
     * @param key the identifier for rate limiting
     * @return the bucket
     */
    public Bucket resolveBucket(String key) {
        return buckets.computeIfAbsent(key, this::createBucket);
    }

    private Bucket createBucket(String key) {
        Bandwidth limit = Bandwidth.builder()
                .capacity(DEFAULT_CAPACITY)
                .refillGreedy(DEFAULT_CAPACITY, DEFAULT_REFILL_DURATION)
                .build();
        return Bucket.builder()
                .addLimit(limit)
                .build();
    }

    /**
     * Filter registration bean for rate limiting.
     */
    @Bean
    @SuppressWarnings("unchecked")
    public FilterRegistrationBean rateLimitFilter() {
        FilterRegistrationBean<OncePerRequestFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RateLimitFilter(this));
        registrationBean.addUrlPatterns("/api/v1/assistant/*");
        registrationBean.setOrder(1);
        registrationBean.setName("rateLimitFilter");
        return registrationBean;
    }

    /**
     * Rate limiting filter using Bucket4j.
     */
    private static class RateLimitFilter extends OncePerRequestFilter {
        private final Bucket4jConfig config;
        RateLimitFilter(Bucket4jConfig config) {
            this.config = config;
        }

        @Override
        protected void doFilterInternal(
                HttpServletRequest request,
                HttpServletResponse response,
                FilterChain filterChain
        ) throws ServletException, IOException {

            String clientKey = getClientKey(request);
            Bucket bucket = config.resolveBucket(clientKey);

            if (bucket.tryConsume(1)) {
                response.setHeader("X-Rate-Limit-Remaining",
                        String.valueOf(bucket.getAvailableTokens()));
                filterChain.doFilter(request, response);
            } else {
                log.debug("Rate limit exceeded for client: {}", clientKey);
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.setContentType("application/problem+json");
                response.getWriter().write("""
                        {
                            "type": "https://httpstatuses.com/429",
                            "title": "Too Many Requests",
                            "status": 429,
                            "detail": "Rate limit exceeded. Maximum 10 requests per minute."
                        }
                        """);
            }
        }

        private String getClientKey(HttpServletRequest request) {
            String apiKey = request.getHeader("X-API-Key");
            if (apiKey != null && !apiKey.isBlank()) {
                return "api-key:" + apiKey;
            }

            String xForwardedFor = request.getHeader("X-Forwarded-For");
            if (xForwardedFor != null && !xForwardedFor.isBlank()) {
                return "ip:" + xForwardedFor.split(",")[0].trim();
            }

            return "ip:" + request.getRemoteAddr();
        }
    }
}
