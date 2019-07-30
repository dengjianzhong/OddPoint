package com.zhong.oddpoint.main.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zhong.oddpoint.main.R;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener, TextView.OnEditorActionListener {

    private TextView close_button;
    private EditText editTextS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        setListener();
    }

    private void initView() {
        close_button = findViewById(R.id.close_button);
        editTextS =findViewById(R.id.editTextS);
    }

    private void setListener() {
        close_button.setOnClickListener(this);
        editTextS.setOnEditorActionListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.close_button) {
            finish();
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//        String editTextSString = editTextS.getText().toString().trim();
//        if (!TextUtils.isEmpty(editTextSString)) {
//            Toast.makeText(this, "搜索", Toast.LENGTH_SHORT).show();
//            return false;
//        }
        Toast.makeText(this, "该功能正在开发阶段", Toast.LENGTH_SHORT).show();
        return false;
    }
}
