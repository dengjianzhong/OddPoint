package com.zhong.oddpoint.main.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhong.oddpoint.main.R;
import com.zhong.oddpoint.main.bean.StopInfo;

import java.util.List;

public class StopAdapter extends BaseAdapter {
    private Context context;
    private List<StopInfo.DataBeanX.DataBean> beanList;
    private String toSiteName;
    private MyViewHolder myViewHolder;

    public StopAdapter(Context context, List<StopInfo.DataBeanX.DataBean> beanList,String toSiteName) {
        this.context=context;
        this.beanList=beanList;
        this.toSiteName=toSiteName;
    }

    @Override
    public int getCount() {
        return beanList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.stop_info_item, null);
        myViewHolder = new MyViewHolder();
        myViewHolder.SiteName=view.findViewById(R.id.SiteName);
        myViewHolder.Reach=view.findViewById(R.id.reach);
        myViewHolder.StartTime=view.findViewById(R.id.StartTime);
        myViewHolder.StopTime=view.findViewById(R.id.StopTime);
        myViewHolder.view=view.findViewById(R.id.view);
        myViewHolder.line1=view.findViewById(R.id.line1);

        myViewHolder.SiteName.setText(beanList.get(position).getStation_name());
        myViewHolder.Reach.setText(beanList.get(position).getArrive_time());
        myViewHolder.StartTime.setText(beanList.get(position).getStart_time());
        myViewHolder.StopTime.setText(beanList.get(position).getStopover_time());

        if ((beanList.get(position).isIsEnabled()&&(position!=0?true:false)&&!beanList.get(position-1).isIsEnabled())||(beanList.get(position).isIsEnabled()&&position==0)){
            setTextColors();
        }else if (beanList.get(position).isIsEnabled()){
            myViewHolder.SiteName.setTextColor(Color.parseColor("#000000"));
            myViewHolder.Reach.setTextColor(Color.parseColor("#000000"));
            myViewHolder.StartTime.setTextColor(Color.parseColor("#000000"));
            myViewHolder.StopTime.setTextColor(Color.parseColor("#000000"));
            if (beanList.get(position).getStation_name().contains(toSiteName)){
                setTextColors();
                myViewHolder.line1.setBackgroundColor(Color.parseColor("#979797"));
                myViewHolder.view.setBackgroundResource(R.drawable.round_bg01);
            }else {
                myViewHolder.view.setBackgroundResource(R.drawable.round_bg02);
            }
        }else {
            myViewHolder.line1.setBackgroundColor(Color.parseColor("#979797"));
            myViewHolder.view.setBackgroundResource(R.drawable.round_bg03);
        }

        if (position==beanList.size()-1){
            myViewHolder.line1.setVisibility(View.GONE);
        }
        return view;
    }

    public void setTextColors(){
        myViewHolder.SiteName.setTextColor(Color.parseColor("#1c8eff"));
        myViewHolder.Reach.setTextColor(Color.parseColor("#1c8eff"));
        myViewHolder.StartTime.setTextColor(Color.parseColor("#1c8eff"));
        myViewHolder.StopTime.setTextColor(Color.parseColor("#1c8eff"));
    }
    public class MyViewHolder{
        TextView SiteName;
        TextView Reach;
        TextView StartTime;
        TextView StopTime;
        TextView view;
        TextView line1;
    }
}
