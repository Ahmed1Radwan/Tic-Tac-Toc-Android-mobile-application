package com.ahmedhamdy.tic_tac_toe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AI {

    private static AI ai = new AI();
    public boolean easy = false;
    public boolean medium = false;
    public boolean hard = false;
    public boolean user = false;
    Random random = new Random();
    private AI(){}

    public static AI getInstance(){
        return ai;
    }

    public byte[] easyMove(byte[][] table){

        byte choice;
        do{
            choice = (byte)random.nextInt(9);
        }while (table[choice/3][choice%3] != 0);

        return new byte[]{(byte) (choice/3), (byte) (choice%3)};
    }

    public byte[] mediumMove(byte[][] table){
        byte choice;
        byte[] coordinate = new byte[2];
        boolean choiceMade = false;

        // first check if there is a winning move for me(AI)
        byte[] bestMove = seekWin(table,(byte)10);
        if(bestMove[0] != -1){
            choiceMade = true;
            coordinate = bestMove;
            return coordinate;
        }

        // check if there is a winning move for enemy (human) and prevent it


        bestMove = seekWin(table, (byte)1);
        if(bestMove[0] != -1){
            choiceMade = true;
            coordinate = bestMove;
            return coordinate;
        }


        // no winning move for any players generate random move

        do{
            choice = (byte)random.nextInt(9);
        }while(table[choice/3][choice%3] != 0);

        return new byte[]{(byte) (choice/3), (byte) (choice%3)};

    }
    private byte[] seekWin(byte[][] table, byte x){
        byte row = -1, col = -1;

        if((table[0][0] == x && table[2][2] == x && table[1][1] == 0) ||
                (table[2][0] == x && table[0][2] == x && table[1][1] == 0)){
            row = 1;
            col = 1;
        }else if(table[1][1] == x){
            if(table[0][0] == x && table[2][2] == 0){
                row = 2;
                col = 2;
            }else if(table[2][2] == x && table[0][0] == 0){
                row = 0;
                col = 0;
            }else if(table[0][2] == x && table[2][0] == 0){
                row = 2;
                col = 0;
            }else if(table[2][0] == x && table[0][2] == 0){
                row = 0;
                col = 2;
            }
        }

        for(int i = 0;i < 3 ; i++){
            if(table[i][0] == x && table[i][1] == x && table[i][2] == 0){
                row = (byte)i;
                col = 2;
            }else if(table[i][1] == x && table[i][2] == x && table[i][0] ==0){
                row = (byte) i;
                col = 0;
            }else if(table[i][0] == x && table[i][2] == x && table[i][1] == 0){
                row = (byte) i;
                col = 1;
            }else if(table[0][i] == x && table[1][i] == x && table[2][i] == 0){
                row = 2;
                col = (byte)i;
            }else if(table[0][i] == x && table[2][i] == x && table[1][i] == 0){
                row = 1;
                col = (byte) i;
            }else if(table[1][i] == x && table[2][i] == x && table[0][i] == 0){
                row = 0;
                col = (byte)i;
            }
        }
        return new byte[]{row,col};
    }

    public byte[] hardMove(byte[][] table){

        Move bestMove = minmax(table,2,2);
        return new byte[]{bestMove.index[0], bestMove.index[1]};
    }

    private Move minmax(byte[][] table, int callingPlayer, int currentPlayer){

        int enemySymbol = 0;
        int callingSymbol = 0;
        if(callingPlayer == 1){
            callingSymbol = 1;
            enemySymbol = 10;
        }else if(callingPlayer == 2){
            callingSymbol = 10;
            enemySymbol = 1;
        }
        int symbol = -1;
        int enemyNumber = 0;
        if(currentPlayer == 1){
            symbol = 1;
            enemyNumber = 2;
        }else if(currentPlayer == 2){
            symbol = 10;
            enemyNumber = 1;
        }

        boolean[][] availableSpots = emptyIndexed(table);

        if(winning(table,(byte) enemySymbol)){
            return new Move(-10);
        }else if(winning(table, (byte) callingSymbol)){
            return new Move(10);
        }else if(!areThereEmptyIndexes(table)){
            return new Move(0);
        }

        List<Move> moves = new ArrayList<>();

        for(byte i = 0; i < 3 ;i++){
            for(byte j = 0;j < 3; j++){
                if(availableSpots[i][j]){
                    Move move = new Move();
                    move.index = new byte[]{i,j};
                    table[i][j] = (byte)symbol;
                    Move result = minmax(table,callingPlayer,enemyNumber);
                    move.score = result.score;
                    table[i][j] = 0;
                    moves.add(move);
                }
            }
        }
        
        int bestMove = 0;
        if (currentPlayer == callingPlayer) {
            int bestScore = -10000;
            for (int i = 0; i < moves.size(); i++) {
                if (moves.get(i).score > bestScore) {
                    bestScore = moves.get(i).score;
                    bestMove = i;
                }
            }
        } else {
            int bestScore = 10000;
            for (int i = 0; i < moves.size(); i++) {
                if (moves.get(i).score < bestScore) {
                    bestScore = moves.get(i).score;
                    bestMove = i;
                }
            }
        }
        return moves.get(bestMove);
    }
    private boolean winning(byte[][] table, byte x){
        return (table[0][0] == x && table[0][1] == x && table[0][2] == x) ||
                (table[1][0] == x && table[1][1] == x && table[1][2] == x) ||
                (table[2][0] == x && table[2][1] == x && table[2][2] == x) ||
                (table[0][0] == x && table[1][0] == x && table[2][0] == x) ||
                (table[0][1] == x && table[1][1] == x && table[2][1] == x) ||
                (table[0][2] == x && table[1][2] == x && table[2][2] == x) ||
                (table[0][0] == x && table[1][1] == x && table[2][2] == x) ||
                (table[0][2] == x && table[1][1] == x && table[2][0] == x);

    }
    private boolean areThereEmptyIndexes(byte[][] table){
        boolean empty = false;
        for(byte i = 0; i < 3; i++){
            for(byte j = 0; j < 3; j++){
                if(table[i][j] == 0) {
                    empty = true;
                    break;
                }
            }
            if(empty)
                break;
        }
        return empty;
    }

    private boolean[][] emptyIndexed(byte[][] table){
        boolean[][] empty = new boolean[3][3];
        for(byte i = 0; i < 3; i++){
            for(byte j = 0; j < 3; j++){
                if(table[i][j] == 0)
                    empty[i][j] = true;
                else
                    empty[i][j] = false;
            }
        }
        return empty;
    }
    class Move{
        byte[] index;
        int score;
        Move(){}
        Move(int s){
            score = s;
        }
    }

}
