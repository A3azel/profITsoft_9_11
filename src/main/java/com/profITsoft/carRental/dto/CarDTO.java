package com.profITsoft.carRental.dto;

import com.profITsoft.carRental.entity.Car;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.NumberFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Data
public class CarDTO {
    private Long id;
    @NotBlank(message = "This field can't be blank")
    @Size(min = 2, max = 256, message = "Length must be from 2 to 256 characters")
    private String carName;
    @NotBlank(message = "This field can't be blank")
    @Size(min = 2, max = 64, message = "Length must be from 2 to 64 characters")
    private String brand;
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    @NotNull(message = "This field can't be blank")
    private BigDecimal carPrise;
    @NotNull(message = "This field can't be blank")
    private LocalDate releaseDate;
    @NotNull(message = "This field can't be blank")
    private Long driverId;

    public static CarDTO createCarDTO(Car car){
        return CarDTO.builder()
                .id(car.getId())
                .carName(car.getCarName())
                .brand(car.getBrand())
                .carPrise(car.getCarPrise())
                .releaseDate(car.getReleaseDate())
                .driverId(car.getDriver().getId())
                .build();
    }
}
