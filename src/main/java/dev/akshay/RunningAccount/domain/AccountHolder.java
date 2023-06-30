package dev.akshay.RunningAccount.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountHolder {
    private int id;
    private double creditLimit;
    private int interestFreePeriod;
    private double interestRate;
}
