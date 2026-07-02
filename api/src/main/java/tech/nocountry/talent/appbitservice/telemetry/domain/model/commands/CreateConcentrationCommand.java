package tech.nocountry.talent.appbitservice.telemetry.domain.model.commands;

import tech.nocountry.talent.appbitservice.telemetry.domain.model.valueobjects.ConcentrationMetrics;
import tech.nocountry.talent.appbitservice.telemetry.domain.model.valueobjects.Ecgi;
import tech.nocountry.talent.appbitservice.telemetry.domain.model.valueobjects.SessionPeriod;

import java.time.LocalDate;

/**
 * Comando para crear una nueva métrica de concentración de red.
 *
 * <p>Este comando encapsula todos los parámetros necesarios para crear
 * una concentración de red, evitando el anti-patrón de constructor con muchos parámetros.</p>
 *
 * <p>Anteriormente conocido como NetworkConcentracion con 14 parámetros.</p>
 *
 * @param ecgi identificador de la celda
 * @param dayDate fecha del día
 * @param period período de la sesión
 * @param userCount número de usuarios únicos
 * @param sessionCount número de sesiones
 * @param downloadBytes bytes descargados
 * @param uploadBytes bytes subidos
 * @param averageDurationS duración promedio en segundos
 * @param concentrationMetrics métricas de calidad de red
 * @param totalCalls total de llamadas
 * @param totalMessages total de mensajes
 * @param latitude latitud geográfica
 * @param longitude longitud geográfica
 */
public record CreateConcentrationCommand(
        Ecgi ecgi,
        LocalDate dayDate,
        SessionPeriod period,
        Integer userCount,
        Integer sessionCount,
        Long downloadBytes,
        Long uploadBytes,
        Double averageDurationS,
        ConcentrationMetrics concentrationMetrics,
        Long totalCalls,
        Long totalMessages,
        Double latitude,
        Double longitude
) { }