package com.migros.tracking.courier.mapper;

import com.migros.tracking.common.config.DefaultMapStructConfiguration;
import com.migros.tracking.courier.dto.CourierRequest;
import com.migros.tracking.courier.dto.LocationRequest;
import com.migros.tracking.courier.entity.Courier;
import com.migros.tracking.courier.entity.CourierLocation;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = DefaultMapStructConfiguration.class, builder = @Builder(disableBuilder = true))
public interface CourierMapper {

    Courier convert(CourierRequest courierRequest);

    @Mapping(target = "courier", source = "courier")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    CourierLocation convert(LocationRequest locationRequest, Courier courier);
}
