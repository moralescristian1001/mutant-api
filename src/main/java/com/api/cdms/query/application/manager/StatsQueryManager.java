package com.api.cdms.query.application.manager;

import com.api.cdms.query.application.dto.StatsDTO;
import com.api.cdms.query.domain.dao.StatsDao;
import org.springframework.stereotype.Component;

/**
 * Caso de uso: Consultar.
 */
@Component
public class StatsQueryManager {

    private final StatsDao statsDao;

    public StatsQueryManager(StatsDao statsDao) {
        this.statsDao = statsDao;
    }

    public StatsDTO execute() {
        return statsDao.getStats();
    }
}
