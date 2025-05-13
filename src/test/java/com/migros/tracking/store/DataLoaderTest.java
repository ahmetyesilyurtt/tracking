package com.migros.tracking.store;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.migros.tracking.store.entity.Store;
import com.migros.tracking.store.repository.StoreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DataLoaderTest {

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private DataLoader dataLoader;

    @Captor
    private ArgumentCaptor<InputStream> inputStreamCaptor;

    @Captor
    private ArgumentCaptor<TypeReference<List<Store>>> typeReferenceArgumentCaptor;

    @Captor
    private ArgumentCaptor<List<Store>> storeListArgumentCaptor;

    @Test
    void shouldInitializeData() throws IOException {
        // given
        var firstStore = Store.builder().name("Ataşehir MMM Migros").latitude(40.9923307).longitude(29.1244229).build();
        var secondStore = Store.builder().id(2L).name("Novada MMM Migros").latitude(40.986106).longitude(29.1161293).build();
        var mockStores = List.of(firstStore, secondStore);

        when(objectMapper.readValue(inputStreamCaptor.capture(), typeReferenceArgumentCaptor.capture())).thenReturn(mockStores);
        when(storeRepository.getByName(firstStore.getName())).thenReturn(Optional.empty());
        when(storeRepository.getByName(secondStore.getName())).thenReturn(Optional.of(secondStore));

        //then
        assertDoesNotThrow(() -> dataLoader.run());

        verify(storeRepository, times(1)).saveAll(storeListArgumentCaptor.capture());
        assertThat(storeListArgumentCaptor.getValue()).hasSize(2);
    }
}