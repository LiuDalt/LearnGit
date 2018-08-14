package com.example.javatest;

import java.util.ArrayList;
import java.util.Iterator;

public class TestArray {
    public static void main(String[] args){
        ArrayList list = new ArrayList();
        for(int i = 0; i < 10; i++){
            list.add(i);
        }
        Iterator<Integer> iterator = list.iterator();
        while (iterator.hasNext()){
            if(iterator.next() % 2 == 0){
                iterator.remove();
            }
        }
        System.out.println(list);
    }
}
