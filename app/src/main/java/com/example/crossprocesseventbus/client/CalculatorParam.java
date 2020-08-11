package com.example.crossprocesseventbus.client;

/**
 * 请求服务器的参数
 */
public class CalculatorParam {

    // 操作数1
    private String op_1;

    // 操作数2
    private String op_2;

    public CalculatorParam(){}

    public CalculatorParam(String op_1, String op_2) {
        this.op_1 = op_1;
        this.op_2 = op_2;
    }

    public String getOp_1() {
        return op_1;
    }

    public void setOp_1(String op_1) {
        this.op_1 = op_1;
    }

    public String getOp_2() {
        return op_2;
    }

    public void setOp_2(String op_2) {
        this.op_2 = op_2;
    }
}
