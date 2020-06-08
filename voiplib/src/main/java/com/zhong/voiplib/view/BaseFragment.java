package com.zhong.voiplib.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment {
    private Unbinder unbinder;
    protected Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.from(getContext()).inflate(getLayoutId(), container,false);

        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getContext();

        initView();
        initData();
    }

    /**
     * 打开activity
     *
     * @param activityClass the activity class
     * @param bundle        the bundle
     */
    protected void openActivity(Class<? extends AppCompatActivity> activityClass, Bundle bundle) {
        Intent intent = new Intent(getContext(), activityClass);
        if (bundle != null) {
            startActivity(intent, bundle);
        } else {
            startActivity(intent);
        }
    }

    /**
     * 打开有返回结果的activity
     *
     * @param activityClass the activity class
     * @param bundle        the bundle
     */
    protected void openActivityForResult(Class<Activity> activityClass, Bundle bundle, int requestCode) {
        Intent intent = new Intent(getContext(), activityClass);
        if (bundle == null) {
            startActivityForResult(intent, requestCode);
        } else {
            startActivityForResult(intent, requestCode, bundle);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    public int dip2px(float dipValue) {
        float scale = this.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }


    /**
     * 得到布局文件ID
     *
     * @return the layout id
     */
    protected abstract int getLayoutId();

    /**
     * Init view.
     */
    protected abstract void initView();

    /**
     * Init data.
     */
    protected abstract void initData();

}
