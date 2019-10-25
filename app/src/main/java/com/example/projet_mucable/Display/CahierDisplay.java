package com.example.projet_mucable.Display;

// Affiche une liste de vocabulaire d'une langue choisie, 4.2

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.example.projet_mucable.CustomAdapter;
import com.example.projet_mucable.R;

public class CahierDisplay extends Activity {

    ListView words_listview;
    String words_list[] = { "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "lo" };
    String translations_list[] = { "Un", "Deux", "Trois", "Quatre", "Cinq", "Six", "Sept", "Huit", "Neuf", "Dix", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "lo" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cahier_display);

        setupElements();
    }

    void setupElements() {
        setupLanguage();
        setupListView();
    }

    void setupLanguage() {
        Intent i = getIntent();
        String language = i.getStringExtra("LangueChoisie");

        TextView textView = (TextView) findViewById(R.id.language);
        textView.setText(language);
    }

    void setupListView() {
        words_listview = (ListView) findViewById(R.id.words_listview);
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), words_list, translations_list);
        words_listview.setAdapter(customAdapter);
    }

}
