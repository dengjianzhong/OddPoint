package com.zhong.toollib.database;

import com.j256.ormlite.field.DatabaseField;

/**
 * 用于数据库表的基类Bean
 */
public abstract class BaseBean {
    @DatabaseField(generatedId = true)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
