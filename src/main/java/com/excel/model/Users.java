package com.excel.model;

public class Users {

    String name;
    double age;
    String town;

    public Users(String name, String town, double age) {
        this.name = name;
        this.town = town;
        this.age = age;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAge() {
        return age;
    }

    public void setAge(double age) {
        this.age = age;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }
}
