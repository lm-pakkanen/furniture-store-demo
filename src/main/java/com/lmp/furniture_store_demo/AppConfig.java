package com.lmp.furniture_store_demo;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Generate beans from controllers
 */
@Configuration
@ComponentScan(basePackages = "com.lmp.furniture_store_demo.controllers")
public class AppConfig {
}