package com.zhong.oddpoint.main.activity;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.zhong.d_oddpoint.utils.PopupFactory;
import com.zhong.oddpoint.main.R;
import com.zhong.oddpoint.main.adapter.StopAdapter;
import com.zhong.oddpoint.main.bean.StopInfo;
import com.zhong.oddpoint.main.bean.car_data;
import com.zhong.oddpoint.main.port.StopDataListener;
import com.zhong.oddpoint.main.request.CallStopData;

import java.io.Serializable;
import java.util.List;

public class StopActivity extends AppCompatActivity implements StopDataListener, View.OnClickListener {

    private ListView listView;
    private String start_time;
    private car_data dataset;
    private Handler handler=new Handler(Looper.getMainLooper());
    private List<StopInfo.DataBeanX.DataBean> data;
    private String toSiteName;
    private PopupWindow loadPopup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop);

        setStatusBar();
        initView();
        initData();
    }
    public void setStatusBar(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.title_bg_color));
        }
    }

    private void initData() {
        start_time = getIntent().getStringExtra("start_time");
        dataset = (car_data) getIntent().getSerializableExtra("dataset");
        toSiteName = getIntent().getStringExtra("toSiteName");
    }

    private void initView() {
        listView = findViewById(R.id.listView);
        findViewById(R.id.back_key).setOnClickListener(this);
    }

    @Override
    public void success(StopInfo stopInfo) {
        data = stopInfo.getData().getData();
        handler.post(new Runnable() {
            @Override
            public void run() {
                listView.setAdapter(new StopAdapter(StopActivity.this, data,toSiteName));
                loadPopup.dismiss();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(490);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            View view = getLayoutInflater().inflate(R.layout.data_load, null);
                            loadPopup = PopupFactory.LoadPopup(view, getWindow());
                            new CallStopData(StopActivity.this).CallData(dataset,start_time);
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
