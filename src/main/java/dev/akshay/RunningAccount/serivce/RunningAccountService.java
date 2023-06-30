package dev.akshay.RunningAccount.serivce;

import dev.akshay.RunningAccount.domain.*;
import dev.akshay.RunningAccount.repository.RunningAccountRepository;
import dev.akshay.RunningAccount.repository.TransactionRepository;
import dev.akshay.RunningAccount.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class RunningAccountService {
    @Autowired
    private RunningAccountRepository runningAccountRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserRepository userRepository;

    public RunningAccount create(AccountHolder accountHolder){
        User user = userRepository.findById(accountHolder.getId());
        RunningAccount runningAccount = user.getRunningAccount();

        if(user == null || runningAccount != null){
            return null;
        }
        runningAccount = new RunningAccount();

        runningAccount.setCreditLimit(accountHolder.getCreditLimit());
        runningAccount.setInterestFreePeriod(accountHolder.getInterestFreePeriod());
        runningAccount.setInterestRate(accountHolder.getInterestRate());
        runningAccount.setUser(user);
        user.setRunningAccount(runningAccount);
        runningAccountRepository.save(runningAccount);
        userRepository.save(user);

        return runningAccount;
    }

    public RunningAccount debit(Field field){
        int id = field.getId();
        double amount = field.getAmount();
        Date transactionDate = field.getTransactionDate();
        RunningAccount runningAccount = runningAccountRepository.findById(id);

        double debitAmount = runningAccount.getDebitAmount();
        if(runningAccount!= null && debitAmount + amount <= runningAccount.getCreditLimit()){
            double pendingAmount = amount;
            if(debitAmount<0) {
                pendingAmount += debitAmount;
                runningAccount.setDebitAmount(pendingAmount);
            }
            else{
                runningAccount.setDebitAmount(debitAmount + amount);
            }
            Transaction cur = new Transaction(runningAccount, transactionDate, amount, pendingAmount);
            transactionRepository.save(cur);
//            return new ResponseEntity<>(runningAccount, HttpStatus.OK);
            return runningAccount;
        }
            return null;
    }

    public RunningAccount credit(Field field){
        int id = field.getId();
        double amount = field.getAmount();

        RunningAccount runningAccount = runningAccountRepository.findById(id);
        if(runningAccount != null && amount > 0) {
            List<Transaction> allTransactions = transactionRepository.findAllByRunningAccount(runningAccount);
            Collections.sort(allTransactions, Comparator.comparing(Transaction::getTransactionDate));
            double leftAmount = amount;
            double debitAmount = runningAccount.getDebitAmount();
            runningAccount.setDebitAmount(debitAmount - leftAmount);

            for (Transaction curTransaction : allTransactions) {
                if (curTransaction.getPendingAmount() == 0) continue;
                double curAmount = curTransaction.getPendingAmount();
                Date curTransactionDate = curTransaction.getTransactionDate();
                double interest = getInterest(curAmount, curTransactionDate, runningAccount);
                double totalAmount = curAmount + interest;
                curTransaction.setPendingAmount(totalAmount);
                if (totalAmount >= leftAmount && leftAmount > 0) {
                    curTransaction.setPendingAmount(curTransaction.getPendingAmount() - leftAmount);
                    leftAmount -= totalAmount;
                } else if (totalAmount < leftAmount && leftAmount > 0) {
                    curTransaction.setPendingAmount(0);
                    leftAmount -= totalAmount;
                }
                runningAccount.setDebitAmount(runningAccount.getDebitAmount()+interest);
                transactionRepository.save(curTransaction);
            }
            runningAccountRepository.save(runningAccount);
//            return new ResponseEntity<>(runningAccount, HttpStatus.OK);
            return runningAccount;
        }
        return null;
    }

    public double getInterest(double amount, Date transactionDate, RunningAccount runningAccount){
        double interest = 0;
        LocalDate expiryDate = transactionDate.toLocalDate().plusDays(runningAccount.getInterestFreePeriod());
        if(expiryDate.compareTo(LocalDate.now()) < 0){
            int noOfDays = (int) Math.abs(ChronoUnit.DAYS.between(transactionDate.toLocalDate(), LocalDate.now()));
            double rate = runningAccount.getInterestRate();
            interest = (amount*((double)noOfDays/365)*rate)/100;
        }
        return interest;
    }

    public double totalDueAmount(int id){
        RunningAccount runningAccount = runningAccountRepository.findById(id);
        List<Transaction> allTrasactions = transactionRepository.findAllByRunningAccount(runningAccount);
        double totalAmount = 0;
        for(Transaction transaction : allTrasactions){
            double interest = getInterest(transaction.getPendingAmount(), transaction.getTransactionDate(), runningAccount);
            totalAmount += transaction.getPendingAmount()+interest;
        }
//        runningAccount.setDebitAmount(totalAmount);
        return totalAmount;
    }
    public Map<Date, List<Double>> dueDates(int id){
        RunningAccount runningAccount = runningAccountRepository.findById(id);
        List<Transaction> allTrasactions = transactionRepository.findAllByRunningAccount(runningAccount);
        Map<Date, List<Double>> map = new HashMap<>();
        for(Transaction transaction : allTrasactions){
            Date curTransactionDate = transaction.getTransactionDate();
            map.put(curTransactionDate, new ArrayList<>());
            map.get(curTransactionDate).add(transaction.getAmount());
            map.get(curTransactionDate).add(transaction.getPendingAmount());
        }
        return map;
    }
    public double availableCredit(int id){
        RunningAccount runningAccount = runningAccountRepository.findById(id);
        double debitAmount = runningAccount.getDebitAmount();
        if(debitAmount > 0)
            return runningAccount.getCreditLimit() - debitAmount;
        else return runningAccount.getCreditLimit();
    }
    public double outstandingBalance(int id){
        RunningAccount runningAccount = runningAccountRepository.findById(id);
        return runningAccount.getDebitAmount();
    }
    public FetchAccount fetchResponse(int id){
        FetchAccount fetchAccountResponse = new FetchAccount();
        RunningAccount runningAccount = runningAccountRepository.findById(id);
        if(runningAccount == null)
            return null;
        double dueAmount = totalDueAmount(id);
        Map<Date, List<Double>> dates = dueDates(id);
        double availableCredit = availableCredit(id);
        double outstandingBalance = outstandingBalance(id);
        fetchAccountResponse.setDates(dates);
        fetchAccountResponse.setDueAmount(dueAmount);
        fetchAccountResponse.setAvailableCredit(availableCredit);
        fetchAccountResponse.setOutstandingBalance(outstandingBalance);

        return fetchAccountResponse;
    }
}
