package com.example.xo;

import static com.example.xo.Data.ARABIC_LANGUAGE;
import static com.example.xo.Data.CHINESE_LANGUAGE;
import static com.example.xo.Data.ENGLISH_LANGUAGE;
import static com.example.xo.Data.FRENCH_LANGUAGE;
import static com.example.xo.Data.SPANISH_LANGUAGE;
import static com.example.xo.Data.language;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class PlyerVsPlyer extends AppCompatActivity {

    private TextView game_mode1;
    private TextView game_mode2;
    private TextView player1_result;
    private TextView player2_result;
    private Button new_game;
    private ImageButton history;
    private ImageButton soundMode;
    private boolean soundIsTrue;
    private MediaPlayer mediaPlayer;

    private SharedPreferences sp ;

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
        player1_result = findViewById(R.id.player1_count);
        player2_result = findViewById(R.id.player2_count);
        new_game = findViewById(R.id.button_new_game);
        history = findViewById(R.id.historyImageButton);
        soundMode = findViewById(R.id.soundModeImageButton);


        buttom11 = findViewById(R.id.button11);
        buttom12 = findViewById(R.id.button12);
        buttom13 = findViewById(R.id.button13);
        buttom21 = findViewById(R.id.button21);
        buttom22 = findViewById(R.id.button22);
        buttom23 = findViewById(R.id.button23);
        buttom31 = findViewById(R.id.button31);
        buttom32 = findViewById(R.id.button32);
        buttom33 = findViewById(R.id.button33);

        Data.player1Count = 0;
        Data.plyer2Count = 0;



        // prefrence sound
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        soundIsTrue = sp.getBoolean("sound",true);
        if(soundIsTrue){
            soundMode.setImageResource(R.drawable.sound);
        }else {
            soundMode.setImageResource(R.drawable.nosound);
        }

        mediaPlayer = MediaPlayer.create(this,R.raw.start);
        if(soundIsTrue){
            mediaPlayer.start();
        }

        soundMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sp.edit();
                if(soundIsTrue){
                    soundMode.setImageResource(R.drawable.nosound);
                    editor.putBoolean("sound",false);
                    soundIsTrue = false;
                }else {
                    soundMode.setImageResource(R.drawable.sound);
                    editor.putBoolean("sound",true);
                    soundIsTrue = true;
                }
                editor.apply();
            }
        });


        //get pc mode
        Intent get_pc_mode = getIntent();
        pc_is_play = get_pc_mode.getBooleanExtra("pc", pc_is_play);
        headline_when_pc_play = get_pc_mode.getStringExtra("headline");
        if(Data.pc_is_play){
            game_mode2.setText(headline_when_pc_play);
            player1_result.setText(getString(R.string.you_won_0_times));
            player2_result.setText(getString(R.string.phone_won_0_times));
        }else{
            game_mode2.setText(Data.player1Name + " " +getString(R.string.x_should_play));
            player1_result.setText(Data.player1Name + " " + getString(R.string.won_0_times));
            player2_result.setText(Data.player2Name + " " + getString(R.string.won_0_times));
        }

        // Button new game
        new_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(soundIsTrue){
                    nullMediaPlayer();
                    mediaPlayer = MediaPlayer.create(v.getContext(),R.raw.start);
                    mediaPlayer.start();
                }
                resetBoard();
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlyerVsPlyer.this, History.class);
                startActivity(intent);
            }
        });

        // Set onClickListeners for all buttons
        setButtonListeners();
    }

    private void setButtonListeners() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(soundIsTrue){
                    nullMediaPlayer();
                    mediaPlayer = MediaPlayer.create(v.getContext(),R.raw.click);
                    mediaPlayer.start();
                }

                if (!((Button) v).getText().toString().equals("")) {
                    Toast.makeText(PlyerVsPlyer.this, getString(R.string.buttom_alredy_played), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (player1Turn) {
                    ((Button) v).setText("X");
                } else {
                    ((Button) v).setText("O");
                }

                roundCount++;

                if (player1Turn) {
                    game_mode2.setText(Data.player2Name + " " + getString(R.string.o_should_play));
                } else {
                    game_mode2.setText(Data.player1Name + " " + getString(R.string.x_should_play));
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
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if(soundIsTrue){
                                    nullMediaPlayer();
                                    mediaPlayer = MediaPlayer.create(v.getContext(),R.raw.phoneplay);
                                    mediaPlayer.start();
                                }
                            }
                        }, 300);

                        game_mode2.setText(getString(R.string.phone_play));
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
        disableUnselectedButtons();
        game_mode1.setText(getString(R.string.game_is_end));
        Data.player1Count ++;
        if(pc_is_play){
            game_mode2.setText(getString(R.string.you_win_congratulations));
            player1_result.setText(getString(R.string.you_won)+ " " + Data.player1Count + " " + getString(R.string.times));
            addGameHistory(getString(R.string.you_won_history));
            if(soundIsTrue){
                nullMediaPlayer();
                mediaPlayer = MediaPlayer.create(this,R.raw.victory);
                mediaPlayer.start();
            }
        }else {
            game_mode2.setText(Data.player1Name + " " + getString(R.string.x_wins));
            player1_result.setText(Data.player1Name + " " + getString(R.string.won) + " " + Data.player1Count + " " + getString(R.string.times));
            addGameHistory(Data.player1Name + getString(R.string.won_history));
            if(soundIsTrue){
                nullMediaPlayer();
                mediaPlayer = MediaPlayer.create(this,R.raw.gameover);
                mediaPlayer.start();
            }
        }
    }

    private void player2Wins() {
        disableUnselectedButtons();
        game_mode1.setText(getString(R.string.game_is_end));
        Data.plyer2Count ++;
        if(pc_is_play){
            game_mode2.setText(getString(R.string.phone_win_good_luck));
            player2_result.setText(getString(R.string.phone_won)+ " " +Data.plyer2Count + " " + getString(R.string.times) );
            addGameHistory(getString(R.string.phone_won_history));
            if(soundIsTrue) {
                nullMediaPlayer();
                mediaPlayer = MediaPlayer.create(this, R.raw.loser);
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.release();
                        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.winnerlaugh);
                        mediaPlayer.start();
                    }
                });
                mediaPlayer.start();
            }
        }else {
            game_mode2.setText(Data.player2Name + " " + getString(R.string.o_wins));
            player2_result.setText(Data.player2Name + " " + getString(R.string.won) + " " + Data.plyer2Count + " " + getString(R.string.times));
            addGameHistory(Data.player2Name + getString(R.string.won_history));
            if(soundIsTrue){
                nullMediaPlayer();
                mediaPlayer = MediaPlayer.create(this,R.raw.gameover);
                mediaPlayer.start();
            }
        }
    }

    private void draw() {
        game_mode1.setText(getString(R.string.game_is_end));
        game_mode2.setText(getString(R.string.draw));
        disableUnselectedButtons();
        addGameHistory(getString(R.string.draw_history));
        if(soundIsTrue){
            nullMediaPlayer();
            mediaPlayer = MediaPlayer.create(this,R.raw.gameover);
            mediaPlayer.start();
        }
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
            game_mode2.setText(Data.player1Name + getString(R.string.x_should_play));
        }
        game_mode1.setText(getString(R.string.text_view_game_mode1));
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
    private void disableUnselectedButtons() {
        if (buttom11.getText().toString().equals("")) buttom11.setEnabled(false);
        if (buttom12.getText().toString().equals("")) buttom12.setEnabled(false);
        if (buttom13.getText().toString().equals("")) buttom13.setEnabled(false);
        if (buttom21.getText().toString().equals("")) buttom21.setEnabled(false);
        if (buttom22.getText().toString().equals("")) buttom22.setEnabled(false);
        if (buttom23.getText().toString().equals("")) buttom23.setEnabled(false);
        if (buttom31.getText().toString().equals("")) buttom31.setEnabled(false);
        if (buttom32.getText().toString().equals("")) buttom32.setEnabled(false);
        if (buttom33.getText().toString().equals("")) buttom33.setEnabled(false);
    }
    private void playComputerMove() {
        switch (Data.modeLevel) {
            case Data.EASY_MODE:
                easyPlayComputerMove();
                break;
            case Data.NORMAL_MODE:
                normalPlayComputerMove();
                break;
            case Data.HARD_MODE:
                hardPlayComputerMove();
                break;
            default:
                break;
        }
    }
    private void easyPlayComputerMove(){
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

            game_mode2.setText(getString(R.string.you_should_play));

            if (checkForWin()) {
                player2Wins();
            } else if (roundCount == 9) {
                draw();
            } else {
                player1Turn = !player1Turn;
            }
        }
    }
    private void normalPlayComputerMove() {
        if (tryToWinOrBlock("O")) {
            finalizeMove();
            return;
        }
        if (tryToWinOrBlock("X")) {
            finalizeMove();
            return;
        }
        easyPlayComputerMove();
    }
    private void finalizeMove() {
        roundCount++;
        game_mode2.setText(getString(R.string.you_should_play));

        if (checkForWin()) {
            player2Wins();
        } else if (roundCount == 9) {
            draw();
        } else {
            player1Turn = !player1Turn;
        }
    }
    private boolean tryToWinOrBlock(String symbol) {
        // Check each line (rows, columns, diagonals) for a winning/blocking move
        if (trySetButton(buttom11, buttom12, buttom13, symbol)) return true;
        if (trySetButton(buttom21, buttom22, buttom23, symbol)) return true;
        if (trySetButton(buttom31, buttom32, buttom33, symbol)) return true;
        if (trySetButton(buttom11, buttom21, buttom31, symbol)) return true;
        if (trySetButton(buttom12, buttom22, buttom32, symbol)) return true;
        if (trySetButton(buttom13, buttom23, buttom33, symbol)) return true;
        if (trySetButton(buttom11, buttom22, buttom33, symbol)) return true;
        if (trySetButton(buttom13, buttom22, buttom31, symbol)) return true;

        return false;
    }
    private boolean trySetButton(Button b1, Button b2, Button b3, String symbol) {
        String opponentSymbol = symbol.equals("O") ? "X" : "O"; // Determine opponent symbol

        if (b1.getText().toString().equals(symbol) && b2.getText().toString().equals(symbol) && b3.getText().toString().equals("")) {
            b3.setText("O");
            return true;
        }
        if (b1.getText().toString().equals(symbol) && b3.getText().toString().equals(symbol) && b2.getText().toString().equals("")) {
            b2.setText("O");
            return true;
        }
        if (b2.getText().toString().equals(symbol) && b3.getText().toString().equals(symbol) && b1.getText().toString().equals("")) {
            b1.setText("O");
            return true;
        }
        // Check for opponent's winning move and block
        if (b1.getText().toString().equals(opponentSymbol) && b2.getText().toString().equals(opponentSymbol) && b3.getText().toString().equals("")) {
            b3.setText("O");
            return true;
        }
        if (b1.getText().toString().equals(opponentSymbol) && b3.getText().toString().equals(opponentSymbol) && b2.getText().toString().equals("")) {
            b2.setText("O");
            return true;
        }
        if (b2.getText().toString().equals(opponentSymbol) && b3.getText().toString().equals(opponentSymbol) && b1.getText().toString().equals("")) {
            b1.setText("O");
            return true;
        }
        return false;
    }
    private void hardPlayComputerMove() {
        if (tryToWinOrBlock("O")) {
            finalizeMove();
            return;
        }
        if (tryToWinOrBlock("X")) {
            finalizeMove();
            return;
        }
        if (buttom22.getText().toString().equals("")) {
            buttom22.setText("O");
            finalizeMove();
            return;
        }
        if (buttom11.getText().toString().equals("")) {
            buttom11.setText("O");
            finalizeMove();
            return;
        }
        if (buttom13.getText().toString().equals("")) {
            buttom13.setText("O");
            finalizeMove();
            return;
        }
        if (buttom31.getText().toString().equals("")) {
            buttom31.setText("O");
            finalizeMove();
            return;
        }
        if (buttom33.getText().toString().equals("")) {
            buttom33.setText("O");
            finalizeMove();
            return;
        }
        easyPlayComputerMove();
    }


    private void addGameHistory(String result) {
        MyDatabase dbHelper = new MyDatabase(this);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("player_name", getPlayerNames());
        values.put("result", result);
        values.put("date_time", getCurrentDateTime());

        database.insert("history", null, values);
        database.close();
    }
    private String getPlayerNames() {
        String modeInHistory = "";
        if(Data.modeLevel  == Data.HARD_MODE){
            modeInHistory = getString(R.string.hard);
        } else if (Data.modeLevel == Data.NORMAL_MODE) {
            modeInHistory = getString(R.string.normal);
        } else if (Data.modeLevel == Data.EASY_MODE) {
            modeInHistory = getString(R.string.easy);
        }

        if (pc_is_play) {
            return getString(R.string.you_vs_phone) + modeInHistory + ")";
        } else {
            return Data.player1Name + " " + getString(R.string.vs) + " " + Data.player2Name;
        }
    }
    private String getCurrentDateTime() {
        Locale localeLanguage;

        switch (language) {
            case ENGLISH_LANGUAGE:
                localeLanguage = new Locale(ENGLISH_LANGUAGE);
                break;
            case CHINESE_LANGUAGE:
                localeLanguage = new Locale("zh", "CN");
                break;
            case SPANISH_LANGUAGE:
                localeLanguage = new Locale(SPANISH_LANGUAGE);
                break;
            case ARABIC_LANGUAGE:
                localeLanguage = new Locale(ARABIC_LANGUAGE);
                break;
            case FRENCH_LANGUAGE:
                localeLanguage = new Locale(FRENCH_LANGUAGE);
                break;
            default:
                localeLanguage = Locale.getDefault();
                break;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(getString(R.string.date_format), localeLanguage);
        return sdf.format(new Date());
    }

    private void nullMediaPlayer(){
        if(mediaPlayer != null) {
            if(mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
