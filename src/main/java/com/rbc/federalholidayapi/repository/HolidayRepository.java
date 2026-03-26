
package com.rbc.federalholidayapi.repository;

import com.rbc.federalholidayapi.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.*;

public interface HolidayRepository extends JpaRepository<Holiday, UUID> {
    boolean existsByDateAndCountry(LocalDate date, Country country);

    List<Holiday> findByCountryAndDateBetween(Country country, LocalDate start, LocalDate end);
}
