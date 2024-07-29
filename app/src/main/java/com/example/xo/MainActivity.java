package com.example.xo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
public class MainActivity extends AppCompatActivity {

    private Button player_vs_player;
    private Button player_vs_pc;
    private boolean pc_play;


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
        player_vs_player = findViewById(R.id.button_1vs1);
        player_vs_pc = findViewById(R.id.button_p_vs_pc);

        player_vs_player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_1vs1 = new Intent(MainActivity.this,PlyerVsPlyer.class);
                pc_play = false;
                intent_1vs1.putExtra(KEY,pc_play);
                startActivity(intent_1vs1);
            }
        });

        player_vs_pc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_1vspc = new Intent(MainActivity.this,PlyerVsPlyer.class);
                pc_play = true;
                intent_1vspc.putExtra(KEY,pc_play);
                intent_1vspc.putExtra("headline","you should play");
                startActivity(intent_1vspc);
            }
        });

    }
}