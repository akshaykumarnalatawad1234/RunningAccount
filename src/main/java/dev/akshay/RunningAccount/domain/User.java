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
    private String firstName;
    private String middleName;
    private String lastName;
    private Date dob;
    private String address;
    @OneToOne
    private RunningAccount runningAccount;

}
