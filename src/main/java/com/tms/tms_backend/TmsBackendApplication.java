package com.tms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = "com.tms")
@EnableJpaRepositories(basePackages = "com.tms.repository")
@EnableCaching
public class TmsBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(TmsBackendApplication.class, args);
	}

	@Bean
	public CacheManager cacheManager() {
		// Simple in-memory cache manager for development/testing
		return new ConcurrentMapCacheManager("employees", "employee");
	}

}
