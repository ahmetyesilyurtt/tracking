package com.migros.tracking.courier.repository;

import com.migros.tracking.courier.dto.CourierDto;
import com.migros.tracking.courier.dto.CourierLocationDto;
import com.migros.tracking.courier.entity.Courier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface CourierRepository extends JpaRepository<Courier, Long> {

    Optional<Courier> findCourierByIdentityNumber(String identityNumber);

    @Query("""
                    SELECT CASE
                               WHEN COUNT(c) > 0
                                   THEN true
                               ELSE false
                               END
                    FROM Courier c
                    WHERE c.id = :courierId
                      AND c.deleted = false
            """)
    boolean isCourierActiveByCourierId(@Param("courierId") Long courierId);

    @Query("""
                SELECT new com.migros.tracking.courier.dto.CourierDto(
                    c.id,c.fullName,c.identityNumber)
                FROM Courier c
                WHERE c.deleted = false
            """)
    Page<CourierDto> getCouriers(Pageable pageable);

    @Query("""
                SELECT new com.migros.tracking.courier.dto.CourierLocationDto(cl.time,cl.latitude,cl.longitude)
                FROM CourierLocation cl
                WHERE cl.courier.id = :courierId AND cl.time > :lastEntranceTime
                ORDER BY cl.time
            """)
    Page<CourierLocationDto> getLocationsGreaterThan(@Param("courierId") Long courierId, @Param("lastEntranceTime") Instant lastEntranceTime,
                                                     Pageable pageable);
}
