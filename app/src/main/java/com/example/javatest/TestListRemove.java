package com.example.javatest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TestListRemove {
    public static void main(String args[]){
        List<Integer> list = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            list.add(i);
        }
        System.out.println(list);
        for(int i = 0; i < list.size();){
            if(list.get(i) % 2 == 0){
                list.remove(i);
            }else{
                i++;
            }
        }
        System.out.println(list);
    }
}
