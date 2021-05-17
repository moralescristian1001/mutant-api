package com.api.cdms.query.infrastructure.dao;

import com.api.cdms.query.application.dto.StatsDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clase que mapea los atributos de la base de datos a el DTO de estadísticas.
 */
public class StatsMapper implements RowMapper<StatsDTO> {

    @Override
    public StatsDTO mapRow(ResultSet resultSet, int rowNum) throws SQLException {

        int countMutantDna = resultSet.getInt("count_mutant_dna");
        int countHumanDna = resultSet.getInt("count_human_dna");
        double ratio = resultSet.getDouble("ratio");

        return new StatsDTO(countMutantDna, countHumanDna, ratio);
    }

}
