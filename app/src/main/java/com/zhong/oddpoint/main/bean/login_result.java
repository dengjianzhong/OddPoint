package com.zhong.oddpoint.main.bean;

public class login_result {

    /**
     * result_message : 登录成功
     * result_code : 0
     * uamtk : 2t0u8ox3fP2D7gFFNMyHOSKWd2JDsSsZOgvne_AtoMw921110
     */

    private String result_message;
    private int result_code;
    private String uamtk;

    public String getResult_message() {
        return result_message;
    }

    public void setResult_message(String result_message) {
        this.result_message = result_message;
    }

    public int getResult_code() {
        return result_code;
    }

    public void setResult_code(int result_code) {
        this.result_code = result_code;
    }

    public String getUamtk() {
        return uamtk;
    }

    public void setUamtk(String uamtk) {
        this.uamtk = uamtk;
    }
}
