package com.zhong.oddpoint.main.activity;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CalendarView;

import com.zhong.oddpoint.main.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class DateActivity extends AppCompatActivity implements CalendarView.OnDateChangeListener, View.OnClickListener {

    private CalendarView calendarView;
    private String month,day;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);
        setStatusBar();
        calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(this);
        findViewById(R.id.back_key).setOnClickListener(this);
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

    @Override
    public void onSelectedDayChange( CalendarView view, int year, int month, int dayOfMonth) {
        Intent intent = getIntent();
        if (month>=0&&month<=9){
            this.month="0"+(month+1);
        }else {
            this.month=""+(month+1);
        }
        if (dayOfMonth>=1&&dayOfMonth<=9){
            this.day="0"+dayOfMonth;
        }else {
            this.day=""+dayOfMonth;
        }
        intent.putExtra("date",year+"-"+this.month+"-"+this.day);
        setResult(-1,intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
