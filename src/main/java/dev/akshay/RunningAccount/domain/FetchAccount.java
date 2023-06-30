package dev.akshay.RunningAccount.domain;

import lombok.Data;

import java.sql.Date;
import java.util.List;
import java.util.Map;

@Data
public class FetchAccount {
    private double dueAmount;
    private Map<Date, List<List<Double>>> dates;
    private double availableCredit;
    private double outstandingBalance;
}
