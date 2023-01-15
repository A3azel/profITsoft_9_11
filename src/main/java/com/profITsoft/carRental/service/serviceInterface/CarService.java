package com.profITsoft.carRental.service.serviceInterface;

import com.profITsoft.carRental.dto.CarDTO;
import com.profITsoft.carRental.entity.Car;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface CarService {
    List<CarDTO> findAllCars(Pageable pageable, int page);
    Car createCar(CarDTO car);
    Car updateCar(Long id, CarDTO car);
    CarDTO getCar(Long id);
    List<CarDTO> findAllBrandCars(Pageable pageable, String brand, int page);
    List<CarDTO> findAllCarByBrandAndPrise(Pageable pageable, String brand, BigDecimal prise, int page);
    void deleteCar(Long id);
}
