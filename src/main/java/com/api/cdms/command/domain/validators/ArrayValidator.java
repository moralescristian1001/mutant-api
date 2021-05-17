package com.api.cdms.command.domain.validators;

import com.api.cdms.command.domain.exception.MutantException;

import java.util.Arrays;
import java.util.Optional;

/**
 * Clase utilitaria para validar arreglos (principalmente el arreglo del ADN).
 */
public class ArrayValidator {

    private ArrayValidator() {
    }

    /**
     * Longitud minima de una secuencia para que sea valida.
     */
    public static final int MINIMUM_SEQUENCE_LENGTH = 4;

    /**
     * Valida que el arreglo enviado no sea vacio.
     *
     * @param array arreglo a verificar.
     */
    public static void validateNullArray(String[] array) {
        if (array == null || array.length == 0) {
            throw new MutantException("The DNA cannot be null.");
        }
    }

    /**
     * Valida que los string del arreglo no sean vacios.
     *
     * @param array arreglo a verificar.
     */
    public static void validateEmptyArray(String[] array) {
        if (Arrays.stream(array).anyMatch(sequence -> sequence.trim().equals(""))) {
            throw new MutantException("The DNA cannot be empty.");
        }
    }

    /**
     * Valida que todas las secuencias del array sean del mismo tamaño para asegurar siempre una matriz cuadrada.
     *
     * @param array secuencia a validar.
     */
    public static void allSequencesSameLength(String[] array) {
        Optional<String> firstSequence = Arrays.stream(array).findFirst();
        if (firstSequence.isPresent()) {
            int lengthFirstSequence = firstSequence.get().length();
            if (!Arrays.stream(array).allMatch(sequence -> sequence.length() == lengthFirstSequence)) {
                throw new MutantException("All sequences in the array must be the same size.");
            }
        }
    }

    /**
     * Valida que la secuencia del arreglo solo tenga los caracteres validos de un ADN (A,T,C,G).
     *
     * @param sequence secuencia a validar.
     */
    public static void containsOnlyValidCharacters(String sequence) {
        if (!sequence.matches("^[ATCG]*$")) {
            throw new MutantException("The sequences of the matrix must consist only of the letters A,T,C,G.");
        }
    }

    /**
     * Valida que las secuencias minimo tengan 4 de longitud para poder que sea una matriz minimo de 4x4.
     *
     * @param sequence secuencia a validar.
     */
    public static void sequenceHasMinimumLength(String sequence) {
        if (sequence.split("").length < MINIMUM_SEQUENCE_LENGTH) {
            throw new MutantException(String.format("Sequences must have a minimum of %s characters.", MINIMUM_SEQUENCE_LENGTH));
        }
    }
}
