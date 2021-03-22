package com.ahmedhamdy.tic_tac_toe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class PlayersNamesActivity extends AppCompatActivity {

    EditText player1ET;
    EditText player2Et;
    Players players;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players_names);
        player1ET = findViewById(R.id.p1NameET);
        player2Et = findViewById(R.id.p2NameET);
        players = Players.getInstance();
    }

    public void GameButton(View view) {

        players.setPlayer1_name(player1ET.getText().toString());
        players.setPlayer2_name(player2Et.getText().toString());
        players.setPlayer1_score(0);
        players.setPlayer2_score(0);
        players.setGameNumber(0);
        YoYo.with(Techniques.StandUp).duration(500).playOn(view);
        Intent in = new Intent(this, BoardActivity.class);
        startActivity(in);


    }
}