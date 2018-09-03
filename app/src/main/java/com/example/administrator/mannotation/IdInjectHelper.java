package com.example.administrator.mannotation;

import android.app.Activity;
import android.view.View;

import com.example.administrator.myapplication.MainActivity;

import java.lang.reflect.Field;

public class IdInjectHelper {
    public static void inject(Activity activity){
        Class cla = activity.getClass();
        Field fields[] = cla.getDeclaredFields();
        for(Field field : fields){
            if(field.isAnnotationPresent(IdInject.class)){
                try {
                    IdInject inject = field.getAnnotation(IdInject.class);
                    View view = activity.findViewById(inject.value());
                    field.setAccessible(true);
                    field.set(activity, view);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void inject(MainActivity activity, int id) {
        Class cla = activity.getClass();
        Field fields[] = cla.getDeclaredFields();
        for(Field field : fields){
            if(field.isAnnotationPresent(IdInject.class)){
                try {
                    IdInject inject = field.getAnnotation(IdInject.class);
                    if(id == inject.value()){
                        View view = activity.findViewById(inject.value());
                        field.setAccessible(true);
                        field.set(activity, view);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
