package com.andreykachanov.gamemaze;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {


    private View settingSetButton;
    private EditText playerNameText;
    private EditText boardWidthText;
    private EditText boardHeightText;
    private RadioButton gameTipText;

    public static final int boardHeightText1111 = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        final Global appState = ((Global)getApplicationContext());

        String playerName = appState.getPlayerName();
        playerNameText = findViewById(R.id.playerName);
        playerNameText.setText(playerName);

        int boardWidth = appState.getBoardWidth();
        boardWidthText = findViewById(R.id.boardWidth);
        boardWidthText.setText(String.valueOf(boardWidth));

        int boardHeight = appState.getBoardHeight();
        boardHeightText = findViewById(R.id.boardHeight);
        boardHeightText.setText(String.valueOf(boardHeight));
    }

    @Override
    public void onClick(View v) {

    }

    public void onClickSettingButton(View view) {

        final Global appState = ((Global)getApplicationContext());

        appState.setPlayerName(playerNameText.getText().toString());

        String s1 = boardWidthText.getText().toString();
        appState.setBoardWidth(Integer.parseInt(s1));

        String s2 = boardHeightText.getText().toString();
        appState.setBoardHeight(Integer.parseInt(s2));

        finish();
    }

}
