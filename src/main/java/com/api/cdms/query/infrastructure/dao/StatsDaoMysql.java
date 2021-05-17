package com.api.cdms.query.infrastructure.dao;

import com.api.cdms.query.application.dto.StatsDTO;
import com.api.cdms.query.domain.dao.StatsDao;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Implementacion de la interface StatsDao especificamente con MySql.
 */
@Component
public class StatsDaoMysql implements StatsDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public StatsDaoMysql(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public StatsDTO getStats() {
        return this.namedParameterJdbcTemplate.query("SELECT @count_mutant_dna := SUM(CASE WHEN is_mutant = true THEN 1 ELSE 0 END) AS count_mutant_dna, @count_human_dna := SUM(CASE WHEN is_mutant = false THEN 1 ELSE 0 END) AS count_human_dna, CASE WHEN @count_human_dna <> 0 THEN @count_mutant_dna/@count_human_dna ELSE 0 END AS ratio FROM mutant", new StatsMapper()).get(0);
    }
}