package com.hr.hrapplication.dao;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "EMPLOYEE", schema = "HR")
public class EmployeeEntity {

    @Id
    private String id;
    private String firstname;
    private String lastname;
    private String division;
    private String building;
    private String title;
    private String room;

    @CreatedDate
    private LocalDateTime createdat;

    @LastModifiedDate
    private LocalDateTime updatedat;

}
