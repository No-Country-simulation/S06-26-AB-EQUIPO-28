package tech.nocountry.talent.appbitservice.shared.domain.model.valueobjects;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Utilidad para generar UUIDs version 7 (time-ordered).
 *
 * <p>UUID v7 combina un timestamp Unix de 48 bits con bits aleatorios,
 * proporcionando IDs ordenados cronológicamente que son ideales para
 * claves primarias de base de datos.</p>
 *
 * <p>Implementación compatible con Java 21 (UUID v7 es nativo en Java 22+).</p>
 *
 * @author GastroSuite
 * @version 1.0.0
 * @since 2026-04-07
 */
public final class UuidV7Generator {
    private UuidV7Generator() {
        throw new UnsupportedOperationException("Clase de utilidad, no instanciable");
    }
    public static UUID generate() { return generate(Instant.now()); }

    public static UUID generate(Instant instant) {
        long timestampMs = instant.toEpochMilli();
        long randomA = ThreadLocalRandom.current().nextLong() & 0xFFFL; //12 bits
        long randomB = ThreadLocalRandom.current().nextLong();          //64 bits para el siguiente bloque

        // Bits 0-47:  Timestamp (48 bits)
        // Bits 48-51: Version (4 bits, valor 7 = 0x7000)
        // Bits 52-63: Random A (12 bits)
        long mostSigBits = (timestampMs << 16) | 0x7000L | randomA;

        // Bits 64-65: Variant (2 bits, valor 10 = 0x8000...)
        // Bits 66-127: Random B (62 bits)
        long leastSigBits = (randomB & 0x3FFFFFFFFFFFFFFFL) | 0x8000000000000000L;
        return new UUID(mostSigBits, leastSigBits);
    }
}