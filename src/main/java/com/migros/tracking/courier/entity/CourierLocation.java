package com.migros.tracking.courier.entity;

import com.migros.tracking.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.Filters;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "courier_location", indexes = {@Index(name = "ix_courierLocation_courierIdTime", columnList = "courier_id,time")})
@Entity
@Filters(@Filter(name = "activeFilter", condition = "is_deleted = :isDeleted"))
public class CourierLocation extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "time")
    private Instant time;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "courier_id", referencedColumnName = "id", nullable = false, updatable = false)
    private Courier courier;

}
