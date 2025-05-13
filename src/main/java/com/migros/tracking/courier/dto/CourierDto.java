package com.migros.tracking.courier.dto;

import java.io.Serializable;

public record CourierDto(Long id, String fullName, String identityNumber) implements Serializable {


}
