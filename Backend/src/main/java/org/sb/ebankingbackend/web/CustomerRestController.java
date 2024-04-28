package org.sb.ebankingbackend.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sb.ebankingbackend.dtos.CustomerDTO;
import org.sb.ebankingbackend.services.BankAcountService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
public class CustomerRestController {

    private BankAcountService bankAcountService;

    @GetMapping("/customers")
    public List<CustomerDTO> customers(){
        return bankAcountService.listCustomers();
    }

    @GetMapping("/customers/{id}")
    public CustomerDTO getCustomer(@PathVariable(name="id") Long customerId){
        return bankAcountService.getCustomer(customerId);
    }

    @PostMapping("/create")
    public CustomerDTO createCustomer(@RequestBody CustomerDTO customerDTO){
        return bankAcountService.createCustomer(customerDTO);
    }

    @PutMapping("update/{customerId}")
    public CustomerDTO updateCustomer(@PathVariable Long customerId,
                                      @RequestBody CustomerDTO customerDTO){
        return bankAcountService.updateCustomer(customerId, customerDTO);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteCustomer(@PathVariable Long id){
        bankAcountService.deleteCustomer(id);
    }


}
