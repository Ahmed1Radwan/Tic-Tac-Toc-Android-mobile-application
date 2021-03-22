package com.ahmedhamdy.tic_tac_toe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class ModesActivity extends AppCompatActivity {

    AI ai = AI.getInstance();
    Players players = Players.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modes);

    }

    public void usersPlayers(View view) {
        ai.user= true;
        ai.hard = false;
        ai.easy = false;
        ai.medium = false;
        YoYo.with(Techniques.ZoomIn).duration(100).playOn(view);
        Intent in = new Intent(this, PlayersNamesActivity.class);
        startActivity(in);
    }

    public void easy(View view) {
        ai.easy = true;
        ai.user= false;
        ai.hard = false;
        ai.medium = false;
        players.setPlayer1_name("");
        players.setPlayer2_name("");
        YoYo.with(Techniques.ZoomIn).duration(100).playOn(view);
        Intent in = new Intent(this, BoardActivity.class);
        startActivity(in);
    }

    public void medium(View view) {
        ai.medium = true;
        ai.easy = false;
        ai.user= false;
        ai.hard = false;
        players.setPlayer1_name("");
        players.setPlayer2_name("");
        YoYo.with(Techniques.ZoomIn).duration(100).playOn(view);
        Intent in = new Intent(this, BoardActivity.class);
        startActivity(in);
    }

    public void hard(View view) {
        ai.hard = true;
        ai.user= false;
        ai.easy = false;
        ai.medium = false;
        players.setPlayer1_name("");
        players.setPlayer2_name("");
        YoYo.with(Techniques.ZoomIn).duration(100).playOn(view);
        Intent in = new Intent(this, BoardActivity.class);
        startActivity(in);
    }
}