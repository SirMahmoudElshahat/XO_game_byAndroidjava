package com.example.xo;

import static androidx.core.util.TypedValueCompat.dpToPx;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ImageButton whatsApp;
    private ImageButton x;
    private ImageButton intagram;
    private ImageButton facebook;

    private Button player_vs_player;
    private Button player_vs_pc;
    private ImageButton easy;
    private ImageButton normal;
    private ImageButton hard;
    private Button ok;
    private ConstraintLayout playerNamesLayout;
    private ConstraintLayout playerModeLayout;
    private boolean pc_play;

    private EditText player1Name;
    private EditText player2Name;

    private Button changeLanguage;


    public static final String KEY = "pc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        whatsApp = findViewById(R.id.whatsapp_imageButton);
        x = findViewById(R.id.x_imageButton);
        intagram = findViewById(R.id.instagram_imageButton);
        facebook = findViewById(R.id.facebook_imageButton);

        player_vs_player = findViewById(R.id.button_1vs1);
        player_vs_pc = findViewById(R.id.button_p_vs_phone);
        easy = findViewById(R.id.imageButton_Easy);
        normal = findViewById(R.id.imageButton_Normal);
        hard = findViewById(R.id.imageButton_Hard);
        ok = findViewById(R.id.button_Ok);
        playerNamesLayout = findViewById(R.id.player_name);
        playerModeLayout = findViewById(R.id.mode_Play);
        player1Name = findViewById(R.id.namePlayer1);
        player2Name = findViewById(R.id.namePalyer2);
        changeLanguage = findViewById(R.id.languagesButton);



        if(Data.language == null) {
            Locale defaultLocale = Locale.getDefault();
            String deviceLanguage = defaultLocale.getLanguage();

            Data.language = deviceLanguage;

            showLanguageDialog();
        }

        changeLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLanguageDialog();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Data.player1Name = player1Name.getText().toString();
                Data.player2Name = player2Name.getText().toString();
                if (player_vs_pc.isSelected() && Data.modeLevel != null) {
                    nextActivity();
                } else if (player_vs_player.isSelected() && !Data.player1Name.isEmpty() && !Data.player2Name.isEmpty()) {
                    nextActivity();
                } else {
                    Toast.makeText(MainActivity.this, getString(R.string.please_choose_play_mode), Toast.LENGTH_SHORT).show();
                }
            }
        });
        player_vs_player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPlayerChoose();
                player_vs_player.setSelected(true);
                playerModeLayout.setVisibility(View.GONE);
                playerNamesLayout.setVisibility(View.VISIBLE);
                pc_play = false;
            }
        });
        player_vs_pc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPlayerChoose();
                player_vs_pc.setSelected(true);
                playerNamesLayout.setVisibility(View.GONE);
                playerModeLayout.setVisibility(View.VISIBLE);
                pc_play = true;
            }
        });
        easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPlaymode();
                easy.setSelected(true);
                Data.modeLevel = Data.EASY_MODE;
            }
        });
        normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPlaymode();
                normal.setSelected(true);
                Data.modeLevel = Data.NORMAL_MODE;
            }
        });
        hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPlaymode();
                hard.setSelected(true);
                Data.modeLevel = Data.HARD_MODE;
            }
        });


        whatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share();
            }
        });
        x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share();
            }
        });
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share();
            }
        });
        intagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share();
            }
        });
    }

    private void nextActivity() {
        Intent intent = new Intent(MainActivity.this, PlyerVsPlyer.class);
        Data.pc_is_play = pc_play;
        intent.putExtra(KEY, pc_play);
        if (pc_play) {
            intent.putExtra("headline",getString(R.string.you_should_play));
        }
        startActivity(intent);
    }

    private void resetPlayerChoose() {
        player_vs_player.setSelected(false);
        player_vs_pc.setSelected(false);
    }

    private void resetPlaymode() {
        easy.setSelected(false);
        normal.setSelected(false);
        hard.setSelected(false);
    }
    void share() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "www.linkedin.com/in/mahm0udelshahat");
        sendIntent.setType("text/plain");
        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }

    private void showLanguageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_dialog_languages, null);
        builder.setView(dialogView);

        RadioGroup radioGroup = dialogView.findViewById(R.id.languageRadioGroup);
        RadioButton rbEnglish = dialogView.findViewById(R.id.englishRadioButton);
        RadioButton rbArabic = dialogView.findViewById(R.id.arabicRadioButton);
        RadioButton rbChinese = dialogView.findViewById(R.id.chineseRadioButton);
        RadioButton rbSpanish = dialogView.findViewById(R.id.spanishRadioButton);
        RadioButton rbFrench = dialogView.findViewById(R.id.frenchRadioButton);
        Button okDialog = dialogView.findViewById(R.id.buttonDialog);

        switch (Data.language) {
            case Data.CHINESE_LANGUAGE:
                rbChinese.setChecked(true);
                break;
            case Data.ENGLISH_LANGUAGE:
                rbEnglish.setChecked(true);
                break;
            case Data.ARABIC_LANGUAGE:
                rbArabic.setChecked(true);
                break;
            case Data.FRENCH_LANGUAGE:
                rbFrench.setChecked(true);
                break;
            case Data.SPANISH_LANGUAGE:
                rbSpanish.setChecked(true);
                break;
            default:
                rbEnglish.setChecked(true);
                break;
        }
        okDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                Data.language = Data.ENGLISH_LANGUAGE;

                if (selectedId == R.id.chineseRadioButton) {
                    Data.language = Data.CHINESE_LANGUAGE;
                } else if (selectedId == R.id.spanishRadioButton) {
                    Data.language = Data.SPANISH_LANGUAGE;
                } else if (selectedId == R.id.arabicRadioButton) {
                    Data.language = Data.ARABIC_LANGUAGE;
                } else if (selectedId == R.id.frenchRadioButton) {
                    Data.language = Data.FRENCH_LANGUAGE;
                }

                updateLanguage(Data.language);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                if (dialog.getWindow() != null) {
                    dialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_dialog_background);

                    int width = dpToPx(300);
                    int height = dpToPx(400);
                    dialog.getWindow().setLayout(width, height);
                }
            }
        });
        dialog.show();
    }
    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
    private void updateLanguage(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("App_Language", languageCode);
        editor.apply();
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}