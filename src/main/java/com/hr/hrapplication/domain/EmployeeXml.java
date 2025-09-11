package com.hr.hrapplication.domain;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Getter;

@Getter
@XmlAccessorType(XmlAccessType.FIELD)
public class EmployeeXml {

    @XmlAttribute(name = "id")
    String id;

    @XmlElement(name = "firstname")
    String firstname;

    @XmlElement(name = "lastname")
    String lastname;
    String division;
    String building;
    String title;
    String room;
}
