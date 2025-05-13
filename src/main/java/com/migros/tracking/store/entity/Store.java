package com.migros.tracking.store.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.migros.tracking.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.Filters;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "store",
       indexes = {@Index(name = "ix_store_name", columnList = "name"), @Index(name = "ix_store_latLon", columnList = "latitude,longitude"),
               @Index(name = "ix_store_lat", columnList = "latitude"), @Index(name = "ix_store_lon", columnList = "longitude")},
       uniqueConstraints = {@UniqueConstraint(name = "ix_unique_storeName", columnNames = "name")})
@Filters(@Filter(name = "activeFilter"))
public class Store extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @NotBlank
    @Column(name = "name", length = 150, nullable = false)
    private String name;

    @JsonProperty("lat")
    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @JsonProperty("lng")
    @Column(name = "longitude", nullable = false)
    private Double longitude;

}
