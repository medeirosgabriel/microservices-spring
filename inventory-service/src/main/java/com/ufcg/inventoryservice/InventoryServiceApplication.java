package com.ufcg.inventoryservice;

import com.ufcg.inventoryservice.model.Inventory;
import com.ufcg.inventoryservice.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableEurekaClient
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadData(InventoryRepository inventoryRepository) {
		return args -> {
			Inventory inventory1 = Inventory.builder().skuCode("iphone").quantity(10).build();
			Inventory inventory2 = Inventory.builder().skuCode("S20").quantity(30).build();
			inventoryRepository.save(inventory1);
			inventoryRepository.save(inventory2);
		};
	}
}
