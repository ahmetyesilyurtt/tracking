package com.migros.tracking.common.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorDetailDtoHolder implements Serializable {

    private String message;

    private List<ErrorDetailDto> details;
}
