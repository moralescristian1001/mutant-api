package com.api.cdms.command.infrastructure.configuration;

import com.api.cdms.command.domain.repository.MutantRepository;
import com.api.cdms.command.domain.service.MutantService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceBean {

    @Bean
    public MutantService mutantService(MutantRepository mutantRepository) {
        return new MutantService(mutantRepository);
    }
}
