package com.migros.tracking.courier.controller;

import com.migros.tracking.courier.dto.CourierDistanceDto;
import com.migros.tracking.courier.dto.CourierDto;
import com.migros.tracking.courier.dto.CourierRequest;
import com.migros.tracking.courier.dto.LocationRequest;
import com.migros.tracking.courier.service.CourierService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/v1/couriers", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Courier")
public class CourierController {

    private final CourierService courierService;

    @GetMapping
    public Page<CourierDto> getCouriers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "30") int size) {
        return courierService.getCouriers(page, size);
    }

    @PostMapping
    public ResponseEntity<Void> saveCourier(@RequestBody @Valid CourierRequest courierRequest) {
        courierService.saveOrUpdateCourier(courierRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/location")
    public ResponseEntity<Void> updateLocation(@Valid @RequestBody LocationRequest locationRequest) {
        courierService.updateLocation(locationRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/total-distances/{courier-id}")
    public CourierDistanceDto totalDistance(@PathVariable("courier-id") Long courierId) {
        return courierService.getTotalDistance(courierId);
    }

}
