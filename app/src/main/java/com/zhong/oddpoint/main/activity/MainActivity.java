package com.zhong.oddpoint.main.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.zhong.oddpoint.main.R;
import com.zhong.oddpoint.main.adapter.MyFragmentAdapter;
import com.zhong.oddpoint.main.fragment.OrderForm_Fragment;
import com.zhong.oddpoint.main.fragment.TrainTicket_Fragment;
import com.zhong.oddpoint.main.request.CallTrainNo;

import java.util.ArrayList;
import java.util.List;

import static com.ashokvarma.bottomnavigation.BottomNavigationBar.MODE_FIXED;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private BottomNavigationBar bottom_navigation_bar;
    private ViewPager viewPager;
    private List<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setStatusBar();
        viewPager = findViewById(R.id.viewPager);
        bottom_navigation_bar = findViewById(R.id.bottom_navigation_bar);
        findViewById(R.id.search).setOnClickListener(this);
        setBNB();
        initLayout();
    }

    public void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//            //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(getResources().getColor(R.color.title_bg_color));
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public void setBNB() {
        bottom_navigation_bar.setMode(MODE_FIXED) // 设置mode
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)  // 背景样式
                .setInActiveColor("#929292") // 未选中状态颜色
                .setActiveColor("#1296db") // 选中状态颜色
                .addItem(new BottomNavigationItem(R.mipmap.main_page, "首页"))
                .addItem(new BottomNavigationItem(R.mipmap.dd, "订单"))
                .addItem(new BottomNavigationItem(R.mipmap.yh, "个人中心"))
                .addItem(new BottomNavigationItem(R.mipmap.setting, "设置"))
                .setFirstSelectedPosition(0) //设置默认选中位置
                .initialise();  // 提交初始化（完成配置）
    }

    public void initLayout() {

        TrainTicket_Fragment trainTicket_fragment = new TrainTicket_Fragment();
        fragmentList.add(trainTicket_fragment);
        OrderForm_Fragment orderFormFragment = new OrderForm_Fragment();
        fragmentList.add(orderFormFragment);
        MyFragmentAdapter fragmentAdapter = new MyFragmentAdapter(getSupportFragmentManager());
        fragmentAdapter.setData(fragmentList);
        viewPager.setAdapter(fragmentAdapter);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onClick(View v) {

//        Toast.makeText(this, "该功能正在开发中", Toast.LENGTH_SHORT).show();
//        startActivity(new Intent(this,SearchActivity.class));
        new CallTrainNo(this).downloadData();
    }
}
