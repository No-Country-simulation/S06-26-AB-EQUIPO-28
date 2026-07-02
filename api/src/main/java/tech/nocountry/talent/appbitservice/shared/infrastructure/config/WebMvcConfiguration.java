package tech.nocountry.talent.appbitservice.shared.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC Configuration for Scalar and API Documentation.
 * Configures resource handlers for static content including API documentation UI.
 */
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Scalar UI resources
        registry.addResourceHandler("/scalar/**")
                .addResourceLocations(
                        "classpath:/META-INF/resources/scalar/",
                        "classpath:/static/scalar/",
                        "classpath:/public/scalar/"
                );

        // WebJars resources for Scalar
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}