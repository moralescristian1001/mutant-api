package com.api.cdms.command.infrastructure.repository;

import com.api.cdms.command.domain.entity.Mutant;
import com.api.cdms.command.domain.repository.MutantRepository;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Implementacion de la interface MutantRepository especificamente con MySql.
 */
@Repository
public class MutantRepositoryMysql implements MutantRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public MutantRepositoryMysql(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public boolean exist(String dna) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("dna", dna);
        return this.namedParameterJdbcTemplate.queryForObject("select count(1) from mutant where dna = :dna", paramSource, Boolean.class);
    }

    @Override
    public void save(Mutant mutant) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("dna", mutant.getDna());
        paramSource.addValue("isMutant", mutant.isMutant());
        this.namedParameterJdbcTemplate.update("INSERT INTO mutant VALUES(:dna, :isMutant)", paramSource);
    }
}