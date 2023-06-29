package dev.akshay.RunningAccount.controller;

import dev.akshay.RunningAccount.domain.DummyAccount;
import dev.akshay.RunningAccount.domain.FetchAccount;
import dev.akshay.RunningAccount.domain.Field;
import dev.akshay.RunningAccount.domain.RunningAccount;
import dev.akshay.RunningAccount.repository.RunningAccountRepository;
import dev.akshay.RunningAccount.repository.UserRepository;
import dev.akshay.RunningAccount.serivce.RunningAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RunningAccountController {
    @Autowired
    private RunningAccountService runningAccountService;
    @Autowired
    private RunningAccountRepository runningAccountRepository;
    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/api/v1/runningAccount/create", method = RequestMethod.POST)
    public ResponseEntity createAccount(@RequestBody DummyAccount dummyAccount) {
         RunningAccount runningAccount = runningAccountService.create(dummyAccount);
         if(runningAccount == null)
              return new ResponseEntity("Account might already exits for the user or user doesn't exist", HttpStatus.BAD_REQUEST);
        return new ResponseEntity(runningAccount, HttpStatus.CREATED);
    }
    @RequestMapping(value = "/api/v1/runningAccount/credit", method = RequestMethod.POST)
    public ResponseEntity credit(@RequestBody Field field){
        RunningAccount runningAccount = runningAccountService.credit(field);
        if(runningAccount == null)
            return new ResponseEntity<String>("Account doesn't exit",HttpStatus.BAD_REQUEST);
        return new ResponseEntity<RunningAccount>(runningAccount, HttpStatus.OK);
    }
    @RequestMapping(value = "/api/v1/runningAccount/debit", method = RequestMethod.POST)
    public ResponseEntity debit(@RequestBody Field field) {

        RunningAccount runningAccount = runningAccountService.debit(field);
        if (runningAccount == null)
            return new ResponseEntity<String>("Account doesn't exit or Amount you entered can't be debited", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<RunningAccount>(runningAccount, HttpStatus.OK);
    }
    @RequestMapping(value = "/api/v1/runningAccount/fetchAccount/{id}", method = RequestMethod.GET)
    public ResponseEntity fetchAccount(@PathVariable int id){
        FetchAccount fetchAccount = runningAccountService.fetchResponse(id);

        if(fetchAccount == null){
            new ResponseEntity("Account was not found", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(fetchAccount, HttpStatus.OK);
    }
}
