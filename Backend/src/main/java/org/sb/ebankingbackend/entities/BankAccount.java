package org.sb.ebankingbackend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sb.ebankingbackend.enums.AccountStatus;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class BankAccount {


    @Id
    private String id;
    private double sold;
    private Date createdAt;
    private AccountStatus status;
    @ManyToOne
    private Customer customer;

    @OneToMany(mappedBy = "bankAccount ")
    private List<AccountOperation> accountOperations;


}
