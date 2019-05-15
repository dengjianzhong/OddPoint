package com.zhong.oddpoint.main.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zaaach.citypicker.CityPickerActivity;
import com.zhong.d_oddpoint.dao.DataDao;
import com.zhong.d_oddpoint.database.table.StationCode;
import com.zhong.oddpoint.main.R;
import com.zhong.oddpoint.main.activity.DateActivity;
import com.zhong.oddpoint.main.activity.TicketListActivity;
import com.zhong.oddpoint.main.adapter.ADAdapter;
import com.zhong.oddpoint.main.config.AppConfig;
import com.zhong.oddpoint.main.config.TransformUtil;
import com.zhong.oddpoint.main.port.StationCodeFinishListener;
import com.zhong.oddpoint.main.request.CallStationCode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class TrainTicket_Fragment extends Fragment implements View.OnClickListener, StationCodeFinishListener, View.OnTouchListener {

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
    private StationCode start_stationCode = null;
    private StationCode end_stationCode = null;
    private ViewPager ad_viewPager;
    private Timer timer;
    private int current_position = 0;
    private List<ImageView> imageList;
    private Handler handler = new Handler(Looper.getMainLooper());
    private LinearLayout docContainer;
    private Map<Integer, View> viewMap = new HashMap<>();
    private boolean endflag = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_train_ticket, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dateView = view.findViewById(R.id.date_TextView);
        whichDayView = view.findViewById(R.id.whichDayView);
        local1 = view.findViewById(R.id.local1);
        local2 = view.findViewById(R.id.local2);
        ad_viewPager = view.findViewById(R.id.ad_ViewPager);
        docContainer = view.findViewById(R.id.docContainer);
        view.findViewById(R.id.select_button).setOnClickListener(this);
        view.findViewById(R.id.selece_date).setOnClickListener(this);
        view.findViewById(R.id.start_local).setOnClickListener(this);
        view.findViewById(R.id.end_local).setOnClickListener(this);
        ad_viewPager.setOnTouchListener(this);

        LoadData();
        initView();
        LoadAD();
        initDatabase();
    }

    private void initView() {
        for (int i = 0; i < AppConfig.ImageUrls.length; i++) {
            View view = new View(getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(TransformUtil.dip2px(getContext(), 6), TransformUtil.dip2px(getContext(), 6));
            if (i != 0) {
                layoutParams.setMargins(TransformUtil.dip2px(getContext(), 5), 0, 0, 0);
                view.setBackgroundResource(R.drawable.doc_bg_f);
            } else {
                view.setBackgroundResource(R.drawable.doc_bg);
            }
            view.setLayoutParams(layoutParams);
            viewMap.put(i, view);
            docContainer.addView(view);
        }
    }

    private void LoadAD() {
        imageList = new ArrayList<>();
        for (int i = 0; i < AppConfig.ImageUrls.length; i++) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            ImageView imageView = new ImageView(getContext());
            imageView.setLayoutParams(layoutParams);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(getContext()).load(AppConfig.ImageUrls[i]).into(imageView);
            imageList.add(imageView);
        }

        ADAdapter adAdapter = new ADAdapter();
        adAdapter.setData(imageList);
        ad_viewPager.setAdapter(adAdapter);
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

    public void initDatabase() {
        dataDao = new DataDao();
        List<StationCode> stationCodes = dataDao.selectAll();
        if (stationCodes.size() <= 0) {
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
                if (start_stationCode != null && end_stationCode != null) {
                    intent.putExtra("start_name", start_stationCode);
                    intent.putExtra("end_name", end_stationCode);
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
                    if (start_stationCodeList.size() > 0) {
                        start_stationCode = start_stationCodeList.get(0);
                    } else {
                        start_stationCode = null;
                    }
                }
                break;
            case 2:
                if (resultCode == -1) {
                    String city2 = data.getStringExtra(CityPickerActivity.KEY_PICKED_CITY);
                    local2.setText(city2);
                    List<StationCode> end_stationList = dataDao.selectData(city2);
                    if (end_stationList.size() > 0) {
                        end_stationCode = end_stationList.get(0);
                    } else {
                        end_stationCode = null;
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
        StationCode stationCode = new StationCode();
        stationCode.setStation_name(station_name);
        stationCode.setStation_code(station_code);
        dataDao.insert(stationCode);
    }

    @Override
    public void onResume() {
        super.onResume();
        startTimer();
    }

    public void startTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (++current_position < imageList.size()) {
                    setCurrentPage();
                } else {
                    endflag = true;
                    current_position = 0;
                    setCurrentPage();
                }
            }
        }, 3000, 3000);
    }

    //切换页面
    public void setCurrentPage() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (current_position != 0) {
                    int doc = current_position - 1;
                    viewMap.get(doc).setBackgroundResource(R.drawable.doc_bg_f);
                } else if (endflag) {
                    viewMap.get(imageList.size() - 1).setBackgroundResource(R.drawable.doc_bg_f);
                }

                viewMap.get(current_position).setBackgroundResource(R.drawable.doc_bg);
                ad_viewPager.setCurrentItem(current_position);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        timer.cancel();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            startTimer();
        } else {
            timer.cancel();
        }
        return false;
    }
}
