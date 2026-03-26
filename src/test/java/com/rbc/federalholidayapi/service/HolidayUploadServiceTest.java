package com.rbc.federalholidayapi.service;

import com.rbc.federalholidayapi.dto.FileUploadResponse;
import com.rbc.federalholidayapi.exception.FileProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class HolidayUploadServiceTest {

    @Autowired
    private HolidayService service;

    @Test
    void upload_validFile() throws Exception {

        String csv = "name,date,country\n" +
                "Test Holiday,2026-01-01,USA";

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.csv",
                "text/csv",
                csv.getBytes()
        );

        FileUploadResponse res = service.upload(file);

        assertEquals(1, res.getSuccessCount());
        assertEquals(0, res.getFailureCount());
    }

    @Test
    void upload_invalidDate() throws Exception {

        String csv = "name,date,country\n" +
                "Bad,2026-99-99,USA";

        MockMultipartFile file = new MockMultipartFile(
                "file", "test.csv", "text/csv", csv.getBytes()
        );

        FileUploadResponse res = service.upload(file);

        assertEquals(0, res.getSuccessCount());
        assertEquals(1, res.getFailureCount());
        assertTrue(res.getErrors().get(0).contains("Invalid date"));
    }

    @Test
    void upload_invalidCountry() throws Exception {

        String csv = "name,date,country\n" +
                "Bad,2026-01-01,INDIA";

        MockMultipartFile file = new MockMultipartFile(
                "file", "test.csv", "text/csv", csv.getBytes()
        );

        FileUploadResponse res = service.upload(file);

        assertEquals(1, res.getFailureCount());
        assertTrue(res.getErrors().get(0).contains("Invalid country"));
    }

    @Test
    void upload_emptyFile_shouldThrow() {

        MockMultipartFile file = new MockMultipartFile(
                "file", "empty.csv", "text/csv", new byte[0]
        );

        assertThrows(FileProcessingException.class,
                () -> service.upload(file));
    }
}
