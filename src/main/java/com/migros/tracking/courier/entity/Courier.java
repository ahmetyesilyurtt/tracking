package com.migros.tracking.courier.entity;

import com.migros.tracking.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.Filters;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "courier", indexes = {@Index(name = "ix_courier_identityNumber", columnList = "identity_number")},
       uniqueConstraints = {@UniqueConstraint(name = "ix_unique_identityNumber", columnNames = "identity_number")})
@Filters(@Filter(name = "activeFilter"))
public class Courier extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "full_name", length = 100, nullable = false)
    private String fullName;

    @Column(name = "identity_number", length = 11, nullable = false)
    private String identityNumber;
}
