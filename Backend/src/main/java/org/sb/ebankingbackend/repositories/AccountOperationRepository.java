package org.sb.ebankingbackend.repositories;

import org.sb.ebankingbackend.entities.AccountOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AccountOperationRepository extends JpaRepository<AccountOperation, Long> {

    public List<AccountOperation> findAccountOperationByBankAccount_Id(String accountId);

    public Page<AccountOperation> findByBankAccountIdOrderByOperationDateDesc(String accountId,
                                                                       Pageable pageable);

}
