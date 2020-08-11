package com.example.crossprocesseventbus.client;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.crossprocesseventbus.R;
import com.example.crossprocesseventbus.service.CalculatorService;
import com.google.gson.Gson;
import com.nobugexception.hermes.EventMessage;
import com.nobugexception.hermes.Request;
import com.nobugexception.hermes.Responce;
import com.nobugexception.hermes.hermes.Hermes;
import com.nobugexception.hermes.hermes.HermesInvocationHander;

import java.lang.reflect.Proxy;

public class SecondActivity extends AppCompatActivity {

    private Button button_1;
    private Button button_2;
    private Button button_3;
    private Button button_4;
    private Button button_5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        // 客户端连接服务器
        Hermes.getDefault().connect(CalculatorService.class);

        button_1 = findViewById(R.id.button_1);
        button_2 = findViewById(R.id.button_2);
        button_3 = findViewById(R.id.button_3);
        button_4 = findViewById(R.id.button_4);
        button_5 = findViewById(R.id.button_5);

        // 加法运算
        button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalculatorParam calculatorParam = new CalculatorParam();
                calculatorParam.setOp_1("5");
                calculatorParam.setOp_2("4");
                Responce responce = Hermes.getDefault().send(new Request("addOperation", new Gson().toJson(calculatorParam), CalculatorService.class.getName()));
                if(responce != null){
                    if(responce.getResultCode() == 0){
                        CalculatorResult calculatorResult = new Gson().fromJson(responce.getData(), CalculatorResult.class);
                        Log.d("yunchong", "加法结果:" + calculatorResult == null ? "" : calculatorResult.getResult());
                    }else{
                        Log.d("yunchong", "错误:" + responce.getErrorMsg());
                    }
                }
            }
        });
        // 减法运算
        button_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalculatorParam calculatorParam = new CalculatorParam();
                calculatorParam.setOp_1("5");
                calculatorParam.setOp_2("4");
                Responce responce = Hermes.getDefault().send(new Request("subOperation", new Gson().toJson(calculatorParam), CalculatorService.class.getName()));
                if(responce != null){
                    if(responce.getResultCode() == 0){
                        CalculatorResult calculatorResult = new Gson().fromJson(responce.getData(), CalculatorResult.class);
                        Log.d("yunchong", "减法结果:" + calculatorResult == null ? "" : calculatorResult.getResult());
                    }else{
                        Log.d("yunchong", "错误:" + responce.getErrorMsg());
                    }
                }
            }
        });

        // 乘法运算
        button_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalculatorParam calculatorParam = new CalculatorParam();
                calculatorParam.setOp_1("5");
                calculatorParam.setOp_2("4");
                Responce responce = Hermes.getDefault().send(new Request("multiOperation", new Gson().toJson(calculatorParam), CalculatorService.class.getName()));
                if(responce != null){
                    if(responce.getResultCode() == 0){
                        CalculatorResult calculatorResult = new Gson().fromJson(responce.getData(), CalculatorResult.class);
                        Log.d("yunchong", "乘法结果:" + calculatorResult == null ? "" : calculatorResult.getResult());
                    }else{
                        Log.d("yunchong", "错误:" + responce.getErrorMsg());
                    }
                }
            }
        });

        // 除法运算
        button_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalculatorParam calculatorParam = new CalculatorParam();
                calculatorParam.setOp_1("5");
                calculatorParam.setOp_2("4");
                Responce responce = Hermes.getDefault().send(new Request("divisionOperation", new Gson().toJson(calculatorParam), CalculatorService.class.getName()));
                if(responce != null){
                    if(responce.getResultCode() == 0){
                        CalculatorResult calculatorResult = new Gson().fromJson(responce.getData(), CalculatorResult.class);
                        Log.d("yunchong", "除法结果:" + calculatorResult == null ? "" : calculatorResult.getResult());
                    }else{
                        Log.d("yunchong", "错误:" + responce.getErrorMsg());
                    }
                }
            }
        });

        // 传递对象
        button_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message();
                message.setName("何化龙");
//                EventMessage message = new EventMessage();
                Hermes.getDefault().postMessage(message);
                Class clazz = null;
                try {
                    clazz = Class.forName(Message.class.getName());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                IMessage message1 = (IMessage) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{IMessage.class}, new HermesInvocationHander(message));
                Log.d("yunchong", "message1:"+message1.getName());
            }
        });

    }

}
