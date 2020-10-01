package com.example.projet_mucable.Display;

// Acitivity between main menu and cahierdisplay

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

import androidx.appcompat.app.AppCompatActivity;

import com.example.projet_mucable.R;

public class ChoixLangueDisplay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choixlangue_display);

        setupSpinner();
    }

    // Setting up the language spinner
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

    // Full clean  DB & FST_LAUNCH value in SharedPreferences
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
        EDITOR.putBoolean("NEED_UPD_01", true);
        EDITOR.commit();

        Intent i = new Intent ( this, LoadingScreenDisplay.class );
        startActivity( i );
        finish();

    }
}
