package com.exercise.criptocurrency.config;

import com.exercise.criptocurrency.service.CryptoNameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class SchedulerConfig {

    @Autowired
    private CryptoNameService cryptoNameService;

    @Scheduled(fixedDelay = 86400000) // 1 día
    public void populateCryptoNames() {
        cryptoNameService.populateCryptoNames();
    }
}
