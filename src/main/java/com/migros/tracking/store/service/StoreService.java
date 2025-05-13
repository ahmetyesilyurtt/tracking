package com.migros.tracking.store.service;

import com.migros.tracking.courier.dto.LocationRequest;
import com.migros.tracking.store.dto.StoreDto;
import com.migros.tracking.store.entity.Store;
import com.migros.tracking.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    public StoreDto findNearestStoreWithinRadiusByTime(LocationRequest event, Double radius, Instant limitTime) {
        return storeRepository.findNearestStoreWithinRadiusByTime(event.latitude(), event.longitude(), radius, event.courierId(), limitTime);
    }

    public Store getReferenceById(Long id) {
        return storeRepository.getReferenceById(id);
    }
}
