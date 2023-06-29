package dev.akshay.RunningAccount.domain;

import dev.akshay.RunningAccount.domain.RunningAccount;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    private String name;
    private Date dob;
    private String address;
    @OneToOne
    private RunningAccount runningAccount;
    public User(String name, Date dob, String address, RunningAccount runningAccount) {
        this.name = name;
        this.dob = dob;
        this.address = address;
        this.runningAccount = null;
    }
}
