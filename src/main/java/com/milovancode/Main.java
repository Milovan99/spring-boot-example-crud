package com.milovancode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@SpringBootApplication
@RestController
@RequestMapping("api/v1/customers")
public class Main {

    private final CustomerRepository customerRepository;

    public Main(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class,args);
    }

    @GetMapping
    public List<Customer> getCustomers(){
        return customerRepository.findAll();
    }

    record NewCustomerRequest(
            Integer id,
            String name,
            String email,
            Integer age){}

    @PostMapping
    public void addCustomer(@RequestBody NewCustomerRequest request){
            Customer customer=new Customer();
            customer.setName(request.name);
            customer.setAge(request.age);
            customer.setEmail(request.email);
            customerRepository.save(customer);
    }

    @DeleteMapping("{customerId}")
    public void deleteCustomerById(@PathVariable("customerId") Integer id){
        customerRepository.deleteById(id);
    }
    @DeleteMapping()
    public void deleteCustomer(@RequestBody NewCustomerRequest request){
        Customer customer=new Customer();
        customer.setId(request.id);
        customer.setName(request.name);
        customer.setAge(request.age);
        customer.setEmail(request.email);
        customerRepository.delete(customer);
    }

    

    @PutMapping("/{customerId}")
    public ResponseEntity<String> updateCustomer(@RequestBody NewCustomerRequest request, @PathVariable Integer customerId) {
        if (customerId == null || request == null) {
            return ResponseEntity.badRequest().body("customerId and request body cannot be null.");
        }

        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        if (!optionalCustomer.isPresent()) {
            return ResponseEntity.badRequest().body("Customer with id " + customerId + " does not exist.");
        }

        Customer customer = optionalCustomer.get();
        customer.setName(request.name);
        customer.setAge(request.age);
        customer.setEmail(request.email);

        customerRepository.save(customer);

        return ResponseEntity.ok("Customer with id " + customerId + " updated successfully.");
    }

}
