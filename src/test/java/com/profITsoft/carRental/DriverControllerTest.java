package com.profITsoft.carRental;

import com.profITsoft.carRental.entity.Car;
import com.profITsoft.carRental.entity.Driver;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Locale;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = CarRentalApplication.class)
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
public class DriverControllerTest {

    private static final String DRIVER_JSON_BODY = """
                  {
                       "firstName": "%s",
                       "lastName": "%s"
                  }             
                """;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private CarRepository carRepository;

    @AfterEach
    public void afterEach() {
        driverRepository.deleteAll();
    }

    @Test
    public void testFindDriverByID() throws Exception {
        Driver driver = new Driver();
        driver.setFirstName("DriverFirstname");
        driver.setLastName("DriverLastname");

        Long createdDriverId = driverRepository.save(driver).getId();

        mvc.perform(get("/api/v1/driver/"+createdDriverId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName", is("DriverFirstname")))
                .andExpect(jsonPath("$.lastName", is("DriverLastname")));
    }

    @Test
    public void testFindAllDriverCars() throws Exception {
        Driver driver = new Driver();
        driver.setFirstName("DriverFirstname");
        driver.setLastName("DriverLastname");

        Long createdDriverId = driverRepository.save(driver).getId();

        Car car1 = new Car();
        car1.setCarName("S-Class");
        car1.setBrand("Mercedes-Benz");
        car1.setCarPrise(BigDecimal.valueOf(90_000.00));
        car1.setReleaseDate(LocalDate.of(2016,5,18));
        car1.setDriver(driverRepository.findById(createdDriverId).orElseThrow(NotFoundException::new));
        carRepository.save(car1);

        Car car2 = new Car();
        car2.setCarName("X5");
        car2.setBrand("BMW");
        car2.setCarPrise(BigDecimal.valueOf(49_999.00));
        car2.setReleaseDate(LocalDate.of(2018,8,15));
        car2.setDriver(driverRepository.findById(createdDriverId).orElseThrow(NotFoundException::new));
        carRepository.save(car2);

        mvc.perform(get("/api/v1/driver/all/cars/"+createdDriverId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].carName", is("S-Class")))
                .andExpect(jsonPath("$[1].carName", is("X5")))
                .andExpect(jsonPath("$[0].brand", is("Mercedes-Benz")))
                .andExpect(jsonPath("$[1].brand", is("BMW")));
    }

    @Test
    public void testFindAllDrivers() throws Exception{
        Driver driver = new Driver();
        driver.setFirstName("DriverFirstname");
        driver.setLastName("DriverLastname");

        Driver secondDriver = new Driver();
        secondDriver.setFirstName("DriverFirstname2");
        secondDriver.setLastName("DriverLastname2");

        driverRepository.save(driver);
        driverRepository.save(secondDriver);

        mvc.perform(get("/api/v1/driver/all/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].firstName", is("DriverFirstname")))
                .andExpect(jsonPath("$[1].firstName", is("DriverFirstname2")))
                .andExpect(jsonPath("$[0].lastName", is("DriverLastname")))
                .andExpect(jsonPath("$[1].lastName", is("DriverLastname2")));

    }

    @Test
    public void testCreateDriver() throws Exception {
        String firstName = "DriverFirstname";
        String lastName = "DriverLastname";
        String body =String.format(Locale.ROOT, DRIVER_JSON_BODY, firstName, lastName);

        MvcResult mvcResult = mvc.perform(post("/api/v1/driver/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(status().isCreated())
                .andReturn();

        Long createdDriverId = Long.parseLong(mvcResult.getResponse().getContentAsString());

        Driver createdDriver = driverRepository.findById(createdDriverId).orElse(null);

        Assertions.assertNotNull(createdDriver);
        Assertions.assertEquals(createdDriver.getFirstName(),"DriverFirstname");
        Assertions.assertEquals(createdDriver.getLastName(),"DriverLastname");
    }

    @Test
    public void testUpdateDriver() throws Exception {
        Driver driver = new Driver();
        driver.setFirstName("DriverFirstname");
        driver.setLastName("DriverLastname");

        Long createdDriverId = driverRepository.save(driver).getId();

        String updatedDriverInfo =String.format(Locale.ROOT, DRIVER_JSON_BODY,"NewDriverName", "NewDriverLastname");

        mvc.perform(put("/api/v1/driver/update/"+createdDriverId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedDriverInfo)
                )
                .andExpect(status().isOk());

        Driver updatedDriver = driverRepository.findById(createdDriverId).orElse(null);

        Assertions.assertNotNull(updatedDriver);
        Assertions.assertEquals(updatedDriver.getFirstName(),"NewDriverName");
        Assertions.assertEquals(updatedDriver.getLastName(),"NewDriverLastname");

    }

    @Test
    public void testDeleteDriver() throws Exception {
        Driver driver = new Driver();
        driver.setFirstName("DriverFirstname");
        driver.setLastName("DriverLastname");

        Long createdDriverId = driverRepository.save(driver).getId();

        mvc.perform(delete("/api/v1/driver/delete/"+createdDriverId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Driver deletedDriver = driverRepository.findById(createdDriverId).orElse(null);

        Assertions.assertNull(deletedDriver);
    }
}
