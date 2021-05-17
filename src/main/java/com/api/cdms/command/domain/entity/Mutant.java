package com.api.cdms.command.domain.entity;

import com.api.cdms.command.domain.validators.ArrayValidator;

import java.util.Arrays;

/**
 * Entidad que representa ADN analizado.
 */
public class Mutant {

    private String dna;
    private boolean isMutant;

    public Mutant(String[] dna, boolean isMutant) {
        ArrayValidator.validateNullArray(dna);
        ArrayValidator.validateEmptyArray(dna);
        ArrayValidator.allSequencesSameLength(dna);
        Arrays.stream(dna).forEach(sequence -> {
            ArrayValidator.containsOnlyValidCharacters(sequence);
            ArrayValidator.sequenceHasMinimumLength(sequence);
        });

        this.dna = Arrays.toString(dna);
        this.isMutant = isMutant;
    }

    public String getDna() {
        return dna;
    }

    public boolean isMutant() {
        return isMutant;
    }

    public void setMutant(boolean mutant) {
        isMutant = mutant;
    }

}