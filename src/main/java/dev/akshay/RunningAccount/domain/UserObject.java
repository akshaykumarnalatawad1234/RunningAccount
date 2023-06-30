package dev.akshay.RunningAccount.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserObject {
    private String fullName;
    private Date dob;
    private String address;
}
