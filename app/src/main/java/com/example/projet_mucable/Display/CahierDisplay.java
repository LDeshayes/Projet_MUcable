package com.example.projet_mucable.Display;

// Affiche une liste de vocabulaire d'une langue choisie, 4.2

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.projet_mucable.CustomAdapter;
import com.example.projet_mucable.R;

public class CahierDisplay extends Activity {

    String language;
    ListView words_listview;
    String words_list[] = { "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "lo" };
    String translations_list[] = { "Un", "Deux", "Trois", "Quatre", "Cinq", "Six", "Sept", "Huit", "Neuf", "Dix", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "lo" };
    String tags_list[] = { "Nombre", "Nombre", "Nombre", "Nombre", "Nombre", "Nombre", "Nombre", "Nombre", "Nombre", "Nombre", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "lo" };
    int key; // future key pour repérer le word à modifier
    SQLiteDatabase CDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cahier_display);

        getLanguage();

        setupDB();
        setupElements();
    }

    void getLanguage() {
        Intent i = getIntent();
        language = i.getStringExtra("LangueChoisie");
    }

    void setupDB() {
        getDB();
        loadDB();
    }

    @SuppressLint("WrongConstant")
    void getDB () {
        CDB = openOrCreateDatabase("CDB.db", SQLiteDatabase.CREATE_IF_NECESSARY, null );
    }

    void loadDB() {

    }

    void setupElements() {
        setupLanguage();
        setupListView();
    }

    void setupLanguage() {
        TextView textView = (TextView) findViewById(R.id.language);
        textView.setText(language);
    }

    void setupListView() {
        words_listview = (ListView) findViewById(R.id.words_listview);
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), words_list, translations_list, tags_list);
        words_listview.setAdapter(customAdapter);

        words_listview = (ListView)findViewById(R.id.words_listview);

        words_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int pos, long id) {

                TextView textView = (TextView) v.findViewById(R.id.word);
                key = Integer.parseInt( ( (String) textView.getText() ).split(". ")[0] ) - 1;
                //Toast.makeText(CahierDisplay.this, "You pressed the item : "+key, Toast.LENGTH_SHORT).show();

            }
        });
    }

}
