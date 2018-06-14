package com.example.administrator.mannotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class TestAnnotation {
    public static void main(String[] args){
//        displayClassAnnotation(Name.class);
//        System.out.println("\n");
//        displayClassAnnotation(Age.class);
//        System.out.println("\n");
//        displayClassAnnotation(Person.class);
//        System.out.println("\n");
//        displayClassAnnotation(ChinesePersion.class);
        analyzeClass(ChinesePersion.class);
        Person person = new Person() {
            @Override
            public String getAllInfo(int level) {
                return null;
            }
        };
        person.setLevel(3, 2);
    }

    private static void displayClassAnnotation(Class cla) {
        Annotation annotations[] = cla.getDeclaredAnnotations();
        for(Annotation annotation : annotations){
            System.out.println(annotation);
        }
    }

    public static void analyzeClass(Class  cla){
        System.out.println("##################AnalyzeClass " + cla.getName() + " ######################");
        displayFileds(cla);
        displayMedthods(cla);
    }

    private static void displayMedthods(Class cla) {
        Method[] methods = cla.getMethods();
        for(Method method : methods){
            Annotation[] methodAnnotations = method.getAnnotations();
            Annotation[][] paramAnotations = method.getParameterAnnotations();
            if(methodAnnotations.length == 0 && paramAnotations.length == 0){
                continue;
            }
            System.out.println( "============================Analyze methodName=" + method.getName() + "================");
            displayMethodAnotations(method);
            displayParamAnotations(method);
        }
    }

    private static void displayFileds(Class cla) {
        System.out.println( "====================DisplayFields========================");
        Field fields[] = cla.getDeclaredFields();
        for(Field field : fields){
            if(field.isAnnotationPresent(Name.class)){
                Name name = field.getAnnotation(Name.class);
                System.out.println("name=" + name.value());
            }
            if(field.isAnnotationPresent(Age.class)){
                Age age = field.getAnnotation(Age.class);
                System.out.println("age=" + age.value());
            }
            if(field.isAnnotationPresent(Gender.class)){
                Gender gender = field.getAnnotation(Gender.class);
                System.out.println("gender=" + gender.value().toString());
            }
            if(field.isAnnotationPresent(Level.class)){
                Level level = field.getAnnotation(Level.class);
                System.out.println("level=" + level);
            }
            if(field.isAnnotationPresent(Job.class)){
                Job job = field.getAnnotation(Job.class);
                System.out.println("job name=" + job.name() + " isManager=" + job.isManager());
            }
        }
    }

    private static void displayParamAnotations(Method method) {
        Annotation[][] paramAnotations = method.getParameterAnnotations();
        if(paramAnotations != null && paramAnotations.length > 0){
            for(Annotation[] annotations : paramAnotations){
                if(annotations == null || annotations.length == 0){
                    continue;
                }
                for(Annotation  annotation : annotations){
                    System.out.println("*****Param Annotation=" + annotation);
                }
            }
        }
    }

    private static void displayMethodAnotations(Method method) {
        Annotation[] anotations = method.getAnnotations();
        if(anotations != null && anotations.length > 0){
            for(Annotation annotation: anotations){
                if(annotation instanceof Job){
                    Job job = (Job) annotation;
                    System.out.println("-----Method Annotation:job name=" + job.name() + " isManager=" + job.isManager());
                }
                if(annotation instanceof Name){
                    Name name = (Name) annotation;
                    System.out.println("-----Method Annotation:name=" + name.value());
                }
                if(annotation instanceof Age){
                    Age age = (Age) annotation;
                    System.out.println("-----Method Annotation:age=" + age.value());
                }
                if(annotation instanceof Gender){
                    Gender gender = (Gender) annotation;
                    System.out.println("-----Method Annotation:gender=" + gender.value().toString());
                }
                if(annotation instanceof Level){
                    Level level = (Level) annotation;
                    System.out.println("-----Method Annotation:level=" + level);
                }
            }
        }
    }

}
