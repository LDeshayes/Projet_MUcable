package com.example.projet_mucable.Display;

// Activité entre menu pcp et cahier pour choisir la langue du cahier

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.projet_mucable.R;

public class ChoixLangueDisplay extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choixlangue_display);

        setupSpinner();
    }

    // Setup du spinner pour les langues
    void setupSpinner() {

        Spinner spinner = (Spinner) findViewById(R.id.language_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.language_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }

    public void activityToCahierDisplay(View view) {

        Spinner mySpinner = (Spinner) findViewById(R.id.language_spinner);
        String language = mySpinner.getSelectedItem().toString();

        Intent i = new Intent ( this, CahierDisplay.class );
        i.putExtra( "LangueChoisie", language );
        startActivity( i );
        finish();

    }

    // Full clean de la DB & de la valeur FST_LAUNCH en SharedPreferences
    @SuppressLint("WrongConstant")
    public void cleanDB(View view) {

        SQLiteDatabase CDB = openOrCreateDatabase(
                "CDB.db"
                , SQLiteDatabase.CREATE_IF_NECESSARY
                , null
        );

        CDB.execSQL("DROP TABLE IF EXISTS t_Anglais");
        CDB.execSQL("DROP TABLE IF EXISTS t_Allemand");
        CDB.execSQL("DROP TABLE IF EXISTS t_Espagnol");

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor EDITOR = preferences.edit();
        EDITOR.putBoolean("FST_LAUNCH", true);
        EDITOR.putString("TAG_LIST", "EMPTY_NULL");
        EDITOR.commit();

        startActivity( new Intent ( this, LoadingScreenDisplay.class ) );
        finish();

    }
}
