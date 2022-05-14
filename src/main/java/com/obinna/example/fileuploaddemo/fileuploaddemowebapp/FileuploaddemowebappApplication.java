package com.obinna.example.fileuploaddemo.fileuploaddemowebapp;

import com.obinna.example.fileuploaddemo.fileuploaddemowebapp.config.StorageConfigProperties;
import com.obinna.example.fileuploaddemo.fileuploaddemowebapp.service.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(StorageConfigProperties.class)
public class FileuploaddemowebappApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileuploaddemowebappApplication.class, args);
	}

	@Bean
	CommandLineRunner init(StorageService storageService) {
		return args -> {
			storageService.deleteAll();
			storageService.init();
		};
	}

}
