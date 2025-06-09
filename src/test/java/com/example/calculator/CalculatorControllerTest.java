package com.example.calculator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(CalculatorController.class)
public class CalculatorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetSum_positiveNumbers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/calculator/sum")
                .param("num1", "5")
                .param("num2", "3"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("8.0"));
    }

    @Test
    public void testGetSum_negativeNumbers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/calculator/sum")
                .param("num1", "-5")
                .param("num2", "-3"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("-8.0"));
    }

    @Test
    public void testGetSum_withZero() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/calculator/sum")
                .param("num1", "0")
                .param("num2", "0"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("0.0"));
    }

    @Test
    public void testGetSum_positiveAndNegative() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/calculator/sum")
                .param("num1", "10")
                .param("num2", "-3"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("7.0"));
    }

    @Test
    public void testGetSum_decimalNumbers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/calculator/sum")
                .param("num1", "2.5")
                .param("num2", "3.5"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("6.0"));
    }

    @Test
    public void testGetSum_missingParameter() throws Exception {
        // Test when num2 is missing
        mockMvc.perform(MockMvcRequestBuilders.get("/api/calculator/sum")
                .param("num1", "5"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Test when num1 is missing
        mockMvc.perform(MockMvcRequestBuilders.get("/api/calculator/sum")
                .param("num2", "5"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testGetSum_invalidParameterType() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/calculator/sum")
                .param("num1", "abc")
                .param("num2", "5"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
