package com.profITsoft.carRental.dto;

import com.profITsoft.carRental.entity.Driver;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.Set;
import java.util.stream.Collectors;

@Builder
@Data
public class DriverDTO {
    private Long id;
    @NotBlank(message = "This field can't be blank")
    @Size(min = 2, max = 64, message = "Length must be from 2 to 64 characters")
    private String firstName;
    @NotBlank(message = "This field can't be blank")
    @Size(min = 2, max = 64, message = "Length must be from 2 to 64 characters")
    private String lastName;
    @NotNull(message = "This field can't be blank")
    private boolean active;
    @Nullable
    private Set<CarDTO> allCars;

    public static DriverDTO createDriverDTO(Driver driver){
        return DriverDTO.builder()
                .id(driver.getId())
                .firstName(driver.getFirstName())
                .lastName(driver.getLastName())
                .active(driver.isActive())
                .allCars(driver.getCarSet()
                        .stream()
                        .map(CarDTO::createCarDTO)
                        .collect(Collectors.toSet()))
                .build();
    }

}
