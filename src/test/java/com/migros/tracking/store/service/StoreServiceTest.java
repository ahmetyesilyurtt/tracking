package com.migros.tracking.store.service;

import com.migros.tracking.common.util.DateUtils;
import com.migros.tracking.courier.dto.LocationRequest;
import com.migros.tracking.store.dto.StoreDto;
import com.migros.tracking.store.entity.Store;
import com.migros.tracking.store.repository.StoreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    @InjectMocks
    private StoreService storeService;

    @Mock
    private StoreRepository storeRepository;

    @Test
    void shouldReturnNearestStoreIfWithinRadius() {
        //given
        var request = new LocationRequest(DateUtils.getCurrentInstant(), 1L, 1.0d, 1.0d);
        var radius = 100d;
        var limitTime = DateUtils.getCurrentInstant();
        var storeDto = new StoreDto() {
            @Override
            public Long getId() {
                return 1L;
            }

            @Override
            public Double getDistanceToStore() {
                return 10.0d;
            }

            @Override
            public String getStoreName() {
                return "Ortaköy MMM Migros";
            }
        };

        when(storeRepository.findNearestStoreWithinRadiusByTime(request.latitude(), request.longitude(), radius, request.courierId(),
                                                                limitTime)).thenReturn(storeDto);

        //when
        var response = storeService.findNearestStoreWithinRadiusByTime(request, radius, limitTime);

        //then
        verify(storeRepository, times(1)).findNearestStoreWithinRadiusByTime(request.latitude(), request.longitude(), radius, request.courierId(),
                                                                             limitTime);
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getDistanceToStore()).isEqualTo(10.0d);
    }

    @Test
    void shouldReturnStoreReferenceById() {
        //given
        var storeId = 1L;
        var store = Store.builder().id(storeId).build();

        when(storeRepository.getReferenceById(storeId)).thenReturn(store);

        //when
        var response = storeService.getReferenceById(storeId);

        //then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(storeId);
        verify(storeRepository, times(1)).getReferenceById(storeId);
    }

}