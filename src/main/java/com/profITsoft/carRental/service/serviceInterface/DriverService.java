package com.profITsoft.carRental.service.serviceInterface;

import com.profITsoft.carRental.dto.CarDTO;
import com.profITsoft.carRental.dto.DriverDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DriverService {
    List<DriverDTO> getAllDrivers(Pageable pageable, int page);
    List<CarDTO> getAllDriverCars(Long driverId);
    DriverDTO findDriver(Long id);
    Long createDriver(DriverDTO driverDTO);
    void deleteDriver(Long id);
    Long updateDriver(Long id, DriverDTO driverDTO);
}
