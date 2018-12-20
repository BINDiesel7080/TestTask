package com.example.diesel.testtask.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;


@Entity(tableName = "thing_table")
public class Thing {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "_id")
    public int uid;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "checked")
    public Boolean checked;

    public Thing(int uid, String title, boolean checked) {
        this.uid = uid;
        this.title = title;
        this.checked = checked;
    }

    public String getTitle() {
        return title;
    }

    public Boolean getChecked() {
        return checked;
    }

    public int getID() {
        return uid;
    }

}