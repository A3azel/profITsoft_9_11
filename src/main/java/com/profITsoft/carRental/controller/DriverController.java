package com.profITsoft.carRental.controller;

import com.profITsoft.carRental.dto.CarDTO;
import com.profITsoft.carRental.dto.DriverDTO;
import com.profITsoft.carRental.exeption.EntityValidationException;
import com.profITsoft.carRental.service.serviceInterface.DriverService;
import com.profITsoft.carRental.validations.ErrorValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/driver", produces="application/json")
@CrossOrigin(origins = "http://localhost:3000")
public class DriverController {
    private static final int DEFAULT_PAGE_SIZE = 10;

    private final DriverService driverService;
    private final ErrorValidator errorValidator;

    @Autowired
    public DriverController(DriverService driverService, ErrorValidator errorValidator) {
        this.driverService = driverService;
        this.errorValidator = errorValidator;
    }

    @GetMapping("/{id}")
    public ResponseEntity<DriverDTO> getDriver(@PathVariable("id") Long id){
        DriverDTO selectedDriver = driverService.findDriver(id);
        return ResponseEntity.ok(selectedDriver);
    }

    @GetMapping("/all/cars/{id}")
    public ResponseEntity<List<CarDTO>> getAllDriverCar(@PathVariable("id") Long id){
        List<CarDTO> allDriverCars = driverService.getAllDriverCars(id);
        return ResponseEntity.ok(allDriverCars);
    }

    @GetMapping("/all/{page}")
    public ResponseEntity<List<DriverDTO>> getAllDrivers(@PageableDefault(size = DEFAULT_PAGE_SIZE) Pageable pageable, @PathVariable("page") int page){
        List<DriverDTO> allDrivers = driverService.getAllDrivers(pageable, page);
        return ResponseEntity.ok(allDrivers);
    }

    @PostMapping("/create")
    public ResponseEntity<Long> createDriver(@RequestBody @Valid DriverDTO driverDTO, BindingResult bindingResult){
        String errorMassage = errorValidator.checkErrors(bindingResult);
        if(!errorMassage.equals("")){
            throw new EntityValidationException(errorMassage);
        }
        return new ResponseEntity<>(driverService.createDriver(driverDTO), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Long> updateDriver(@RequestBody @Valid DriverDTO driverDTO, @PathVariable("id") Long id, BindingResult bindingResult){
        String errorMassage = errorValidator.checkErrors(bindingResult);
        if(!errorMassage.equals("")){
            throw new EntityValidationException(errorMassage);
        }
        return ResponseEntity.ok(driverService.updateDriver(id, driverDTO));
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDriver(@PathVariable("id") Long id){
        driverService.deleteDriver(id);
    }


}
