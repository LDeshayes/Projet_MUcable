package com.example.projet_mucable;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.zip.Inflater;

public class CustomAdapter extends BaseAdapter {
    Context context;
    String words_list[];
    String translations_list[];
    LayoutInflater inflter;

    public CustomAdapter(Context applicationContext, String[] words_list, String[] translations_list) {
        this.context = context;
        this.words_list = words_list;
        this.translations_list = translations_list;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return words_list.length;
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
        view = inflter.inflate(R.layout.word_listview, null);
        TextView country = (TextView) view.findViewById(R.id.word);
        TextView icon = (TextView) view.findViewById(R.id.translation);
        country.setText(words_list[i]);
        icon.setText(translations_list[i]);
        return view;
    }

}