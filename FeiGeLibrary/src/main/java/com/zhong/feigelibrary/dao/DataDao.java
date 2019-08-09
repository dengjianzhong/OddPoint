package com.zhong.feigelibrary.dao;

import com.j256.ormlite.dao.Dao;
import com.zhong.feigelibrary.database.DatabaseFactory;
import com.zhong.feigelibrary.database.table.StationCode;
import com.zhong.feigelibrary.utils.GlobleFactory;

import java.sql.SQLException;
import java.util.List;

public class DataDao {

    private Dao dao;
    private List<StationCode> dataMap;

    public DataDao() {
        DatabaseFactory station_codes = new DatabaseFactory(GlobleFactory.getGlobleContext(), "station_codes", StationCode.class);
        try {
            dao = station_codes.getDao(StationCode.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 向user表中添加一条数据
    public void insert(StationCode data) {
        try {
            dao.create(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 删除user表中的一条数据
    public void delete(StationCode data) {
        try {
            dao.delete(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 修改user表中的一条数据
    public void update(StationCode data) {
        try {
            dao.update(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 查询user表中的所有数据
    public List<StationCode> selectAll() {
        List<StationCode> users = null;
        try {
            users = dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public List<StationCode> selectData(String name){
        try {
            dataMap = dao.queryBuilder().where().eq("station_name", name).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dataMap;
    }

    public List<StationCode> selectCodeData(String name){
        try {
            dataMap = dao.queryBuilder().where().eq("station_code", name).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataMap;
    }

    // 根据ID取出用户信息
    public StationCode queryById(int id) {
        StationCode user = null;
        try {
            user = (StationCode) dao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
}
