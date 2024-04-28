package org.sb.ebankingbackend.services;

import org.sb.ebankingbackend.dtos.*;
import org.sb.ebankingbackend.entities.CurrentAccount;
import org.sb.ebankingbackend.entities.Customer;
import org.sb.ebankingbackend.entities.SavingAccount;
import org.sb.ebankingbackend.exceptions.BalanceNotSufficientException;
import org.sb.ebankingbackend.exceptions.BankAccountNotFoundException;
import org.sb.ebankingbackend.exceptions.CustomerNotFoundException;
import org.springframework.web.bind.annotation.PathVariable;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;

public interface BankAcountService {

    public Customer saveCustomer(Customer customer);

    CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance,
                                                 double overDraft,
                                                 Long customerId) throws CustomerNotFoundException;

    SavingBankAccountDTO saveSavingBankAccount(double initialBalance,
                                               double interestRate,
                                               Long customerId) throws CustomerNotFoundException;

    CustomerDTO getCustomer(Long id);

    List<CustomerDTO> listCustomers();

    List<BankAccountDTO> bankAccountList();

    BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException;

    CurrentAccount getCurrentAccount(String accountId) throws AccountNotFoundException;

    SavingAccount getSavingAccount(String accountId) throws AccountNotFoundException;

    void debit();

    void debit(String accountId, double amount, String description) throws BankAccountNotFoundException,
            BalanceNotSufficientException;

    void credit(String accountId, double amount, String description) throws BalanceNotSufficientException,
            BankAccountNotFoundException;

    void transfer(String accountId, String accountIdDestination, double amount) throws BankAccountNotFoundException,
            BalanceNotSufficientException;

    CustomerDTO createCustomer(CustomerDTO customerDTO);

    CustomerDTO updateCustomer(Long customerId, CustomerDTO customerDTO);

    void deleteCustomer(Long id);



}
