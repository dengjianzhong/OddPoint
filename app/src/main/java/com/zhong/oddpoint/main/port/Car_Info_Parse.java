package com.zhong.oddpoint.main.port;

import com.zhong.oddpoint.main.bean.car_data;

public interface Car_Info_Parse {
    void parse_Price(String data, car_data carData);
    void refreshSuccessfully(int code);
}


