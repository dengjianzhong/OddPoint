package com.zhong.utilslibrary.database.base;

import com.j256.ormlite.field.DatabaseField;

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
