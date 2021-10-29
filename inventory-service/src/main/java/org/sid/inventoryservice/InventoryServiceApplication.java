package org.sid.inventoryservice;

import org.sid.inventoryservice.entitie.Product;
import org.sid.inventoryservice.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;

@SpringBootApplication
public class InventoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }
    @Bean
    CommandLineRunner start(ProductRepository productRepository, RepositoryRestConfiguration restConfiguration){
        restConfiguration.exposeIdsFor(Product.class);
        return args -> {
            productRepository.save(new Product(null,"Laptop",12000,10));
            productRepository.save(new Product(null,"Desktop",10000,7));
            productRepository.save(new Product(null,"Smartphone",7000,12));
            productRepository.findAll().forEach(product -> {
                System.out.println(product.getName());
            });
        };
    }
}
