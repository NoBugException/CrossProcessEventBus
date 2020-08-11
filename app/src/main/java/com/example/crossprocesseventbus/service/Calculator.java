package com.example.crossprocesseventbus.service;

/**
 * 假设服务器进程有一个计算器的工具类
 * 包含加减乘除四种运算
 */
public class Calculator {

    /**
     * 加法运算
     * @param a
     * @param b
     * @return
     */
    public static int addOperation(int a, int b){
        return a + b;
    }

    /**
     * 减法运算
     * @param a
     * @param b
     * @return
     */
    public static int subOperation(int a, int b){
        return a - b;
    }

    /**
     * 乘法运算
     * @param a
     * @param b
     * @return
     */
    public static int multiOperation(int a, int b){
        return a * b;
    }

    /**
     * 除法运算
     * @param a
     * @param b
     * @return
     */
    public static int divisionOperation(int a, int b){
        return a / b;
    }




}
