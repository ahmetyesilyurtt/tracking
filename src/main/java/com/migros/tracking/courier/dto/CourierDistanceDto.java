package com.migros.tracking.courier.dto;

import java.io.Serializable;

public record CourierDistanceDto(double totalInKilometers, double totalInMeters) implements Serializable {

}
