package com.api.cmds.command.domain.service;

import com.api.cdms.command.domain.entity.Mutant;
import com.api.cdms.command.domain.exception.HumanException;
import com.api.cdms.command.domain.exception.MutantException;
import com.api.cdms.command.domain.repository.MutantRepository;
import com.api.cdms.command.domain.service.MutantService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;

@RunWith(MockitoJUnitRunner.class)
class MutantServiceTest {

    @Mock
    private MutantRepository mutantRepository;

    private MutantService mutantService;

    @BeforeEach
    public void initialize() {
        MockitoAnnotations.openMocks(this);
        mutantService = new MutantService(mutantRepository);
    }

    @Test
    @DisplayName("Valida que cuando se envie un array con un ADN que sea de un mutante, este deuvelva true y guarde el ADN.")
    void serviceValidateDNASubmittedIsMutantTest() {
        //Arrange
        String[] dna = new String[]{"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
        Mutant mutant = new Mutant(dna, false);
        Mockito.doReturn(false).when(mutantRepository).exist(Arrays.toString(dna));
        Mockito.doNothing().when(mutantRepository).save(mutant);

        //Act
        boolean isMutant = mutantService.execute(dna);

        //Assert
        Assertions.assertTrue(isMutant);
        Mockito.verify(mutantRepository, Mockito.times(1)).save(ArgumentMatchers.any());
    }

    @Test
    @DisplayName("Valida que cuando se envie un array con un ADN que no sea de un mutante, se guarde el ADN y devuelva la excepcion esperada.")
    void serviceValidateDNASubmittedIsNotMutantTest() {
        //Arrange
        String[] dna = new String[]{"ATGCTA", "CAGTTC", "TTATGT", "AGAATG", "CCCGTA", "TCACTG"};
        Mutant mutant = new Mutant(dna, false);
        Mockito.doReturn(false).when(mutantRepository).exist(Arrays.toString(dna));
        Mockito.doNothing().when(mutantRepository).save(mutant);

        //Act
        Throwable exception = Assertions.assertThrows(HumanException.class, () -> mutantService.execute(dna));

        //Assert
        Mockito.verify(mutantRepository, Mockito.times(1)).save(ArgumentMatchers.any());
        Assertions.assertEquals("The DNA sent does not belong to a mutant.", exception.getMessage());
    }

    @Test
    @DisplayName("Valida que cuando se envie un array con un ADN que ya esta guardado en DB, se devuelva la excepcion esperada.")
    void validateExistingDNATest() {
        //Arrange
        String[] dna = new String[]{"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
        Mockito.doReturn(true).when(mutantRepository).exist(Arrays.toString(dna));

        //Act
        Throwable exception = Assertions.assertThrows(MutantException.class, () -> mutantService.execute(dna));

        //Assert
        Assertions.assertEquals("The DNA sent already exists.", exception.getMessage());
    }

}
