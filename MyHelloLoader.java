package com.my.springboot;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MyHelloLoader extends ClassLoader {
    public static void main(String[] args) {
        try{
            Class aClass = new MyHelloLoader().findClass("com.my.springboot.helloLoader");
            Object obj = aClass.newInstance();
            Method method = aClass.getMethod("helloLoader");
            method.invoke(obj);
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }catch (NoSuchMethodException e){
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException{
        String path = MyHelloLoader.class.getResource("helloLoader.class").getPath();
        System.out.println(path);
        byte[] bytes = getBytes(path);
        return defineClass(name,bytes,0,bytes.length);
    }

    public byte[] getBytes(String path){
        File f = new File(path);
        int length = (int)f.length();
        byte[] bytes = new byte[length];
        try {
            new FileInputStream(f).read(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        for (int i = 0; i < bytes.length; ++i) {
//            bytes[i] = (byte)(255 - bytes[i]);
//        }
        return bytes;
    }
}
