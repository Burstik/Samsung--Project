package com.andreykachanov.gamemaze;

import android.app.Application;

public class Global extends Application{

    public String playerName = "NoName";

    public String getPlayerName() {
        return this.playerName;
    }
    public void setPlayerName(String PlayerName) {
        this.playerName = PlayerName;
    }

    private String gameStatus = "NoGame";

    public String getGameStatus() {
        return this.gameStatus;
    }
    public void setGameStatus(String GameStatus) {
        this.gameStatus = GameStatus;
    }

    private String gameTip = "GameTipAge";

    public String getGameTip() {
        return this.gameTip;
    }
    public void setGameTip(String GameTip) {
        this.gameTip = GameTip;
    }

    private int boardWidth = 10;

    public int getBoardWidth() {
        return this.boardWidth;
    }
    public void setBoardWidth(int BoardWidth) {
        this.boardWidth = BoardWidth;
    }

    private int boardHeight = 10;

    public int getBoardHeight() {
        return this.boardHeight;
    }
    public void setBoardHeight(int BoardHeight) {
        this.boardHeight = BoardHeight;
    }

    private int[][] gameBoard;

    public int[][] getGameBoard() {
        return this.gameBoard;
    }
    public void setGameBoard(int GameBoard[][]) {
        this.gameBoard = GameBoard;
    }

}

