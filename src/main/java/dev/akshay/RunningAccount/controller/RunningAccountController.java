package dev.akshay.RunningAccount.controller;

import dev.akshay.RunningAccount.domain.FetchAccountResponse;
import dev.akshay.RunningAccount.repository.RunningAccountRepository;
import dev.akshay.RunningAccount.repository.UserRepository;
import dev.akshay.RunningAccount.domain.RunningAccount;
import dev.akshay.RunningAccount.domain.User;
import dev.akshay.RunningAccount.serivce.RunningAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
public class RunningAccountController {
    @Autowired
    private RunningAccountService runningAccountService;
    @Autowired
    private RunningAccountRepository runningAccountRepository;
    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/api/v1/runningAccount/create", method = RequestMethod.POST)
    public ResponseEntity createRunningAccount(@RequestBody Map<String, String> payload) {
        int id = Integer.parseInt(payload.get("id"));
        User user = userRepository.findById(id);
        RunningAccount runningAccount = user.getRunningAccount();
        if (runningAccount != null) {
            return new ResponseEntity("Account already exists", HttpStatus.BAD_REQUEST);
        }
        runningAccount = new RunningAccount();
        double creditLimit = Double.parseDouble(payload.get("creditLimit"));
        int interestFreePeriod = Integer.parseInt(payload.get("interestFreePeriod"));
        double interestRate = Double.parseDouble(payload.get("interestRate"));

        runningAccount.setCreditLimit(creditLimit);
        runningAccount.setInterestFreePeriod(interestFreePeriod);
        runningAccount.setInterestRate(interestRate);
        runningAccount.setUser(user);
        user.setRunningAccount(runningAccount);
        runningAccountRepository.save(runningAccount);
        userRepository.save(user);
        return new ResponseEntity<>(runningAccount, HttpStatus.CREATED);
    }
    @RequestMapping(value = "/api/v1/runningAccount/credit", method = RequestMethod.POST)
    public ResponseEntity<RunningAccount> credit(@RequestBody Map<String, String> payload){
        int id = Integer.parseInt(payload.get("id"));
        double amount = Double.parseDouble(payload.get("amount"));
        RunningAccount runningAccount = runningAccountRepository.findById(id);
        if(runningAccount == null)
            return new ResponseEntity<RunningAccount>(HttpStatus.BAD_REQUEST);
        runningAccountService.credit(id, amount);
        return new ResponseEntity<RunningAccount>(runningAccount, HttpStatus.OK);
    }
    @RequestMapping(value = "/api/v1/runningAccount/debit", method = RequestMethod.POST)
    public ResponseEntity debit(@RequestBody Map<String, String> payload) {
        int id = Integer.parseInt(payload.get("id"));
        double amount = Double.parseDouble(payload.get("amount"));
        LocalDate transactionDate = LocalDate.parse(payload.get("transactionDate"));
        RunningAccount runningAccount = runningAccountRepository.findById(id);
        if (runningAccount == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        runningAccountService.debit(id, amount, Date.valueOf(transactionDate));
        return new ResponseEntity<>(runningAccount, HttpStatus.OK);
    }
    @RequestMapping(value = "/api/v1/runningAccount/fetchAccount/{id}", method = RequestMethod.GET)
    public ResponseEntity fetchAccount(@PathVariable int id){
        FetchAccountResponse fetchAccountResponse = new FetchAccountResponse();
        RunningAccount runningAccount = runningAccountRepository.findById(id);
        if(runningAccount == null)
            return new ResponseEntity("Account was not found", HttpStatus.BAD_REQUEST);
        double dueAmount = runningAccountService.totalDueAmount(id);
        Map<Date, List<Double>> dates = runningAccountService.dueDates(id);
        double availableCredit = runningAccountService.availableCredit(id);
        double outstandingBalance = runningAccountService.outstandingBalance(id);
        fetchAccountResponse.setDates(dates);
        fetchAccountResponse.setDueAmount(dueAmount);
        fetchAccountResponse.setAvailableCredit(availableCredit);
        fetchAccountResponse.setOutstandingBalance(outstandingBalance);

        return new ResponseEntity(fetchAccountResponse, HttpStatus.OK);
    }
}
