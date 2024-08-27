package com.example.xo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends BaseAdapter {
    private Context context;
    private List<HistoryItem> historyItems;

    public HistoryAdapter(Context context, List<HistoryItem> historyItems) {
        this.context = context;
        this.historyItems = historyItems;
    }

    @Override
    public int getCount() {
        return historyItems.size();
    }

    @Override
    public Object getItem(int position) {
        return historyItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_game_history, parent, false);
        }

        TextView playersName = convertView.findViewById(R.id.playersName);
        TextView timeAndDate = convertView.findViewById(R.id.timeAndDate);
        TextView historyResult = convertView.findViewById(R.id.historyResult);

        HistoryItem item = historyItems.get(position);

        playersName.setText(item.getPlayerName());
        timeAndDate.setText(item.getDateTime());
        historyResult.setText(item.getResult());

        return convertView;
    }
}

class HistoryItem {
    private String playerName;
    private String result;
    private String dateTime;

    public HistoryItem(String playerName, String result, String dateTime) {
        this.playerName = playerName;
        this.result = result;
        this.dateTime = dateTime;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getResult() {
        return result;
    }

    public String getDateTime() {
        return dateTime;
    }
}