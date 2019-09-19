package com.zhong.oddpoint.main.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhong.utilslibrary.database.table.StationCode;
import com.zhong.utilslibrary.dao.DataDao;
import com.zhong.oddpoint.main.R;
import com.zhong.oddpoint.main.bean.car_data;

import java.util.List;

public class PurchaseList extends BaseAdapter {
    private Context context;
    private List<car_data> list;
    private final DataDao dataDao;
    private List<StationCode> stationCodes1;

    public PurchaseList(Context context) {
        this.context = context;
        dataDao = new DataDao();
    }

    public void setData(List<car_data> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_view, null);

        //起始位置
        TextView start_positon=view.findViewById(R.id.textView1_1);
        //目的地
        TextView end_positon=view.findViewById(R.id.textView3_1);
        TextView startTime = view.findViewById(R.id.textView1_2);
        TextView endTime = view.findViewById(R.id.textView3_2);
        TextView cartId = view.findViewById(R.id.textView2_1);
        TextView countTime = view.findViewById(R.id.textView2_2);
        TextView seat2 = view.findViewById(R.id.seat2);
        TextView seat1 = view.findViewById(R.id.seat1);
        TextView seat = view.findViewById(R.id.seat);
        TextView no_seat = view.findViewById(R.id.no_seat);
        TextView sw_seat = view.findViewById(R.id.sw_seat);
        TextView min_money = view.findViewById(R.id.min_money);
        TextView qi_text = view.findViewById(R.id.qi);

        List<StationCode> stationCodes1 = dataDao.selectCodeData(list.get(position).getStart_site_code());
        List<StationCode> stationCodes2 = dataDao.selectCodeData(list.get(position).getEnd_site_code());
        if (stationCodes1!=null&&stationCodes2!=null){
            start_positon.setText(stationCodes1.get(0).getStation_name());
            end_positon.setText(stationCodes2.get(0).getStation_name());
        }

        cartId.setText(list.get(position).getCar_id());
        startTime.setText(list.get(position).getStart_time());
        endTime.setText(list.get(position).getEnd_time());
        countTime.setText(list.get(position).getCount_time());
        if (!TextUtils.isEmpty(list.get(position).getTheLowestPrice())) {
            min_money.setVisibility(View.VISIBLE);
            qi_text.setVisibility(View.VISIBLE);
            min_money.setText(list.get(position).getTheLowestPrice());
        } else {
            min_money.setVisibility(View.INVISIBLE);
            qi_text.setVisibility(View.INVISIBLE);
        }
        setCarInfo(position, seat2, seat1, seat, sw_seat, no_seat);

        return view;
    }

    public void setCarInfo(int position, TextView seat2, TextView seat1, TextView seat, TextView sw_seat, TextView no_seat) {
        if (list.get(position).getCar_id().contains("C") || list.get(position).getCar_id().contains("G") || list.get(position).getCar_id().contains("D")) {
            //CDG预售
            String presale = list.get(position).getPresale();
            if (!TextUtils.isEmpty(presale)) {
                seat2.setText(presale);
                seat1.setVisibility(View.GONE);
                seat.setVisibility(View.GONE);
                sw_seat.setVisibility(View.GONE);
                no_seat.setVisibility(View.GONE);
                return;
            }

            int flag = 0;
            //CDG二等座
            String data2 = list.get(position).getSeat2();
            if (!TextUtils.isEmpty(data2)) {
                flag++;
                if (data2.contains("有")) {
                    seat2.setText("二等座:有票");
                } else if (data2.contains("无")) {
                    seat2.setText("二等座:无票");
                } else {
                    seat2.setText("二等座:" + data2 + "张");
                }
            } else {
                seat2.setVisibility(View.GONE);
            }

            //CDG一等座
            String data1 = list.get(position).getSeat1();
            if (!TextUtils.isEmpty(data1)) {
                flag++;
                if (data1.contains("有")) {
                    seat1.setText("一等座:有票");
                } else if (data1.contains("无")) {
                    seat1.setText("一等座:无票");
                } else {
                    seat1.setText("一等座:" + data1 + "张");
                }
            } else {
                seat1.setVisibility(View.GONE);
            }

            //特等座
            String data3 = list.get(position).getSeat();
            if (!TextUtils.isEmpty(data3)) {
                flag++;
                if (data3.contains("有")) {
                    seat.setText("特等座:有票");
                } else if (data3.contains("无")) {
                    seat.setText("特等座:无票");
                } else {
                    seat.setText("特等座:" + data3 + "张");
                }
            } else {
                seat.setVisibility(View.GONE);
            }

            //CDG商务坐座
            String swSeat = list.get(position).getSwSeat();
            if (!TextUtils.isEmpty(swSeat)) {
                flag++;
                if (swSeat.contains("有")) {
                    sw_seat.setText("商务座:有票");
                } else if (swSeat.contains("无")) {
                    sw_seat.setText("商务座:无票");
                } else {
                    sw_seat.setText("商务座:" + swSeat + "张");
                }
            } else {
                sw_seat.setVisibility(View.GONE);
            }

            if (flag < 4) {
                //CDG无座
                String noSeat = list.get(position).getNoSeat();
                if (!TextUtils.isEmpty(noSeat)) {
                    if (noSeat.contains("有")) {
                        no_seat.setText("无座:有票");
                    } else if (noSeat.contains("无")) {
                        no_seat.setText("无座:无票");
                    } else {
                        no_seat.setText("无座:" + noSeat + "张");
                    }
                } else {
                    no_seat.setVisibility(View.INVISIBLE);
                }
            } else {
                no_seat.setVisibility(View.GONE);
            }

        } else {

            sw_seat.setVisibility(View.GONE);
            //火车票预售
            String presale = list.get(position).getPresale();
            if (!TextUtils.isEmpty(presale)) {
                seat2.setText(presale);
                seat1.setVisibility(View.GONE);
                seat.setVisibility(View.GONE);
                sw_seat.setVisibility(View.GONE);
                no_seat.setVisibility(View.GONE);
                return;
            }

            //火车硬卧二等卧
            String data2 = list.get(position).getSeat2();
            if (!TextUtils.isEmpty(data2)) {
                if (data2.contains("有")) {
                    seat2.setText("硬卧:有票");
                } else if (data2.contains("无")) {
                    seat2.setText("硬卧:无票");
                } else {
                    seat2.setText("硬卧:" + data2 + "张");
                }
            } else {
                seat2.setText("硬卧:--");
            }

            //火车软卧一等卧
            String data1 = list.get(position).getSeat1();
            if (!TextUtils.isEmpty(data1)) {
                if (data1.contains("有")) {
                    seat1.setText("软卧:有票");
                } else if (data1.contains("无")) {
                    seat1.setText("软卧:无票");
                } else {
                    seat1.setText("软卧:" + data1 + "张");
                }
            } else {
                seat1.setText("软卧:--");
            }
            //火车硬座
            String data3 = list.get(position).getSeat();
            if (!TextUtils.isEmpty(data3)) {
                if (data3.contains("有")) {
                    seat.setText("硬座:有票");
                } else if (data3.contains("无")) {
                    seat.setText("硬座:无票");
                } else {
                    seat.setText("硬座:" + data3 + "张");
                }
            } else {
                seat.setVisibility(View.GONE);
            }
            //火车无座
            String noSeat = list.get(position).getNoSeat();
            if (!TextUtils.isEmpty(noSeat)) {
                if (noSeat.contains("有")) {
                    no_seat.setText("无座:有票");
                } else if (noSeat.contains("无")) {
                    no_seat.setText("无座:无票");
                } else {
                    no_seat.setText("无座:" + noSeat + "张");
                }
            } else {
                no_seat.setVisibility(View.INVISIBLE);
            }
        }
    }
}
