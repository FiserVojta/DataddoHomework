package com.dataddo.homework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.dataddo.homework.handler.DataddoFileHandler;

@Configuration
@ComponentScan(basePackages = "handler")
public class AppConfig {

    @Bean
    public DataddoFileHandler dataddoFileHandler() {
        return new DataddoFileHandler();
    }

}
