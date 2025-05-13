package com.migros.tracking.store.entity;

import com.migros.tracking.common.entity.BaseEntity;
import com.migros.tracking.courier.entity.Courier;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "store_entrance", indexes = {@Index(name = "ix_storeEntrance_courierIdCreatedAt", columnList = "courier_id,created_at")})
public class StoreEntrance extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "courier_id", referencedColumnName = "id", nullable = false)
    private Courier courier;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "store_id", referencedColumnName = "id", nullable = false)
    private Store store;

    @Column(name = "entrance_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Instant entranceTime;

    @Column(name = "total_trip", nullable = false)
    private Double totalTrip;
}
