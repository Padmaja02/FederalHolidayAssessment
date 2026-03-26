
package com.rbc.federalholidayapi.dto;

import com.rbc.federalholidayapi.entity.Country;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class HolidayRequest {
    @NotBlank
    private String name;
    @NotNull
    private LocalDate date;
    @NotNull
    private Country country;
}
