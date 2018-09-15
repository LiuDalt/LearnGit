package com.example.javatest;

public class TestEqual {
    public static class Abc{}

    public static void main(String args[]){
        Abc a = new Abc();
        Abc b = new Abc();
        Abc c = a;
        System.out.println((a==b) + " " + (a==c));
    }
}
