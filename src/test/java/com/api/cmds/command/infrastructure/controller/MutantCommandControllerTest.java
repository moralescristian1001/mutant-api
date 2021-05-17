package com.api.cmds.command.infrastructure.controller;

import com.api.cdms.command.application.dto.MutantDTO;
import com.api.cdms.command.infrastructure.controller.MutantCommandController;
import com.api.cmds.ApplicationMock;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ApplicationMock.class)
@WebMvcTest(MutantCommandController.class)
class MutantCommandControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Valida y guarda el ADN enviado al realizar la comprobación de que es un mutante.")
    void validateAndSaveDNASubmittedWhenIsMutantTest() throws Exception {
        //Arrage
        String[] dna = new String[]{"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
        MutantDTO mutantDTO = new MutantDTO(dna);

        //Act - Assert
        this.mockMvc.perform(post("/v1/mutant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(mutantDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @DisplayName("Valida y guarda el ADN enviado al realizar la comprobación de que no es mutante retornando 403(Forbidden).")
    void validateAndSaveDNASubmittedWhenIsNotMutantTest() throws Exception {
        //Arrage
        String[] dna = new String[]{"ATGCTA", "CAGTTC", "TTATGT", "AGAATG", "CCCGTA", "TCACTG"};
        MutantDTO mutantDTO = new MutantDTO(dna);

        //Act - Assert
        this.mockMvc.perform(post("/v1/mutant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(mutantDTO)))
                .andExpect(status().isForbidden())
                .andExpect(content().json("{\"exceptionName\":\"HumanException\",\"message\":\"The DNA sent does not belong to a mutant.\"}"));

    }

    @Test
    @DisplayName("Valida que al consumir una url no reconocida, se retorne 404(Not found).")
    void validateDNAWrongUrlTest() throws Exception {
        //Arrange - act - assert
        mockMvc.perform(post("/v1/mumtant")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}
