package org.sb.ebankingbackend.web;

import org.sb.ebankingbackend.dtos.*;
import org.sb.ebankingbackend.entities.CurrentAccount;
import org.sb.ebankingbackend.entities.SavingAccount;
import org.sb.ebankingbackend.exceptions.BalanceNotSufficientException;
import org.sb.ebankingbackend.exceptions.BankAccountNotFoundException;
import org.sb.ebankingbackend.mappers.BankAccountMapper;
import org.sb.ebankingbackend.services.BankAcountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;

@RestController
@CrossOrigin("*")

public class BankAccountRestController {

    private BankAcountService bankAcountService;

    private BankAccountMapper bankAccountMapper;

     @Autowired
    public BankAccountRestController(BankAcountService bankAcountService,
                                     BankAccountMapper bankAccountMapper) {
        this.bankAcountService = bankAcountService;
        this.bankAccountMapper = bankAccountMapper;
    }

    @GetMapping("/accounts")
    public List<BankAccountDTO> getBankAccounts() {
        List<BankAccountDTO> bankAccountsList = bankAcountService.bankAccountList();
        return bankAccountsList;
    }

    @GetMapping("/account/{id}")
    public BankAccountDTO getBankAccount(@PathVariable String id)
            throws BankAccountNotFoundException, AccountNotFoundException {
        BankAccountDTO bankAccount = bankAcountService.getBankAccount(id);
        return bankAccount;
    }

    @GetMapping("/currentAccount/{accountId}")
    public CurrentBankAccountDTO getCurrentBankAccount(@PathVariable String accountId)
            throws BankAccountNotFoundException, AccountNotFoundException {
        CurrentAccount currentAccount = bankAcountService.getCurrentAccount(accountId);
       return bankAccountMapper.toCurrentBankAccountDTO(currentAccount);
    }

    @GetMapping("/savingAccount/{accountId}")
    public SavingBankAccountDTO getSavingBankAccount(@PathVariable String accountId)
            throws AccountNotFoundException{
        SavingAccount savingAccount = bankAcountService.getSavingAccount(accountId);
        return bankAccountMapper.toSavingBankAccountDto(savingAccount);
    }

    @PostMapping("/accounts/debit")
    public void debit(@RequestBody DebitDTO debitDTO) throws BankAccountNotFoundException, BalanceNotSufficientException {
        this.bankAcountService.debit(debitDTO.getAccountId(), debitDTO.getAmount(), debitDTO.getDescription());
    }

    @PostMapping("/accounts/credit")
    public void credit(@RequestBody CreditDTO creditDTO) throws BankAccountNotFoundException, BalanceNotSufficientException {
         this.bankAcountService.credit(creditDTO.getAccountId(), creditDTO.getAmount(), creditDTO.getDescription());
    }

    @PostMapping("/accounts/transfer")
    public void transfert(@RequestBody TransferRequestDTO transferDTO) throws BankAccountNotFoundException, BalanceNotSufficientException {
         this.bankAcountService.transfer(transferDTO.getAccountSource(), transferDTO.getAccountDestination(), transferDTO.getAmount() );
    }

}
