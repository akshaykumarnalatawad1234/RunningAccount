package dev.akshay.RunningAccount.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.akshay.RunningAccount.domain.RunningAccount;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    @JsonIgnore
    @ManyToOne
    private RunningAccount runningAccount;
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    private Date transactionDate;
    private double amount;
    private double pendingAmount;

    public Transaction(RunningAccount runningAccount, Date transactionDate, double amount, double pendingAmount) {
        this.runningAccount = runningAccount;
        this.transactionDate = transactionDate;
        this.amount = amount;
        this.pendingAmount = pendingAmount;
    }
}
