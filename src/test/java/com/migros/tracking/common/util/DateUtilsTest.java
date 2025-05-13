package com.migros.tracking.common.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.*;

import static com.migros.tracking.common.util.DateUtils.ZONE_TR;
import static com.migros.tracking.common.util.DateUtils.ZONE_UTC;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class DateUtilsTest {

    private Clock clock;

    @BeforeEach
    public void init() {
        var localDateTime = LocalDateTime.of(2020, Month.OCTOBER, 9, 0, 5);
        clock = Clock.fixed(localDateTime.atZone(ZONE_UTC).toInstant(), ZONE_UTC);
    }

    @Test
    void shouldGetCurrentInstant() {
        try (MockedStatic<DateUtils> mockedUtils = mockStatic(DateUtils.class, Mockito.CALLS_REAL_METHODS)) {
            // given
            mockedUtils.when(DateUtils::getCurrentInstant).thenReturn(Instant.now(clock));

            var expected = Instant.now(clock);

            // when
            var actual = DateUtils.getCurrentInstant();

            // then
            assertEquals(expected, actual);
        }
    }

    @Test
    void shouldFormatInstantAtUTC() {
        // given
        var dateFormat = "ddyyyyMM";

        var localDate = LocalDate.of(2021, 6, 15);
        var localDateTime = localDate.atTime(LocalTime.of(20, 45));
        var instant = localDateTime.atZone(ZONE_TR).toInstant();

        var expected = "15202106";

        // when
        var actual = DateUtils.formatInstantAtUTC(instant, dateFormat);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void shouldFormatInstantAtTR() {
        // given
        var dateFormat = "ddMM";

        var localDate = LocalDate.of(2021, 6, 15);
        var localDateTime = localDate.atTime(LocalTime.of(20, 45));
        var instant = localDateTime.atZone(ZONE_TR).toInstant();

        var expected = "1506";

        // when
        var actual = DateUtils.formatInstantAtTR(instant, dateFormat);

        // then
        assertEquals(expected, actual);
    }
}