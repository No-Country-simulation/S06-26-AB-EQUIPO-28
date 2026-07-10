import type {
  MobilityODPairDto,
  TravelTimeDto,
  EmployabilityGapDto,
} from "@/shared/api/index.ts";
import type {
  MobilityODPair,
  TravelTime,
  EmployabilityGap,
  EmployabilityGapSeverity,
} from "../model/types.ts";

/** Maps a backend MobilityODPairDto to our domain MobilityODPair. */
export function toOdPair(dto: MobilityODPairDto): MobilityODPair {
  return {
    originCluster: dto.originCluster,
    originMunicipio: dto.originMunicipio ?? "",
    originLatitude: dto.originLatitude,
    originLongitude: dto.originLongitude,
    destinationCluster: dto.destinationCluster,
    destinationMunicipio: dto.destinationMunicipio ?? "",
    destinationLatitude: dto.destinationLatitude,
    destinationLongitude: dto.destinationLongitude,
    sameCluster: dto.sameCluster,
    uniqueUsers: dto.uniqueUsers ?? 0,
    totalTrips: dto.totalTrips ?? 0,
    averageDistanceKm: dto.averageDistanceKm ?? 0,
    predominantPeriod: dto.predominantPeriod ?? "",
  };
}

/** Maps a backend TravelTimeDto to our domain TravelTime. */
export function toTravelTime(dto: TravelTimeDto): TravelTime {
  return {
    originCluster: dto.originCluster,
    destinationCluster: dto.destinationCluster,
    sameCluster: dto.sameCluster,
    observations: dto.observations ?? 0,
    averageDistanceKm: dto.averageDistanceKm ?? 0,
    p25DistanceKm: dto.p25DistanceKm ?? null,
    p75DistanceKm: dto.p75DistanceKm ?? null,
    predominantPeriod: dto.predominantPeriod ?? "",
  };
}

/** Maps a backend EmployabilityGapDto to our domain EmployabilityGap. */
export function toEmployabilityGap(dto: EmployabilityGapDto): EmployabilityGap {
  return {
    cluster: dto.cluster,
    municipalities: dto.municipalities ?? [],
    citizenCount: dto.citizenCount ?? 0,
    incomeDCount: dto.incomeDCount ?? 0,
    incomeCCount: dto.incomeCCount ?? 0,
    youthCount18_24: dto.youthCount18_24 ?? 0,
    hasTelemetryCoverage: dto.hasTelemetryCoverage,
    daytimeAvgUsers: dto.daytimeAvgUsers ?? 0,
    outboundTripsToHubs: dto.outboundTripsToHubs ?? 0,
    distanceToNearestHubKm: dto.distanceToNearestHubKm ?? 0,
    mobilityIntensity: dto.mobilityIntensity ?? "",
    gapSeverity: normalizeGapSeverity(dto.gapSeverity),
    gapScore: dto.gapScore ?? 0,
    primaryFactors: dto.primaryFactors ?? [],
  };
}

function normalizeGapSeverity(
  value: string | undefined,
): EmployabilityGapSeverity {
  switch (value) {
    case "CRITICAL":
      return "CRITICAL";
    case "HIGH":
      return "HIGH";
    case "MODERATE":
      return "MODERATE";
    case "LOW":
      return "LOW";
    case "NONE":
      return "NONE";
    default:
      return "NONE";
  }
}
