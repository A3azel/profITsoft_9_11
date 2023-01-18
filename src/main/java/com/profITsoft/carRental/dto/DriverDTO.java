package com.profITsoft.carRental.dto;

import com.profITsoft.carRental.entity.Driver;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Data
public class DriverDTO {
    private Long id;
    @Size(min = 2, max = 64, message = "Length must be from 2 to 64 characters")
    private String firstName;
    @Size(min = 2, max = 64, message = "Length must be from 2 to 64 characters")
    private String lastName;
    @Nullable
    private List<CarDTO> allCars;

    public static DriverDTO createDriverDTO(Driver driver){
        return DriverDTO.builder()
                .id(driver.getId())
                .firstName(driver.getFirstName())
                .lastName(driver.getLastName())
                .allCars(driver.getCarList()
                        .stream()
                        .map(CarDTO::createCarDTO)
                        .collect(Collectors.toList()))
                .build();
    }

}
