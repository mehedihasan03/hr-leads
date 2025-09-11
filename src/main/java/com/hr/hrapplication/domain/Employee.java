package com.hr.hrapplication.domain;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    private String id;
    private String firstname;
    private String lastname;
    private String division;
    private String building;
    private String title;
    private String room;
    private LocalDateTime createdat;
    private LocalDateTime updatedat;
}
