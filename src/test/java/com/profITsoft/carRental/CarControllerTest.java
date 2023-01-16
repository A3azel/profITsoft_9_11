package com.profITsoft.carRental;

import com.profITsoft.carRental.entity.Car;
import com.profITsoft.carRental.exeption.NotFoundException;
import com.profITsoft.carRental.repository.CarRepository;
import com.profITsoft.carRental.repository.DriverRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Locale;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = CarRentalApplication.class)
@AutoConfigureMockMvc
@Sql(value = {"carControllerTestScripts/create-driver-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"carControllerTestScripts/create-driver-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@TestPropertySource("/application-test.properties")
public class CarControllerTest {

    private static final String CAR_JSON_BODY = """
                  {
                       "carName": "%s",
                       "brand": "%s",
                       "carPrise": %.2f,
                       "releaseDate": "%s",
                       "driverId": %s
                  }             
                """;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private DriverRepository driverRepository;

    @AfterEach
    public void afterEach() {
        carRepository.deleteAll();
    }

    @Test
    public void testFindCarById() throws Exception{
        Car car = new Car();
        car.setCarName("S-Class");
        car.setBrand("Mercedes-Benz");
        car.setCarPrise(BigDecimal.valueOf(90_000.00));
        car.setReleaseDate(LocalDate.of(2016,5,18));
        car.setDriver(driverRepository.findById(1L).orElseThrow(NotFoundException::new));

        Long createdCarId = carRepository.save(car).getId();

        mvc.perform(get("/api/v1/car/"+createdCarId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.carName", is("S-Class")))
                .andExpect(jsonPath("$.brand", is("Mercedes-Benz")))
                .andExpect(jsonPath("$.carPrise", is(90000.00)))
                .andExpect(jsonPath("$.releaseDate", is(LocalDate.of(2016,5,18).toString())))
                .andExpect(jsonPath("$.driverId", is(1)));
    }

    @Test
    public void testFindNotExistingCarById() throws Exception {
        mvc.perform(get("/api/v1/car/0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateCar() throws Exception{
        String carName = "S-Class";
        String brand = "Mercedes-Benz";
        BigDecimal prise = BigDecimal.valueOf(85_000.00).setScale(2);
        LocalDate releaseDate = LocalDate.of(2016,5,18);
        Long driverId = 1L;
        String body =String.format(Locale.ROOT, CAR_JSON_BODY,carName, brand, prise, releaseDate, driverId);

        MvcResult mvcResult = mvc.perform(post("/api/v1/car/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(status().isCreated())
                .andReturn();

        Long createdCarId = Long.parseLong(mvcResult.getResponse().getContentAsString());

        Car createdCar = carRepository.findById(createdCarId).orElse(null);

        Assertions.assertNotNull(createdCar);
        Assertions.assertEquals(createdCar.getCarName(),carName);
        Assertions.assertEquals(createdCar.getBrand(),brand);
        Assertions.assertEquals(createdCar.getCarPrise(),prise);
        Assertions.assertEquals(createdCar.getReleaseDate(),releaseDate);
        Assertions.assertEquals(createdCar.getDriver().getId(),driverId);
    }

    @Test
    public void testCreateWithWrongBody() throws Exception{
        mvc.perform(post("/api/v1/car/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("wrongBody")
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateWithEmptyField() throws Exception{
        String carName = "S-Class";
        String brand = "Mercedes-Benz";
        BigDecimal prise = BigDecimal.valueOf(85_000.00).setScale(2);
        LocalDate releaseDate = LocalDate.of(2016,5,18);
        Long driverId = 0L;
        String body =String.format(Locale.ROOT, CAR_JSON_BODY,carName, brand, prise, releaseDate, driverId);

        mvc.perform(post("/api/v1/car/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateWithNotExistingDriver() throws Exception{
        String carName = "";
        String brand = "";
        BigDecimal prise = BigDecimal.valueOf(85_000.00).setScale(2);
        LocalDate releaseDate = LocalDate.of(2016,5,18);
        Long driverId = 1L;
        String body =String.format(Locale.ROOT, CAR_JSON_BODY,carName, brand, prise, releaseDate, driverId);

        mvc.perform(post("/api/v1/car/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(status().isBadRequest());
    }


    @Test
    public void testUpdateCarInfo() throws Exception {
        Car car = new Car();
        car.setCarName("S-Class");
        car.setBrand("Mercedes-Benz");
        car.setCarPrise(BigDecimal.valueOf(90_000.00));
        car.setReleaseDate(LocalDate.of(2016,5,18));
        car.setDriver(driverRepository.findById(1L).orElseThrow(NotFoundException::new));

        Long createdCarId = carRepository.save(car).getId();

        String newCarName = "GLE-Class";
        String newBrand = "Mercedes-Benz";
        BigDecimal newPrise = BigDecimal.valueOf(49_999.00).setScale(2);
        LocalDate newReleaseDate = LocalDate.of(2019,1,3);
        Long newUserId = 2L;
        String newBody =String.format(Locale.ROOT, CAR_JSON_BODY,newCarName, newBrand, newPrise, newReleaseDate, newUserId);

        mvc.perform(put("/api/v1/car/update/"+createdCarId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newBody)
                )
                .andExpect(status().isOk());

        Car updatedCar = carRepository.findById(createdCarId).orElse(null);

        Assertions.assertNotNull(updatedCar);
        Assertions.assertEquals(updatedCar.getCarName(),newCarName);
        Assertions.assertEquals(updatedCar.getBrand(),newBrand);
        Assertions.assertEquals(updatedCar.getCarPrise(),newPrise);
        Assertions.assertEquals(updatedCar.getReleaseDate(),newReleaseDate);
        Assertions.assertEquals(updatedCar.getDriver().getId(),newUserId);
    }

    @Test
    public void testDeleteCar() throws Exception {
        Car car = new Car();
        car.setCarName("S-Class");
        car.setBrand("Mercedes-Benz");
        car.setCarPrise(BigDecimal.valueOf(90_000.00));
        car.setReleaseDate(LocalDate.of(2016,5,18));
        car.setDriver(driverRepository.findById(1L).orElseThrow(NotFoundException::new));
        Long createdCarId = carRepository.save(car).getId();

        mvc.perform(delete("/api/v1/car/delete/"+createdCarId))
                .andExpect(status().isNoContent());

        Car deletedCar = carRepository.findById(createdCarId).orElse(null);

        Assertions.assertNull(deletedCar);
    }

    @Test
    public void testFindAllCarsByBrand() throws Exception {
        Car car1 = new Car();
        car1.setCarName("S-Class");
        car1.setBrand("Mercedes-Benz");
        car1.setCarPrise(BigDecimal.valueOf(90_000.00));
        car1.setReleaseDate(LocalDate.of(2016,5,18));
        car1.setDriver(driverRepository.findById(1L).orElseThrow(NotFoundException::new));
        carRepository.save(car1);

        Car car2 = new Car();
        car2.setCarName("X5");
        car2.setBrand("BMW");
        car2.setCarPrise(BigDecimal.valueOf(49_999.00));
        car2.setReleaseDate(LocalDate.of(2018,8,15));
        car2.setDriver(driverRepository.findById(1L).orElseThrow(NotFoundException::new));
        carRepository.save(car2);

        mvc.perform(get("/api/v1/car/all/brand/BMW/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].carName", is("X5")))
                .andExpect(jsonPath("$[0].carPrise", is(49_999.00)));
    }

    @Test
    public void testFindAllCarsByNotExistingBrand() throws Exception {
        Car car1 = new Car();
        car1.setCarName("S-Class");
        car1.setBrand("Mercedes-Benz");
        car1.setCarPrise(BigDecimal.valueOf(90_000.00));
        car1.setReleaseDate(LocalDate.of(2016,5,18));
        car1.setDriver(driverRepository.findById(1L).orElseThrow(NotFoundException::new));
        carRepository.save(car1);

        Car car2 = new Car();
        car2.setCarName("X5");
        car2.setBrand("BMW");
        car2.setCarPrise(BigDecimal.valueOf(49_999.00));
        car2.setReleaseDate(LocalDate.of(2018,8,15));
        car2.setDriver(driverRepository.findById(1L).orElseThrow(NotFoundException::new));
        carRepository.save(car2);

        mvc.perform(get("/api/v1/car/all/brand/Porsche/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(0))));
    }

    @Test
    public void testFindAllCarsByBrandAndPrise() throws Exception {
        Car car1 = new Car();
        car1.setCarName("S-Class");
        car1.setBrand("Mercedes-Benz");
        car1.setCarPrise(BigDecimal.valueOf(90_000.00));
        car1.setReleaseDate(LocalDate.of(2016,5,18));
        car1.setDriver(driverRepository.findById(1L).orElseThrow(NotFoundException::new));
        carRepository.save(car1);

        Car car2 = new Car();
        car2.setCarName("450 EQ Boost");
        car2.setBrand("Mercedes-Benz");
        car2.setCarPrise(BigDecimal.valueOf(90_000.00));
        car2.setReleaseDate(LocalDate.of(2021,7,23));
        car2.setDriver(driverRepository.findById(1L).orElseThrow(NotFoundException::new));
        carRepository.save(car2);

        Car car3 = new Car();
        car3.setCarName("Coupe AMG 450");
        car3.setBrand("Mercedes-Benz");
        car3.setCarPrise(BigDecimal.valueOf(35_000.00));
        car3.setReleaseDate(LocalDate.of(2014,11,4));
        car3.setDriver(driverRepository.findById(1L).orElseThrow(NotFoundException::new));
        carRepository.save(car3);

        Car car4 = new Car();
        car4.setCarName("X5");
        car4.setBrand("BMW");
        car4.setCarPrise(BigDecimal.valueOf(49_999.00));
        car4.setReleaseDate(LocalDate.of(2018,8,15));
        car4.setDriver(driverRepository.findById(1L).orElseThrow(NotFoundException::new));
        carRepository.save(car4);

        mvc.perform(get("/api/v1/car/all/brand/Mercedes-Benz/prise/90000/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].carName", is("S-Class")))
                .andExpect(jsonPath("$[1].carName", is("450 EQ Boost")));
    }

    @Test
    public void testFindAllCars() throws Exception {
        Car car1 = new Car();
        car1.setCarName("S-Class");
        car1.setBrand("Mercedes-Benz");
        car1.setCarPrise(BigDecimal.valueOf(90_000.00));
        car1.setReleaseDate(LocalDate.of(2016,5,18));
        car1.setDriver(driverRepository.findById(1L).orElseThrow(NotFoundException::new));
        carRepository.save(car1);

        Car car2 = new Car();
        car2.setCarName("450 EQ Boost");
        car2.setBrand("Mercedes-Benz");
        car2.setCarPrise(BigDecimal.valueOf(90_000.00));
        car2.setReleaseDate(LocalDate.of(2021,7,23));
        car2.setDriver(driverRepository.findById(1L).orElseThrow(NotFoundException::new));
        carRepository.save(car2);

        Car car3 = new Car();
        car3.setCarName("Coupe AMG 450");
        car3.setBrand("Mercedes-Benz");
        car3.setCarPrise(BigDecimal.valueOf(35_000.00));
        car3.setReleaseDate(LocalDate.of(2014,11,4));
        car3.setDriver(driverRepository.findById(1L).orElseThrow(NotFoundException::new));
        carRepository.save(car3);

        Car car4 = new Car();
        car4.setCarName("X5");
        car4.setBrand("BMW");
        car4.setCarPrise(BigDecimal.valueOf(49_999.00));
        car4.setReleaseDate(LocalDate.of(2018,8,15));
        car4.setDriver(driverRepository.findById(1L).orElseThrow(NotFoundException::new));
        carRepository.save(car4);

        mvc.perform(get("/api/v1/car/all/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].carName", is("S-Class")))
                .andExpect(jsonPath("$[1].carName", is("450 EQ Boost")))
                .andExpect(jsonPath("$[2].carName", is("Coupe AMG 450")))
                .andExpect(jsonPath("$[3].carName", is("X5")));
    }

}
