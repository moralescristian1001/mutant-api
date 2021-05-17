package com.api.cmds.query.infrastructure.controller;

import com.api.cdms.query.infrastructure.controller.StatsQueryController;
import com.api.cmds.ApplicationMock;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ApplicationMock.class)
@WebMvcTest(StatsQueryController.class)
class StatsQueryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Se realiza la consulta de las estadísticas de los ADN verificados, comprobando la información retornada.")
    void getStatsTest() throws Exception {
        //Arrange - Act - assert
        mockMvc.perform(get("/v1/stats")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.countMutantDna", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.countHumanDna", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.ratio", Matchers.is(0.5)));
    }

    @Test
    @DisplayName("Valida que al consumir una url no reconocida, se retorne 404(Not found).")
    void getStatsWrongUrlTest() throws Exception {
        //Arrange - act - assert
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/statss")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
