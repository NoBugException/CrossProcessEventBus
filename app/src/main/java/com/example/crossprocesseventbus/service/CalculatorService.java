package com.example.crossprocesseventbus.service;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.nobugexception.hermes.Request;
import com.nobugexception.hermes.Responce;
import com.nobugexception.hermes.hermes.HermesService;
import com.nobugexception.hermes.hermes.ResultCode;

/**
 *  计算机服务（服务器进程）
 */
public class CalculatorService extends HermesService {

    /**
     * 参数错误的处理
     * @return
     */
    private Responce paramError(){
        Responce responce = new Responce();
        responce.setData("");
        responce.setErrorMsg(ResultCode.ERROR_MSG_4);
        responce.setResultCode(ResultCode.ERROR_CODE_4);
        return responce;
    }

    @Override
    protected Responce getResponce(Request request) {

        Responce responce = new Responce();
        if("addOperation".equals(request.getRequestName())){
            // 加法运算
            CalculatorParam calculatorParam = new Gson().fromJson(request.getParamJson(), CalculatorParam.class);
            if (calculatorParam != null && !TextUtils.isEmpty(calculatorParam.getOp_1()) && !TextUtils.isEmpty(calculatorParam.getOp_2())){
                responce.setResultCode(0);
                responce.setErrorMsg("");
                CalculatorResult calculatorResult = new CalculatorResult();
                calculatorResult.setOprationName("addOperation");
                try {
                    calculatorResult.setResult(String.valueOf(Calculator.addOperation(Integer.parseInt(calculatorParam.getOp_1()), Integer.parseInt(calculatorParam.getOp_2()))));
                }catch (NumberFormatException e){
                    return paramError();
                }
                responce.setData(new Gson().toJson(calculatorResult));
                return responce;
            } else {
                return paramError();
            }
        } else if ("subOperation".equals(request.getRequestName())) {
            // 减法运算
            CalculatorParam calculatorParam = new Gson().fromJson(request.getParamJson(), CalculatorParam.class);
            if (calculatorParam != null && !TextUtils.isEmpty(calculatorParam.getOp_1()) && !TextUtils.isEmpty(calculatorParam.getOp_2())){
                responce.setResultCode(0);
                responce.setErrorMsg("");
                CalculatorResult calculatorResult = new CalculatorResult();
                calculatorResult.setOprationName("subOperation");
                try {
                    calculatorResult.setResult(String.valueOf(Calculator.subOperation(Integer.parseInt(calculatorParam.getOp_1()), Integer.parseInt(calculatorParam.getOp_2()))));
                }catch (NumberFormatException e){
                    return paramError();
                }
                responce.setData(new Gson().toJson(calculatorResult));
                return responce;
            } else {
                return paramError();
            }
        } else if ("multiOperation".equals(request.getRequestName())) {
            // 乘法运算
            CalculatorParam calculatorParam = new Gson().fromJson(request.getParamJson(), CalculatorParam.class);
            if (calculatorParam != null && !TextUtils.isEmpty(calculatorParam.getOp_1()) && !TextUtils.isEmpty(calculatorParam.getOp_2())){
                responce.setResultCode(0);
                responce.setErrorMsg("");
                CalculatorResult calculatorResult = new CalculatorResult();
                calculatorResult.setOprationName("multiOperation");
                try {
                    calculatorResult.setResult(String.valueOf(Calculator.multiOperation(Integer.parseInt(calculatorParam.getOp_1()), Integer.parseInt(calculatorParam.getOp_2()))));
                }catch (NumberFormatException e){
                    return paramError();
                }
                responce.setData(new Gson().toJson(calculatorResult));
                return responce;
            } else {
                return paramError();
            }
        } else if ("divisionOperation".equals(request.getRequestName())) {
            // 除法运算
            CalculatorParam calculatorParam = new Gson().fromJson(request.getParamJson(), CalculatorParam.class);
            if (calculatorParam != null && !TextUtils.isEmpty(calculatorParam.getOp_1()) && !TextUtils.isEmpty(calculatorParam.getOp_2())){
                responce.setResultCode(0);
                responce.setErrorMsg("");
                CalculatorResult calculatorResult = new CalculatorResult();
                calculatorResult.setOprationName("divisionOperation");
                try {
                    calculatorResult.setResult(String.valueOf(Calculator.divisionOperation(Integer.parseInt(calculatorParam.getOp_1()), Integer.parseInt(calculatorParam.getOp_2()))));
                }catch (NumberFormatException e){
                    return paramError();
                }
                responce.setData(new Gson().toJson(calculatorResult));
                return responce;
            } else {
                return paramError();
            }
        } else {
            responce.setResultCode(ResultCode.ERROR_CODE_5);
            responce.setData("");
            responce.setErrorMsg(ResultCode.ERROR_MSG_5);
            return responce;
        }
    }
}
