package org.sb.ebankingbackend.services.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sb.ebankingbackend.dtos.*;
import org.sb.ebankingbackend.entities.*;
import org.sb.ebankingbackend.entities.CurrentAccount;
import org.sb.ebankingbackend.enums.OperationType;
import org.sb.ebankingbackend.exceptions.BalanceNotSufficientException;
import org.sb.ebankingbackend.exceptions.BankAccountNotFoundException;
import org.sb.ebankingbackend.exceptions.CustomerNotFoundException;
import org.sb.ebankingbackend.mappers.BankAccountMapper;
import org.sb.ebankingbackend.mappers.CustomerMapper;
import org.sb.ebankingbackend.repositories.AccountOperationRepository;
import org.sb.ebankingbackend.repositories.BankAccountRepository;
import org.sb.ebankingbackend.repositories.CustomerRepository;
import org.sb.ebankingbackend.services.BankAcountService;
import org.springframework.stereotype.Service;
import javax.security.auth.login.AccountNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j // from lombok
public class BankAccountServiceImpl implements BankAcountService {

    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;
    private CustomerMapper customerMapper;
    private BankAccountMapper bankAccountMapper;



    @Override
    public CurrentBankAccountDTO
    saveCurrentBankAccount(double initialBalance,
                 double overDraft,
                 Long customerId) throws CustomerNotFoundException {

        CurrentAccount currentAccount;

        Customer customer = customerRepository.findById(customerId)
                .orElse(null);

        if(customer == null){
            throw new CustomerNotFoundException("Customer not found Exception");
        }

        currentAccount = new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedAt(new Date());
        currentAccount.setBalance(initialBalance);
        currentAccount.setOverDraft(overDraft);
        currentAccount.setCustomer(customer);

        CurrentAccount savedCurrentBankAccount = bankAccountRepository.save(currentAccount);

        return bankAccountMapper.fromCurrentBankAccount(savedCurrentBankAccount);

    }
    @Override
    public SavingBankAccountDTO saveSavingBankAccount(double initialBalance,
                                                      double interestRate,
                                                      Long customerId) throws CustomerNotFoundException {

         SavingAccount savingAccount;

        Customer customer = customerRepository.findById(customerId).orElse(null);

        if(customer == null){
            throw new CustomerNotFoundException("Customer not found Exception");
        }

        savingAccount = new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedAt(new Date());
        savingAccount.setBalance(initialBalance);
        savingAccount.setInterestRate(interestRate);
        savingAccount.setCustomer(customer);

        SavingAccount savedSavingBankAccount = bankAccountRepository.save(savingAccount);

        return bankAccountMapper.fromSavingBankAccount(savedSavingBankAccount);

    }

    @Override
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException {

        BankAccount bankAccount =
                bankAccountRepository.findById(accountId)
                        .orElseThrow(
                                () ->
                   new BankAccountNotFoundException("Bank Account not found!"));

        if(bankAccount instanceof SavingAccount){
            SavingAccount savingAccount = (SavingAccount) bankAccount;
            return bankAccountMapper.fromSavingBankAccount(savingAccount);
        } else {
            CurrentAccount currentAccount = (CurrentAccount) bankAccount;
            return bankAccountMapper.fromCurrentBankAccount(currentAccount);
        }

    }


    @Override
    public void debit(String accountId, double amount, String description)
            throws BankAccountNotFoundException, BalanceNotSufficientException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(
                () -> new BankAccountNotFoundException("Bank account not found!")
        );
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance() - amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void credit(String accountId, double amount, String description)
            throws BalanceNotSufficientException, BankAccountNotFoundException {

        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(
                () -> new BankAccountNotFoundException("Bank account not found!")
        );

        if(bankAccount.getBalance() < amount)
            throw new BalanceNotSufficientException("Balance not sufficient");

        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance() + amount);
        bankAccountRepository.save(bankAccount);

    }


    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount)
            throws BankAccountNotFoundException, BalanceNotSufficientException {
        debit(accountIdSource, amount, "Transfer to " + accountIdDestination);
        credit(accountIdDestination, amount, "Transfer from "+accountIdSource);
    }

   /* @Override
    public List<BankAccount> bankAccountList(){
        return bankAccountRepository.findAll();
    } */

    @Override
    public List<BankAccountDTO> bankAccountList(){

        List<BankAccount> bankAccounts = bankAccountRepository.findAll();

        List<BankAccountDTO> bankAccountDTOS = bankAccounts.stream().map(bankAccount -> {
            if (bankAccount instanceof SavingAccount) {
                SavingAccount savingAccount = (SavingAccount) bankAccount;
                return bankAccountMapper.fromSavingBankAccount(savingAccount);
            } else {
                CurrentAccount currentAccount = (CurrentAccount) bankAccount;
                return bankAccountMapper.fromCurrentBankAccount(currentAccount);
            }
        }).collect(Collectors.toList());

        return bankAccountDTOS;
    }


    @Override
    public CurrentAccount getCurrentAccount(String accoundId) throws AccountNotFoundException {
        CurrentAccount currentAccount = (CurrentAccount) bankAccountRepository.findById(accoundId).orElseThrow(
                () -> {

                    try {
                        throw new AccountNotFoundException("Account not found!");
                    } catch (AccountNotFoundException e) {
                        throw new RuntimeException(e);
                    }

                }
        );
        return currentAccount;
    }


    @Override
    public SavingAccount getSavingAccount(String accoundId) throws AccountNotFoundException {
        SavingAccount savingAccount = (SavingAccount) bankAccountRepository.findById(accoundId).orElseThrow(
                () -> {
                    try {
                        throw new AccountNotFoundException("Account not found Exception !");
                    } catch (AccountNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
        );

        return savingAccount;
    }



    /* @Override
    public CustomerDTO updateCustomer(Long customerId, CustomerDTO customerDTO) {

        log.info("Update customer");

        Customer customer = customerRepository.findById(customerId).orElseThrow(
                () -> new CustomerNotFoundException("Customer not found Exception")
        );

        return dtoMapper.fromCustomer(
                customerRepository.save(dtoMapper.fromCustomerDTO(customerDTO)));

    } */


}
