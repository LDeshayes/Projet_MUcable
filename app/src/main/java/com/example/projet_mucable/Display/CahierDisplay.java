package com.example.projet_mucable.Display;

// Affiche une liste de vocabulaire d'une langue choisie, 4.2

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projet_mucable.CustomAdapter;
import com.example.projet_mucable.R;

public class CahierDisplay extends Activity {

    String language;
    ListView words_listview;
    int[] key_list;
    String[] words_list;// = { "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "lo" };
    String[] translations_list;// = { "Un", "Deux", "Trois", "Quatre", "Cinq", "Six", "Sept", "Huit", "Neuf", "Dix", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "lo" };
    String[] tags_list;// = { "Nombre", "Nombre", "Nombre", "Nombre", "Nombre", "Nombre", "Nombre", "Nombre", "Nombre", "Nombre", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "lo" };
    int key = -1; // future key pour repérer le word à modifier
    View key_view;
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

    // FUTURE : ADD FILTER HERE
    void loadDB() {

        Cursor cursor = CDB.query(
                "t_"+language,
                null,
                null,
                null,
                null,
                null,
                null
        );

        int rowCount = cursor.getCount();

        key_list = new int[rowCount];
        words_list = new String[rowCount];
        translations_list = new String[rowCount];
        tags_list = new String[rowCount];

        String[] tempTag;

        cursor.moveToFirst();
        for ( int i = 0; i < rowCount; i++ ) {
            key_list[i] = cursor.getInt(0);
            words_list[i] = cursor.getString(1);
            translations_list[i] = cursor.getString(2);
            tags_list[i] = printNAN ( cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6) );
            cursor.moveToNext();
        }

    }

    String printNAN ( String tag1, String tag2, String tag3, String tag4 ) {

        String[] tabTag = ( tag1+tag2+tag3+tag4 ).split("NAN");
        String tempTag = "";

        for ( int i = 0; ( i < tabTag.length ) && !( tabTag[i].equals("NAN") ); i++ ) {
            tempTag = tempTag + " - " + tabTag[i];
        }

        return ( tempTag.substring(3) );

    }

    void setupElements() {
        setupLanguage();
        setupListView();
    }

    void setupLanguage() {
        TextView textView = (TextView) findViewById(R.id.language);
        textView.setText(language+" ");
    }

    void setupListView() {
        words_listview = (ListView) findViewById(R.id.words_listview);
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), words_list, translations_list, tags_list);
        words_listview.setAdapter(customAdapter);

        words_listview = (ListView)findViewById(R.id.words_listview);

        words_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int pos, long id) {
                if ( key != -1 ) {
                    key_view.setBackgroundColor(0xFAFAFA);
                }
                key_view = v;
                key_view.setBackgroundColor(Color.LTGRAY);
                key = key_list[pos];
            }
        });
    }

    public void onClicModify(View view) {
        if ( key == -1 ) {
            Toast.makeText(getApplicationContext(),"Veuillez sélectionner une entrée à modifier !",Toast.LENGTH_SHORT).show();
        } else {
            Intent i = new Intent ( this, GestionMotDisplay.class );
            i.putExtra("key",key);
            launchGestionMots("Modify", i);
        }

    }

    public void onClicAdd(View view) {
        launchGestionMots("Add", new Intent ( this, GestionMotDisplay.class ));
    }

    void launchGestionMots ( String mode, Intent i ) {
        i.putExtra( "MODE", mode );
        i.putExtra( "LangueChoisie", language );
        startActivity( i );
        finish();
    }

}
