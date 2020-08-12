package com.example.crossprocesseventbus.service;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.crossprocesseventbus.R;
import com.example.crossprocesseventbus.client.Message;
import com.example.crossprocesseventbus.client.SecondActivity;
import com.nobugexception.hermes.eventbus.NoHermesEventBus;
import com.nobugexception.hermes.eventbus.NoHermesSubscribe;
import com.nobugexception.hermes.eventbus.NoHermesThreadMode;

/**
 * 服务器进程
 */
public class MainActivity extends AppCompatActivity {

    private Button button_1;

    private TextView add_result;
    private TextView sub_result;
    private TextView multi_result;
    private TextView dis_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        add_result = findViewById(R.id.add_result);
        sub_result = findViewById(R.id.sub_result);
        multi_result = findViewById(R.id.multi_result);
        dis_result = findViewById(R.id.dis_result);

        NoHermesEventBus.getDefault().register(this);

        button_1 = findViewById(R.id.button_1);

        button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 启动客户端进程
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);

            }
        });

    }

    @NoHermesSubscribe(threadMode = NoHermesThreadMode.MAIN, sticky = false)
    public void getMessage(Message message){
        Log.d("yunchong", "Name:"+message.getName());
    }

    @NoHermesSubscribe(threadMode = NoHermesThreadMode.MAIN, sticky = false)
    public void getCalculatorResult(CalculatorResult calculatorResult){

        if("addOperation".equals(calculatorResult.getOprationName())){
            // 加法运算
            add_result.setText(calculatorResult.getResult());
        } else if ("subOperation".equals(calculatorResult.getOprationName())) {
            // 减法运算
            sub_result.setText(calculatorResult.getResult());
        } else if ("multiOperation".equals(calculatorResult.getOprationName())) {
            // 乘法运算
            multi_result.setText(calculatorResult.getResult());
        } else if ("divisionOperation".equals(calculatorResult.getOprationName())) {
            // 除法运算
            dis_result.setText(calculatorResult.getResult());
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NoHermesEventBus.getDefault().unregister(this);
    }
}
