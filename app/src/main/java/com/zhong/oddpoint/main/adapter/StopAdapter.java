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
    private int TITLE_COUNT = 1;

    public StopAdapter(Context context, List<StopInfo.DataBeanX.DataBean> beanList, String toSiteName) {
        this.context = context;
        this.beanList = beanList;
        this.toSiteName = toSiteName;
    }

    @Override
    public int getCount() {
        return beanList.size() + TITLE_COUNT;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.stop_info_item, null);
            myViewHolder = new MyViewHolder();
            myViewHolder.SiteName = convertView.findViewById(R.id.SiteName);
            myViewHolder.Reach = convertView.findViewById(R.id.reach);
            myViewHolder.StartTime = convertView.findViewById(R.id.StartTime);
            myViewHolder.StopTime = convertView.findViewById(R.id.StopTime);
            myViewHolder.view = convertView.findViewById(R.id.view);
            myViewHolder.line1 = convertView.findViewById(R.id.line1);
            convertView.setTag(myViewHolder);
        } else {
            myViewHolder = (MyViewHolder) convertView.getTag();
        }

        if (position != 0) {
            position--;

            myViewHolder.SiteName.setTextColor(Color.parseColor("#929292"));
            myViewHolder.Reach.setTextColor(Color.parseColor("#929292"));
            myViewHolder.StartTime.setTextColor(Color.parseColor("#929292"));
            myViewHolder.StopTime.setTextColor(Color.parseColor("#929292"));

            myViewHolder.SiteName.setTextSize(14);
            myViewHolder.Reach.setTextSize(14);
            myViewHolder.StartTime.setTextSize(14);
            myViewHolder.StopTime.setTextSize(14);

            myViewHolder.line1.setVisibility(View.VISIBLE);
            myViewHolder.view.setVisibility(View.VISIBLE);

            myViewHolder.SiteName.setText(beanList.get(position).getStation_name());
            myViewHolder.Reach.setText(beanList.get(position).getArrive_time());
            myViewHolder.StartTime.setText(beanList.get(position).getStart_time());
            myViewHolder.StopTime.setText(beanList.get(position).getStopover_time());

            if ((beanList.get(position).isIsEnabled() && (position != 0 ? true : false) && !beanList.get(position - 1).isIsEnabled()) || (beanList.get(position).isIsEnabled() && position == 0)) {
                setTextColors(true);
            } else if (beanList.get(position).isIsEnabled()) {
                setTextColors(false);
                if (beanList.get(position).getStation_name().contains(toSiteName)) {
                    setTextColors(true);
                    myViewHolder.line1.setBackgroundColor(Color.parseColor("#979797"));
                    myViewHolder.view.setBackgroundResource(R.drawable.round_bg01);
                } else {
                    myViewHolder.view.setBackgroundResource(R.drawable.round_bg02);
                }
            } else {
                myViewHolder.line1.setBackgroundColor(Color.parseColor("#979797"));
                myViewHolder.view.setBackgroundResource(R.drawable.round_bg03);
            }

            if (position == beanList.size() - 1) {
                myViewHolder.line1.setVisibility(View.GONE);
            }
        } else {
            setTextColors(false);
            myViewHolder.line1.setVisibility(View.INVISIBLE);
            myViewHolder.view.setVisibility(View.INVISIBLE);

            myViewHolder.SiteName.setTextSize(16);
            myViewHolder.Reach.setTextSize(16);
            myViewHolder.StartTime.setTextSize(16);
            myViewHolder.StopTime.setTextSize(16);

            myViewHolder.SiteName.setText("站名");
            myViewHolder.Reach.setText("到达");
            myViewHolder.StartTime.setText("发车");
            myViewHolder.StopTime.setText("停留");
        }
        return convertView;
    }

    public class ViewHolder {

    }

    public void setTextColors(boolean b) {
        if (b) {
            myViewHolder.SiteName.setTextColor(Color.parseColor("#1c8eff"));
            myViewHolder.Reach.setTextColor(Color.parseColor("#1c8eff"));
            myViewHolder.StartTime.setTextColor(Color.parseColor("#1c8eff"));
            myViewHolder.StopTime.setTextColor(Color.parseColor("#1c8eff"));
        } else {
            myViewHolder.SiteName.setTextColor(Color.parseColor("#000000"));
            myViewHolder.Reach.setTextColor(Color.parseColor("#000000"));
            myViewHolder.StartTime.setTextColor(Color.parseColor("#000000"));
            myViewHolder.StopTime.setTextColor(Color.parseColor("#000000"));
        }
    }

    public class MyViewHolder {
        TextView SiteName;
        TextView Reach;
        TextView StartTime;
        TextView StopTime;
        View view;
        View line1;
    }
}
