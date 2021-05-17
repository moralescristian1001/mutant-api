package com.api.cdms.command.domain.repository;

import com.api.cdms.command.domain.entity.Mutant;

/**
 * Repositorio de mutantes, en el cual se definen las firmas de los métodos a utilizar.
 */
public interface MutantRepository {

    /**
     * Permite verificar si el ADN enviado ya existe
     *
     * @param dna a verificar.
     * @return si existe o no
     */
    boolean exist(String dna);

    /**
     * Permite guardar el registro enviado
     *
     * @param mutant mutante a guardar.
     */
    void save(Mutant mutant);
}
