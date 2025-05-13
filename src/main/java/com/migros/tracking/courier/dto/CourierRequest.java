package com.migros.tracking.courier.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public record CourierRequest(@Schema(name = "fullName", example = "Ahmet Yeşilyurt") @NotBlank String fullName,
                             @Schema(name = "identityNumber", example = "40236654820") String identityNumber) implements Serializable {

}
