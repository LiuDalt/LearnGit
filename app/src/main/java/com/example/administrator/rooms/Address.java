package com.example.administrator.rooms;

public class Address {
    public String city;


    public Address(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "city:" + city;
    }
}
