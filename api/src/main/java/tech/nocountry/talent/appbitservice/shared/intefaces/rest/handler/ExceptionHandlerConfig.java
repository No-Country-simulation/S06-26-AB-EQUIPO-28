package tech.nocountry.talent.appbitservice.shared.intefaces.rest.handler;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import tech.nocountry.talent.appbitservice.shared.intefaces.rest.handler.handlers.*;
import tech.nocountry.talent.appbitservice.shared.intefaces.rest.handler.helpers.FieldErrorFormatter;

/**
 * Configuracion que importa todos los manejadores de excepciones.
 * Usar en tests con @Import(ExceptionHandlerConfig.class).
 */
@Configuration
@Import({
        FieldErrorFormatter.class,
        BaseExceptionHandler.class,
        ValidationExceptionHandler.class,
        DatabaseExceptionHandler.class,
        AuthenticationExceptionHandler.class,
        GenericExceptionHandler.class
})
public class ExceptionHandlerConfig { }
