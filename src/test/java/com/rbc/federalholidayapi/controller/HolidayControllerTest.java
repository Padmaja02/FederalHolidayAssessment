
package com.rbc.federalholidayapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbc.federalholidayapi.dto.HolidayRequest;
import com.rbc.federalholidayapi.entity.Country;
import com.rbc.federalholidayapi.repository.HolidayRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class HolidayControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private HolidayRepository repo;

    @BeforeEach
    void setup() {
        repo.deleteAll();
    }

    @Test
    void createHoliday_api() throws Exception {

        HolidayRequest req = new HolidayRequest();
        req.setName("API Test");
        req.setDate(LocalDate.of(2026, 1, 1));
        req.setCountry(Country.USA);

        mockMvc.perform(post("/api/v1/holidays")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }

    @Test
    void createHoliday_duplicate_shouldReturn409() throws Exception {

        HolidayRequest req = new HolidayRequest();
        req.setName("API Test");
        req.setDate(LocalDate.of(2026, 1, 1));
        req.setCountry(Country.USA);

        mockMvc.perform(post("/api/v1/holidays")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/holidays")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isConflict()); // 🔥 409
    }

    @Test
    void getHoliday_invalidCountry_shouldReturn400() throws Exception {

        mockMvc.perform(get("/api/v1/holidays")
                        .param("country", "INVALID")
                        .param("year", "2026"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void uploadFile_api() throws Exception {

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.csv",
                "text/csv",
                "name,date,country\nTest,2026-01-01,USA".getBytes()
        );

        mockMvc.perform(multipart("/api/v1/holidays/upload")
                        .file(file))
                .andExpect(status().isOk());
    }
}
