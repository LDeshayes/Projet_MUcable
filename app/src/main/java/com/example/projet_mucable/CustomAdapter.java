package com.example.projet_mucable;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {
    Context context;
    String words_list[];
    String translations_list[];
    String tags_list[];
    LayoutInflater inflter;

    public CustomAdapter(Context applicationContext, String[] words_list, String[] translations_list, String[] tags_list) {
        this.words_list = words_list;
        this.translations_list = translations_list;
        this.tags_list = tags_list;
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
        TextView word = (TextView) view.findViewById(R.id.word);
        TextView tags = (TextView) view.findViewById(R.id.tags);
        word.setText("  "+words_list[i]+" -> "+translations_list[i]);
        tags.setText("  Tags : "+tags_list[i]);
        return view;
    }

}