package com.zhong.oddpoint.main.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.zhong.toollib.broccoli.Broccoli;
import com.zhong.toollib.broccoli.BroccoliGradientDrawable;
import com.zhong.toollib.broccoli.PlaceholderParameter;
import com.zhong.toollib.factory.PopupFactory;
import com.zhong.oddpoint.main.R;
import com.zhong.oddpoint.main.bean.login_result;
import com.zhong.oddpoint.main.bean.verificationCode;
import com.zhong.oddpoint.main.bean.verify_result;
import com.zhong.toollib.helper.DaoHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {
    private ImageView yz_code;
    private String image = "";
    private Random random = new Random();
    private RequestOptions requestOptions = new RequestOptions()
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE);


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 200:
                    Glide.with(LoginActivity.this)
                            .load("data:image/jpg;base64," + image)
                            .apply(requestOptions)
                            .into(yz_code);
                    break;
                case 101:
                    if (loadPopup != null && loadPopup.isShowing()) {
                        loadPopup.dismiss();
                    }
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                    break;
                case 102:
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                    break;
                //验证码验证失败
                case 404:
                    if (loadPopup != null && loadPopup.isShowing()) {
                        loadPopup.dismiss();
                    }
                    Toast.makeText(LoginActivity.this, "验证码验证失败", Toast.LENGTH_SHORT).show();
                    break;
                //用户登录失败
                case 405:
                    if (loadPopup != null && loadPopup.isShowing()) {
                        loadPopup.dismiss();
                    }
                    Toast.makeText(LoginActivity.this, "用户或密码错误", Toast.LENGTH_SHORT).show();
                    break;
                case 500:
                    videoView.seekTo(msg.arg1);
                    break;
            }
        }
    };
    private List<verificationCode> list = new ArrayList<>();
    private TextView yz_info;
    private String captchaInformation = "";
    private EditText username, password;
    private List<String> cookie_info = new ArrayList<>();
    private String cookie = "";

    private String data;
    private String uname;
    private String pwd;
    private VideoView videoView;
    private TextView lf_login;
    private Timer timer;
    private PopupWindow loadPopup;
    private ImageView finish_im;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setStatusBar();

        yz_code = findViewById(R.id.yz_code);
        yz_code.setOnTouchListener(this);
        yz_info = findViewById(R.id.yz_info);
        username = findViewById(R.id.editText1);
        password = findViewById(R.id.editText2);
        videoView = findViewById(R.id.videoView);
        lf_login = findViewById(R.id.lf_login);
        finish_im = findViewById(R.id.finish);
        finish_im.setOnClickListener(this);
        lf_login.setOnClickListener(this);
        findViewById(R.id.button).setOnClickListener(this);
        findViewById(R.id.flush).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yz_info.setText("");
                list.clear();
                cookie_info.clear();
                captchaInformation = "";
                cookie = "";
                getVerificationCode();
            }
        });
        initData();
        //生成验证码
        getVerificationCode();
    }

    private void initData() {
        lf_login.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        lf_login.getPaint().setAntiAlias(true);//抗锯齿

        Broccoli broccoli = new Broccoli();

        broccoli.addPlaceholder(new PlaceholderParameter.Builder()
                .setView(findViewById(R.id.button))
                .setDrawable(new BroccoliGradientDrawable(Color.parseColor("#DDDDDD"),
                        Color.parseColor("#CCCCCC"), 0, 1000, new LinearInterpolator()))
                .build());
        broccoli.show();
    }

    public void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public void getLoginParameter() {
        //-----提取验证码-------
        data = new String();
        for (int i = 0; i < list.size(); i++) {
            if (i == 0) {
                data = list.get(i).getX() + ",";
            } else {
                data = data + "," + list.get(i).getX() + ",";
            }
            data = data + list.get(i).getY();
        }

        //------过滤cookie信息-------
        for (int i = 0; i < cookie_info.size(); i++) {
            int end_index = cookie_info.get(i).indexOf(";");
            String substring = "";
            if (i == cookie_info.size() - 1) {
                substring = cookie_info.get(i).substring(0, end_index);
            } else {
                substring = cookie_info.get(i).substring(0, end_index + 1);
            }
            cookie = cookie + substring;
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button) {
            uname = username.getText().toString();
            pwd = password.getText().toString();

            if (!TextUtils.isEmpty(uname) && !TextUtils.isEmpty(pwd)) {
                //弹出PopupWindow登录提示框
                View view01 = getLayoutInflater().inflate(R.layout.data_load, null);
                TextView status_text = view01.findViewById(R.id.status_tex);
                status_text.setText("登录中...");
                loadPopup = PopupFactory.loadPopupWindow(LoginActivity.this, view01, Gravity.CENTER);

                getLoginParameter();
                graphicVerification();
            } else {
                Toast.makeText(this, "请输入账号或密码", Toast.LENGTH_SHORT).show();
            }
        } else if (view.getId() == R.id.finish) {
            finish();
        } else {
            handler.sendEmptyMessage(102);
        }

//        try {
//            boolean zipFile = CoZipTool.zipFile("/mnt/sdcard/down/log/downLog.1.txt", "/mnt/sdcard/email/aa.zip");
//            if (zipFile){
//
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    //------图文校验------
    public void graphicVerification() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://kyfw.12306.cn/passport/captcha/captcha-check?callback=jQuery19101905026360597606_1557740600721&answer=" + data + "&rand=sjrand&login_site=E&_=1557740600723")
                .header("Referer", "https://kyfw.12306.cn/otn/resources/login.html")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.112 Safari/537.36")
                .header("Cookie", cookie)
                .get()
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    String jsondata = response.body().string();
                    if (jsondata.contains("jQuery")) {
                        jsondata = jsondata.substring(jsondata.indexOf("{"), jsondata.indexOf("}") + 1);
                    }
                    verify_result verify_result = new Gson().fromJson(jsondata, verify_result.class);
                    if (verify_result.getResult_message().equals("验证码校验成功")) {
                        userAuthentication();
                    } else {
                        handler.sendEmptyMessage(404);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(404);
                }
            }
        });
    }

    //12306用户登录验证
    public void userAuthentication() {
        FormBody.Builder builder = new FormBody.Builder()
                .add("username", uname)
                .add("password", pwd)
                .add("appid", "otn")
                .add("answer", data);
        OkHttpClient okHttpClient = new OkHttpClient();
        Request requestt = new Request.Builder()
                .url("https://kyfw.12306.cn/passport/web/login")
                .post(builder.build())
                .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.112 Safari/537.36")
                .header("Cookie", cookie + "_jc_save_wfdc_flag=dc; _jc_save_fromStation=%u91CD%u5E86%2CCQW; _jc_save_toDate=2019-02-19; _jc_save_toStation=%u4E07%u5DDE%2CWYW; _jc_save_fromDate=2019-02-19; route=c5c62a339e7744272a54643b3be5bf64; BIGipServerotn=4141285642.64545.0000; RAIL_EXPIRATION=1558317758068; RAIL_DEVICEID=Eb8dce01sgrPdSKy-Zx3DNf-7dQ8d9cOd5kNTha_lNUzoBzSNZkwKhi4O3Nwip1KQjYDFVouzsOJC6GoP6CISAxqxeIFXRqaLhTjnA-llS-wStez_KwPnbgdNPlnOkF2RAxTc6j-e3APortK3WuOsoyJaMZhOlbR")
                .build();
        okHttpClient.newCall(requestt).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    String s = response.body().string();
                    if (s != null) {
                        login_result login_result = new Gson().fromJson(s, login_result.class);
                        if (login_result.getResult_message().equals("登录成功")) {
                            handler.sendEmptyMessage(101);
                        } else {
                            handler.sendEmptyMessage(405);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //------开始生成验证码------
    public void getVerificationCode() {
        OkHttpClient okHttpClient = new OkHttpClient();
        // 使用ConcurrentMap存储cookie信息，因为数据在内存中，所以只在程序运行阶段有效，程序结束后即清空
//        okHttpClient = new OkHttpClient.Builder()
//                .cookieJar(new CookieJar() {
//                    // 使用ConcurrentMap存储cookie信息，因为数据在内存中，所以只在程序运行阶段有效，程序结束后即清空
//                    private ConcurrentMap<String, List<Cookie>> storage = new ConcurrentHashMap<>();
//
//                    @Override
//                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
//                        String host = url.host();
//                        if (cookies != null && !cookies.isEmpty()) {
//                            storage.put(host, cookies);
//                        }
//                    }
//                    @Override
//                    public List<Cookie> loadForRequest(HttpUrl url) {
//                        String host = url.host();
//                        List<Cookie> list = storage.get(host);
//                        return list == null ? new ArrayList<Cookie>() : list;
//                    }
//                }).build();
        final Request request = new Request.Builder()
                .url("https://kyfw.12306.cn/passport/captcha/captcha-image64")
                .get()
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Response result = response;
                String cartId = result.body().string();
                List<String> headers = result.headers("Set-Cookie");
                cookie_info.addAll(headers);
                String substring = cartId.substring(cartId.indexOf("{"), cartId.indexOf("}") + 1);
                parseJson(substring);
            }
        });
    }

    public void parseJson(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            image = jsonObject.getString("image");
            handler.sendEmptyMessage(200);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        verificationCode verificationCode = new verificationCode();
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        /*得到图文验证的Y轴坐标位置*/
        if (y >= 0.0 & y <= 245.0) {
            verificationCode.setY(random.nextInt(50) + 16);
        } else if (y >= 255.0 && y <= 500.0) {
            verificationCode.setY(random.nextInt(43) + 94);
        }
        /*得到图文验证的X轴坐标位置*/
        if (x < 220.0) {
            verificationCode.setX(random.nextInt(48) + 16);
        } else if (x >= 240.0 && x <= 440.0) {
            verificationCode.setX(random.nextInt(48) + 88);
        } else if (x >= 460.0 && x <= 660.0) {
            verificationCode.setX(random.nextInt(49) + 160);
        } else if (x >= 680.0 && x <= 880.0) {
            verificationCode.setX(random.nextInt(47) + 234);
        }
        list.add(verificationCode);

        if (TextUtils.isEmpty(captchaInformation)) {
            captchaInformation = verificationCode.getX() + "," + verificationCode.getY();
        } else {
            captchaInformation = verificationCode.getX() + "," + verificationCode.getY() + "," + captchaInformation;
        }

        yz_info.setText(captchaInformation);
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
//        initVideo();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        if (videoView.isPlaying()) {
//            videoView.stopPlayback();
//            if (timer!=null){
//                timer.cancel();
//            }
//        }
    }

    public void initVideo() {
//        VideoUri=Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.welcome_video).toString();
        Uri vurl = Uri.parse("https://qzonestyle.gtimg.cn/qzone/qzact/act/external/weishi/video/pc-home-video.mp4");
        videoView.setVideoURI(vurl);
//        videoView.setVideoPath(VideoUri);
        videoView.start();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                mp.setVolume(0f, 0f);
                mp.setLooping(true);
                mp.seekTo(9000);
            }
        });
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (videoView.getCurrentPosition() > 18000) {
                    Message obtain = handler.obtainMessage();
                    obtain.what = 500;
                    obtain.arg1 = 9000;
                    handler.sendMessage(obtain);
                }
            }
        }, 0, 1000);
    }

}
