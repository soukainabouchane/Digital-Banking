package org.sb.ebankingbackend.services.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.sb.ebankingbackend.dtos.AccountHistoryDTO;
import org.sb.ebankingbackend.dtos.AccountOperationDTO;
import org.sb.ebankingbackend.entities.AccountOperation;
import org.sb.ebankingbackend.entities.BankAccount;
import org.sb.ebankingbackend.exceptions.BankAccountNotFoundException;
import org.sb.ebankingbackend.mappers.AccountOperationMapper;
import org.sb.ebankingbackend.repositories.AccountOperationRepository;
import org.sb.ebankingbackend.repositories.BankAccountRepository;
import org.sb.ebankingbackend.services.AccountOperationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import javax.security.auth.login.AccountNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class AccountOperationServiceImpl implements AccountOperationService {

    private AccountOperationRepository accountOperationRepository;
    private BankAccountRepository bankAccountRepository;
    private AccountOperationMapper accountOperationMapper;

    /* @Override
    public List<AccountOperationDTO> getAccountOperation(String accountId) throws AccountNotFoundException {

        List<AccountOperationDTO> accountOperationDTOS = new ArrayList<>();

        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(
                () -> new AccountNotFoundException("Account not found Exception!"));

        // List<AccountOperation> accountOperations = accountOperationRepository.findById()

        return null;

    } */

    @Override
    public List<AccountOperationDTO> getAccountOperationByIdAccount(String accountId)
            throws AccountNotFoundException {

         List<AccountOperationDTO> accountOperationDTOS = new ArrayList<>();

         BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(
                () -> new AccountNotFoundException("Account not found Exception!"));

         List<AccountOperation> accountOperations =
                accountOperationRepository.findAccountOperationByBankAccount_Id(accountId);

         accountOperationDTOS = accountOperations.stream().map(
                op -> accountOperationMapper.fromAccountOperation(op))
                .collect(Collectors.toList());

        return accountOperationDTOS;
    }

    @Override
    public AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException {

        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElse(
                null
        );

        if(bankAccount == null)
            throw new BankAccountNotFoundException("Account not Found Exception");

        Page<AccountOperation> accountOperations =
                accountOperationRepository.findAccountOperationByBankAccount_Id(
                        accountId,
                        PageRequest.of(page, size));

        AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO();

        List<AccountOperationDTO> accountOperationDTOS =
        accountOperations.getContent().stream().map(
                op -> accountOperationMapper.fromAccountOperation(op)
        ).collect(Collectors.toList());

        accountHistoryDTO.setAccountOperationDTOList(accountOperationDTOS);
        accountHistoryDTO.setAccountId(bankAccount.getId());
        accountHistoryDTO.setBalance(bankAccount.getBalance());
        accountHistoryDTO.setCurrentPage(page);
        accountHistoryDTO.setPageSize(size);
        accountHistoryDTO.setTotalPages(accountOperations.getTotalPages());

        return  accountHistoryDTO;
    }
}
