package com.profITsoft.carRental.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "car")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "car_name")
    private String carName;

    @Column(name = "brand")
    private String brand;

    @Column(name = "car_prise")
    private BigDecimal carPrise;

    @Column(name = "release_date")
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate releaseDate;

    @ManyToOne(fetch = FetchType.LAZY)
    //@ManyToOne
    @JoinColumn(name = "driver_id")
    @JsonIdentityReference(alwaysAsId = true)
    private Driver driver;

}
