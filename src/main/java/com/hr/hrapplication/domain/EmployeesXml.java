package com.hr.hrapplication.domain;

import jakarta.xml.bind.annotation.*;
import lombok.Getter;

import java.util.List;

@Getter
@XmlRootElement(name = "employees")
@XmlAccessorType(XmlAccessType.FIELD)
public class EmployeesXml {
    @XmlElement(name = "employee")
    List<EmployeeXml> employees;
}


