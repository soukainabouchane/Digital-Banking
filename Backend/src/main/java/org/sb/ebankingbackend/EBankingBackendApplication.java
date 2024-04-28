package org.sb.ebankingbackend;

import org.sb.ebankingbackend.dtos.BankAccountDTO;
import org.sb.ebankingbackend.dtos.CurrentBankAccountDTO;
import org.sb.ebankingbackend.dtos.SavingBankAccountDTO;
import org.sb.ebankingbackend.entities.*;
import org.sb.ebankingbackend.exceptions.BalanceNotSufficientException;
import org.sb.ebankingbackend.exceptions.BankAccountNotFoundException;
import org.sb.ebankingbackend.exceptions.CustomerNotFoundException;
import org.sb.ebankingbackend.services.BankAcountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.util.List;
import java.util.stream.Stream;

@SpringBootApplication

public class EBankingBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(EBankingBackendApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(BankAcountService bankAccountService) {
		return args -> {

			Stream.of("Hassan", "Imane").forEach(
					name -> {
						Customer customer = new Customer();
						customer.setName(name);
						customer.setEmail(name + "@gmail.com");
						bankAccountService.saveCustomer(customer);
					}
			);
			bankAccountService.listCustomers().forEach(
					customer -> {
						try {
							bankAccountService.saveCurrentBankAccount(
									9000,
									9000,
									customer.getId() );

							bankAccountService.saveSavingBankAccount(
									12000,
									5.5,
									customer.getId() );



						}
						catch (CustomerNotFoundException e) {
								e.printStackTrace();
						}

					}
			);

			List<BankAccountDTO> bankAccounts = bankAccountService.bankAccountList();
			for (BankAccountDTO bankAccount : bankAccounts) {
				for (int i = 0; i < 10; i++) {

					String accountId;

					if(bankAccount instanceof SavingBankAccountDTO){
						accountId = ((SavingBankAccountDTO) bankAccount).getId();
					} else {
						accountId = ((CurrentBankAccountDTO) bankAccount).getId();
					}

					try {
						bankAccountService.credit(accountId,
								100 , "Credit");
					} catch (BalanceNotSufficientException e) {
						throw new RuntimeException(e);
					}
					try {
						bankAccountService.debit(accountId, 100, "Debit");
					} catch (BalanceNotSufficientException e) {
						throw new RuntimeException(e);
					}

				}
			}
		};
	}

	/*@Bean
	CommandLineRunner commandLineRunner(CustomerRepository customerRepository,
										BankAccountRepository bankAccountRepository,
										AccountOperationRepository accountOperationRepository){
		return args -> {
			BankAccount bankAccount =
					bankAccountRepository.findById("06b51f01-2345-458c-b134-c14912ae5c9e").orElse(null);

			System.out.println("********************");
			System.out.println(bankAccount.getId());
			System.out.println(bankAccount.getBalance());
			System.out.println(bankAccount.getStatus());
			System.out.println(bankAccount.getCreatedAt());
			System.out.println(bankAccount.getCustomer().getName());
			System.out.println(bankAccount.getClass().getSimpleName());

			if(bankAccount instanceof CurrentAccount){
				System.out.println("Over Dreaft => " + ((CurrentAccount)bankAccount).getOverDraft());
			} else if(bankAccount instanceof SavingAccount) {
				System.out.println("Rate => " + ((SavingAccount)bankAccount).getInterestRate());
			}
					bankAccount.getAccountOperations().forEach(
							op -> {
								System.out.println(op.getType()+"\t"+
										op.getOperationDate()+"\t"+op.getAmount());
							}
					);
		};
	}
	//@Bean
	CommandLineRunner start(CustomerRepository customerRepository,
							BankAccountRepository bankAccountRepository,
							AccountOperationRepository accountOperationRepository){

		return args -> {

			Stream.of("Hassan", "Yassine", "Aicha").forEach(
					name -> {
						Customer customer = new Customer();
						customer.setName(name);
						customer.setEmail(name + "@gmail.com");
						customerRepository.save(customer);
					}
			);

			customerRepository.findAll().forEach(
					cust -> {
						CurrentAccount currentAccount = new CurrentAccount();
						currentAccount.setId(UUID.randomUUID().toString());
						currentAccount.setBalance(Math.random()*9000);
						currentAccount.setCreatedAt(new Date());
						currentAccount.setStatus(AccountStatus.CREATED);
						currentAccount.setCustomer(cust);
						currentAccount.setOverDraft(90000);
						bankAccountRepository.save(currentAccount);

						SavingAccount savingAccount = new SavingAccount();
						savingAccount.setId(UUID.randomUUID().toString());
						savingAccount.setBalance(Math.random()*9000);
						savingAccount.setCreatedAt(new Date());
						savingAccount.setStatus(AccountStatus.CREATED);
						savingAccount.setCustomer(cust);
						savingAccount.setInterestRate(5.5);
						bankAccountRepository.save(savingAccount);
					}
			);

			bankAccountRepository.findAll().forEach(
					acc -> {
						for(int i=0; i<10; i++){
							AccountOperation accountOperation = new AccountOperation();
							accountOperation.setOperationDate(new Date());
							accountOperation.setAmount(Math.random()*12000);
							accountOperation.setType(Math.random()>0.5? OperationType.DEBIT: OperationType.CREDIT);
							accountOperation.setBankAccount(acc);
							accountOperationRepository.save(accountOperation);
						}
					}
			);



		};

	}

	 */

}
