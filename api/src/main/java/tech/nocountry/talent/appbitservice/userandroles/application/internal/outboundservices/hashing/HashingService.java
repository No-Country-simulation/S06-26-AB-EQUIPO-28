package tech.nocountry.talent.appbitservice.userandroles.application.internal.outboundservices.hashing;

/** Interfaz del servicio de hashing de contraseñas. */
public interface HashingService {
    /** Codifica una contraseña encriptada. */
    String encode(CharSequence rawPassword);

    /** Verifica si una contraseña coincide con su versión encriptada. */
    boolean matches(CharSequence rawPassword, String encodedPassword);
}

