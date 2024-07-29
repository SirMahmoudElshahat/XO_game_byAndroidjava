package com.example.xo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlyerVsPlyer extends AppCompatActivity {

    private TextView game_mode1;
    private TextView game_mode2;
    private Button new_game;

    private Button buttom11;
    private Button buttom12;
    private Button buttom13;
    private Button buttom21;
    private Button buttom22;
    private Button buttom23;
    private Button buttom31;
    private Button buttom32;
    private Button buttom33;

    private boolean pc_is_play;
    private boolean player1Turn = true;
    private int roundCount = 0;
    private String headline_when_pc_play;
    private String[][] board = new String[3][3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_plyer_vs_plyer);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        game_mode1 = findViewById(R.id.textView_game_mode1);
        game_mode2 = findViewById(R.id.textView_game_mode2);
        new_game = findViewById(R.id.button_new_game);

        buttom11 = findViewById(R.id.button11);
        buttom12 = findViewById(R.id.button12);
        buttom13 = findViewById(R.id.button13);
        buttom21 = findViewById(R.id.button21);
        buttom22 = findViewById(R.id.button22);
        buttom23 = findViewById(R.id.button23);
        buttom31 = findViewById(R.id.button31);
        buttom32 = findViewById(R.id.button32);
        buttom33 = findViewById(R.id.button33);

        //get pc mode
        Intent get_pc_mode = getIntent();
        pc_is_play = get_pc_mode.getBooleanExtra("pc", pc_is_play);
        headline_when_pc_play = get_pc_mode.getStringExtra("headline");
        if(pc_is_play){
            game_mode2.setText(headline_when_pc_play);
        }

        // Button new game
        new_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetBoard();
            }
        });

        // Set onClickListeners for all buttons
        setButtonListeners();
    }

    private void setButtonListeners() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!((Button) v).getText().toString().equals("")) {
                    Toast.makeText(PlyerVsPlyer.this, "Buttom alredy played", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (player1Turn) {
                    ((Button) v).setText("X");
                } else {
                    ((Button) v).setText("O");
                }

                roundCount++;

                if (player1Turn) {
                    game_mode2.setText("Player 2 (o) should play");
                } else {
                    game_mode2.setText("Player 1 (x) should play");
                }

                if (checkForWin()) {
                    if (player1Turn) {
                        player1Wins();
                    } else {
                        player2Wins();
                    }
                } else if (roundCount == 9) {
                    draw();
                } else {
                    player1Turn = !player1Turn;

                    if (!player1Turn && pc_is_play) {
                        game_mode2.setText("Phone play ⌛");
                        disableAllButtons();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                enableAllButtons();
                                playComputerMove();
                            }
                        }, 2000);
                    }
                }
            }
        };

        buttom11.setOnClickListener(listener);
        buttom12.setOnClickListener(listener);
        buttom13.setOnClickListener(listener);
        buttom21.setOnClickListener(listener);
        buttom22.setOnClickListener(listener);
        buttom23.setOnClickListener(listener);
        buttom31.setOnClickListener(listener);
        buttom32.setOnClickListener(listener);
        buttom33.setOnClickListener(listener);
    }

    private boolean checkForWin() {
        String[][] field = new String[3][3];

        field[0][0] = buttom11.getText().toString();
        field[0][1] = buttom12.getText().toString();
        field[0][2] = buttom13.getText().toString();
        field[1][0] = buttom21.getText().toString();
        field[1][1] = buttom22.getText().toString();
        field[1][2] = buttom23.getText().toString();
        field[2][0] = buttom31.getText().toString();
        field[2][1] = buttom32.getText().toString();
        field[2][2] = buttom33.getText().toString();

        for (int i = 0; i < 3; i++) {
            if (field[i][0].equals(field[i][1]) && field[i][0].equals(field[i][2]) && !field[i][0].equals("")) {
                return true;
            }
            if (field[0][i].equals(field[1][i]) && field[0][i].equals(field[2][i]) && !field[0][i].equals("")) {
                return true;
            }
        }

        if (field[0][0].equals(field[1][1]) && field[0][0].equals(field[2][2]) && !field[0][0].equals("")) {
            return true;
        }
        if (field[0][2].equals(field[1][1]) && field[0][2].equals(field[2][0]) && !field[0][2].equals("")) {
            return true;
        }

        return false;
    }

    private void player1Wins() {
        game_mode1.setText("Game is End");
        if(pc_is_play){
            game_mode2.setText("You Win, Congratulations!");
        }else {
            game_mode2.setText("Player 1 (x) wins!");
        }
        disableAllButtons();
    }

    private void player2Wins() {
        game_mode1.setText("Game is End");
        if(pc_is_play){
            game_mode2.setText("Phone Win, Good luck next time");
        }else {
            game_mode2.setText("Player 2 (o) wins!");
        }
        disableAllButtons();
    }

    private void draw() {
        game_mode1.setText("Game is End");
        game_mode2.setText("Draw!");
        disableAllButtons();
    }

    private void resetBoard() {
        buttom11.setText("");
        buttom12.setText("");
        buttom13.setText("");
        buttom21.setText("");
        buttom22.setText("");
        buttom23.setText("");
        buttom31.setText("");
        buttom32.setText("");
        buttom33.setText("");
        roundCount = 0;
        player1Turn = true;

        if(pc_is_play){
            game_mode2.setText(headline_when_pc_play);
        }else {
            game_mode2.setText("Player 1 (x) should play");
        }
        game_mode1.setText("Game is Run");
        enableAllButtons();
    }

    private void enableAllButtons() {
        buttom11.setEnabled(true);
        buttom12.setEnabled(true);
        buttom13.setEnabled(true);
        buttom21.setEnabled(true);
        buttom22.setEnabled(true);
        buttom23.setEnabled(true);
        buttom31.setEnabled(true);
        buttom32.setEnabled(true);
        buttom33.setEnabled(true);
    }

    private void disableAllButtons() {
        buttom11.setEnabled(false);
        buttom12.setEnabled(false);
        buttom13.setEnabled(false);
        buttom21.setEnabled(false);
        buttom22.setEnabled(false);
        buttom23.setEnabled(false);
        buttom31.setEnabled(false);
        buttom32.setEnabled(false);
        buttom33.setEnabled(false);
    }
    private void playComputerMove() {
        List<Button> emptyButtons = new ArrayList<>();

        if (buttom11.getText().toString().equals("")) emptyButtons.add(buttom11);
        if (buttom12.getText().toString().equals("")) emptyButtons.add(buttom12);
        if (buttom13.getText().toString().equals("")) emptyButtons.add(buttom13);
        if (buttom21.getText().toString().equals("")) emptyButtons.add(buttom21);
        if (buttom22.getText().toString().equals("")) emptyButtons.add(buttom22);
        if (buttom23.getText().toString().equals("")) emptyButtons.add(buttom23);
        if (buttom31.getText().toString().equals("")) emptyButtons.add(buttom31);
        if (buttom32.getText().toString().equals("")) emptyButtons.add(buttom32);
        if (buttom33.getText().toString().equals("")) emptyButtons.add(buttom33);

        if (!emptyButtons.isEmpty()) {
            Random random = new Random();
            int index = random.nextInt(emptyButtons.size());
            Button chosenButton = emptyButtons.get(index);
            chosenButton.setText("O");

            roundCount++;

            game_mode2.setText("you should play");

            if (checkForWin()) {
                player2Wins();
            } else if (roundCount == 9) {
                draw();
            } else {
                player1Turn = !player1Turn;
            }
        }
    }
}
