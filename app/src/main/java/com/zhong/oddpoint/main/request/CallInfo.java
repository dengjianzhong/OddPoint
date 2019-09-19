package com.zhong.oddpoint.main.request;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;


import com.google.gson.Gson;
import com.zhong.utilslibrary.database.table.StationCode;
import com.zhong.oddpoint.main.bean.car_data;
import com.zhong.oddpoint.main.bean.car_info;
import com.zhong.oddpoint.main.port.Car_Info_Parse;
import com.zhong.oddpoint.main.table.Parse_Data_Table;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CallInfo {
    private List<car_data> car_data_list = new ArrayList<>();
    private Handler handler;
    private Handler _handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    private Car_Info_Parse carInfoParse;
    private boolean flush;
    private String date;
    private StationCode start_stationCode;
    private StationCode end_stationCode;
    private String from_station="CQW";
    private String to_station="WYW";
    private String start_city="重庆";
    private String end_city="万州";
    private int cartype=200;

    public CallInfo(Handler handler, Car_Info_Parse carInfoParse) {
        this.handler = handler;
        this.carInfoParse=carInfoParse;
        ObtainDate();
    }

    public void ObtainDate(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date d = new Date(System.currentTimeMillis());
        date = simpleDateFormat.format(d);
    }

    public void requestCartIdInfo(boolean flush,String date,StationCode from_station,StationCode to_station,int cartype) {
        this.flush = flush;
        this.cartype=cartype;
        if (!TextUtils.isEmpty(date)) {
            this.date = date;
        }
        if (from_station != null && to_station != null) {
            this.start_stationCode = from_station;
            this.end_stationCode = to_station;

            car_data_list.clear();
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://kyfw.12306.cn/otn/leftTicket/query?leftTicketDTO.train_date=" + date + "&leftTicketDTO.from_station=" + start_stationCode.getStation_code() + "&leftTicketDTO.to_station=" + end_stationCode.getStation_code() + "&purpose_codes=ADULT")
                    .get()
                    .build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    carInfoParse.refreshSuccessfully(404);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String cartId = response.body().string();
                    ParseData(cartId);
                }
            });
        }
    }

    public void ParseData(String data) {
        if (!TextUtils.isEmpty(data)) {
            car_info car_info = new Gson().fromJson(data, car_info.class);
            if (car_info != null) {
                com.zhong.oddpoint.main.bean.car_info.DataBean data1 = car_info.getData();
                List<String> result = data1.getResult();
                for (String carinfo : result) {
                    int index = carinfo.indexOf("预");
                    if (index != -1) {

                        //可预订票
                        String sub_data = carinfo.substring(index, carinfo.length());
                        String[] split = sub_data.split("\\|");
                        //添加票价查询信息
                        car_data car_data = new car_data();
                        String s = Parse_Data_Table.parse_city_code(from_station);
                        start_city=TextUtils.isEmpty(s)?start_city:s;
                        String s1 = Parse_Data_Table.parse_city_code(to_station);
                        end_city=TextUtils.isEmpty(s1)?end_city:s1;
                        car_data.setStart_site_code(split[5]);
                        car_data.setEnd_site_code(split[6]);
                        car_data.setTrain_no(split[1]);
                        car_data.setFrom_station_no(split[15]);
                        car_data.setTo_station_no(split[16]);
                        car_data.setSeat_types(split[34]);
                        if (split[2].contains("C") || split[2].contains("G") || split[2].contains("D")) {
                            if(cartype==200||cartype==201) {
                                car_data.setStart_time(split[12]);
                                car_data.setCar_id(split[2]);
                                car_data.setStart_time(split[7]);
                                car_data.setEnd_time(split[8]);
                                car_data.setCount_time(split[9]);
                                car_data.setSeat2(split[29]);
                                car_data.setSeat1(split[30]);
                                car_data.setSeat(split[24]);
                                car_data.setSwSeat(split[31]);
                                car_data.setNoSeat(split[25]);
                                car_data_list.add(car_data);
                            }
                        } else {
                            if(cartype==200||cartype==202) {
                                car_data.setStart_time(split[12]);
                                car_data.setCar_id(split[2]);
                                car_data.setStart_time(split[7]);
                                car_data.setEnd_time(split[8]);
                                car_data.setCount_time(split[9]);
                                car_data.setSeat2(split[27]);
                                car_data.setSeat1(split[22]);
                                car_data.setSeat(split[28]);
                                car_data.setNoSeat(split[25]);
                                car_data_list.add(car_data);
                            }
                        }
                    } else {
                        //预售票
                        int i = carinfo.indexOf("售");
                        if (i != -1) {
                            String sub_data = carinfo.substring(1, carinfo.length());
                            String[] split = sub_data.split("\\|");
                            car_data car_data = new car_data();
                            car_data.setTrain_no(split[1]);
                            car_data.setFrom_station_no(split[15]);
                            car_data.setTo_station_no(split[16]);
                            car_data.setSeat_types(split[34]);
                            //预售提示信息
                            String presale_info = "*" + split[0] + "*";
                            String[] split1 = presale_info.split("<br/>");
                            String presale = "";
                            for (String d : split1) {
                                presale = presale + d;
                            }
                            car_data.setPresale(presale);
                            car_data.setStart_time(split[12]);
                            car_data.setCar_id(split[2]);
                            car_data.setStart_time(split[7]);
                            car_data.setEnd_time(split[8]);
                            car_data.setCount_time(split[9]);
                            car_data_list.add(car_data);
                        }
                    }
                }
                if (flush){
                    carInfoParse.refreshSuccessfully(200);
                }
                Message message = handler.obtainMessage();
                message.what = 200;
                message.obj = car_data_list;
                handler.sendMessage(message);
            }else {
                if (flush){
                    carInfoParse.refreshSuccessfully(200);
                }
                handler.sendEmptyMessage(403);
            }
        }

    }
}
