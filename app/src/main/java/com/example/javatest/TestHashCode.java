package com.example.javatest;

import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TestHashCode {
    public static void main(String[] args) {
        Test t1 = new Test(100);
        Test t2 = new Test(100);
        System.out.println(t1.equals(t2));

        List<Test> list = new ArrayList<>();
        list.add(t1);
        System.out.println(list.contains(t2) + "  " + list.indexOf(t2));
    }

    static class  Test{
        int id;

        public Test(int id) {
            this.id = id;
        }

        @Override
        public int hashCode() {
            return id;
        }

        @Override
        public boolean equals(Object obj) {
            if(obj instanceof Test) {
                return id == ((Test)obj).id;
            }
            return super.equals(obj);
        }
    }
}
