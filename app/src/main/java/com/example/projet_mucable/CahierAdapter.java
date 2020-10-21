package com.example.projet_mucable;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Map;


public class CahierAdapter extends BaseAdapter {

    //Context context;
    String[] words_list;
    String[] translations_list;
    String[] tags_list;
    LayoutInflater inflter;
    Map<String,String> tagColMap;


    public CahierAdapter(Context applicationContext, String[] words_list, String[] translations_list, String[] tags_list, Map<String, String> tagCol) {
        this.words_list = words_list;
        this.translations_list = translations_list;
        this.tags_list = tags_list;
        this.tagColMap = tagCol;
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
        view = inflter.inflate(R.layout.word_listview_updated, null);
        TextView word = view.findViewById(R.id.word);

        TextView tag1 = view.findViewById(R.id.tag1);
        TextView tag2 = view.findViewById(R.id.tag2);
        TextView tag3 = view.findViewById(R.id.tag3);
        TextView tag4 = view.findViewById(R.id.tag4);

        TextView trad = view.findViewById(R.id.trad);
        String[] tagsSplit = tags_list[i].split(" - ");
        //String[] tagColor = new String[tagsSplit.length];
        int nbTag = 0;

        for(String t : tagsSplit){
            if(t != null && !t.equals("") && !t.equals("NAN") && !t.equals("NAN_NULL")){
                nbTag++;
            }
        }


        switch (nbTag) {
            case 1:
                tag1.setText(tagsSplit[0]);
                tag1.setTextColor(Color.parseColor(tagColMap.get(tagsSplit[0])));
                break;
            case 2:
                tag1.setText(tagsSplit[0]+"  -  ");
                tag1.setTextColor(Color.parseColor(tagColMap.get(tagsSplit[0])));

                tag2.setText(tagsSplit[1]);
                tag2.setTextColor(Color.parseColor(tagColMap.get(tagsSplit[1])));
                break;
            case 3:
                tag1.setText(tagsSplit[0]+"  -  ");
                tag1.setTextColor(Color.parseColor(tagColMap.get(tagsSplit[0])));

                tag2.setText(tagsSplit[1]+"  -  ");
                tag2.setTextColor(Color.parseColor(tagColMap.get(tagsSplit[1])));

                tag3.setText(tagsSplit[2]);
                tag3.setTextColor(Color.parseColor(tagColMap.get(tagsSplit[2])));
                break;
            case 4:
                tag1.setText(tagsSplit[0]+"  -  ");
                tag1.setTextColor(Color.parseColor(tagColMap.get(tagsSplit[0])));

                tag2.setText(tagsSplit[1]+"  -  ");
                tag2.setTextColor(Color.parseColor(tagColMap.get(tagsSplit[1])));

                tag3.setText(tagsSplit[2]+"  -  ");
                tag3.setTextColor(Color.parseColor(tagColMap.get(tagsSplit[2])));

                tag4.setText(tagsSplit[3]);
                tag4.setTextColor(Color.parseColor(tagColMap.get(tagsSplit[3])));
                break;

            default:
                tag1.setText("Aucuns");
        }


        word.setText("  "+words_list[i]);
        trad.setText(""+translations_list[i]);
        return view;
    }
}




















