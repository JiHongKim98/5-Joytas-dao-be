package com.example.daobe.common.config;

import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.MongoRepository;

@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(
        basePackages = {
                "com.example.daobe.common.outbox",
                "com.example.daobe.*.domain.repository",
        },
        excludeFilters = {
                @Filter(type = FilterType.ASSIGNABLE_TYPE, value = MongoRepository.class),
        }
)
public class JpaConfig {
}
