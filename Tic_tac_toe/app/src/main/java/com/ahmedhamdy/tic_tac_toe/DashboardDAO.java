package com.ahmedhamdy.tic_tac_toe;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DashboardDAO {

    @Insert
    long insert(DashboardObject p);

    @Query("SELECT * FROM Dashboard")
    List<DashboardObject> getAllRows();

    @Query("SELECT Name FROM Dashboard")
    List<String> getNames();

    @Query("SELECT * FROM Dashboard WHERE Name=:name limit 1")
    List<DashboardObject> checkIfNameFound(String name);

    @Delete
    int delete(DashboardObject x);

    @Update
    int update(DashboardObject x);

    @Query("DELETE FROM Dashboard")
    void deleteAll();

}
