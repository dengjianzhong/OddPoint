package com.zhong.toollib.database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "StationCode")
public class StationCode extends BaseDBBean implements Serializable {
    @DatabaseField(columnName = "station_name")
    private String station_name;
    @DatabaseField(columnName = "station_code")
    private String station_code;

    public String getStation_name() {
        return station_name;
    }

    public void setStation_name(String station_name) {
        this.station_name = station_name;
    }

    public String getStation_code() {
        return station_code;
    }

    public void setStation_code(String station_code) {
        this.station_code = station_code;
    }
}
