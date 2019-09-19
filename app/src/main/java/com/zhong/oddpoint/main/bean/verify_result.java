package com.zhong.oddpoint.main.bean;

public class verify_result {

    /**
     * result_message : 验证码校验成功
     * result_code : 4
     */

    private String result_message;
    private String result_code;

    public String getResult_message() {
        return result_message;
    }

    public void setResult_message(String result_message) {
        this.result_message = result_message;
    }

    public String getResult_code() {
        return result_code;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}
