package org.sb.ebankingbackend.services;

import org.sb.ebankingbackend.dtos.AccountHistoryDTO;
import org.sb.ebankingbackend.dtos.AccountOperationDTO;
import org.sb.ebankingbackend.exceptions.BankAccountNotFoundException;
import javax.security.auth.login.AccountNotFoundException;
import java.util.List;

public interface AccountOperationService {


    List<AccountOperationDTO> getAccountOperationByIdAccount(String accountId) throws AccountNotFoundException;

    AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException;
}
