package com.profITsoft.carRental.repository;

import com.profITsoft.carRental.entity.Driver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    void deleteById(Long id);
    Page<Driver> findAll(Pageable pageable);
    @Modifying
    @Query(value = "UPDATE driver SET is_active = :active WHERE id = :id", nativeQuery = true)
    void setDriverStatus(@Param("active") boolean active, @Param("id") Long id);
}
