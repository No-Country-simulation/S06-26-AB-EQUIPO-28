package tech.nocountry.talent.appbitservice.shared.infrastructure.documentation.scalar;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador que sirve la interfaz Scalar API Reference con configuración
 * personalizada vía CDN.
 * <p>
 * Reemplaza el starter {@code springdoc-openapi-starter-webmvc-scalar} porque
 * éste tiene un bug conocido (<a href=
 * "https://github.com/springdoc/springdoc-openapi/issues/3087">springdoc#3087</a>)
 * que impide pasar {@code customCss}, {@code theme} y otras propiedades a Scalar.
 * <p>
 * La versión de Scalar se actualiza verificando en
 * <a href="https://www.jsdelivr.com/package/npm/@scalar/api-reference">jsDelivr</a>
 * la última disponible.
 */
@Hidden
@RestController
public class ScalarDocumentationController {
    private static final String SCALAR_VERSION = "1.55.3";

    @GetMapping(value = "/scalar", produces = MediaType.TEXT_HTML_VALUE)
    public String scalarDocs() {
        return """
            <!doctype html>
            <html lang="es" class="dark-mode">
            <head>
                <meta charset="utf-8">
                <meta name="viewport" content="width=device-width, initial-scale=1">
                <title>APP BIT API</title>
                <link rel="icon" href="/favicon.svg">
            </head>
            <body>
                <div id="scalar-app"></div>

                <script src="https://cdn.jsdelivr.net/npm/@scalar/api-reference@%s/dist/browser/standalone.js"></script>
                <script>
                    Scalar.createApiReference('#scalar-app', {
                        url: '/v3/api-docs',
                        theme: 'default',
                        darkMode: true,
                        hideDarkModeToggle: true,
                        layout: 'modern',
                        hideModels: true,
                        showOperationId: true,
                        defaultHttpClient: {
                            targetKey: 'js',
                            clientKey: 'axios',
                        },
                        customCss: [
                            /* 1. Root container transparente para ver la nebulosa */
                            '.scalar-app {',
                            '  --scalar-background-1: transparent !important;',
                            '  background: transparent !important;',
                            '}',
                            'body, #scalar-app {',
                            '  background: transparent !important;',
                            '}',

                            /* 2. Paneles principales opacos (heredan --scalar-background-1:transparent del root) */
                            '.dark-mode .t-doc__sidebar,',
                            '.dark-mode .t-doc__content,',
                            '.dark-mode .t-doc__mobile-header,',
                            '.dark-mode .t-doc__address-bar,',
                            '.dark-mode .test-request,',
                            '.dark-mode .section.test-request,',
                            '.dark-mode .scalar-api-client,',
                            '.dark-mode .api-client-section,',
                            '.dark-mode .scalar-client {',
                            '  --scalar-background-1: #09090b !important;',
                            '  --scalar-background-2: #09090b !important;',
                            '  --scalar-background-3: #18181b !important;',
                            '  background: #09090b !important;',
                            '}',

                            /* 3. NEBULOSA en body */
                            'html.dark-mode body {',
                            '  background-color: #000000 !important;',
                            '  background-image:',
                            '    radial-gradient(circle at 50%% 0%%, rgba(228, 150, 255, 0.35) 0%%, transparent 75%%),',
                            '    linear-gradient(rgba(255, 255, 255, 0.03) 1px, transparent 1px),',
                            '    linear-gradient(90deg, rgba(255, 255, 255, 0.03) 1px, transparent 1px) !important;',
                            '  background-size: 100%% 100%%, 40px 40px, 40px 40px !important;',
                            '  background-attachment: fixed !important;',
                            '  animation: nebulaBreathing 8s ease-in-out infinite alternate !important;',
                            '  min-height: 100vh !important;',
                            '}',

                            '@keyframes nebulaBreathing {',
                            '  0%% { background-color: #000000; }',
                            '  100%% { background-color: #2d1b4e; }',
                            '}',

                            /* Sidebar highlight */
                            '.dark-mode .t-doc__sidebar .group\\\\/sidebar-section:has(.bg-sidebar-b-active) {',
                            '  border-left: 2px solid #E496FF;',
                            '  border-radius: 0 4px 4px 0;',
                            '  background: rgba(228, 150, 255, 0.05);',
                            '}',
                            '.dark-mode .t-doc__sidebar {',
                            '  --scalar-sidebar-item-active-background: rgba(228, 150, 255, 0.2);',
                            '  --scalar-sidebar-color-active: #E496FF;',
                            '  --scalar-sidebar-indent-border-active: #E496FF;',
                            '}',
                            '.dark-mode .t-doc__sidebar .bg-sidebar-indent-border-active {',
                            '  background-color: #E496FF;',
                            '  width: 2px;',
                            '}',
                        ].join('\\n'),
                    });
                </script>
            </body>
            </html>
            """.formatted(SCALAR_VERSION);
    }
}
