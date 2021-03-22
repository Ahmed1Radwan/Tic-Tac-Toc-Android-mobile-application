package com.ahmedhamdy.tic_tac_toe;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Dashboard")
public class DashboardObject {
    @PrimaryKey(autoGenerate = true)
    public int Id;

    public String Name;
    public int Score;
    public int PlayedGames;

}
