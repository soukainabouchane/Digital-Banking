package org.sb.ebankingbackend.web;

import lombok.AllArgsConstructor;
import org.sb.ebankingbackend.dtos.AccountHistoryDTO;
import org.sb.ebankingbackend.dtos.AccountOperationDTO;
import org.sb.ebankingbackend.exceptions.BankAccountNotFoundException;
import org.sb.ebankingbackend.services.AccountOperationService;
import org.springframework.web.bind.annotation.*;
import javax.security.auth.login.AccountNotFoundException;
import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin("*")

public class AccountOperationRestController {

    private AccountOperationService accountOperationService;

    @GetMapping("/accounts/{accountId}/operations")
    public List<AccountOperationDTO> getAccountOperationsHistory(@PathVariable String accountId)
            throws AccountNotFoundException {
        List<AccountOperationDTO> accountOperationDTOS =
                accountOperationService.getAccountOperationByIdAccount(accountId);
        return accountOperationDTOS;
    }

    @GetMapping("/accounts/{accountId}/pageOperations")
    public AccountHistoryDTO getAccountHistory(
            @PathVariable String accountId,
            @RequestParam(name="page", defaultValue="0") int page,
            @RequestParam(name="size", defaultValue = "5") int size
    ) throws AccountNotFoundException, BankAccountNotFoundException {
        return accountOperationService.getAccountHistory(accountId, page, size);
    }






}
