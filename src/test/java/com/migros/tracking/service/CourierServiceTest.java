package com.migros.tracking.service;

import com.migros.tracking.common.util.DateUtils;
import com.migros.tracking.courier.dto.CourierLocationDto;
import com.migros.tracking.courier.dto.CourierRequest;
import com.migros.tracking.courier.dto.LocationRequest;
import com.migros.tracking.courier.entity.Courier;
import com.migros.tracking.courier.entity.CourierLocation;
import com.migros.tracking.courier.mapper.CourierMapper;
import com.migros.tracking.courier.repository.CourierLocationRepository;
import com.migros.tracking.courier.repository.CourierRepository;
import com.migros.tracking.courier.service.CourierService;
import com.migros.tracking.store.service.StoreEntranceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourierServiceTest {

    @InjectMocks
    private CourierService courierService;

    @Mock
    private CourierMapper courierMapper;

    @Mock
    private CourierRepository courierRepository;

    @Mock
    private CourierLocationRepository courierLocationRepository;

    @Mock
    private StoreEntranceService storeEntranceService;

    @Mock
    private ApplicationEventPublisher publisher;

    @Test
    void shouldSaveCourier_whenCourierDoesNotExist() {
        //given
        var request = new CourierRequest("Ahmet Yeşilyurt", "1111111111");
        var courier = Courier.builder().fullName(request.fullName()).identityNumber(request.identityNumber()).build();

        when(courierRepository.findCourierByIdentityNumber(request.identityNumber())).thenReturn(Optional.empty());
        when(courierMapper.convert(request)).thenReturn(courier);

        //when
        courierService.saveOrUpdateCourier(request);

        //then
        verify(courierRepository, times(1)).findCourierByIdentityNumber(request.identityNumber());
        verify(courierMapper, times(1)).convert(request);
        verify(courierRepository, times(1)).save(courier);
    }

    @Test
    void shouldUpdateCourier_whenCourierExists() {
        //given
        var request = new CourierRequest("Ahmet Yeşilyurt", "1111111111");

        var courier = Courier.builder().id(1L).fullName("Ahmet Yeşilyurt").identityNumber(request.identityNumber()).build();

        when(courierRepository.findCourierByIdentityNumber(request.identityNumber())).thenReturn(Optional.of(courier));

        //when
        courierService.saveOrUpdateCourier(request);

        //then
        verify(courierRepository, times(1)).findCourierByIdentityNumber(request.identityNumber());
        verify(courierMapper, never()).convert(request);
        assertThat(courier.getFullName()).isEqualTo(request.fullName());
    }

    @Test
    void shouldNotSaveLocation_whenCourierDeletedOrDoesNotExist() {
        //given
        var request = new LocationRequest(DateUtils.getCurrentInstant(), 1L, 1.0d, 1.0d);

        when(courierRepository.isCourierActiveByCourierId(request.courierId())).thenReturn(false);

        //when
        courierService.updateLocation(request);

        //then
        verify(courierRepository, times(1)).isCourierActiveByCourierId(request.courierId());
        verify(courierRepository, never()).getReferenceById(request.courierId());
        verify(publisher, never()).publishEvent(request);
    }

    @Test
    void shouldSaveLocation_whenCourierExist() {
        //given
        var request = new LocationRequest(DateUtils.getCurrentInstant(), 1L, 1.0d, 1.0d);
        var courierReference = Courier.builder().id(1L).build();
        var courierLocation = CourierLocation.builder()
                                             .courier(courierReference)
                                             .time(request.time())
                                             .latitude(request.latitude())
                                             .longitude(request.longitude())
                                             .build();

        when(courierRepository.isCourierActiveByCourierId(request.courierId())).thenReturn(true);
        when(courierRepository.getReferenceById(request.courierId())).thenReturn(courierReference);
        when(courierMapper.convert(request, courierReference)).thenReturn(courierLocation);

        //when
        courierService.updateLocation(request);

        //then
        verify(courierRepository, times(1)).isCourierActiveByCourierId(request.courierId());
        verify(courierRepository, times(1)).getReferenceById(request.courierId());
        verify(courierMapper, times(1)).convert(request, courierReference);
        verify(publisher, times(1)).publishEvent(request);
        verify(courierLocationRepository, times(1)).save(courierLocation);
    }

    @Test
    void shouldGetCouriers() {
        //given
        var page = 0;
        var size = 30;

        //when
        courierService.getCouriers(page, size);

        //then
        verify(courierRepository, times(1)).getCouriers(PageRequest.of(page, size));
    }

    @Test
    void shouldGetTotalDistance() {
        //given
        var courierId = 1L;
        var totalDistanceInMeter = 1000d;
        var totalDistanceInKiloMeter = 1d;

        when(storeEntranceService.getTotalTravelDistanceByCourierId(courierId)).thenReturn(totalDistanceInMeter);

        //when
        var courierDistanceDto = courierService.getTotalDistance(courierId);

        //then
        verify(storeEntranceService, times(1)).getTotalTravelDistanceByCourierId(courierId);
        assertThat(courierDistanceDto.totalInMeters()).isEqualTo(totalDistanceInMeter);
        assertThat(courierDistanceDto.totalInKilometers()).isEqualTo(totalDistanceInKiloMeter);
    }

    @Test
    void shouldReturnCourierReferenceById() {
        //given
        var courierReference = Courier.builder().id(1L).build();

        when(courierRepository.getReferenceById(1L)).thenReturn(courierReference);

        //when
        var response = courierService.getReferenceById(1L);

        //then
        verify(courierRepository, times(1)).getReferenceById(1L);
        assertThat(response).isEqualTo(courierReference);
    }

    @Test
    void shouldGetLocations() {
        //given
        var courierId = 1L;
        var lastEntranceTime = DateUtils.getCurrentInstant();
        var pageRequest = PageRequest.of(0, 30);
        var pageResponse = new PageImpl<CourierLocationDto>(new ArrayList<>());

        when(courierRepository.getLocationsGreaterThan(courierId, lastEntranceTime, pageRequest)).thenReturn(pageResponse);

        //when
        var response = courierService.getLocationsGreaterThan(courierId, lastEntranceTime, pageRequest);

        //then
        verify(courierRepository, times(1)).getLocationsGreaterThan(courierId, lastEntranceTime, pageRequest);
        assertThat(response).isEqualTo(pageResponse);

    }
}