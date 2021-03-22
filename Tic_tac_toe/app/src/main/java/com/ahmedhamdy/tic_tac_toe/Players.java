package com.ahmedhamdy.tic_tac_toe;

public class Players {

    private static Players players = new Players();
    private String player1_name ="";
    private String player2_name ="";
    private int player1_score;
    private int player2_score;
    private int gameNumber;


    private Players(){}

    public static Players getInstance(){
        return players;
    }

    public int getPlayer1_score() {
        return player1_score;
    }

    public void setPlayer1_score(int player1_score) {
        this.player1_score = player1_score;
    }

    public int getPlayer2_score() {
        return player2_score;
    }

    public void setPlayer2_score(int player2_score) {
        this.player2_score = player2_score;
    }

    public int getGameNumber() {
        return gameNumber;
    }

    public void setGameNumber(int gameNumber) {
        this.gameNumber = gameNumber;
    }


    public void setPlayer1_name(String name){
        player1_name = name;
    }
    public void setPlayer2_name(String name){
        player2_name = name;
    }

    public String getPlayer1_name(){
        return player1_name;
    }
    public String getPlayer2_name(){
        return player2_name;
    }
}
