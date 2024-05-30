package org.sb.ebankingbackend.services.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sb.ebankingbackend.dtos.CustomerDTO;
import org.sb.ebankingbackend.entities.Customer;
import org.sb.ebankingbackend.exceptions.CustomerNotFoundException;
import org.sb.ebankingbackend.mappers.CustomerMapper;
import org.sb.ebankingbackend.repositories.CustomerRepository;
import org.sb.ebankingbackend.services.CustomerService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private CustomerRepository customerRepository;
    private CustomerMapper customerMapper;

    @Override
    public Customer saveCustomer(Customer customer) {
        log.info("Saving new Customer!");
        Customer savedCustomer = customerRepository.save(customer);
        return savedCustomer;
    }

    @Override
    public CustomerDTO getCustomer(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(
                ( ) -> new CustomerNotFoundException("Customer Not Found!")
        );
        CustomerDTO customerDTO = customerMapper.fromCustomer(customer);
        return customerDTO;
    }

    @Override
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        log.info("Saving new Customer");
        Customer customer =  customerRepository.save(customerMapper.fromCustomerDTO(customerDTO));
        return customerMapper.fromCustomer(customer);
    }


    @Override
    public List<CustomerDTO> listCustomers() {
        List<Customer> customers = customerRepository.findAll();

        List<CustomerDTO> customerDTOS = customers.stream().map(
                customer -> customerMapper.fromCustomer(customer)
        ).collect(Collectors.toList());

        /* List<CustomerDTO> customerDTOS = new ArrayList<>();
        for(Customer customer : customers){
            CustomerDTO customerDTO = dtoMapper.fromCustomer(customer);
            customerDTOS.add(customerDTO);
        } */
        return customerDTOS;
    }

    @Override
    public CustomerDTO updateCustomer(Long customerId, CustomerDTO customerDTO) {
        log.info("Update customer");

        // Retrieve the existing customer from the database
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found Exception"));

        // Update the existing customer entity with values from customerDTO
        // You can use setters or BeanUtils.copyProperties() as shown in the previous example
        customer.setName(customerDTO.getName());
        customer.setEmail(customerDTO.getEmail());
        // Update other properties similarly...

        // Save the updated customer entity back to the database
        customer = customerRepository.save(customer);

        // Convert the updated customer entity back to DTO and return
        return customerMapper.fromCustomer(customer);
    }

    @Override
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(
                () -> new CustomerNotFoundException("Customer not found Exception"));
        customerRepository.deleteById(id);
    }

    @Override
    public List<CustomerDTO> searchCustomers(String keyword) {

        List<Customer> listCustomers = customerRepository.searchCustomers(keyword);
        List<CustomerDTO> customersDTO = listCustomers.stream().map(
                customer -> customerMapper.fromCustomer(customer)
        ).collect(Collectors.toList());
        return customersDTO;
    }
}
