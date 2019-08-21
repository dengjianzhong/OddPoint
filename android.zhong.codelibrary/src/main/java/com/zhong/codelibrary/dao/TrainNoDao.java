package com.zhong.codelibrary.dao;

import com.j256.ormlite.dao.Dao;
import com.zhong.codelibrary.database.DatabaseFactory;
import com.zhong.codelibrary.database.table.StationTrainNo;
import com.zhong.codelibrary.utils.GlobleFactory;

import java.sql.SQLException;
import java.util.List;

public class TrainNoDao {

    private Dao dao;
    private List<StationTrainNo> dataMap;

    public TrainNoDao() {
        DatabaseFactory station_codes = new DatabaseFactory(GlobleFactory.getGlobleContext(), "station_train_no", StationTrainNo.class);
        try {
            dao = station_codes.getDao(StationTrainNo.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 向user表中添加一条数据
    public void insert(StationTrainNo data) {
        try {
            dao.create(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 删除user表中的一条数据
    public void delete(StationTrainNo data) {
        try {
            dao.delete(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 修改user表中的一条数据
    public void update(StationTrainNo data) {
        try {
            dao.update(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 查询user表中的所有数据
    public List<StationTrainNo> selectAll() {
        List<StationTrainNo> users = null;
        try {
            users = dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public List<StationTrainNo> selectTrain_Code(String name){
        try {
            dataMap = dao.queryBuilder().where().eq("station_name", name).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dataMap;
    }

    public List<StationTrainNo> selectTrain_No(String name){
        try {
            dataMap = dao.queryBuilder().where().eq("station_code", name).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataMap;
    }

    // 根据ID取出用户信息
    public StationTrainNo queryById(int id) {
        StationTrainNo user = null;
        try {
            user = (StationTrainNo) dao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
}
