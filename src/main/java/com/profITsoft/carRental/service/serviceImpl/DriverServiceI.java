package com.profITsoft.carRental.service.serviceImpl;

import com.profITsoft.carRental.dto.CarDTO;
import com.profITsoft.carRental.dto.DriverDTO;
import com.profITsoft.carRental.entity.Driver;
import com.profITsoft.carRental.exeption.NotFoundException;
import com.profITsoft.carRental.repository.CarRepository;
import com.profITsoft.carRental.repository.DriverRepository;
import com.profITsoft.carRental.service.serviceInterface.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DriverServiceI implements DriverService {

    private final DriverRepository driverRepository;
    private final CarRepository carRepository;

    @Autowired
    public DriverServiceI(DriverRepository driverRepository, CarRepository carRepository) {
        this.driverRepository = driverRepository;
        this.carRepository = carRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DriverDTO> getAllDrivers(Pageable pageable, int page) {
        Pageable changedPageable = PageRequest.of(page - 1, pageable.getPageSize());
        Page<DriverDTO> driverPage = driverRepository.findAll(changedPageable).map(DriverDTO::createDriverDTO);
        return driverPage.getContent();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CarDTO> getAllDriverCars(Long driverId) {
        DriverDTO searchDriver = findDriver(driverId);
        return searchDriver.getAllCars().stream().toList();
    }

    @Override
    @Transactional(readOnly = true)
    public DriverDTO findDriver(Long id) {
        Driver driver = driverRepository.findById(id).orElseThrow(NotFoundException::new);
        return DriverDTO.createDriverDTO(driver);
    }

    @Override
    @Transactional
    public Driver createDriver(DriverDTO driverDTO) {
        Driver newDriver = driverDtoToDriver(driverDTO);
        return driverRepository.save(newDriver);
    }

    @Override
    @Transactional
    public void deleteDriver(Long id) {
        driverRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Driver updateDriver(Long id, DriverDTO driverDTO) {
        Driver newInfo = driverDtoToDriver(driverDTO);
        newInfo.setId(id);
        return driverRepository.save(newInfo);
    }

    private Driver driverDtoToDriver(DriverDTO driverDTO){
        Driver driver = new Driver();
        driver.setFirstName(driverDTO.getFirstName());
        driver.setLastName(driverDTO.getLastName());
        if(driverDTO.getAllCars()!=null){
            driver.setCarList(driverDTO.getAllCars().stream()
                    .map(car -> carRepository.findById(car.getId()).orElseThrow(NotFoundException::new))
                    .collect(Collectors.toList()));
        }
        return driver;
    }
}
