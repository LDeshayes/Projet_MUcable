package com.example.projet_mucable;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

public class ResultatsAdapter extends BaseAdapter {


    Context context;
    String[] question_list;
    String[] rep_list;
    String[] attendu_list;
    String[] mark_list;
    LayoutInflater inflter;

    public ResultatsAdapter(Context applicationContext, String[] question_list, String[] rep_list, String[] attendu_list, String[] mark_list) {
        this.question_list = question_list;
        this.rep_list = rep_list;
        this.attendu_list = attendu_list;
        this.mark_list = mark_list;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return question_list.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.resultats_listview, null);
        TextView question = view.findViewById(R.id.question);
        TextView rep = view.findViewById(R.id.rep);
        TextView attendu = view.findViewById(R.id.attendu);
        TextView symbol = view.findViewById(R.id.symbol);
        question.setText(question_list[i]);
        rep.setText(rep_list[i]);
        attendu.setText(attendu_list[i]);
        symbol.setText(mark_list[i]);
        if(mark_list[i].charAt(0) == '✓'){
            rep.setTextColor(Color.GREEN);
        }
        else{
            rep.setTextColor(Color.RED);
        }

        return view;
    }
}
