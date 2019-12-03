package com.zhong.oddpoint.main.request;

import com.zhong.oddpoint.main.bean.car_data;
import com.zhong.oddpoint.main.port.Car_Info_Parse;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CallFare {
    private Car_Info_Parse carInfoParse;
    private car_data carData;
    private String start_date;
    public static void obtainPrice(final Car_Info_Parse carInfoParse, final car_data carData, String start_date) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://kyfw.12306.cn/otn/leftTicket/queryTicketPrice?train_no=" + carData.getTrain_no() + "&from_station_no=" + carData.getFrom_station_no() + "&to_station_no=" + carData.getTo_station_no() + "&seat_types=" + carData.getSeat_types() + "&train_date="+start_date)
                .addHeader("Cookie","JSESSIONID=BC0BF7B15C20A2539D531A7BCC8D3683")
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
                carInfoParse.parse_Price(string, carData);
            }
        });
    }
}
