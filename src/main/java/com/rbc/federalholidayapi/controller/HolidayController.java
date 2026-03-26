package com.rbc.federalholidayapi.controller;

import com.rbc.federalholidayapi.dto.*;
import com.rbc.federalholidayapi.entity.*;
import com.rbc.federalholidayapi.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/api/v1/holidays")
@RequiredArgsConstructor
public class HolidayController {
    private final HolidayService holidayService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody HolidayRequest request) {
        return ResponseEntity.ok(holidayService.create(request));
    }

    @GetMapping
    public ResponseEntity<?> list(@RequestParam Country country, @RequestParam int year) {
        return ResponseEntity.ok(holidayService.list(country, year));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody HolidayRequest request) {
        return ResponseEntity.ok(holidayService.update(id, request));
    }

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<FileUploadResponse> upload(
            @RequestParam("file") MultipartFile file) {
        FileUploadResponse response = holidayService.upload(file);
        return ResponseEntity.ok(response);
    }
}
