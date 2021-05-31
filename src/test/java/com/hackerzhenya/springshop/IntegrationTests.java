package com.hackerzhenya.springshop;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.Assert;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class IntegrationTests {
    @Autowired
    private MockMvc mvc;

    @Test
    void contextLoads() {
        Assert.notNull(mvc, "MockMvc must be initialized");
    }

    @Test
    void getProducts() throws Exception {
        mvc.perform(get("/products/"))
           .andDo(print())
           .andExpect(status().isOk())
           .andExpect(content().contentType(MediaType.APPLICATION_JSON))
           .andExpect(jsonPath("$", hasSize(greaterThan(1))))
           .andExpect(jsonPath("$[*].id", everyItem(greaterThan(0))))
           .andExpect(jsonPath("$[*].title", everyItem(isA(String.class))))
           .andExpect(jsonPath("$[*].price", everyItem(greaterThan(0d))))
           .andExpect(jsonPath("$[*].quantity", everyItem(greaterThan(0))));
    }

    @Test
    void tryToBuyMoreThenInStock() throws Exception {
        mvc.perform(post("/cart/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"productId\": 2, \"quantity\": 99999}"))
           .andDo(print())
           .andExpect(status().isInternalServerError())
           .andExpect(content().contentType(MediaType.APPLICATION_JSON))
           .andExpect(jsonPath("$.error", startsWith("Не достаточно товара на складе")));
    }
}
