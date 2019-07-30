package com.zhong.oddpoint.main.request;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhong.oddpoint.main.bean.CarTrainNo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CallTrainNo {
    private Context context;
    private Handler handler=new Handler(Looper.getMainLooper());
    public CallTrainNo(Context context) {
        this.context = context;
    }
    public void downloadData(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("carid","G8708");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkHttpClient okHttpClient=new OkHttpClient();
        MediaType mediaType=MediaType.parse("application/json;charset=utf-8");
        RequestBody requestBody=RequestBody.create(mediaType,jsonObject.toString());
        Request request=new Request.Builder()
                .url("https://www.196mk.com/TrainData/do")
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response){
                try {
                    String data = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void parseData(){

    }
}
