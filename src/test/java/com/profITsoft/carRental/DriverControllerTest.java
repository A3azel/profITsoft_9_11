package com.profITsoft.carRental;

import com.profITsoft.carRental.entity.Driver;
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

import java.util.Locale;

import static org.hamcrest.CoreMatchers.is;
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
                       "lastName": "%s",
                       "active": %b
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
        driverRepository.deleteAll();
    }

    @Test
    public void testFindDriverByID() throws Exception {
        Driver driver = new Driver();
        driver.setFirstName("Driver1");
        driver.setLastName("Driver1");
        driver.setActive(true);

        Long createdDriverId = driverRepository.save(driver).getId();

        mvc.perform(get("/api/v1/driver/"+createdDriverId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName", is("Driver1")))
                .andExpect(jsonPath("$.lastName", is("Driver1")))
                .andExpect(jsonPath("$.active", is(true)));
    }

    @Test
    public void testFindAllDriverCars() throws Exception {

    }

    @Test
    public void testCreateDriver() throws Exception {
        String firstName = "Driver1";
        String lastName = "Driver1";
        boolean active = true;
        String body =String.format(Locale.ROOT, DRIVER_JSON_BODY, firstName, lastName, active);

        MvcResult mvcResult = mvc.perform(post("/api/v1/driver/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(status().isCreated())
                .andReturn();

        String createdDriverId = mvcResult.getResponse().getContentAsString();

        mvc.perform(get("/api/v1/driver/"+createdDriverId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName", is("Driver1")))
                .andExpect(jsonPath("$.lastName", is("Driver1")))
                .andExpect(jsonPath("$.active", is(true)));

    }

    @Test
    public void testUpdateDriver() throws Exception {
        Driver driver = new Driver();
        driver.setFirstName("Driver1");
        driver.setLastName("Driver1");
        driver.setActive(true);

        Long createdDriverId = driverRepository.save(driver).getId();

        String updatedDriverInfo =String.format(Locale.ROOT, DRIVER_JSON_BODY,"NewDriverName", "NewDriverLastname", false);

        mvc.perform(put("/api/v1/driver/update/"+createdDriverId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedDriverInfo)
                )
                .andExpect(status().isOk());

        Driver updatedDriver = driverRepository.findById(createdDriverId).orElse(null);

        Assertions.assertNotNull(updatedDriver);
        Assertions.assertEquals(updatedDriver.getFirstName(),"NewDriverName");
        Assertions.assertEquals(updatedDriver.getLastName(),"NewDriverLastname");
        Assertions.assertFalse(updatedDriver.isActive());

    }

    @Test
    public void testSetDriverStatus(){

    }

    @Test
    public void testDeleteDriver() throws Exception {
        Driver driver = new Driver();
        driver.setFirstName("Driver1");
        driver.setLastName("Driver1");
        driver.setActive(true);

        Long createdDriverId = driverRepository.save(driver).getId();

        mvc.perform(delete("/api/v1/driver/delete/"+createdDriverId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Driver deletedDriver = driverRepository.findById(createdDriverId).orElse(null);

        Assertions.assertNull(deletedDriver);
    }
}
