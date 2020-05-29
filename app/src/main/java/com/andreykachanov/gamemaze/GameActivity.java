package com.andreykachanov.gamemaze;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class GameActivity extends AppCompatActivity implements View.OnClickListener,
        View.OnLongClickListener {

    public static int scores = 0;
    public static int SizeSell = 0;

    private static int width = 10;
    private static int height = 10;
    public int[][] gameBoard = new int [width][height];


    public static boolean isRestore = false;
    public static boolean isGameEnd = true;
    public static boolean isGameEdit = true;
    public static long speed = 700;
    public static int[] position = {0,0,0,0,1,1,1};

    private ImageView[][] make;

    TextView textScore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        textScore = (TextView) findViewById(R.id.textScore);
        makeBoard();
        generateBoard();
    }

    @Override
    public boolean onLongClick(View v) {
        if (isGameEdit) {
            ImageView tappedCell = (ImageView) v;
            int X = getX(tappedCell);
            int Y = getY(tappedCell);

            if (gameBoard[X][Y] != 5) {
                setObject(X, position[0], Y, position[2]);
            }
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        if ((v instanceof ImageView) && (isGameEdit)) {
            ImageView tappedCell = (ImageView) v;
            int X = getX(tappedCell);
            int Y = getY(tappedCell);
            int cellStatus = gameBoard[X][Y];
            if ((cellStatus != 3) && (cellStatus != 5)) {
                if ((cellStatus == 0)) {
                    gameBoard[X][Y] = 1;
                    if(checkOut(gameBoard,height,width))
                    {
                        int iNumber = new Random().nextInt(100);
                        if (iNumber < 100)  make[X][Y].setImageResource(R.drawable.wall_1);
                        if (iNumber < 67)   make[X][Y].setImageResource(R.drawable.wall_2);
                        if (iNumber < 33)   make[X][Y].setImageResource(R.drawable.wall_3);
                    }
                    else {
                        gameBoard[X][Y] = 0;
                        make[X][Y].setImageResource(R.drawable.ground);
                    }
                }
                else {
                    gameBoard[X][Y] = 0;
                    make[X][Y].setImageResource(R.drawable.ground);
                }
            }
        }
    }
    private Handler mHandler = new Handler();
    private Runnable gameRunnable = new Runnable() {
        public void run() {
            calcStep();
            emulationStep();

            if (!isGameEnd) {
                mHandler.postDelayed(this, speed);
            }
        }
    };

    @Override
    protected void onPause() {
        mHandler.removeCallbacks(gameRunnable);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.postDelayed(gameRunnable, speed);
    }

    public void clickButtonGo(View v) {
        if (isGameEnd) {
            if (getPosition()) {
                Button button = (Button) v;
                scores = 0;
                textScore.setText(String.valueOf(scores));
                isGameEdit = false;
                mHandler.postDelayed(gameRunnable, speed);
                isGameEnd = false;
            }
        }
    }

    public void clickButtonStop(View v) {
        isGameEnd = true;
        isGameEdit = true;
        scores = 0;
        mHandler.removeCallbacks(gameRunnable);
        isRestore = false;
        generateBoard();
    }

    public void clickButtonDown(View view) {
        speed = speed + 100;
    }

    public void clickButtonUp(View view) {
        speed = speed - 100;
        if (speed < 50) {
            speed = 50;
        }
    }

    public void clickAbout(View view) {
        Intent intent = new Intent(GameActivity.this, RulesActivity.class);
        startActivity(intent);
    }


    int getX(View view) {
        return Integer.parseInt(((String) view.getTag()).split(",")[0]);
    }

    int getY(View view) {
        return Integer.parseInt(((String) view.getTag()).split(",")[1]);
    }

    private void makeBoard() {
        make = new ImageView[height][width];
        GridLayout cellsLayout = (GridLayout) findViewById(R.id.CellsLayout);
        cellsLayout.removeAllViews();
        cellsLayout.setColumnCount(height);

        for (int Y = 0; Y < height; Y++)
            for (int X = 0; X < width; X++) {
                LayoutInflater inflater = (LayoutInflater) getApplicationContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                switch (SizeSell) {
                    case 1:
                        make[X][Y] = (ImageView) inflater.inflate(R.layout.cell_image1, cellsLayout, false);
                        break;
                    case 2:
                        make[X][Y] = (ImageView) inflater.inflate(R.layout.cell_image2, cellsLayout, false);
                        break;
                    default:
                        make[X][Y] = (ImageView) inflater.inflate(R.layout.cell_image, cellsLayout, false);
                        break;
                }

                make[X][Y].setOnClickListener(this);
                make[X][Y].setOnLongClickListener(this);
                make[X][Y].setTag(X + "," + Y);
                cellsLayout.addView(make[X][Y]);
            }
    }

    private void generateBoard() {

        if (!isRestore) {
            for (int X = 0; X < width; X++)
                for (int Y = 0; Y < height; Y++) {
                    make[X][Y].setImageResource(R.drawable.ground);
                    gameBoard[X][Y] = 0;
                }
            setObject(0,0,0,0);
            isRestore = true;
        }
        else {
            for (int X = 0; X < width; X++)
                for (int Y = 0; Y < height; Y++) {
                    if (gameBoard[X][Y] == 0) {
                        make[X][Y].setImageResource(R.drawable.ground);
                    }
                    else if (gameBoard[X][Y] == 1) {
                        int iNumber = new Random().nextInt(100);
                        if (iNumber < 100)  make[X][Y].setImageResource(R.drawable.wall_1);
                        if (iNumber < 67)   make[X][Y].setImageResource(R.drawable.wall_2);
                        if (iNumber < 33)   make[X][Y].setImageResource(R.drawable.wall_3);
                    }
                    else if (gameBoard[X][Y] == 3) {
                        int Z = position[4];
                        switch (Z) {
                            case 1:
                                make[X][Y].setImageResource(R.drawable.turtle_down_1);
                                break;
                            case 2:
                                make[X][Y].setImageResource(R.drawable.turtle_left_1);
                                break;
                            case 3:
                                make[X][Y].setImageResource(R.drawable.turtle_up_1);
                                break;
                            case 4:
                                make[X][Y].setImageResource(R.drawable.turtle_right_1);
                                break;
                        }
                    }
                }
            make[width - 1][height - 1].setImageResource(R.drawable.house);
        }
    }

    private boolean checkOut(int map[][], int h, int w){
          int gameBoard[][] = new int [h][w];

        for (int Y = 0; Y < h; Y++)
            for (int X = 0; X < w; X++) {
                gameBoard[Y][X]=map[X][Y];
            }
        int height = h;
         int width = w;

        boolean [][] UsedStart = new boolean [height][width];
        boolean [][] UsedEnd = new boolean [height][width];
        Queue<Integer> QStart = new LinkedList<>();
        Queue<Integer> QEnd = new LinkedList<>();
        QStart.offer(0);
        QStart.offer(0);
        QEnd.offer(height - 1);
        QEnd.offer(width - 1);
        int xs, ys, xe, ye;
        while(!QStart.isEmpty() && !QEnd.isEmpty()) {
            ys = QStart.poll();
            xs = QStart.poll();
            UsedStart[ys][xs] = true;
            ye = QEnd.poll();
            xe = QEnd.poll();
            UsedEnd[ye][xe] = true;
            if (xs > 0 && UsedStart[ys][xs - 1] == false && gameBoard[ys][xs - 1] != 1) {
                if (UsedEnd[ys][xs - 1] == true)
                    return true;
                QStart.offer(ys);
                QStart.offer(xs - 1);
            }
            if (xs + 1 < width && UsedStart[ys][xs + 1] == false && gameBoard[ys][xs + 1] != 1) {
                if (UsedEnd[ys][xs + 1] == true)
                    return true;
                QStart.offer(ys);
                QStart.offer(xs + 1);
            }
            if (ys > 0 && UsedStart[ys - 1][xs] == false && gameBoard[ys - 1][xs] != 1) {
                if (UsedEnd[ys - 1][xs] == true)
                    return true;
                QStart.offer(ys - 1);
                QStart.offer(xs);
            }
            if (ys + 1 < height && UsedStart[ys + 1][xs] == false && gameBoard[ys + 1][xs] != 1) {
                if (UsedEnd[ys + 1][xs] == true)
                    return true;
                QStart.offer(ys + 1);
                QStart.offer(xs);
            }
            if (xe > 0 && UsedEnd[ye][xe - 1] == false && gameBoard[ye][xe - 1] != 1) {
                if (UsedStart[ye][xe - 1] == true)
                    return true;
                QEnd.offer(ye);
                QEnd.offer(xe - 1);
            }
            if (xe + 1 < width && UsedEnd[ye][xe + 1] == false && gameBoard[ye][xe + 1] != 1) {
                if (UsedStart[ye][xe + 1] == true)
                    return true;
                QEnd.offer(ye);
                QEnd.offer(xe + 1);
            }
            if (ye > 0 && UsedEnd[ye - 1][xe] == false && gameBoard[ye - 1][xe] != 1) {
                if (UsedStart[ye - 1][xe] == true)
                    return true;
                QEnd.offer(ye - 1);
                QEnd.offer(xe);
            }
            if (ye + 1 < height && UsedEnd[ye + 1][xe] == false && gameBoard[ye + 1][xe] != 1) {
                if (UsedStart[ye + 1][xe] == true)
                    return true;
                QEnd.offer(ye + 1);
                QEnd.offer(xe);
            }
        }
        String sss = "Выход нельзя закрывать полностью";
        outMessage(sss);
        return false;

    }

    private void setObject(int X, int X0, int Y, int Y0) {
        make[X][Y].setImageResource(R.drawable.turtle_down_1);
        gameBoard[X][Y] = 3;
        position[0] = X;
        position[1] = X;
        position[2] = Y;
        position[3] = Y;
        position[4] = 1;
        position[5] = 1;
        position[6] = 1;
        make[X][Y].setImageResource(R.drawable.turtle_down_1);
        gameBoard[X][Y] = 3;
        if (!((X == X0) && (Y == Y0))) {
            make[X0][Y0].setImageResource(R.drawable.ground);
            gameBoard[X0][Y0] = 0; }
        make[width - 1][height - 1].setImageResource(R.drawable.house);
        gameBoard[width - 1][height - 1] = 5;
    }

    private boolean getPosition() {
        for (int X = 0; X < width; X++) {
            for (int Y = 0; Y < height; Y++) {
                if (gameBoard[X][Y] == 3) {
                    position[0] = X;
                    position[1] = X;
                    position[2] = Y;
                    position[3] = Y;
                    position[4] = 1;
                    position[5] = 1;
                    position[6] = 1;
                    return true;
                }
            }
        }
        outMessage("bruh");
        return false;
    }

    public void emulationStep() {

        if (!isGameEnd) {
            int X = position[0];
            int X0 = position[1];
            int Y = position[2];
            int Y0 = position[3];
            int Z = position[4];
            int Z0 = position[5];
            int F = position[6];

            switch (Z) {
                case 1:
                    if (F == 1) {
                        make[X][Y].setImageResource(R.drawable.turtle_down_2);
                        position[6] = 2;
                    } else {
                        make[X][Y].setImageResource(R.drawable.turtle_down_1);
                        position[6] = 1;
                    }
                    break;
                case 2:
                    if (F == 1) {
                        make[X][Y].setImageResource(R.drawable.turtle_right_2);
                        position[6] = 2;
                    } else {
                        make[X][Y].setImageResource(R.drawable.turtle_right_1);
                        position[6] = 1;
                    }
                    break;
                case 3:
                    if (F == 1) {
                        make[X][Y].setImageResource(R.drawable.turtle_up_2);
                        position[6] = 2;
                    } else {
                        make[X][Y].setImageResource(R.drawable.turtle_up_1);
                        position[6] = 1;
                    }
                    break;
                case 4:
                    if (F == 1) {
                        make[X][Y].setImageResource(R.drawable.turtle_left_2);
                        position[6] = 2;
                    } else {
                        make[X][Y].setImageResource(R.drawable.turtle_left_1);
                        position[6] = 1;
                    }
                    break;
            }
            if (!((X==X0) && (Y==Y0))) {
                make[X0][Y0].setImageResource(R.drawable.ground);
            }
            textScore.setText(String.valueOf(scores));
            if ((X == width - 1) && (Y == height - 1)) {
                isGameEnd = true;
                isGameEdit = true;
                String ssss = String.valueOf(scores);
                outMessage("Твой результат " + ssss);
                setObject(0, width - 1, 0,height - 1);

                mHandler.removeCallbacks(gameRunnable);
            }
        }
    }

    void calcStep() {
        if (!isGameEnd) {
            int X = position[0];
            int X0 = position[1];
            int Y = position[2];
            int Y0 = position[3];
            int Z = position[4];
            int Z0 = position[5];

            int k = Z;
            if (Z != Z0) {
                k = Z0;          }
            switch (Z) {
                case 1:
                    if (isStepAllow(X - 1 ,Y)) {
                        position[0] = X - 1;
                        position[1] = X;
                        position[2] = Y;
                        position[3] = Y;
                        position[4] = 4;
                    }
                    else if (isStepAllow(X ,Y + 1)) {
                        position[0] = X;
                        position[1] = X;
                        position[2] = Y + 1;
                        position[3] = Y;
                        position[4] = 1;
                    }
                    else {
                        position[0] = X;
                        position[1] = X;
                        position[2] = Y;
                        position[3] = Y;
                        position[4] = 2;
                    }
                    break;
                case 2:
                    if (isStepAllow(X ,Y + 1)) {
                        position[0] = X;
                        position[1] = X;
                        position[2] = Y + 1;
                        position[3] = Y;
                        position[4] = 1;
                    }
                    else if (isStepAllow(X + 1 ,Y)) {
                        position[0] = X + 1;
                        position[1] = X;
                        position[2] = Y;
                        position[3] = Y;
                        position[4] = 2;
                    }
                    else {
                        position[0] = X;
                        position[1] = X;
                        position[2] = Y;
                        position[3] = Y;
                        position[4] = 3;
                    }
                    break;
                case 3:
                    if (isStepAllow(X + 1 ,Y)) {
                        position[0] = X + 1;
                        position[1] = X;
                        position[2] = Y;
                        position[3] = Y;
                        position[4] = 2;
                    }
                    else if (isStepAllow(X ,Y - 1)) {
                        position[0] = X;
                        position[1] = X;
                        position[2] = Y - 1;
                        position[3] = Y;
                        position[4] = 3;
                    }
                    else {
                        position[0] = X;
                        position[1] = X;
                        position[2] = Y;
                        position[3] = Y;
                        position[4] = 4;
                    }
                    break;
                case 4:
                    if (isStepAllow(X ,Y - 1)) {
                        position[0] = X;
                        position[1] = X;
                        position[2] = Y - 1;
                        position[3] = Y;
                        position[4] = 3;
                    }
                    else if (isStepAllow(X - 1 ,Y)) {
                        position[0] = X - 1;
                        position[1] = X;
                        position[2] = Y;
                        position[3] = Y;
                        position[4] = 4;
                    }
                    else {
                        position[0] = X;
                        position[1] = X;
                        position[2] = Y;
                        position[3] = Y;
                        position[4] = 1;
                    }
                    break;
            }
            gameBoard[position[1]][position[3]] = 0;
            gameBoard[position[0]][position[2]] = 3;
            if (!((X==X0) && (Y==Y0))) scores++;
        }

    }

    private boolean isStepAllow(int X, int Y) {
        if (X >= 0 && X <= width - 1 && Y >= 0 && Y <= height - 1 && gameBoard[X][Y] != 1) {
            return true;
        }
        else return false;
    }
    private void outMessage(String s) {
        Dialog dialog = new Dialog(GameActivity.this);
        dialog.setTitle("Сообщение");
        dialog.setContentView(R.layout.dialog);
        TextView textDialog = (TextView) dialog.findViewById(R.id.dialogTextView);
        textDialog.setText("\n" + s + "\n\n");
        dialog.show();
    }

}

