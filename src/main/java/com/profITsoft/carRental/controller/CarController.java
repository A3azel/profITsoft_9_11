package com.profITsoft.carRental.controller;

import com.profITsoft.carRental.dto.CarDTO;
import com.profITsoft.carRental.exeption.EntityValidationException;
import com.profITsoft.carRental.service.serviceInterface.CarService;
import com.profITsoft.carRental.validations.ErrorValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/car",produces="application/json")
public class CarController {
    private static final int DEFAULT_PAGE_SIZE = 10;

    private final CarService carService;
    private final ErrorValidator errorValidator;

    @Autowired
    public CarController(CarService carService, ErrorValidator errorValidator) {
        this.carService = carService;
        this.errorValidator = errorValidator;
    }

    @GetMapping("/all/{page}")
    public ResponseEntity<List<CarDTO>> getAllCars(@PageableDefault(size = DEFAULT_PAGE_SIZE) Pageable pageable, @PathVariable("page") int page){
        List<CarDTO> allCars = carService.findAllCars(pageable, page);
        return ResponseEntity.ok(allCars);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarDTO> getCar(@PathVariable("id") Long id){
        CarDTO selectedCar = carService.getCar(id);
        return ResponseEntity.ok(selectedCar);
    }

    @GetMapping("/all/brand/{brand}/{page}")
    public ResponseEntity<List<CarDTO>> getAllBrandCars(@PageableDefault(size = DEFAULT_PAGE_SIZE) Pageable pageable, @PathVariable("brand") String brand, @PathVariable("page") int page){
        List<CarDTO> allCars = carService.findAllBrandCars(pageable, brand, page);
        return ResponseEntity.ok(allCars);
    }

    @GetMapping("/all/priseAndBrand/{page}")
    public ResponseEntity<List<CarDTO>> getAllCarByBrandAndPrise(@PageableDefault(size = DEFAULT_PAGE_SIZE) Pageable pageable, @RequestParam(value = "brand") String brand,
                                                                 @RequestParam(value = "prise") BigDecimal prise, @PathVariable("page") int page){
        List<CarDTO> allCars = carService.findAllCarByBrandAndPrise(pageable, brand, prise, page);
        return ResponseEntity.ok(allCars);
    }

    @PostMapping("/create")
    public ResponseEntity<Long> createCat(@RequestBody @Valid CarDTO carDTO, BindingResult bindingResult){
        String errorMassage = errorValidator.checkErrors(bindingResult);
        if(!errorMassage.equals("")){
            throw new EntityValidationException(errorMassage);
        }
        return new ResponseEntity<>(carService.createCar(carDTO), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Long> updateCar(@RequestBody @Valid CarDTO carDTO, @PathVariable("id") Long id, BindingResult bindingResult){
        String errorMassage = errorValidator.checkErrors(bindingResult);
        if(!errorMassage.equals("")){
            throw new EntityValidationException(errorMassage);
        }
        return ResponseEntity.ok(carService.updateCar(id, carDTO));
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCar(@PathVariable("id") Long id){
        carService.deleteCar(id);
    }

}
