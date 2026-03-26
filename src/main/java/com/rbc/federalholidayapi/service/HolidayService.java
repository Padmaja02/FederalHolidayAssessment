
package com.rbc.federalholidayapi.service;

import com.rbc.federalholidayapi.dto.*;
import com.rbc.federalholidayapi.entity.*;
import com.rbc.federalholidayapi.exception.DuplicateHolidayException;
import com.rbc.federalholidayapi.exception.FileProcessingException;
import com.rbc.federalholidayapi.exception.ResourceNotFoundException;
import com.rbc.federalholidayapi.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class HolidayService {
    private final HolidayRepository repo;

    public Holiday create(HolidayRequest request) {
        if (repo.existsByDateAndCountry(request.getDate(), request.getCountry())) {
            throw new DuplicateHolidayException(
                    "Holiday already exists for given date and country"
            );
        }
        Holiday h = Holiday.builder().name(request.getName()).date(request.getDate()).country(request.getCountry()).build();
        return repo.save(h);
    }

    public List<Holiday> list(Country country, int year) {
        return repo.findByCountryAndDateBetween(country, LocalDate.of(year, 1, 1), LocalDate.of(year, 12, 31));
    }

    public Holiday update(UUID id, HolidayRequest request) {
        Holiday h = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Holiday not found"));
        h.setName(request.getName());
        h.setDate(request.getDate());
        h.setCountry(request.getCountry());
        return repo.save(h);
    }

    public FileUploadResponse upload(MultipartFile file) {

        if (file.isEmpty()) {
            throw new FileProcessingException("Uploaded file is empty");
        }

        List<String> errors = new ArrayList<>();
        int successCount = 0;
        int failureCount = 0;

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(file.getInputStream()))) {

            List<String> lines = br.lines().toList();

            if (lines.size() <= 1) {
                throw new FileProcessingException("CSV file contains no data rows");
            }

            for (int i = 1; i < lines.size(); i++) {

                String line = lines.get(i);

                try {
                    String[] p = line.split(",");

                    if (p.length != 3) {
                        throw new IllegalArgumentException(
                                "Invalid CSV format at line: " + line
                        );
                    }

                    LocalDate date;
                    try {
                        date = LocalDate.parse(p[1]);
                    } catch (DateTimeParseException e) {
                        throw new IllegalArgumentException(
                                "Invalid date format at line: " + line
                        );
                    }

                    Country country;
                    try {
                        country = Country.valueOf(p[2]);
                    } catch (IllegalArgumentException e) {
                        throw new IllegalArgumentException(
                                "Invalid country value at line: " + line
                        );
                    }

                    if (!repo.existsByDateAndCountry(date, country)) {
                        repo.save(Holiday.builder()
                                .name(p[0])
                                .date(date)
                                .country(country)
                                .build());
                        successCount++;
                    } else {
                        errors.add("Duplicate skipped at line: " + line);
                        failureCount++;
                        continue;
                    }

                } catch (Exception e) {
                    errors.add(e.getMessage());
                    failureCount++;
                }
            }

        } catch (IOException e) {
            throw new FileProcessingException("Failed to read file");
        }

        return new FileUploadResponse(successCount, failureCount, errors);
    }
}
