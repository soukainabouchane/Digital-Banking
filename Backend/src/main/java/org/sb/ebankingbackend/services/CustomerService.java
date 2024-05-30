package org.sb.ebankingbackend.services;

import org.sb.ebankingbackend.dtos.CustomerDTO;
import org.sb.ebankingbackend.entities.Customer;

import java.util.List;

public interface CustomerService {

    public Customer saveCustomer(Customer customer);

    CustomerDTO getCustomer(Long id);

    List<CustomerDTO> listCustomers();

    CustomerDTO createCustomer(CustomerDTO customerDTO);

    CustomerDTO updateCustomer(Long customerId, CustomerDTO customerDTO);

    void deleteCustomer(Long id);

    List<CustomerDTO> searchCustomers(String keyword);


}
