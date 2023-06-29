package dev.akshay.RunningAccount.repository;

import dev.akshay.RunningAccount.domain.RunningAccount;
import dev.akshay.RunningAccount.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Integer>{
    List<Transaction> findAllByRunningAccount(RunningAccount runningAccount);
}
