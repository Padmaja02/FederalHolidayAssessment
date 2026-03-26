
package com.rbc.federalholidayapi.service;

import com.rbc.federalholidayapi.dto.HolidayRequest;
import com.rbc.federalholidayapi.entity.Country;
import com.rbc.federalholidayapi.entity.Holiday;
import com.rbc.federalholidayapi.exception.DuplicateHolidayException;
import com.rbc.federalholidayapi.exception.ResourceNotFoundException;
import com.rbc.federalholidayapi.repository.HolidayRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class HolidayServiceTest {

    @Autowired
    private HolidayService service;

    @Autowired
    private HolidayRepository repo;

    @BeforeEach
    void setup() {
        repo.deleteAll();
    }

    @Test
    void createHoliday_success() {
        HolidayRequest req = new HolidayRequest();
        req.setName("Test Holiday");
        req.setDate(LocalDate.of(2026, 1, 1));
        req.setCountry(Country.USA);

        Holiday res = service.create(req);

        assertNotNull(res.getId());
        assertEquals("Test Holiday", res.getName());
    }

    @Test
    void createHoliday_duplicate_shouldThrow() {
        HolidayRequest req = new HolidayRequest();
        req.setName("Duplicate");
        req.setDate(LocalDate.of(2026, 1, 1));
        req.setCountry(Country.USA);

        service.create(req);

        assertThrows(DuplicateHolidayException.class,
                () -> service.create(req));
    }

    @Test
    void getHolidays_success() {
        HolidayRequest req = new HolidayRequest();
        req.setName("Holiday");
        req.setDate(LocalDate.of(2026, 5, 5));
        req.setCountry(Country.CANADA);

        service.create(req);

        List<Holiday> list = service.list(Country.CANADA, 2026);

        assertFalse(list.isEmpty());
    }

    @Test
    void updateHoliday_success() {
        HolidayRequest req = new HolidayRequest();
        req.setName("Old");
        req.setDate(LocalDate.of(2026, 1, 1));
        req.setCountry(Country.USA);

        Holiday saved = service.create(req);

        HolidayRequest update = new HolidayRequest();
        update.setName("Updated");
        update.setDate(LocalDate.of(2026, 1, 1));
        update.setCountry(Country.USA);

        Holiday result = service.update(saved.getId(), update);

        assertEquals("Updated", result.getName());
    }

    @Test
    void updateHoliday_notFound() {
        HolidayRequest req = new HolidayRequest();
        req.setName("Test");
        req.setDate(LocalDate.of(2026, 1, 1));
        req.setCountry(Country.USA);

        assertThrows(ResourceNotFoundException.class,
                () -> service.update(UUID.randomUUID(), req));
    }
}
