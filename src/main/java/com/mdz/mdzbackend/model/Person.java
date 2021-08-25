package com.mdz.mdzbackend.model;

import java.sql.Date;

public class Person {
    private long id;
    private String name;
    private Date dob;
    private float salary;
    private long age;

    public Person() {

    }
    public Person(long id, String name, Date dob, float salary, long age) {
        this.id = id;
        this.name = name;
        this.dob = dob;
        this.salary = salary;
        this.age = age;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public float getSalary() {
        return salary;
    }

    public void setSalary(float salary) {
        this.salary = salary;
    }

    public long getAge() {
        return age;
    }

    public void setAge(long age) {
        this.age = age;
    }



}
