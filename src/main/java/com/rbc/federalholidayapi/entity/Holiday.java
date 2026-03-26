
package com.rbc.federalholidayapi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"date", "country"}))
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Holiday {
    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private LocalDate date;
    @Enumerated(EnumType.STRING)
    private Country country;
}
