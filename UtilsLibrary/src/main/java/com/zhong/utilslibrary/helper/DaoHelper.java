package com.zhong.utilslibrary.helper;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.zhong.utilslibrary.database.BaseBean;

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
    public DaoHelper(Context context, Class<? extends BaseBean> tableBean) {
        try {
            dbHelper = SqlLiteDBHelper.getManager(context);
            dao = dbHelper.getDao(tableBean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 向user表中添加一条数据.
     *
     * @param bean the bean
     */
    public void insert(BaseBean bean) {
        try {
            dao.create(bean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除user表中的一条数据
     *
     * @param baseBean the entry bean
     */
    public void delete(BaseBean baseBean) {
        try {
            dao.delete(baseBean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改user表中的一条数据
     *
     * @param baseBean the entry bean
     */
    public void update(BaseBean baseBean) {
        try {
            dao.update(baseBean);
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
    public BaseBean queryById(int id) {
        BaseBean user = null;
        try {
            user = (BaseBean) dao.queryForId(id);
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
