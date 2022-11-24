package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);


	}

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository, CardRepository cardRepository){
		return args -> {



			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com", passwordEncoder.encode("123456"));
			Client client2 = new Client("Lucas", "Molina", "lucas@gmail.com", passwordEncoder.encode("lucas123"));
			Client client3 = new Client("Ludmila", "Fernandez", "ludmila@gmail.com", passwordEncoder.encode("lud123"));
			Client client4 = new Client("ADMIN", "ADMIN", "BANCOADMINMINDHUB@mindhub.com", passwordEncoder.encode("123456"));


			clientRepository.save(client1);
			clientRepository.save(client2);
			clientRepository.save(client3);
			clientRepository.save(client4);

			Account account1 = new Account("VIN-00000001", LocalDateTime.now(), 5000.00, AccountType.CURRENT, true);
			Account account2 = new Account("VIN-00000002", LocalDateTime.now().plusDays(1), 7000.00, AccountType.SAVING, true);
			Account account3 = new Account("VIN-00000003", LocalDateTime.now().plusDays(2), 6000.0, AccountType.CURRENT, true);
			Account account4 = new Account("VIN-00000004", LocalDateTime.now().plusDays(2), 4500.0, AccountType.SAVING, true);
			Account account5 = new Account("VIN-00000005", LocalDateTime.now(), 8000.0, AccountType.CURRENT, true);

			client1.addAccount(account1);
			client1.addAccount(account2);
			client2.addAccount(account3);
			client2.addAccount(account4);
			client3.addAccount(account5);
			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);
			accountRepository.save(account4);
			accountRepository.save(account5);

			Transaction transaction1 = new Transaction(TransactionType.CREDIT, 8000.0, "venta mercadolibre", LocalDateTime.now());
			Transaction transaction2 = new Transaction(TransactionType.DEBIT, -2000.0, "compra mac", LocalDateTime.now().plusHours(3));
			Transaction transaction3 = new Transaction(TransactionType.DEBIT, -4500.0, "compra steam", LocalDateTime.now().plusDays(2).plusHours(4));
			Transaction transaction4 = new Transaction(TransactionType.DEBIT, -2000.0, "pedidos ya", LocalDateTime.now().plusDays(3).plusHours(8));
			Transaction transaction5 = new Transaction(TransactionType.DEBIT, -1800.0, "pedidos ya", LocalDateTime.now().plusDays(5).plusHours(7));
			Transaction transaction6 = new Transaction(TransactionType.DEBIT, -1200.0, "pedidos ya", LocalDateTime.now().plusDays(6).plusHours(15));

			account1.addTransaction(transaction1);
			account1.addTransaction(transaction2);
			account2.addTransaction(transaction3);
			account1.addTransaction(transaction4);
			account1.addTransaction(transaction5);
			account1.addTransaction(transaction6);
			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);
			transactionRepository.save(transaction4);
			transactionRepository.save(transaction5);
			transactionRepository.save(transaction6);

			Loan loan1 = new Loan("Hipotecario", 500000.0, List.of(12,24,36,48,60));
			Loan loan2 = new Loan("Personal", 100000.0, List.of(6,12,24));
			Loan loan3 = new Loan("Automotriz", 300000.0, List.of(6,12,24,36));

			loanRepository.save(loan1);
			loanRepository.save(loan2);
			loanRepository.save(loan3);

			ClientLoan clientLoan1 = new ClientLoan(400000.0, 60, client1,loan1);
			ClientLoan clientLoan2 = new ClientLoan(50000.0, 12, client1,loan2);
			ClientLoan clientLoan3 = new ClientLoan(100000.0, 24, client2,loan2);
			ClientLoan clientLoan4 = new ClientLoan(200000.0, 36, client2,loan3);

			clientLoanRepository.save(clientLoan1);
			clientLoanRepository.save(clientLoan2);
			clientLoanRepository.save(clientLoan3);
			clientLoanRepository.save(clientLoan4);

			Card card1 = new Card(client1.getFirstName()+ " " + client1.getLastName(), CardType.DEBIT, CardColor.GOLD, "4845-2152-6594-2561", 654, LocalDate.now(), LocalDate.now().plusYears(5), true);
			Card card2 = new Card(client1.getFirstName()+ " " + client1.getLastName(), CardType.CREDIT, CardColor.TITANIUM, "6584-5986-2456-2591", 233, LocalDate.of(2016,11,11), LocalDate.of(2021,11,11), true);
			Card card3 = new Card(client2.getFirstName()+ " " + client2.getLastName(), CardType.CREDIT, CardColor.SILVER, "6523-5215-8451-6591", 842, LocalDate.now(), LocalDate.now().plusYears(5), true);

			client1.addCard(card1);
			client1.addCard(card2);
			client2.addCard(card3);

			cardRepository.save(card1);
			cardRepository.save(card2);
			cardRepository.save(card3);


		};
	}

}
