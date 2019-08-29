package com.zhong.utilslibrary.anim;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LottieDialog extends DialogFragment {

    private DisplayMetrics dm;
    private String tipText;

    public void setTipText(String tipText) {
        this.tipText = TextUtils.isEmpty(tipText) ? "加载中..." : tipText;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View contentView  = LottieAnimation.getInstance(getContext()).loaddingAnimationFile(getContext(), "permission.json",tipText);
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return contentView ;
    }


    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout((int) (dm.widthPixels * 0.5), (int) (dm.widthPixels * 0.5));
    }
}
