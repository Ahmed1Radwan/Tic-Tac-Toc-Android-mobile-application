package com.ahmedhamdy.tic_tac_toe;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Semaphore;

import static com.ahmedhamdy.tic_tac_toe.R.drawable.ic_o;


public class BoardActivity extends AppCompatActivity {
    TextView tvGameNumber;
    TextView tvScore;
    List<ImageView> imageViewList;
    ImageView soundBoard;

    EditText player1ET;
    EditText player2ET;

    MediaPlayer mediaPlayer;
    SharedPreferences sharedPreference;
    Players players = Players.getInstance();;

    DashboardObject p;

    AI ai = AI.getInstance();
    Semaphore semaA;
    Semaphore semaB;
    int player1_wins;
    int player2_wins;
    int numberOfGames;
    byte[][] table;
    boolean player1; // X
    boolean player2; // O
    boolean gameFinish; // anyone wins
    boolean stalemateGame;

    boolean soundOn ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        tvGameNumber = findViewById(R.id.tvGameNumber);
        tvScore = findViewById(R.id.tvScore);
        soundBoard = findViewById(R.id.soundBoard);
        mediaPlayer = MediaPlayer.create(this,R.raw.wining_sound);
        player1ET = findViewById(R.id.player1ET);
        player2ET = findViewById(R.id.player2ET);

        if(!players.getPlayer1_name().isEmpty())
            player1ET.setText(players.getPlayer1_name());
        else
            player1ET.setText("Player X");
        if(!players.getPlayer2_name().isEmpty())
            player2ET.setText(players.getPlayer2_name());
        else
            player2ET.setText("Player O");

        sharedPreference = getPreferences(MODE_PRIVATE);

        player1 = true;
        player2 = false;
        player1_wins = 0;
        player2_wins = 0;
        gameFinish = false;
        numberOfGames = 1;
        stalemateGame = false;
        table = new byte[3][3];
        imageViewList = new ArrayList<>();


        if(sharedPreference.contains("soundStatus"))
            soundOn = sharedPreference.getBoolean("soundStatus", true);
        else
            soundOn = true;

        if(!soundOn)
            soundBoard.setImageResource(R.drawable.ic_soundoff);

        semaA = new Semaphore(1);
        semaB = new Semaphore(1);
    }

    @Override
    public void onBackPressed() {

        if(!players.getPlayer1_name().isEmpty()) {
            List<DashboardObject> x = DashboardDatabase.getInstance(this).dashboardDAO().checkIfNameFound(players.getPlayer1_name());
            if(x.size() != 0){
                x.get(0).PlayedGames += numberOfGames;
                x.get(0).Score += player1_wins;
                if(DashboardDatabase.getInstance(this).dashboardDAO().update(x.get(0)) > 0)
                    Toast.makeText(this, "recored Upadted", Toast.LENGTH_SHORT).show();
            }else{
                p = new DashboardObject();
                p.Name = players.getPlayer1_name();
                p.Score = player1_wins;
                p.PlayedGames = numberOfGames;
                DashboardDatabase.getInstance(this).dashboardDAO().insert(p);
            }
        }

        if(!players.getPlayer2_name().isEmpty()) {
            List<DashboardObject> x = DashboardDatabase.getInstance(this).dashboardDAO().checkIfNameFound(players.getPlayer2_name());
            if(x.size() != 0){
                x.get(0).PlayedGames += numberOfGames;
                x.get(0).Score += player2_wins;
                if(DashboardDatabase.getInstance(this).dashboardDAO().update(x.get(0)) > 0)
                    Toast.makeText(this, "recored Upadted", Toast.LENGTH_SHORT).show();
            }else{
                p = new DashboardObject();
                p.Name = players.getPlayer2_name();
                p.Score = player2_wins;
                p.PlayedGames = numberOfGames;
                DashboardDatabase.getInstance(this).dashboardDAO().insert(p);
            }
        }


        SharedPreferences.Editor editor = sharedPreference.edit();
        editor.putBoolean("soundStatus", soundOn);
        editor.apply();
        super.onBackPressed();
    }

    public void reloadButton(View view) {
        System.out.println("reload button pressed");
        if(gameFinish || stalemateGame) {
            numberOfGames++;
            tvGameNumber.setText("Game #" + numberOfGames + "");
        }
        resetGame();

    }
    private void resetGame(){
        for (byte i = 0; i < 3; i++) {
            Arrays.fill(table[i], (byte) 0);
        }
        for(ImageView viewPostion : imageViewList){
            viewPostion.setImageResource(R.drawable.ic_untitled);
        }
        imageViewList.clear();
        player1 = true;
        player2 = false;
        gameFinish = false;
    }

    public void playerClickPosition(View view) {

        if(!gameFinish && ai.user){
            ImageView position = (ImageView) view;
            String tag = position.getTag().toString();
            byte row = (byte)Character.getNumericValue(tag.charAt(0));
            byte col = (byte)Character.getNumericValue(tag.charAt(2));

            if(table[row][col] == 0){
                if (player1) {
                    position.setImageResource(R.drawable.ic_x);
                    table[row][col] = 1;
                    player1 = false;
                    player2 = true;
                } else if (player2) {
                    position.setImageResource(ic_o);
                    table[row][col] = 10;
                    player2 = false;
                    player1 = true;
                }
                imageViewList.add(position);
            }

            if(checkWinning()){
                gameFinish = true;
                // mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.wining_sound);
                //mediaPlayer.start();
                if(soundOn){
                    mediaPlayer.start();
                }
                return;
            }
            if(!gameFinish && stalemate()){
                gameFinish = true;
                Toast.makeText(this, "! It's a stalemate game", Toast.LENGTH_SHORT).show();
                return;
            }

        }else if(!gameFinish && ai.easy){
            ImageView position = (ImageView) view;
            String tag = position.getTag().toString();
            byte row = (byte) Character.getNumericValue(tag.charAt(0));
            byte col = (byte) Character.getNumericValue(tag.charAt(2));

            if(table[row][col] == 0) {
                position.setImageResource(R.drawable.ic_x);
                table[row][col] = 1;
                imageViewList.add(position);
                if (checkWinning()) {
                    gameFinish = true;
                    if (soundOn) {
                        mediaPlayer.start();
                    }
                    return;
                }
                if (!gameFinish && stalemate()) {
                    gameFinish = true;
                    Toast.makeText(this, "! It's a stalemate game", Toast.LENGTH_SHORT).show();
                    return;
                }


                byte[] pos = ai.easyMove(table);
                table[pos[0]][pos[1]] = 10;
                String tagName = pos[0] + "_";
                tagName += pos[1];
                System.out.println("----------------------");
                System.out.println(tagName);
                ImageView temp = (ImageView) findViewById(R.id.tableLayout).findViewWithTag(tagName);

                temp.setImageResource(R.drawable.ic_o);
                imageViewList.add(temp);

                if (checkWinning()) {
                    gameFinish = true;
                    if (soundOn) {
                        mediaPlayer.start();
                    }
                    return;
                }
                if (!gameFinish && stalemate()) {
                    gameFinish = true;
                    Toast.makeText(this, "! It's a stalemate game", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }else if(!gameFinish && ai.medium){
            ImageView position = (ImageView) view;
            String tag = position.getTag().toString();
            byte row = (byte)Character.getNumericValue(tag.charAt(0));
            byte col = (byte)Character.getNumericValue(tag.charAt(2));
            if(table[row][col] == 0) {
                position.setImageResource(R.drawable.ic_x);
                table[row][col] = 1;
                imageViewList.add(position);
                if (checkWinning()) {
                    gameFinish = true;
                    if (soundOn) {
                        mediaPlayer.start();
                    }
                    return;
                }
                if (!gameFinish && stalemate()) {
                    gameFinish = true;
                    Toast.makeText(this, "! It's a stalemate game", Toast.LENGTH_SHORT).show();
                    return;
                }
                byte[] pos = ai.mediumMove(table);
                table[pos[0]][pos[1]] = 10;
                String tagName = pos[0] + "_";
                tagName += pos[1];
                System.out.println("----------------------");
                System.out.println(tagName);
                ImageView temp = (ImageView) findViewById(R.id.tableLayout).findViewWithTag(tagName);

                temp.setImageResource(R.drawable.ic_o);
                imageViewList.add(temp);

                if (checkWinning()) {
                    gameFinish = true;
                    if (soundOn) {
                        mediaPlayer.start();
                    }
                    return;
                }
                if (!gameFinish && stalemate()) {
                    gameFinish = true;
                    Toast.makeText(this, "! It's a stalemate game", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }else if(!gameFinish && ai.hard){
            ImageView position = (ImageView) view;
            String tag = position.getTag().toString();
            byte row = (byte)Character.getNumericValue(tag.charAt(0));
            byte col = (byte)Character.getNumericValue(tag.charAt(2));
            if(table[row][col] == 0) {
                position.setImageResource(R.drawable.ic_x);
                table[row][col] = 1;
                imageViewList.add(position);
                if (checkWinning()) {
                    gameFinish = true;
                    if (soundOn) {
                        mediaPlayer.start();
                    }
                    return;
                }
                if (!gameFinish && stalemate()) {
                    gameFinish = true;
                    Toast.makeText(this, "! It's a stalemate game", Toast.LENGTH_SHORT).show();
                    return;
                }
                byte[] pos = ai.hardMove(table);
                table[pos[0]][pos[1]] = 10;
                String tagName = pos[0] + "_";
                tagName += pos[1];
                System.out.println("----------------------");
                System.out.println(tagName);
                ImageView temp = (ImageView) findViewById(R.id.tableLayout).findViewWithTag(tagName);

                temp.setImageResource(R.drawable.ic_o);
                imageViewList.add(temp);

                if (checkWinning()) {
                    gameFinish = true;
                    if (soundOn) {
                        mediaPlayer.start();
                    }
                    return;
                }
                if (!gameFinish && stalemate()) {
                    gameFinish = true;
                    Toast.makeText(this, "! It's a stalemate game", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }




    }

    private boolean stalemate() {

        for(byte i = 0; i < 3;i++)
            for(byte j = 0; j < 3;j++)
                if(table[i][j] == 0) return false;

        return true;
    }

    private boolean checkWinning(){
        byte sum1=0,sum2=0,sum3=0;
        for(byte i = 0;i < 3;i++){
            sum1 += table[0][i];
            sum2 += table[1][i];
            sum3 += table[2][i];
        }

        if(sum1 == 3 || sum2 == 3 || sum3 == 3){
            //Toast.makeText(this, "Player 1 Win", Toast.LENGTH_SHORT).show();
            player1_wins++;
            tvScore.setText(player1_wins + "   |   " + player2_wins+"");
            return true;
        }else if(sum1 == 30 || sum2 == 30 || sum3 == 30){
            //Toast.makeText(this, "Player 2 Win", Toast.LENGTH_SHORT).show();
            player2_wins++;
            tvScore.setText(player1_wins + "   |   " + player2_wins+"");
            return true;
        }
        sum1=0;sum2=0;sum3=0;
        for (byte i = 0; i < 3; i++) {
            sum1 += table[i][0];
            sum2 += table[i][1];
            sum3 += table[i][2];
        }
        if(sum1 == 3 || sum2 == 3 || sum3 == 3){
            //Toast.makeText(this, "Player 1 Win", Toast.LENGTH_SHORT).show();
            player1_wins++;
            tvScore.setText(player1_wins + "   |   " + player2_wins+"");
            return true;
        }else if(sum1 == 30 || sum2 == 30 || sum3 == 30){
            //Toast.makeText(this, "Player 2 Win", Toast.LENGTH_SHORT).show();
            player2_wins++;
            tvScore.setText(player1_wins + "   |   " + player2_wins+"");
            return true;
        }

        sum1 = (byte) (table[0][0] + table[1][1] + table[2][2]);
        sum2 = (byte) (table[0][2] + table[1][1] + table[2][0]);
        if(sum1 == 3 || sum2 == 3 ){
            //Toast.makeText(this, "Player 1 Win", Toast.LENGTH_SHORT).show();
            player1_wins++;
            tvScore.setText(player1_wins + "   |   " + player2_wins+"");
            return true;
        }else if(sum1 == 30 || sum2 == 30){
            //Toast.makeText(this, "Player 2 Win", Toast.LENGTH_SHORT).show();
            player2_wins++;
            tvScore.setText(player1_wins + "   |   " + player2_wins+"");
            return true;
        }
        return false;
    }

    public void soundPressed(View view) {
        if(soundOn) {
            soundBoard.setImageResource(R.drawable.ic_soundoff);
            //mediaPlayer.stop();
        }else {
            soundBoard.setImageResource(R.drawable.ic_soundon);
            //mediaPlayer.start();
        }
        soundOn = !soundOn;
    }
}