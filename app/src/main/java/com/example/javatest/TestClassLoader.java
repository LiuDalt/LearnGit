package com.example.javatest;

import android.os.Parcelable;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;

public class TestClassLoader {
    public static void main(String []args) throws ClassNotFoundException, IllegalAccessException, InstantiationException, MalformedURLException {
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        Class<?> clazz = loader.loadClass("com.example.javatest.People");
        People people = (People) clazz.newInstance();
        people.mName = "11111111";
        people.getClass().getClassLoader();
        System.out.println(loader.toString() +" " + people.mName + " " + People.class.getClassLoader());
        File file = new File("F:\\zz\\");
        System.out.println(file.exists());
        URL url = new URL("file:F:\\zz\\");
        MyClassLoader urlClassLoader = new MyClassLoader(new URL[]{url});
        Class<?> c = urlClassLoader.loadClass("com.example.javatest.People");
        people = (People) c.newInstance();
        people.mName = "4444444";
        System.out.println(urlClassLoader.toString() +" " + people.mName + " " + people.age);
    }

    static class MyClassLoader extends URLClassLoader{

        public MyClassLoader(URL[] urls, ClassLoader parent) {
            super(urls, parent);
        }

        public MyClassLoader(URL[] urls) {
            super(urls);
        }

        public MyClassLoader(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory) {
            super(urls, parent, factory);
        }

        @Override
        protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
            return super.loadClass(name, resolve);
        }

        @Override
        public Class<?> loadClass(String name) throws ClassNotFoundException {
            return loadClass(name, false);
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            return null;
        }
    }
}
