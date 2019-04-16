package com.zhong.oddpoint.main.request;

import android.util.Log;

import com.zhong.oddpoint.main.port.StationCodeFinishListener;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CallStationCode {
    private StationCodeFinishListener listener;
    private String car_data=null;
    public CallStationCode(StationCodeFinishListener listener) {
        this.listener=listener;
    }

    public void callHtpp(){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://kyfw.12306.cn/otn/resources/js/framework/station_name.js")
                .get()
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response){
                try {
                    car_data=response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                parseData();
            }
        });
    }

    public void parseData(){
        int index = car_data.indexOf("'");
        String subData = car_data.substring(index+2, car_data.length()-2);
        String[] split = subData.split("@");
        for (String data:split){
            String[] split1 = data.split("\\|");
            listener.StationCodeFinish(split1[1],split1[2]);
        }
        Log.i("=====>", "数据解析完毕");
    }
}
