package com.api.cdms.command.domain.service;

import com.api.cdms.command.domain.entity.Mutant;
import com.api.cdms.command.domain.enumerator.DirectionType;
import com.api.cdms.command.domain.exception.HumanException;
import com.api.cdms.command.domain.exception.MutantException;
import com.api.cdms.command.domain.repository.MutantRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MutantService {

    /**
     * Lista que contiene los caracteres validos que catalogan un GEN del ADN como mutante.
     */
    private static final List<String> VALID_DNA = Arrays.asList("AAAA", "TTTT", "CCCC", "GGGG");

    /**
     * Lista de posiciones que no deben volver a ser re evaluadas en el analisis de la matriz.
     */
    private List<String> positionsShouldNotBeReEvaluated;

    /**
     * Contador del número de secuencias de los GENES mutantes.
     */
    private int countSequence;

    /**
     * Número de caracteres que se deben tomar para formar la secuencia de caracteres a analizar.
     */
    private int numCharactersCompleteSequence = 3;

    private final MutantRepository mutantRepository;

    public MutantService(MutantRepository mutantRepository) {
        this.mutantRepository = mutantRepository;
    }

    /**
     * Método de entrada para la validación del ADN enviado.
     *
     * @param dna ADN a analizar.
     * @return boolean
     */
    public boolean execute(String[] dna) {
        positionsShouldNotBeReEvaluated = new ArrayList<>();
        countSequence = 0;

        Mutant mutant = new Mutant(dna, false);
        validateExistDna(dna);
        boolean isMutant = isMutant(dna);
        mutant.setMutant(isMutant);
        this.mutantRepository.save(mutant);
        if (isMutant) {
            return true;
        } else {
            throw new HumanException("The DNA sent does not belong to a mutant.");
        }
    }

    /**
     * Valida si el ADN enviado pertenece a un mutante o no.
     *
     * @param dna ADN a analizar.
     * @return boolean
     */
    private boolean isMutant(String[] dna) {
        String[][] matrixDna = createDnaMatrix(dna);
        return validateMatrix(matrixDna);
    }

    /**
     * Crea una matriz N*N con el ADN enviado.
     *
     * @param dna dna ADN a analizar.
     * @return String[][]
     */
    private String[][] createDnaMatrix(String[] dna) {
        int dnaArrayLength = dna.length;
        String[][] matrixDna = new String[dnaArrayLength][dnaArrayLength];
        for (int row = 0; row < dnaArrayLength; row++) {
            String[] rowDna = dna[row].split("");
            System.arraycopy(rowDna, 0, matrixDna[row], 0, dnaArrayLength);
        }
        return matrixDna;
    }

    /**
     * Valida cada pisición de la matriz en todas sus posibles direcciones para saber si se encuentran GENES mutantes.
     *
     * @param matrixDna matriz con el ADN a analizar.
     * @return boolean
     */
    private boolean validateMatrix(String[][] matrixDna) {
        int matrixDnaLength = matrixDna.length;
        for (int row = 0; row < matrixDnaLength; row++) {
            for (int column = 0; column < matrixDnaLength; column++) {
                if (countSequence > 1) {
                    return true;
                }
                validateHorizontalLeft(matrixDna, row, column);
                validateHorizontalRight(matrixDna, row, column);
                validateVerticalUp(matrixDna, row, column);
                validateVerticalDown(matrixDna, row, column);
                validateLowerLeftDiagonal(matrixDna, row, column);
                validateLowerRightDiagonal(matrixDna, row, column);
                validateUpperLeftDiagonal(matrixDna, row, column);
                validateUpperRightDiagonal(matrixDna, row, column);
            }
        }
        return false;
    }

    /**
     * Valida si en la posición actual de la matriz se puede evaluar su horizontal izquierda sin desbordamiento.
     *
     * @param matrixDna matriz con el ADN.
     * @param row       fila actual.
     * @param column    columna actual.
     */
    private void validateHorizontalLeft(String[][] matrixDna, int row, int column) {
        boolean hasLeftHorizontal = row - numCharactersCompleteSequence >= 0;
        if (hasLeftHorizontal) {
            boolean hasCoincidences = hasCoincidencesInitialFinalCharacter(matrixDna[row][column], matrixDna[row - numCharactersCompleteSequence][column]);
            if (hasCoincidences) {
                List<String> listPositions = createListCurrentPositions(row, column, DirectionType.HORIZONTAL_LEFT);
                if (!positionsShouldNotBeReEvaluated.contains(listPositions.toString())) {
                    String sequence = matrixDna[row][column] + matrixDna[row - 1][column] + matrixDna[row - 2][column] + matrixDna[row - 3][column];
                    boolean mutantSequence = validateGeneSequence(sequence);
                    if (mutantSequence) {
                        addPositionsEvaluated(listPositions);
                    }
                }
            }
        }
    }

    /**
     * Valida si en la posición actual de la matriz se puede evaluar su horizontal derecha sin desbordamiento.
     *
     * @param matrixDna matriz con el ADN.
     * @param row       fila actual.
     * @param column    columna actual.
     */
    private void validateHorizontalRight(String[][] matrixDna, int row, int column) {
        boolean hasRightHorizontal = row + numCharactersCompleteSequence < matrixDna.length;
        if (hasRightHorizontal) {
            boolean hasCoincidences = hasCoincidencesInitialFinalCharacter(matrixDna[row][column], matrixDna[row + numCharactersCompleteSequence][column]);
            if (hasCoincidences) {
                List<String> listPositions = createListCurrentPositions(row, column, DirectionType.HORIZONTAL_RIGHT);
                if (!positionsShouldNotBeReEvaluated.contains(listPositions.toString())) {
                    String sequence = matrixDna[row][column] + matrixDna[row + 1][column] + matrixDna[row + 2][column] + matrixDna[row + 3][column];
                    boolean mutantSequence = validateGeneSequence(sequence);
                    if (mutantSequence) {
                        addPositionsEvaluated(listPositions);
                    }
                }
            }
        }
    }

    /**
     * Valida si en la posición actual de la matriz se puede evaluar su vertical hacia arriba sin desbordamiento.
     *
     * @param matrixDna matriz con el ADN.
     * @param row       fila actual.
     * @param column    columna actual.
     */
    private void validateVerticalUp(String[][] matrixDna, int row, int column) {
        boolean hasUpVertical = column - numCharactersCompleteSequence >= 0;
        if (hasUpVertical) {
            boolean hasCoincidences = hasCoincidencesInitialFinalCharacter(matrixDna[row][column], matrixDna[row][column - numCharactersCompleteSequence]);
            if (hasCoincidences) {
                List<String> listPositions = createListCurrentPositions(row, column, DirectionType.VERTICAL_UP);
                if (!positionsShouldNotBeReEvaluated.contains(listPositions.toString())) {
                    String sequence = matrixDna[row][column] + matrixDna[row][column - 1] + matrixDna[row][column - 2] + matrixDna[row][column - 3];
                    boolean mutantSequence = validateGeneSequence(sequence);
                    if (mutantSequence) {
                        addPositionsEvaluated(listPositions);
                    }
                }
            }
        }
    }

    /**
     * Valida si en la posición actual de la matriz se puede evaluar su vertical hacia abajo sin desbordamiento.
     *
     * @param matrixDna matriz con el ADN.
     * @param row       fila actual.
     * @param column    columna actual.
     */
    private void validateVerticalDown(String[][] matrixDna, int row, int column) {
        boolean hasDownVertical = column + numCharactersCompleteSequence < matrixDna.length;
        if (hasDownVertical) {
            boolean hasCoincidences = hasCoincidencesInitialFinalCharacter(matrixDna[row][column], matrixDna[row][column + numCharactersCompleteSequence]);
            if (hasCoincidences) {
                List<String> listPositions = createListCurrentPositions(row, column, DirectionType.VERTICAL_DOWN);
                if (!positionsShouldNotBeReEvaluated.contains(listPositions.toString())) {
                    String sequence = matrixDna[row][column] + matrixDna[row][column + 1] + matrixDna[row][column + 2] + matrixDna[row][column + 3];
                    boolean mutantSequence = validateGeneSequence(sequence);
                    if (mutantSequence) {
                        addPositionsEvaluated(listPositions);

                    }
                }
            }
        }
    }

    /**
     * Valida si en la posición actual de la matriz se puede evaluar su diagonal inferior derecha sin desbordamiento.
     *
     * @param matrixDna matriz con el ADN.
     * @param row       fila actual.
     * @param column    columna actual.
     */
    private void validateLowerRightDiagonal(String[][] matrixDna, int row, int column) {
        boolean hasLowerRightDiagonal = (row + numCharactersCompleteSequence < matrixDna.length && column + numCharactersCompleteSequence < matrixDna.length);
        if (hasLowerRightDiagonal) {
            boolean hasCoincidences = hasCoincidencesInitialFinalCharacter(matrixDna[row][column], matrixDna[row + numCharactersCompleteSequence][column + numCharactersCompleteSequence]);
            if (hasCoincidences) {
                List<String> listPositions = createListCurrentPositions(row, column, DirectionType.LOWER_RIGHT_DIAGONAL);
                if (!positionsShouldNotBeReEvaluated.contains(listPositions.toString())) {
                    String sequence = matrixDna[row][column] + matrixDna[row + 1][column + 1] + matrixDna[row + 2][column + 2] + matrixDna[row + 3][column + 3];
                    boolean mutantSequence = validateGeneSequence(sequence);
                    if (mutantSequence) {
                        addPositionsEvaluated(listPositions);
                    }
                }
            }
        }
    }

    /**
     * Valida si en la posición actual de la matriz se puede evaluar su diagonal inferior izquierda sin desbordamiento.
     *
     * @param matrixDna matriz con el ADN.
     * @param row       fila actual.
     * @param column    columna actual.
     */
    private void validateLowerLeftDiagonal(String[][] matrixDna, int row, int column) {
        boolean hasLowerLeftDiagonal = (row - numCharactersCompleteSequence >= 0 && column + numCharactersCompleteSequence < matrixDna.length);
        if (hasLowerLeftDiagonal) {
            boolean hasCoincidences = hasCoincidencesInitialFinalCharacter(matrixDna[row][column], matrixDna[row - numCharactersCompleteSequence][column + numCharactersCompleteSequence]);
            if (hasCoincidences) {
                List<String> listPositions = createListCurrentPositions(row, column, DirectionType.LOWER_LEFT_DIAGONAL);
                if (!positionsShouldNotBeReEvaluated.contains(listPositions.toString())) {
                    String sequence = matrixDna[row][column] + matrixDna[row - 1][column + 1] + matrixDna[row - 2][column + 2] + matrixDna[row - 3][column + 3];
                    boolean mutantSequence = validateGeneSequence(sequence);
                    if (mutantSequence) {
                        addPositionsEvaluated(listPositions);
                    }
                }
            }
        }
    }

    /**
     * Valida si en la posición actual de la matriz se puede evaluar su diagonal superior derecha sin desbordamiento.
     *
     * @param matrixDna matriz con el ADN.
     * @param row       fila actual.
     * @param column    columna actual.
     */
    private void validateUpperRightDiagonal(String[][] matrixDna, int row, int column) {
        boolean hasUpperRightDiagonal = (row + numCharactersCompleteSequence < matrixDna.length && column - numCharactersCompleteSequence >= 0);
        if (hasUpperRightDiagonal) {
            boolean hasCoincidences = hasCoincidencesInitialFinalCharacter(matrixDna[row][column], matrixDna[row + numCharactersCompleteSequence][column - numCharactersCompleteSequence]);
            if (hasCoincidences) {
                List<String> listPositions = createListCurrentPositions(row, column, DirectionType.UPPER_RIGHT_DIAGONAL);
                if (!positionsShouldNotBeReEvaluated.contains(listPositions.toString())) {
                    String sequence = matrixDna[row][column] + matrixDna[row + 1][column - 1] + matrixDna[row + 2][column - 2] + matrixDna[row + 3][column - 3];
                    boolean mutantSequence = validateGeneSequence(sequence);
                    if (mutantSequence) {
                        addPositionsEvaluated(listPositions);
                    }
                }
            }
        }
    }

    /**
     * Valida si en la posición actual de la matriz se puede evaluar su diagonal superior izquierda sin desbordamiento.
     *
     * @param matrixDna matriz con el ADN.
     * @param row       fila actual.
     * @param column    columna actual.
     */
    private void validateUpperLeftDiagonal(String[][] matrixDna, int row, int column) {
        boolean hasUpperLeftDiagonal = (row - numCharactersCompleteSequence >= 0 && column - numCharactersCompleteSequence >= 0);
        if (hasUpperLeftDiagonal) {
            boolean hasCoincidences = hasCoincidencesInitialFinalCharacter(matrixDna[row][column], matrixDna[row - numCharactersCompleteSequence][column - numCharactersCompleteSequence]);
            if (hasCoincidences) {
                List<String> listPositions = createListCurrentPositions(row, column, DirectionType.UPPER_LEFT_DIAGONAL);
                if (!positionsShouldNotBeReEvaluated.contains(listPositions.toString())) {
                    String sequence = matrixDna[row][column] + matrixDna[row - 1][column - 1] + matrixDna[row - 2][column - 2] + matrixDna[row - 3][column - 3];
                    boolean mutantSequence = validateGeneSequence(sequence);
                    if (mutantSequence) {
                        addPositionsEvaluated(listPositions);
                    }
                }
            }
        }
    }

    /**
     * Verifica que la letra de la posición inicial si coincida con la letra de la posición final,
     * con el fin de poder encontrar pistas sobre si vale la pena o no verificar la cadena de genes.
     *
     * @param start letra inicial.
     * @param end   letra final.
     * @return boolean
     */
    private boolean hasCoincidencesInitialFinalCharacter(String start, String end) {
        return start.equals(end);
    }

    /**
     * Devuelve una lista con las posiciones analizadas de la matriz.
     *
     * @param row           fila actual
     * @param column        columna actual
     * @param directionType dirección del recorrido.
     * @return List<String>
     */
    private List<String> createListCurrentPositions(int row, int column, DirectionType directionType) {
        List<String> listPositions = new ArrayList<>();
        listPositions.add(row + "" + column);
        switch (directionType) {
            case HORIZONTAL_LEFT:
                listPositions.add((row - 1) + "" + column);
                listPositions.add((row - 2) + "" + column);
                listPositions.add((row - 3) + "" + column);
                break;
            case HORIZONTAL_RIGHT:
                listPositions.add((row + 1) + "" + column);
                listPositions.add((row + 2) + "" + column);
                listPositions.add((row + 3) + "" + column);
                break;
            case VERTICAL_UP:
                listPositions.add(row + "" + (column - 1));
                listPositions.add(row + "" + (column - 2));
                listPositions.add(row + "" + (column - 3));
                break;
            case VERTICAL_DOWN:
                listPositions.add(row + "" + (column + 1));
                listPositions.add(row + "" + (column + 2));
                listPositions.add(row + "" + (column + 3));
                break;
            case LOWER_RIGHT_DIAGONAL:
                listPositions.add((row + 1) + "" + (column + 1));
                listPositions.add((row + 2) + "" + (column + 2));
                listPositions.add((row + 3) + "" + (column + 3));
                break;
            case LOWER_LEFT_DIAGONAL:
                listPositions.add((row - 1) + "" + (column + 1));
                listPositions.add((row - 2) + "" + (column + 2));
                listPositions.add((row - 3) + "" + (column + 3));
                break;
            case UPPER_RIGHT_DIAGONAL:
                listPositions.add((row + 1) + "" + (column - 1));
                listPositions.add((row + 2) + "" + (column - 2));
                listPositions.add((row + 3) + "" + (column - 3));
                break;
            case UPPER_LEFT_DIAGONAL:
                listPositions.add((row - 1) + "" + (column - 1));
                listPositions.add((row - 2) + "" + (column - 2));
                listPositions.add((row - 3) + "" + (column - 3));
                break;
            default:
                break;
        }
        return listPositions;
    }

    /**
     * Valida una secuencia de caracteres para saber si coinciden con el adn de un mutante.
     *
     * @param sequence caracteres a validar.
     * @return boolean
     */
    private boolean validateGeneSequence(String sequence) {
        if (VALID_DNA.contains(sequence)) {
            countSequence++;
            return true;
        }
        return false;
    }

    /**
     * Agrega las posiciones evaluadas y su inversa cuando se encuentra un gen mutante, con el fin de que estas no se vuelvan a evaluar.
     *
     * @param listPositions lista de posiciones evaluadas.
     */
    private void addPositionsEvaluated(List<String> listPositions) {
        positionsShouldNotBeReEvaluated.add(listPositions.toString());
        Collections.reverse(listPositions);
        positionsShouldNotBeReEvaluated.add(listPositions.toString());
    }

    /**
     * Valida si el ADN enviado ya esta registrado en la DB y de esta manera tener un registro por ADN.
     *
     * @param dna ADN a validar
     */
    private void validateExistDna(String[] dna) {
        boolean exist = this.mutantRepository.exist(Arrays.toString(dna));
        if (exist) {
            throw new MutantException("The DNA sent already exists.");
        }
    }
}
