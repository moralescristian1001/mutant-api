package com.api.cdms.query.domain.dao;

import com.api.cdms.query.application.dto.StatsDTO;

/**
 * Interfaz de estadísticas, en la cual se definen las firmas de los métodos a utilizar.
 */
public interface StatsDao {

    /**
     * Permite consultar las estadísticas de las verificaciones de los ADN
     *
     * @return StatsDTO
     */
    StatsDTO getStats();
}
