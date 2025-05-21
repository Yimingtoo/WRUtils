package com.yiming.wrutils.test1;

public interface AInterface {
    default public void printInfo(){
        System.out.println("This is AInterface.");
    }
}
