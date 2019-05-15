package com.zhong.oddpoint.main.request;

import com.google.gson.Gson;
import com.zhong.oddpoint.main.bean.StopInfo;
import com.zhong.oddpoint.main.bean.car_data;
import com.zhong.oddpoint.main.port.StopDataListener;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CallStopData {
    private StopDataListener listener;
    public CallStopData(StopDataListener listener) {
        this.listener=listener;
    }
    public void CallData(car_data car_data, String start_time){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://kyfw.12306.cn/otn/czxx/queryByTrainNo?train_no="+car_data.getTrain_no()+"&from_station_telecode="+car_data.getStart_site_code()+"&to_station_telecode="+car_data.getEnd_site_code()+"&depart_date="+start_time)
                .get()
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                StopInfo stopInfo = new Gson().fromJson(string, StopInfo.class);
                listener.success(stopInfo);
            }
        });
    }
}
