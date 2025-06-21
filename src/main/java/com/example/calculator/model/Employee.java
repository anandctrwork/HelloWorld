package com.example.calculator.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import javax.xml.datatype.XMLGregorianCalendar;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Employee {

    @XmlElement(required = true)
    private String name;

    private int age;

    @XmlElement(required = true)
    private String department;

    @XmlElement(required = true)
    @XmlSchemaType(name = "date") // Represents the date part, can also be "dateTime"
    private XMLGregorianCalendar joinDate;

    // JAXB requires a no-arg constructor
    public Employee() {
    }

    public Employee(String name, int age, String department, XMLGregorianCalendar joinDate) {
        this.name = name;
        this.age = age;
        this.department = department;
        this.joinDate = joinDate;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public XMLGregorianCalendar getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(XMLGregorianCalendar joinDate) {
        this.joinDate = joinDate;
    }

    // toString() method for easier debugging (optional)
    @Override
    public String toString() {
        return "Employee{" +
               "name='" + name + '\'' +
               ", age=" + age +
               ", department='" + department + '\'' +
               ", joinDate=" + (joinDate != null ? joinDate.toString() : "null") +
               '}';
    }
}
