package tech.nocountry.talent.appbitservice.userandroles.infrastructure.hashing.bcrypt;

import org.springframework.security.crypto.password.PasswordEncoder;
import tech.nocountry.talent.appbitservice.userandroles.application.internal.outboundservices.hashing.HashingService;

/** Interfaz de servicio de hashing BCrypt. */
public interface BCryptHashingService extends HashingService, PasswordEncoder { }

