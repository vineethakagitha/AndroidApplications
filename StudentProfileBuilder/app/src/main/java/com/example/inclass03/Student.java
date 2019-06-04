package com.example.inclass03;

import java.io.Serializable;

public class Student implements Serializable {

    String fname;
    String lname;
    String id;
    String dept;

    public Student(String fname, String lname, String id,String dept) {
        this.fname = fname;
        this.lname = lname;
        this.id = id;
        this.dept = dept;
    }

    @Override
    public String toString() {
        return "Student{" +
                "firstname='" + fname + '\'' +
                ", lastname='" + lname + '\'' +
                ", id='" + id + '\'' +
                ", department='" + dept + '\'' +
                '}';
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public String getId() {
        return id;
    }

    public String getDept() {
        return dept;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }
}
