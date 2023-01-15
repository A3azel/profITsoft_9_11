package com.profITsoft.carRental;

import com.profITsoft.carRental.repository.CarRepository;
import com.profITsoft.carRental.repository.DriverRepository;
import org.junit.jupiter.api.AfterEach;
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
    public void testFindAllDriverCars(){

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
    }

    @Test
    public void testSetDriverStatus(){

    }

    @Test
    public void testDeleteDriver() throws Exception {
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

        mvc.perform(delete("/api/v1/driver/delete/"+createdDriverId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(status().isNoContent());

        mvc.perform(get("/api/v1/driver/"+createdDriverId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
