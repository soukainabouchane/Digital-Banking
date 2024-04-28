package org.sb.ebankingbackend.dtos;

import lombok.Data;

import java.util.List;

@Data
public class AccountHistoryDTO {


    private String accountId;
    private double balance;

    private String type;

    private int currentPage;
    private int totalPages;

    private int pageSize;
    private List<AccountOperationDTO> accountOperationDTOList;

}
