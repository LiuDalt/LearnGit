package com.example.javatest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

        List<Integer> list1 = new ArrayList<>();
        list1.add(3);
        list1.add(2);
        list1.add(1);
        list1.add(100);
        System.out.println(list1.contains(100) + "   " + list1.contains(new Integer(100)));
        System.out.println(list1);
        list1.remove(new Integer(100));
        System.out.println(list1);

        long a = 10232343;
        System.out.println("" + ((short)(a / 1000)));

        List<Integer> list2 = new ArrayList<>();
        list2.add(2);
        list2.add(1);
        Collections.sort(list2, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 > o2 ? 1 : -1;
            }
        });
        System.out.println(list2);
    }
}
