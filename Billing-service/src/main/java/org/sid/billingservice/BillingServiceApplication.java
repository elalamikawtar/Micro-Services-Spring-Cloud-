package org.sid.billingservice;

import org.sid.billingservice.entitie.Bill;
import org.sid.billingservice.entitie.Customer;
import org.sid.billingservice.entitie.Product;
import org.sid.billingservice.entitie.ProductItem;
import org.sid.billingservice.feign.CustomerRestClient;
import org.sid.billingservice.feign.ProductItemRestClient;
import org.sid.billingservice.repositories.BillRepository;
import org.sid.billingservice.repositories.ProductItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.PagedModel;

import java.util.Collection;
import java.util.Date;
import java.util.Random;

@SpringBootApplication
@EnableFeignClients
public class BillingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BillingServiceApplication.class, args);
    }
    @Bean
    CommandLineRunner start(BillRepository billRepository,
                            ProductItemRepository productItemRepository,
                            CustomerRestClient customerRestClient,
                            ProductItemRestClient productItemRestClient){
        return args -> {

            Customer customer=customerRestClient.getCustomerById(1L);
            //System.out.println(customer.getName());
            Bill bill=billRepository.save(new Bill(null,new Date(),null,customer.getId(),null));
            PagedModel<Product> products=productItemRestClient.pageProducts();
            products.forEach(p ->{
                        ProductItem productItem=new ProductItem();
                        productItem.setPrice(p.getPrice());
                        productItem.setQuantity(1+new Random().nextInt(100));
                        productItem.setBill(bill);
                        productItem.setProductID(p.getId());
                        productItemRepository.save(productItem);
                    } );
        };
    }
}
