package com.zhong.oddpoint.main.fragment;

import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zaaach.citypicker.CityPickerActivity;
import com.zhong.d_oddpoint.database.table.StationCode;
import com.zhong.d_oddpoint.dao.DataDao;
import com.zhong.oddpoint.main.activity.DateActivity;
import com.zhong.oddpoint.main.R;
import com.zhong.oddpoint.main.activity.TicketListActivity;
import com.zhong.oddpoint.main.port.StationCodeFinishListener;
import com.zhong.oddpoint.main.request.CallStationCode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TrainTicket_Fragment extends Fragment implements View.OnClickListener,StationCodeFinishListener {

    private View view;
    private TextView dateView;
    private TextView whichDayView;
    private LinearLayout start_local;
    private LinearLayout end_local;
    private TextView local1;
    private TextView local2;
    private String start_city_code = "CQW";
    private String end_city_code = "WYW";
    private DataDao dataDao;
    private StationCode start_stationCode=null;
    private StationCode end_stationCode=null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = getLayoutInflater().inflate(R.layout.fragment_train_ticket, null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dateView = view.findViewById(R.id.date_TextView);
        whichDayView = view.findViewById(R.id.whichDayView);
        local1 = view.findViewById(R.id.local1);
        local2 = view.findViewById(R.id.local2);
        view.findViewById(R.id.select_button).setOnClickListener(this);
        view.findViewById(R.id.selece_date).setOnClickListener(this);
        view.findViewById(R.id.start_local).setOnClickListener(this);
        view.findViewById(R.id.end_local).setOnClickListener(this);

        LoadData();
        initDatabase();
    }

    public void LoadData() {
        //获取当前日期
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        String format = simpleDateFormat.format(date);
        //获取当前周几
        SimpleDateFormat whichDay = new SimpleDateFormat("EEEE");
        String format1 = whichDay.format(date);

        dateView.setText(format);
        whichDayView.setText(format1);
    }

    public void initDatabase(){
        dataDao = new DataDao();
        List<StationCode> stationCodes = dataDao.selectAll();
        if (stationCodes.size()<=0){
            new CallStationCode(this).callHtpp();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_button:
                Intent intent = new Intent(getActivity(), TicketListActivity.class);
                intent.putExtra("start_date", dateView.getText());
                intent.putExtra("week", whichDayView.getText());
                if (start_stationCode!=null&&end_stationCode!=null){
                    intent.putExtra("start_name",start_stationCode);
                    intent.putExtra("end_name",end_stationCode);
                }
                startActivity(intent);
                break;
            case R.id.selece_date:
                startActivityForResult(new Intent(getActivity(), DateActivity.class), 0);
                break;
            case R.id.start_local:
                startActivityForResult(new Intent(getActivity(), CityPickerActivity.class),
                        1);
                break;
            case R.id.end_local:
                startActivityForResult(new Intent(getActivity(), CityPickerActivity.class),
                        2);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (resultCode == -1) {
                    String date = data.getStringExtra("date");
                    dateView.setText(date);
                    whichDayView.setText(getWeek(date));
                }
                break;
            case 1:
                if (resultCode == -1) {
                    String city1 = data.getStringExtra(CityPickerActivity.KEY_PICKED_CITY);
                    local1.setText(city1);
                    List<StationCode> start_stationCodeList = dataDao.selectData(city1);
                    if (start_stationCodeList.size()>0){
                        start_stationCode = start_stationCodeList.get(0);
                    }else {
                        start_stationCode=null;
                    }
                }
                break;
            case 2:
                if (resultCode == -1) {
                    String city2 = data.getStringExtra(CityPickerActivity.KEY_PICKED_CITY);
                    local2.setText(city2);
                    List<StationCode> end_stationList = dataDao.selectData(city2);
                    if (end_stationList.size()>0){
                        end_stationCode = end_stationList.get(0);
                    }else {
                        end_stationCode=null;
                    }
                }
                break;
        }
    }

    private String getWeek(String pTime) {

        String Week = "星期";

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {

            c.setTime(format.parse(pTime));

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            Week += "天";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 2) {
            Week += "一";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 3) {
            Week += "二";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 4) {
            Week += "三";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 5) {
            Week += "四";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 6) {
            Week += "五";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 7) {
            Week += "六";
        }

        return Week;
    }

    @Override
    public void StationCodeFinish(String station_name, String station_code) {
        StationCode stationCode=new StationCode();
        stationCode.setStation_name(station_name);
        stationCode.setStation_code(station_code);
        dataDao.insert(stationCode);
    }
}
