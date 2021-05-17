package com.api.cdms.command.application.dto;

/**
 * Representa el objeto DTO de entrada de los datos del ADN.
 */
public class MutantDTO {

    private String[] dna;

    public MutantDTO(String[] dna) {
        this.dna = dna;
    }

    public MutantDTO() {
    }

    public String[] getDna() {
        return dna;
    }
}