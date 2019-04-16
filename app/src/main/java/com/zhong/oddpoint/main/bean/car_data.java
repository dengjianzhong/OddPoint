package com.zhong.oddpoint.main.bean;

public class car_data {
    private String yd;
    private String id;
    private String car_id;
    private String start_time;
    private String end_time;
    private String count_time;
    private String start_date;
    private String seat2;
    private String seat1;
    private String seat;
    private String noSeat;
    private String swSeat;

    private String start_site_code;
    private String end_site_code;

    public String getStart_site_code() {
        return start_site_code;
    }

    public void setStart_site_code(String start_site_code) {
        this.start_site_code = start_site_code;
    }

    public String getEnd_site_code() {
        return end_site_code;
    }

    public void setEnd_site_code(String end_site_code) {
        this.end_site_code = end_site_code;
    }

    public String getSwSeat() {
        return swSeat;
    }

    public void setSwSeat(String swSeat) {
        this.swSeat = swSeat;
    }

    //预售
    private String presale;
    //最低价格
    private String theLowestPrice;

    //获取最低价格所需的参数
    private String train_no;
    private String from_station_no;
    private String to_station_no;
    private String seat_types;


    public String getTrain_no() {
        return train_no;
    }

    public void setTrain_no(String train_no) {
        this.train_no = train_no;
    }

    public String getFrom_station_no() {
        return from_station_no;
    }

    public void setFrom_station_no(String from_station_no) {
        this.from_station_no = from_station_no;
    }

    public String getTo_station_no() {
        return to_station_no;
    }

    public void setTo_station_no(String to_station_no) {
        this.to_station_no = to_station_no;
    }

    public String getSeat_types() {
        return seat_types;
    }

    public void setSeat_types(String seat_types) {
        this.seat_types = seat_types;
    }
    public String getTheLowestPrice() {
        return theLowestPrice;
    }

    public void setTheLowestPrice(String theLowestPrice) {
        this.theLowestPrice = theLowestPrice;
    }

    public String getPresale() {
        return presale;
    }

    public void setPresale(String presale) {
        this.presale = presale;
    }

    public String getNoSeat() {
        return noSeat;
    }

    public void setNoSeat(String noSeat) {
        this.noSeat = noSeat;
    }

    public String getSeat2() {
        return seat2;
    }

    public void setSeat2(String seat2) {
        this.seat2 = seat2;
    }

    public String getSeat1() {
        return seat1;
    }

    public void setSeat1(String seat1) {
        this.seat1 = seat1;
    }

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    public String getYd() {
        return yd;
    }

    public void setYd(String yd) {
        this.yd = yd;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCar_id() {
        return car_id;
    }

    public void setCar_id(String car_id) {
        this.car_id = car_id;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getCount_time() {
        return count_time;
    }

    public void setCount_time(String count_time) {
        this.count_time = count_time;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }
}
