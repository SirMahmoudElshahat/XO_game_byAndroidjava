package com.example.xo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class History extends AppCompatActivity {
    private ListView historyListView;
    private HistoryAdapter historyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_history);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        historyListView = findViewById(R.id.historyListView);
        List<HistoryItem> historyItems = loadHistory();
        historyAdapter = new HistoryAdapter(this, historyItems);
        historyListView.setAdapter(historyAdapter);
    }

    private List<HistoryItem> loadHistory() {
        List<HistoryItem> historyItems = new ArrayList<>();
        MyDatabase dbHelper = new MyDatabase(this);
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        Cursor cursor = database.query("history", null, null, null, null, null, "id DESC");

        int playerNameIndex = cursor.getColumnIndex("player_name");
        int resultIndex = cursor.getColumnIndex("result");
        int dateTimeIndex = cursor.getColumnIndex("date_time");

        if (playerNameIndex == -1 || resultIndex == -1 || dateTimeIndex == -1) {
            throw new RuntimeException();
        }

        while (cursor.moveToNext()) {
            String playerName = cursor.getString(playerNameIndex);
            String result = cursor.getString(resultIndex);
            String dateTime = cursor.getString(dateTimeIndex);
            historyItems.add(new HistoryItem(playerName, result, dateTime));
        }
        cursor.close();
        database.close();

        return historyItems;
    }
}