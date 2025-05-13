package com.migros.tracking.common.service;

@FunctionalInterface
public interface DistanceCalculationService {

    Double getTotalTravelDistance(double lat1, double lon1, double lat2, double lon2);

}
