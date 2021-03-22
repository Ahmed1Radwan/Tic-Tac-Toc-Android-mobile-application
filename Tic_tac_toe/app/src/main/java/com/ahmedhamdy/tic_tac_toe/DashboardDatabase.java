package com.ahmedhamdy.tic_tac_toe;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {DashboardObject.class}, version = 1)
public abstract class DashboardDatabase extends RoomDatabase {

    private static DashboardDatabase ourInstance;
    public abstract DashboardDAO dashboardDAO();

    public static DashboardDatabase getInstance(Context context){
        if(ourInstance == null){
            ourInstance = Room.databaseBuilder(context,DashboardDatabase.class,"Dashboard.db")
                    .createFromAsset("databases/Dashboard.db")
                    .allowMainThreadQueries()
                    .build();
        }
        return ourInstance;
    }
}
