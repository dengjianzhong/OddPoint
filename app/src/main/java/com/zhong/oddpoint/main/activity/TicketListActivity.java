package com.zhong.oddpoint.main.activity;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.zhong.d_oddpoint.database.table.StationCode;
import com.zhong.d_oddpoint.utils.PopupFactory;
import com.zhong.oddpoint.main.R;
import com.zhong.oddpoint.main.adapter.PurchaseList;
import com.zhong.oddpoint.main.bean.car_data;
import com.zhong.oddpoint.main.port.Car_Info_Parse;
import com.zhong.oddpoint.main.request.CallInfo;
import com.zhong.oddpoint.main.request.CallFare;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class TicketListActivity extends AppCompatActivity implements View.OnClickListener, Car_Info_Parse, SwipeRefreshLayout.OnRefreshListener {
    private ListView listView;
    private List<Map<String, String>> cartInfo;
    private PurchaseList purchaseList;
    private List<car_data> car_data_list;
    private CallInfo callInfo;
    private String start_date;
    private TextView stv;
    private TextView weekView;
    private TextView form_station_view;
    private TextView to_station_view;
    private StationCode start_stationCode;
    private StationCode end_stationCode;
    private LinearLayout address;
    private TextView point_info;
    private SwipeRefreshLayout swipeRefreshListView;
    private SwipeRefreshLayout swipeRefreshNoCar;
    private ImageView more_view;
    private int widthPixels;
    private int heightPixels;
    private PopupWindow popupWindow;
    private ImageView all_car;
    private ImageView gdc_car;
    private ImageView k_car;
    private PopupWindow loadPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_list);
        setStatusBar();
        initView();
        getScreenWH();
    }

    public void getScreenWH() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        widthPixels = displayMetrics.widthPixels;
        heightPixels = displayMetrics.heightPixels;
    }

    public void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.title_bg_color));
        }
    }

    public void initView() {
        listView = findViewById(R.id.listView);
        stv = findViewById(R.id.start_timr_view);
        weekView = findViewById(R.id.weekView);
        form_station_view = findViewById(R.id.form_station_view);
        to_station_view = findViewById(R.id.to_station_view);
        address = findViewById(R.id.address);
        point_info = findViewById(R.id.point_info);
        swipeRefreshListView = findViewById(R.id.swipeRefreshListView);
        swipeRefreshNoCar = findViewById(R.id.swipeRefreshNoCar);
        more_view = findViewById(R.id.more_view);
        more_view.setOnClickListener(this);
        swipeRefreshListView.setOnRefreshListener(this);
        findViewById(R.id.back_key).setOnClickListener(this);

        initData();
    }

    public void initData() {

        SharedPreferences.Editor car_type = getSharedPreferences("car_type", MODE_PRIVATE).edit();
        car_type.putInt("cartype", 200).apply();
        start_date = getIntent().getStringExtra("start_date");
        start_stationCode = (StationCode) getIntent().getSerializableExtra("start_name");
        end_stationCode = (StationCode) getIntent().getSerializableExtra("end_name");
        stv.setText(start_date);
        weekView.setText(getIntent().getStringExtra("week"));
        callInfo = new CallInfo(handler, this);
        purchaseList = new PurchaseList(this);
        if (start_stationCode != null && end_stationCode != null) {
            form_station_view.setText(start_stationCode.getStation_name());
            to_station_view.setText(end_stationCode.getStation_name());
            callInfo.requestCartIdInfo(false, start_date, start_stationCode, end_stationCode, 200);
        } else {
            address.setVisibility(View.GONE);
            more_view.setVisibility(View.GONE);
            swipeRefreshListView.setVisibility(View.GONE);
            swipeRefreshNoCar.setVisibility(View.VISIBLE);
        }

        swipeRefreshNoCar.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                SharedPreferences car_type = getSharedPreferences("car_type", MODE_PRIVATE);
                int cartype = car_type.getInt("cartype", 200);
                callInfo.requestCartIdInfo(true, start_date, start_stationCode, end_stationCode, cartype);
            }
        });
    }


    @Override
    public void onRefresh() {
        SharedPreferences car_type = getSharedPreferences("car_type", MODE_PRIVATE);
        int cartype = car_type.getInt("cartype", 200);
        callInfo.requestCartIdInfo(true, start_date, start_stationCode, end_stationCode, cartype);
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 200:
                    car_data_list = (List<car_data>) msg.obj;
                    if (car_data_list.size() <= 0) {
                        swipeRefreshListView.setVisibility(View.GONE);
                        swipeRefreshNoCar.setVisibility(View.VISIBLE);
                        point_info.setText("呀，当前线路没有列车跑路了~");
                        if (loadPopup!=null&&loadPopup.isShowing()) {
                            loadPopup.dismiss();
                        }
                    } else {
                        swipeRefreshListView.setVisibility(View.VISIBLE);
                        purchaseList.setData(car_data_list);
                        listView.setAdapter(purchaseList);
                        obtainPrice();
                    }
                    break;
                case 666:
                    if (loadPopup!=null&&loadPopup.isShowing()) {
                        loadPopup.dismiss();
                    }
                    purchaseList.notifyDataSetChanged();
                    break;
                case 404:
                    if (loadPopup!=null&&loadPopup.isShowing()) {
                        loadPopup.dismiss();
                    }
                    Toast.makeText(TicketListActivity.this, "接口已改或网络错误", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    public void obtainPrice() {
        for (car_data data : car_data_list) {
            CallFare.obtainPrice(this, data, start_date);
        }
    }

    @Override
    public void parse_Price(String data, car_data carData) {
        try {
            if (carData != null) {
                JSONObject jsonObject = new JSONObject(data);
                JSONObject jsonObject1 = (JSONObject) jsonObject.get("data");
                String o = jsonObject1.getString("WZ");
                carData.setTheLowestPrice(o);
                handler.sendEmptyMessage(666);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void refreshSuccessfully(int code) {
        switch (code) {
            case 200:
                globleToast("刷新成功");
                break;
            case 403:
                globleToast("刷新失败，请稍后重试");
                break;
            case 404:
                globleToast("网络开小差了");
                break;
        }
    }

    public void globleToast(final String content) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (loadPopup!=null&&loadPopup.isShowing()) {
                    loadPopup.dismiss();
                }
                swipeRefreshNoCar.setRefreshing(false);
                swipeRefreshListView.setRefreshing(false);
                if (content.equals("网络开小差了")) {
                    swipeRefreshListView.setVisibility(View.GONE);
                    swipeRefreshNoCar.setVisibility(View.VISIBLE);
                    point_info.setText("网络开小差了,下拉刷新");

                    return;
                }
                Toast.makeText(TicketListActivity.this, content, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.back_key:
                finish();
                break;
            case R.id.more_view:
                View view = getLayoutInflater().inflate(R.layout.popup_layout, null);
                popupWindow = PopupFactory.ShowPopup(view, heightPixels / 4, getWindow());
                view.findViewById(R.id.close_popup).setOnClickListener(this);
                view.findViewById(R.id.AllCarLayout).setOnClickListener(this);
                view.findViewById(R.id.GDCCarLayout).setOnClickListener(this);
                view.findViewById(R.id.KCarLayout).setOnClickListener(this);
                all_car = view.findViewById(R.id.all_car);
                gdc_car = view.findViewById(R.id.gdc_car);
                k_car = view.findViewById(R.id.k_car);

                SharedPreferences car_type = getSharedPreferences("car_type", MODE_PRIVATE);
                int cartype = car_type.getInt("cartype", 200);
                if (cartype == 200) {
                    all_car.setVisibility(View.VISIBLE);
                    gdc_car.setVisibility(View.GONE);
                    k_car.setVisibility(View.GONE);
                } else if (cartype == 201) {
                    all_car.setVisibility(View.GONE);
                    gdc_car.setVisibility(View.VISIBLE);
                    k_car.setVisibility(View.GONE);
                } else {
                    all_car.setVisibility(View.GONE);
                    gdc_car.setVisibility(View.GONE);
                    k_car.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.close_popup:
                popupWindow.dismiss();
                break;
            case R.id.AllCarLayout:
                SharedPreferences.Editor car_type01 = getSharedPreferences("car_type", MODE_PRIVATE).edit();
                car_type01.putInt("cartype", 200).apply();
                all_car.setVisibility(View.VISIBLE);
                gdc_car.setVisibility(View.GONE);
                k_car.setVisibility(View.GONE);
                popupWindow.dismiss();
                View view01 = getLayoutInflater().inflate(R.layout.data_load, null);
                loadPopup = PopupFactory.LoadPopup(view01, getWindow());
                callInfo.requestCartIdInfo(false, start_date, start_stationCode, end_stationCode, 200);
                break;
            case R.id.GDCCarLayout:
                SharedPreferences.Editor car_type02 = getSharedPreferences("car_type", MODE_PRIVATE).edit();
                car_type02.putInt("cartype", 201).apply();
                all_car.setVisibility(View.GONE);
                gdc_car.setVisibility(View.VISIBLE);
                k_car.setVisibility(View.GONE);
                this.popupWindow.dismiss();
                View view02 = getLayoutInflater().inflate(R.layout.data_load, null);
                loadPopup = PopupFactory.LoadPopup(view02, getWindow());
                callInfo.requestCartIdInfo(false, start_date, start_stationCode, end_stationCode, 201);
                break;
            case R.id.KCarLayout:
                SharedPreferences.Editor car_type03 = getSharedPreferences("car_type", MODE_PRIVATE).edit();
                car_type03.putInt("cartype", 202).apply();
                all_car.setVisibility(View.GONE);
                gdc_car.setVisibility(View.GONE);
                k_car.setVisibility(View.VISIBLE);
                this.popupWindow.dismiss();
                View view03 = getLayoutInflater().inflate(R.layout.data_load, null);
                loadPopup = PopupFactory.LoadPopup(view03, getWindow());
                callInfo.requestCartIdInfo(false, start_date, start_stationCode, end_stationCode, 202);
                break;
        }
    }
}
