package com.zhong.voiplib.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * The type Base activity.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        unbinder = ButterKnife.bind(this);

        initView();
        initData();
        regEvent();

    }



    /**
     * 收起软键盘
     *
     * @param view the view
     */
    protected void packUpKeyboard(View view) {
        InputMethodManager manager = ((InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE));
        if (manager != null) {
            manager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    /**
     * 打开activity
     *
     * @param activityClass the activity class
     * @param bundle        数据bundle
     * @param options       有关如何启动活动的其他选项。
     */
    protected void openActivity(Class<? extends Activity> activityClass, Bundle bundle, Bundle options) {
        Intent intent = new Intent(this, activityClass);
        if (bundle != null) intent.putExtras(bundle);

        if (options != null) {
            startActivity(intent, options);
        } else {
            startActivity(intent);
        }
    }

    /**
     * 打开有返回结果的activity
     *
     * @param activityClass the activity class
     * @param bundle        数据bundle
     * @param requestCode   the request code
     * @param options       有关如何启动活动的其他选项。
     */
    protected void openActivityForResult(Class<? extends Activity> activityClass, Bundle bundle, int requestCode, Bundle options) {
        Intent intent = new Intent(this, activityClass);
        if (bundle != null) intent.putExtras(bundle);

        if (options != null) {
            startActivityForResult(intent, requestCode, options);
            return;
        }
        startActivityForResult(intent, requestCode);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (unbinder != null) {
            unbinder.unbind();
        }
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


    /**
     * 注册监听
     */
    protected abstract void regEvent();

}
