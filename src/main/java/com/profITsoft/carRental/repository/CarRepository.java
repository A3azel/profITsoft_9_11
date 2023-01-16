package com.profITsoft.carRental.repository;

import com.profITsoft.carRental.entity.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    Page<Car> findAllByBrandAndCarPrise(Pageable pageable, String carBrand, BigDecimal prise);
    Page<Car> findAllByBrand(Pageable pageable, String carBrand);
    Page<Car> findAll(Pageable pageable);
    void deleteById(Long id);
}
