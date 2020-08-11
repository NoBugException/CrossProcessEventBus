package com.example.crossprocesseventbus.service;

/**
 * 请求服务器的参数
 */
public class CalculatorResult {

    // 操作方式：加减乘除
    private String oprationName;

    // 计算结果
    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getOprationName() {
        return oprationName;
    }

    public void setOprationName(String oprationName) {
        this.oprationName = oprationName;
    }
}
