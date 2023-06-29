package dev.akshay.RunningAccount.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class RunningAccount {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    private double creditLimit;
    private int interestFreePeriod;
    private double interestRate;
    private double debitAmount;
    @OneToMany(mappedBy = "runningAccount")
    private List<Transaction> transactions;
    @JsonIgnore
    @OneToOne
    User user;
    public RunningAccount(double creditLimit, int interestFreePeriod, int interestRate) {
        this.creditLimit = creditLimit;
        this.interestFreePeriod = interestFreePeriod;
        this.interestRate = interestRate;
        this.debitAmount = 0.0;
        this.transactions = new ArrayList<Transaction>();
    }

}
