package com.zhong.oddpoint.main.request;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.zhong.oddpoint.main.bean.car_data;
import com.zhong.oddpoint.main.config.AppConfig;
import com.zhong.oddpoint.main.port.Car_Info_Parse;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CallImage {
    private String imageName;

    public static void downloadImage(String url, final String imageName) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://www.baidu.com/img/bd_logo1.png?where=super")
                .get()
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream inputStream = response.body().byteStream();
                BufferedInputStream inputStream1=new BufferedInputStream(inputStream);
                File filed = new File("/mnt/sdcard/oddpoint/");
                File file = new File("/mnt/sdcard/oddpoint/gg" + imageName + ".jpg");
                FileOutputStream out = new FileOutputStream(file);
                BufferedOutputStream outputStream=new BufferedOutputStream(out);
                if (!filed.exists()) {
                    filed.mkdirs();
                }else if (!file.exists()) {
                    file.createNewFile();
                }
                int len=0;
                byte[] bytes=new byte[1024*5];
                while ((len=inputStream1.read(bytes))!=-1){
                    outputStream.write(bytes,0,len);
                }

                outputStream.flush();
                out.flush();
                outputStream.close();
                out.close();
                inputStream1.close();
                inputStream.close();
            }
        });
    }
}
