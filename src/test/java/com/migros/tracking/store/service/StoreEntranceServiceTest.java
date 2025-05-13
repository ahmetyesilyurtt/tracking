package com.migros.tracking.store.service;

import com.migros.tracking.common.util.DateUtils;
import com.migros.tracking.courier.entity.Courier;
import com.migros.tracking.store.entity.Store;
import com.migros.tracking.store.entity.StoreEntrance;
import com.migros.tracking.store.mapper.StoreEntranceMapper;
import com.migros.tracking.store.repository.StoreEntranceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StoreEntranceServiceTest {

    @InjectMocks
    private StoreEntranceService storeEntranceService;

    @Mock
    private StoreEntranceRepository storeEntranceRepository;

    @Mock
    private StoreEntranceMapper storeEntranceMapper;

    @Test
    void shouldSaveEntrance() {
        //given
        var storeReference = Store.builder().id(1L).build();
        var courierReference = Courier.builder().id(1L).build();
        var entranceTime = DateUtils.getCurrentInstant();
        var totalDistance = 100d;
        var storeEntrance = StoreEntrance.builder()
                                         .store(storeReference)
                                         .courier(courierReference)
                                         .entranceTime(entranceTime)
                                         .totalTrip(totalDistance)
                                         .build();

        when(storeEntranceMapper.convert(storeReference, courierReference, entranceTime, totalDistance)).thenReturn(storeEntrance);

        //when
        storeEntranceService.saveEntrance(storeReference, courierReference, entranceTime, totalDistance);

        //then
        verify(storeEntranceMapper, times(1)).convert(storeReference, courierReference, entranceTime, totalDistance);
        verify(storeEntranceRepository, times(1)).save(storeEntrance);
    }

    @Test
    void shouldReturnNull_whenCourierDoesNotHaveEntrance() {
        //given
        var courierId = 1L;

        when(storeEntranceRepository.getLastEntranceTime(courierId, PageRequest.of(0, 1))).thenReturn(new ArrayList<>());

        //when
        var response = storeEntranceService.getLastEntranceTime(courierId);

        //then
        assertNull(response);
    }

    @Test
    void shouldReturnLastEntrance_whenCourierHasEntrance() {
        //given
        var courierId = 1L;
        var lastEntranceTime = DateUtils.getCurrentInstant();
        var pageRequest = PageRequest.of(0, 1);

        when(storeEntranceRepository.getLastEntranceTime(courierId, pageRequest)).thenReturn(List.of(lastEntranceTime));

        //when
        var response = storeEntranceService.getLastEntranceTime(courierId);

        //then
        verify(storeEntranceRepository, times(1)).getLastEntranceTime(courierId, pageRequest);
        assertThat(response).isEqualTo(lastEntranceTime);
    }

    @Test
    void shouldReturnCourierTotalDistance() {
        //given
        var courierId = 1L;
        var totalDistance = 100d;

        when(storeEntranceRepository.getTotalTravelDistanceByCourierId(courierId)).thenReturn(totalDistance);

        //when
        var response = storeEntranceService.getTotalTravelDistanceByCourierId(courierId);

        //then
        verify(storeEntranceRepository, times(1)).getTotalTravelDistanceByCourierId(courierId);
        assertThat(response).isEqualTo(totalDistance);
    }

}