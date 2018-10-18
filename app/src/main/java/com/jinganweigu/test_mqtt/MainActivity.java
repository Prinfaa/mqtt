package com.jinganweigu.test_mqtt;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity  implements IGetMessageCallBack {

    private TextView textView;
    private Button button,btnUp,btnDown,btnStop;
    private MyServiceConnection serviceConnection;
    private MQTTService mqttService;
    private StringBuilder sb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        sb=new StringBuilder();
        textView = (TextView) findViewById(R.id.text);
        button = (Button) findViewById(R.id.button);
        btnDown = (Button) findViewById(R.id.btn_down);
        btnStop = (Button) findViewById(R.id.btn_stop);
        btnUp = (Button) findViewById(R.id.btn_up);
        btnUp.setEnabled(false);
        btnDown.setEnabled(false);
        btnStop.setEnabled(false);


        serviceConnection = new MyServiceConnection();
        serviceConnection.setIGetMessageCallBack(MainActivity.this);

        Intent intent = new Intent(this, MQTTService.class);

        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                MQTTService.publish("测试一下子");
            }
        });


        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MQTTService.publish("rise");

            }
        });


        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MQTTService.publish("pause");

            }
        });


        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                MQTTService.publish("falling");

            }
        });

    }

    @Override
    public void setMessage(String message) {

//        sb.append(message);
//        textView.setText(sb.toString());
        mqttService = serviceConnection.getMqttService();
        mqttService.toCreateNotification(message);
    }

    @Override
    public void isconnect(String message) {

        if(message.equals("连接成功")){

            textView.setText(message);

            btnUp.setEnabled(true);
            btnDown.setEnabled(true);
            btnStop.setEnabled(true);
        }else{
            btnUp.setEnabled(false);
            btnDown.setEnabled(false);
            btnStop.setEnabled(false);
        }


    }




    @Override
    protected void onDestroy() {
        unbindService(serviceConnection);
        super.onDestroy();
    }





    public class MyServiceConnection implements ServiceConnection {

        private MQTTService mqttService;
        private IGetMessageCallBack IGetMessageCallBack;

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mqttService = ((MQTTService.CustomBinder)iBinder).getService();
            mqttService.setIGetMessageCallBack(IGetMessageCallBack);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }

        public MQTTService getMqttService(){
            return mqttService;
        }

        public void setIGetMessageCallBack(IGetMessageCallBack IGetMessageCallBack){
            this.IGetMessageCallBack = IGetMessageCallBack;
        }
    }


}
