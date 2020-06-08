package com.zhong.voiplib.utils;

public class CallType {
    /**
     * 1, 对方无应答
     * 2, 对方繁忙（已在通话中）
     * 3, 未接通挂断
     * 5, 已接通远程挂断
     */

    public static String getCallType(int state, boolean isSelf) {
        String callTypeText;
        switch (state) {
            case 1:
                callTypeText = isSelf ? "对方无应答" :"对方已取消";
                break;
            case 3:
                callTypeText = isSelf ? "已取消":"对方已取消";
                break;
            case 2:
                callTypeText = isSelf ? "通话繁忙":"对方通话繁忙";
                break;
            case 4:
            case 5:
            case 6:
                callTypeText = "已挂断";
                break;
            case 7:
                callTypeText = "连接超时";
                break;
            case 8:
                callTypeText = "已被其他客户端接受";
                break;
            default:
                callTypeText = "未知错误";
                break;
        }

        return callTypeText;
    }
}
