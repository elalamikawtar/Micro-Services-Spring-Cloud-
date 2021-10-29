package org.sid.billingservice.web;

import org.sid.billingservice.entitie.Bill;
import org.sid.billingservice.entitie.Customer;
import org.sid.billingservice.entitie.Product;
import org.sid.billingservice.feign.CustomerRestClient;
import org.sid.billingservice.feign.ProductItemRestClient;
import org.sid.billingservice.repositories.BillRepository;
import org.sid.billingservice.repositories.ProductItemRepository;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BillingRestController {
    private BillRepository billRepository;
    private ProductItemRepository productItemRepositor;
    private CustomerRestClient customerRestClient;
    private ProductItemRestClient productItemRestClient;

    public BillingRestController(BillRepository billRepository, ProductItemRepository productItemRepositor, CustomerRestClient customerRestClient, ProductItemRestClient productItemRestClient) {
        this.billRepository = billRepository;
        this.productItemRepositor = productItemRepositor;
        this.customerRestClient = customerRestClient;
        this.productItemRestClient = productItemRestClient;
    }

    @GetMapping(path = "/fullBill/{id}")
    public Bill getBill(@PathVariable(name = "id") Long id){
     Bill bill=billRepository.findById(id).get();
     Customer customer=customerRestClient.getCustomerById(bill.getCustomerID());
     bill.setCustomer(customer);
     bill.getProductItems().forEach(productItem ->{
         Product product=productItemRestClient.getProductById(productItem.getProductID());
         productItem.setProduct(product);
     });
     return bill;
    }
}
