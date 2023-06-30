package dev.akshay.RunningAccount.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Field {
    private int id;
    private double amount;
    private Date transactionDate;
}
