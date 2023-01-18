package com.profITsoft.carRental.service.serviceImpl;

import com.profITsoft.carRental.dto.CarDTO;
import com.profITsoft.carRental.entity.Car;
import com.profITsoft.carRental.exeption.NotFoundException;
import com.profITsoft.carRental.repository.CarRepository;
import com.profITsoft.carRental.repository.DriverRepository;
import com.profITsoft.carRental.service.serviceInterface.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CarServiceI implements CarService {

    private final CarRepository carRepository;
    private final DriverRepository driverRepository;

    @Autowired
    public CarServiceI(CarRepository carRepository, DriverRepository driverRepository) {
        this.carRepository = carRepository;
        this.driverRepository = driverRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CarDTO> findAllCars(Pageable pageable, int page) {
        Pageable changedPageable = PageRequest.of(page - 1, pageable.getPageSize());
        Page<CarDTO> avtoPage =  carRepository.findAll(changedPageable).map(CarDTO::createCarDTO);
        return avtoPage.getContent();
    }

    @Override
    @Transactional
    public Long createCar(CarDTO carDTO) {
        Car newCar = carDtoToCar(carDTO);
        return carRepository.save(newCar).getId();
    }

    @Override
    @Transactional
    public Long updateCar(Long id, CarDTO carDTO) {
        Car newInfo = carDtoToCar(carDTO);
        newInfo.setId(id);
        return carRepository.save(newInfo).getId();
    }

    @Override
    @Transactional(readOnly = true)
    public CarDTO getCar(Long id) {
        Car selectedCar = carRepository.findById(id).orElseThrow(NotFoundException::new);
        return CarDTO.createCarDTO(selectedCar);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CarDTO> findAllBrandCars(Pageable pageable, String brand, int page) {
        Pageable changedPageable = PageRequest.of(page - 1, pageable.getPageSize());
        Page<CarDTO> avtoPage = carRepository.findAllByBrand(changedPageable, brand)
                .map(CarDTO::createCarDTO);
        return avtoPage.getContent();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CarDTO> findAllCarByBrandAndPrise(Pageable pageable, String brand, BigDecimal prise, int page) {
        Pageable changedPageable = PageRequest.of(page - 1, pageable.getPageSize());
        Page<CarDTO> avtoPage =  carRepository.findAllByBrandAndCarPrise(changedPageable, brand, prise)
                .map(CarDTO::createCarDTO);
        return avtoPage.getContent();
    }

    @Override
    @Transactional
    public void deleteCar(Long id) {
        carRepository.deleteById(id);
    }

    private Car carDtoToCar(CarDTO carDTO){
        Car car = new Car();
        car.setCarName(carDTO.getCarName());
        car.setBrand(carDTO.getBrand());
        car.setCarPrise(carDTO.getCarPrise());
        car.setReleaseDate(carDTO.getReleaseDate());
        car.setDriver(driverRepository.findById(carDTO.getDriverId()).orElseThrow(NotFoundException::new));
        return car;
    }
}
