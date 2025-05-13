package com.migros.tracking.store.service;

import com.migros.tracking.courier.entity.Courier;
import com.migros.tracking.store.entity.Store;
import com.migros.tracking.store.mapper.StoreEntranceMapper;
import com.migros.tracking.store.repository.StoreEntranceRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class StoreEntranceService {

    private final StoreEntranceRepository storeEntranceRepository;

    private final StoreEntranceMapper storeEntranceMapper;

    @Transactional
    public void saveEntrance(Store storeReference, Courier courierReference, Instant entranceTime, Double totalDistance) {
        var storeEntrance = storeEntranceMapper.convert(storeReference, courierReference, entranceTime, totalDistance);
        storeEntranceRepository.save(storeEntrance);
    }

    public Instant getLastEntranceTime(Long courierId) {
        var entrance = storeEntranceRepository.getLastEntranceTime(courierId, PageRequest.of(0, 1));
        if (CollectionUtils.isEmpty(entrance)) {
            return null;
        }
        return entrance.getFirst();
    }

    public double getTotalTravelDistanceByCourierId(Long courierId) {
        return storeEntranceRepository.getTotalTravelDistanceByCourierId(courierId);
    }
}
