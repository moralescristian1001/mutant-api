package com.api.cdms.query.application.dto;

/**
 * Representa el objeto DTO de las estadísticas generales de los ADN analizados por la aplicación.
 */
public class StatsDTO {
    private int countMutantDna;
    private int countHumanDna;
    private double ratio;

    public StatsDTO(int countMutantDna, int countHumanDna, double ratio) {
        this.countMutantDna = countMutantDna;
        this.countHumanDna = countHumanDna;
        this.ratio = ratio;
    }

    public int getCountMutantDna() {
        return countMutantDna;
    }

    public int getCountHumanDna() {
        return countHumanDna;
    }

    public double getRatio() {
        return ratio;
    }
}
