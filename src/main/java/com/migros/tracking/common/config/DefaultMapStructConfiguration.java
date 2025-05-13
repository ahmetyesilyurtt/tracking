package com.migros.tracking.common.config;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.MapperConfig;
import org.mapstruct.ReportingPolicy;

import static org.mapstruct.MappingConstants.ComponentModel;

@MapperConfig(componentModel = ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR,
              unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DefaultMapStructConfiguration {

}
