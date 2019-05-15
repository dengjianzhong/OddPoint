package com.zhong.oddpoint.main.activity;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.zhong.d_oddpoint.utils.ToastFactory;
import com.zhong.oddpoint.main.R;
import com.zhong.oddpoint.main.bean.login_result;
import com.zhong.oddpoint.main.bean.verificationCode;
import com.zhong.oddpoint.main.bean.verify_result;

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
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {
    private ImageView yz_code;
    private String image = "";
    private Random random = new Random();
    private RequestOptions requestOptions = new RequestOptions()
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE);
    private String VideoUri = null;

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
//                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    View view = getLayoutInflater().inflate(R.layout.layout, null);
                    new ToastFactory(LoginActivity.this).setLayoutView(view).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                    break;
                //验证码验证失败
                case 404:
                    Toast.makeText(LoginActivity.this, "验证码验证失败", Toast.LENGTH_SHORT).show();
                    break;
                //用户登录失败
                case 405:
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
    private long _data = 8;
    private List<String> cookie_info = new ArrayList<>();
    private String co = "";

    //登录标识
    private boolean login_flag = false;
    private String data;
    private String uname;
    private String pwd;
    private ImageView flush;
    private VideoView videoView;
    private TextView lf_login;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setStatusBar();

        yz_code = findViewById(R.id.yz_code);
        yz_code.setOnTouchListener(this);
        yz_info = findViewById(R.id.yz_info);
        yz_code.setAlpha(0.8f);
        username = findViewById(R.id.editText1);
        password = findViewById(R.id.editText2);
        videoView = findViewById(R.id.videoView);
        lf_login = findViewById(R.id.lf_login);
        lf_login.setOnClickListener(this);
        findViewById(R.id.button).setOnClickListener(this);
        findViewById(R.id.flush).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yz_info.setText("");
                list.clear();
                cookie_info.clear();
                captchaInformation = "";
                co = "";
                getVerificationCode();
            }
        });
        //生成验证码
        getVerificationCode();
    }

    public void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button) {
            uname = username.getText().toString();
            pwd = password.getText().toString();
            //提取验证码
            data = new String();
            for (int i = 0; i < list.size(); i++) {
                if (i == 0) {
                    data = list.get(i).getX() + ",";
                } else {
                    data = data + "," + list.get(i).getX() + ",";
                }
                data = data + list.get(i).getY();
            }

            //过滤cookie信息
            for (int i = 0; i < cookie_info.size(); i++) {
                int end_index = cookie_info.get(i).indexOf(";");
                String substring = "";
                if (i == cookie_info.size() - 1) {
                    substring = cookie_info.get(i).substring(0, end_index);
                } else {
                    substring = cookie_info.get(i).substring(0, end_index + 1);
                }
                co = co + substring;
                Log.i("stat", "" + substring);
            }

            //图文校验
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://kyfw.12306.cn/passport/captcha/captcha-check?callback=jQuery19101905026360597606_1557740600721&answer=" + data + "&rand=sjrand&login_site=E&_=1557740600723")
                    .header("Referer", "https://kyfw.12306.cn/otn/resources/login.html")
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.112 Safari/537.36")
                    .header("Cookie", co)
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
                        if (jsondata.contains("jQuery")){
                            jsondata=jsondata.substring(jsondata.indexOf("{"),jsondata.indexOf("}")+1);
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
        } else {
            handler.sendEmptyMessage(101);
        }
    }

    //12306用户登录验证
    public void userAuthentication() {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8");
        //"username=" + uname + "&password=" + pwd + "&appid=otn&answer=" + data
        FormBody.Builder builder=new FormBody.Builder()
                .add("username",uname)
                .add("password",pwd)
                .add("appid","otn")
                .add("answer",data);
        OkHttpClient okHttpClient = new OkHttpClient();
        Request requestt = new Request.Builder()
                .url("https://kyfw.12306.cn/passport/web/login")
                .post(builder.build())
                .header("Origin", "https://kyfw.12306.cn")
                .header("Referer", "https://kyfw.12306.cn/otn/resources/login.html")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.112 Safari/537.36")
                .header("Cookie", co)
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
                    login_result login_result = new Gson().fromJson(s, login_result.class);
                    if (login_result.getResult_message().equals("登录成功")) {
                        handler.sendEmptyMessage(101);
                    } else {
                        handler.sendEmptyMessage(405);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void getVerificationCode() {
        OkHttpClient okHttpClient = new OkHttpClient();
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
                Response result=response;
                String cartId = result.body().string();
                List<String> headers = result.headers("Set-Cookie");
                cookie_info.addAll(headers);
                Log.i("status", "info=" + cartId);
                String substring = cartId.substring(cartId.indexOf("{"),cartId.indexOf("}")+1);
                parseJson(substring);
                Log.i("status", "substring=" + substring);
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
            verificationCode.setX(random.nextInt(48)+16);
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
        initVideo();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (videoView.isPlaying()) {
            videoView.stopPlayback();
            timer.cancel();
        }
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
