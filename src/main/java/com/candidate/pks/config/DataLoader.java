package com.candidate.pks.config;

import com.candidate.pks.employee.service.EmployeeSyncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DataLoader implements CommandLineRunner {

    @Autowired
    private EmployeeSyncService employeeSyncService;

    @Override
    public void run(String... args) throws Exception {
        log.info("Starting employee synchronization at server startup...");
        employeeSyncService.syncEmployees();
        log.info("Employee synchronization completed.");
    }
}
