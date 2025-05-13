package com.migros.tracking.store.mapper;

import com.migros.tracking.common.config.DefaultMapStructConfiguration;
import com.migros.tracking.courier.entity.Courier;
import com.migros.tracking.store.entity.Store;
import com.migros.tracking.store.entity.StoreEntrance;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;

@Mapper(config = DefaultMapStructConfiguration.class, builder = @Builder(disableBuilder = true))
public interface StoreEntranceMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    StoreEntrance convert(Store store, Courier courier, Instant entranceTime, Double totalTrip);
}
