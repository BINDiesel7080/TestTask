package com.example.diesel.testtask.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface ThingDAO {
    @Query("SELECT * FROM thing_table")
    LiveData<List<Thing>> getAll();

    @Query("SELECT * FROM thing_table WHERE _id IN (:nameIds)")
    List<Thing> getByIds(int[] nameIds);

    @Insert()
    void insert(Thing thing);

    @Update
    void update(Thing thing);

    @Insert
    void insertAll(Thing... things);

    @Delete
    void delete(Thing thing);

    @Query("DELETE FROM thing_table")
    void deleteAll();
}
