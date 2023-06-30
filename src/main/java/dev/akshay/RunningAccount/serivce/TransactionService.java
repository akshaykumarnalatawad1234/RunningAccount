package dev.akshay.RunningAccount.serivce;

import dev.akshay.RunningAccount.domain.RunningAccount;
import dev.akshay.RunningAccount.domain.Transaction;
import dev.akshay.RunningAccount.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    public void setRepository(Transaction transaction){
        transactionRepository.save(transaction);
    }
    public List<Transaction> getTransactionByRunningAccount(RunningAccount runningAccount){
        return transactionRepository.findAllByRunningAccount(runningAccount);
    }
}
