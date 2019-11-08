package com.zhong.utilslibrary.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.zhong.utilslibrary.database.StationCode;
import com.zhong.utilslibrary.database.StationTrainNo;

import java.sql.SQLException;


public class SqlLiteDBHelper extends OrmLiteSqliteOpenHelper {
    private static final int DB_VERSION = 11;
    private static final String databaseName = "ormlite_db";
    private static SqlLiteDBHelper dbHelper;
    private Dao dao;

    private SqlLiteDBHelper(Context context) {
        super(context, databaseName, null, DB_VERSION);
    }

    /**
     * Gets manager.
     *
     * @param context the context
     * @return the manager
     */
    public static SqlLiteDBHelper getManager(Context context) {
        if (dbHelper == null) {
            synchronized (SqlLiteDBHelper.class) {
                if (dbHelper == null) {
                    dbHelper = new SqlLiteDBHelper(context);
                }
            }
        }
        return dbHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, StationTrainNo.class);
            TableUtils.createTableIfNotExists(connectionSource, StationCode.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {
        onCreate(sqLiteDatabase, connectionSource);
    }

    public synchronized Dao getDao(Class tableClass) throws SQLException {
        dao = super.getDao(tableClass);
        return dao;
    }

    @Override
    public void close() {
        super.close();
        dao = null;
    }
}
