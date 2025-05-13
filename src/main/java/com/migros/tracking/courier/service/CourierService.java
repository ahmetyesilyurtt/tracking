package com.migros.tracking.courier.service;

import com.migros.tracking.courier.dto.*;
import com.migros.tracking.courier.entity.Courier;
import com.migros.tracking.courier.mapper.CourierMapper;
import com.migros.tracking.courier.repository.CourierLocationRepository;
import com.migros.tracking.courier.repository.CourierRepository;
import com.migros.tracking.store.service.StoreEntranceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourierService {

    private final CourierMapper courierMapper;

    private final CourierRepository courierRepository;

    private final CourierLocationRepository courierLocationRepository;

    private final StoreEntranceService storeEntranceService;

    private final ApplicationEventPublisher publisher;

    @Transactional
    public void saveOrUpdateCourier(CourierRequest courierRequest) {
        var optionalCourier = courierRepository.findCourierByIdentityNumber(courierRequest.identityNumber());
        optionalCourier.ifPresentOrElse(existingCourier -> existingCourier.setFullName(courierRequest.fullName()), () -> {
            var entity = courierMapper.convert(courierRequest);
            courierRepository.save(entity);
            log.info("Courier {}-{} saved", courierRequest.fullName(), courierRequest.identityNumber());
        });
    }

    @Transactional
    public void updateLocation(LocationRequest locationRequest) {
        var isCourierActive = isCourierActiveById(locationRequest.courierId());
        if (!isCourierActive) {
            log.info("Courier with id {} not found", locationRequest.courierId());
            return;
        }
        var courierReference = courierRepository.getReferenceById(locationRequest.courierId());
        var courierLocation = courierMapper.convert(locationRequest, courierReference);
        courierLocationRepository.save(courierLocation);
        publisher.publishEvent(locationRequest);
    }

    @Transactional(readOnly = true)
    public Page<CourierDto> getCouriers(int page, int size) {
        return courierRepository.getCouriers(PageRequest.of(page, size));
    }

    public CourierDistanceDto getTotalDistance(Long courierId) {
        double totalDistanceInMeters = storeEntranceService.getTotalTravelDistanceByCourierId(courierId);
        return new CourierDistanceDto(totalDistanceInMeters / 1000, totalDistanceInMeters);
    }

    public Courier getReferenceById(Long courierId) {
        return courierRepository.getReferenceById(courierId);
    }

    @Transactional(readOnly = true)
    public Page<CourierLocationDto> getLocationsGreaterThan(Long courierId, Instant lastEntranceTime, PageRequest pageRequest) {
        return courierRepository.getLocationsGreaterThan(courierId, lastEntranceTime, pageRequest);
    }

    private boolean isCourierActiveById(Long id) {
        return courierRepository.isCourierActiveByCourierId(id);
    }
}
