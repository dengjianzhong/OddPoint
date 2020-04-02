package com.zhong.toollib.helper;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.table.TableUtils;
import com.zhong.toollib.database.BaseDBBean;

import java.sql.SQLException;
import java.util.List;

/**
 * The type Data dao.
 */
public class DaoHelper {

    private Dao dao;
    private SqlLiteDBHelper dbHelper;
    private List dataMap;

    /**
     * Instantiates a new Data dao.
     *
     * @param context   the context
     * @param tableBean the table bean
     */
    public DaoHelper(Context context, Class<? extends BaseDBBean> tableBean) {
        try {
            dbHelper = SqlLiteDBHelper.getManager(context);
            dao = dbHelper.getDao(tableBean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTable(Class<? extends BaseDBBean> tableBean){
        try {
            if (dao.isTableExists())
            TableUtils.createTable(dao.getConnectionSource(),tableBean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 向user表中添加一条数据.
     *
     * @param bean the bean
     */
    public void insert(BaseDBBean bean) {
        try {
            dao.create(bean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除user表中的一条数据
     *
     * @param baseDBBean the entry bean
     */
    public void delete(BaseDBBean baseDBBean) {
        try {
            dao.delete(baseDBBean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改user表中的一条数据
     *
     * @param baseDBBean the entry bean
     */
    public void update(BaseDBBean baseDBBean) {
        try {
            dao.update(baseDBBean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询表中的所有数据
     *
     * @return the list
     */
    public List selectAll() {
        try {
            return dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Select data list.
     *
     * @param name the name
     * @return the list
     */
    public List selectData(String name) {
        try {
            dataMap = dao.queryBuilder().where().eq("station_name", name).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dataMap;
    }

    /**
     * Select code data list.
     *
     * @param name the name
     * @return the list
     */
    public List selectCodeData(String name) {
        try {
            dataMap = dao.queryBuilder().where().eq("station_code", name).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataMap;
    }

    /**
     * 根据ID取出用户信息
     *
     * @param id the id
     * @return the station code
     */
    public BaseDBBean queryById(int id) {
        BaseDBBean user = null;
        try {
            user = (BaseDBBean) dao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    /**
     * Recycler.
     */
    public void recycler() {
        dbHelper.close();
        dbHelper = null;
    }
}
