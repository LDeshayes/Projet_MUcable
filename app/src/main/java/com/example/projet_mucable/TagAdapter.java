package com.example.projet_mucable;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.HashMap;
import java.util.Map;


public class TagAdapter extends BaseAdapter {

    Context context;
    String[] tags_list;
    LayoutInflater inflter;
    Map<String,String> tagColMap;


    public TagAdapter(Context applicationContext, String[] tags_list, Map<String, String> tagCol) {
        this.tags_list = tags_list;
        this.tagColMap = tagCol;
        inflter = (LayoutInflater.from(applicationContext));

    }

    @Override
    public int getCount() {
        return tags_list.length;
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
        view = inflter.inflate(R.layout.tag_listview, null);

        // Get elements
        TextView tag = view.findViewById(R.id.tag);

        // Set elements
        tag.setText(tags_list[i]);
        tag.setTextColor(Color.parseColor(tagColMap.get(tags_list[i])));

        return view;
    }
}




















