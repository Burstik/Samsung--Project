package com.andreykachanov.gamemaze;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.Queue;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    public static String GAME_STATUS = "NoGame";
    public static String TIP_GAME = "GameTipAge";
    public static int SIZE_SELL = 0;
    public static int WIDTH = 10;
    public static int HEIGHT = 10;
    public static int[][] game_board = new int [WIDTH][HEIGHT];

    private View gameButton;
    private View recordsButton;
    private View settingButton;
    private View rulesButton;
    private View exitButton;

    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        gameButton = findViewById(R.id.gameButton);
        gameButton.setOnClickListener(this);
        recordsButton = findViewById(R.id.recordsButton);
        recordsButton.setOnClickListener(this);
        settingButton = findViewById(R.id.settingButton);
        settingButton.setOnClickListener(this);
        rulesButton = findViewById(R.id.rulesButton);
        rulesButton.setOnClickListener(this);
        exitButton = findViewById(R.id.exitButton);
        exitButton.setOnClickListener(this);
        dialog = new Dialog(MainActivity.this);
        dialog.setTitle("Таблица рекордов");
        dialog.setContentView(R.layout.dialog);
        TextView text = (TextView) dialog.findViewById(R.id.dialogTextView);
        text.setText("\n В разработке \n");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gameButton:
                startActivity(new Intent(MainActivity.this, GameActivity.class));
                break;
            case R.id.recordsButton:
                dialog.show();
                break;
            case R.id.settingButton:
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
                break;
            case R.id.rulesButton:
                startActivity(new Intent(MainActivity.this, RulesActivity.class));
                break;
            case R.id.exitButton:
                finish();
                break;
        }

    }
}
