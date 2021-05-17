package com.api.cmds.command.domain.validators;


import com.api.cdms.command.domain.exception.MutantException;
import com.api.cdms.command.domain.validators.ArrayValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ArrayValidatorTest {

    @Test
    @DisplayName("Valida que cuando el array sea null, devuelva la excepcion y el mensaje esperado.")
    void validateNullArray() {
        Throwable exception = Assertions.assertThrows(MutantException.class, () -> ArrayValidator.validateNullArray(null));
        Assertions.assertEquals("The DNA cannot be null.", exception.getMessage());
    }

    @Test
    @DisplayName("Valida que cuando el array no tenga elementos, devuelva la excepcion y el mensaje esperado.")
    void validateArrayWithoutElements() {
        Throwable exception = Assertions.assertThrows(MutantException.class, () -> ArrayValidator.validateNullArray(new String[]{}));
        Assertions.assertEquals("The DNA cannot be null.", exception.getMessage());
    }

    @Test
    @DisplayName("Valida que cuando el array no sea null, no se dispare la excepcion.")
    void validateArrayNotNull() {
        String[] arrayTest = new String[]{"ATCG", "ASFA"};
        Assertions.assertDoesNotThrow(() -> ArrayValidator.validateNullArray(arrayTest));
    }

    @Test
    @DisplayName("Valida que cuando se mande un array con String vacios, devuelva la excepcion y el mensaje esperado.")
    void validateEmptyArray() {
        String[] arrayTest = new String[]{"", ""};
        Throwable exception = Assertions.assertThrows(MutantException.class, () -> ArrayValidator.validateEmptyArray(arrayTest));
        Assertions.assertEquals("The DNA cannot be empty.", exception.getMessage());
    }

    @Test
    @DisplayName("Valida que cuando se mande un array con String llenos de espacios unicamente, devuelva la excepcion y el mensaje esperado.")
    void validateEmptyArrayWithSpaceOnlyElements() {
        String[] arrayTest = new String[]{"    ", "  "};
        Throwable exception = Assertions.assertThrows(MutantException.class, () -> ArrayValidator.validateEmptyArray(arrayTest));
        Assertions.assertEquals("The DNA cannot be empty.", exception.getMessage());
    }

    @Test
    @DisplayName("Valida que cuando se envia un array con valores, no se dispare la excepcion.")
    void validateNotEmptyArray() {
        String[] arrayTest = new String[]{"ATCG", "ASFA"};
        Assertions.assertDoesNotThrow(() -> ArrayValidator.validateEmptyArray(arrayTest));
    }

    @Test
    @DisplayName("Valida que cuando se mande un array con String de diferente longitud, devuelva la excepcion y el mensaje esperado.")
    void validateArrayWithoutAllSequencesSameLength() {
        String[] arrayTest = new String[]{"ATGCT", "ATC", "ATTTTTT"};
        Throwable exception = Assertions.assertThrows(MutantException.class, () -> ArrayValidator.allSequencesSameLength(arrayTest));
        Assertions.assertEquals("All sequences in the array must be the same size.", exception.getMessage());
    }

    @Test
    @DisplayName("Valida que cuando se mande un array con String de la misma longitud, no se dispare la excepcion.")
    void validateArrayWithAllSequencesSameLength() {
        String[] arrayTest = new String[]{"ATCG", "ASFA", "TSFV"};
        Assertions.assertDoesNotThrow(() -> ArrayValidator.allSequencesSameLength(arrayTest));
    }

    @Test
    @DisplayName("Valida que cuando se mande una secuencia con caracteres no validos, es decir, diferentes de (A,T,C,G), devuelva la excepcion y el mensaje esperado.")
    void validateSequenceWithInvalidCharacters() {
        String sequenceTest = "AATY";
        Throwable exception = Assertions.assertThrows(MutantException.class, () -> ArrayValidator.containsOnlyValidCharacters(sequenceTest));
        Assertions.assertEquals("The sequences of the matrix must consist only of the letters A,T,C,G.", exception.getMessage());
    }

    @Test
    @DisplayName("Valida que cuando se mande una secuencia que solo contenga caracteres permitidos, no se dispare la excepcion.")
    void validateArrayWithOnlyValidCharacters() {
        String sequenceTest = "AATG";
        Assertions.assertDoesNotThrow(() -> ArrayValidator.containsOnlyValidCharacters(sequenceTest));
    }

    @Test
    @DisplayName("Valida que cuando se mande una secuencia con con menos de 4 caracteres, devuelva la excepcion y el mensaje esperado.")
    void validateSequenceWithLessThanFourCharacters() {
        String sequenceTest = "AAT";
        Throwable exception = Assertions.assertThrows(MutantException.class, () -> ArrayValidator.sequenceHasMinimumLength(sequenceTest));
        Assertions.assertEquals(String.format("Sequences must have a minimum of %s characters.", ArrayValidator.MINIMUM_SEQUENCE_LENGTH), exception.getMessage());
    }

    @Test
    @DisplayName("Valida que cuando se mande una secuencia con 4 o mas caracteres, no se dispare la excepcion.")
    void validateSequenceGreaterThanOrEqualsToFourCharacters() {
        String sequenceTest = "AATG";
        Assertions.assertDoesNotThrow(() -> ArrayValidator.sequenceHasMinimumLength(sequenceTest));
    }
}
