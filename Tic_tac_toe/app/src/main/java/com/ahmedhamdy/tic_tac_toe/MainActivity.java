package com.ahmedhamdy.tic_tac_toe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void startButton(View view) {
        YoYo.with(Techniques.StandUp).duration(500).playOn(view);
        Intent in = new Intent(this,ModesActivity.class);
        startActivity(in);

    }

    public void dashboardButton(View view) {
        YoYo.with(Techniques.StandUp).duration(500).playOn(view);
        Intent in = new Intent(this, DashboardActivity.class);
        startActivity(in);
    }
}