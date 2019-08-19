package com.example.javatest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

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

        ArrayList listLimit = new ArrayList(3);
        listLimit.add(1);
        listLimit.add(2);
        listLimit.add(3);
        listLimit.add(4);
        listLimit.add(5);
        System.out.print(listLimit);

        ArrayList<String> list1 = new ArrayList();
        ArrayList<String> list2 = new ArrayList<>();
        list2.add("ddd");
        list1.add("aaaa");
        list1.add("bbb");
        list1.addAll(2, list2);
        System.out.println(list1);

        List<Integer> compareList = new ArrayList<>();
        compareList.add(3);
        compareList.add(1);
        compareList.add(2);
        compareList.add(4);
        compareList.add(0);
        compareList.add(3);
        Collections.sort(compareList, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 < o2 ? -1: 1;
            }
        });
        System.out.println(compareList);

        List<Integer> listA = new ArrayList<>();
        listA.add(1);
        listA.add(2);
        listA.add(3);
        listA.add(4);
        System.out.println(listA.subList(0, 3));
    }
}
