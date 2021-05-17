package com.api.cdms.command.application.manager;

import com.api.cdms.command.domain.service.MutantService;
import org.springframework.stereotype.Component;

/**
 * Caso de uso: Guardar.
 */
@Component
public class MutantSaveManager {

    private final MutantService mutantService;

    public MutantSaveManager(MutantService mutantService) {
        this.mutantService = mutantService;
    }

    public boolean execute(String[] dna) {
        return this.mutantService.execute(dna);
    }
}
