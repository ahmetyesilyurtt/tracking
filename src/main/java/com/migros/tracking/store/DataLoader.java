package com.migros.tracking.store;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.migros.tracking.store.entity.Store;
import com.migros.tracking.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private static final String PATH = "/static/stores.json";

    private final StoreRepository storeRepository;

    private final ObjectMapper objectMapper;

    @Override
    public void run(String... args) {
        try (var inputStream = DataLoader.class.getResourceAsStream(PATH)) {
            var jsonStores = objectMapper.readValue(inputStream, new TypeReference<List<Store>>() {
            });
            List<Store> stores = new ArrayList<>();
            jsonStores.forEach(s -> {
                var optionalStore = storeRepository.getByName(s.getName());
                optionalStore.ifPresentOrElse((store -> {
                    store.setLatitude(s.getLatitude());
                    store.setLongitude(s.getLongitude());
                    stores.add(store);
                }), () -> stores.add(s));
            });
            storeRepository.saveAll(stores);
            log.info("Store Data created/updated successfully");
        } catch (Exception e) {
            log.error("Error while loading store data", e);
        }
    }
}
