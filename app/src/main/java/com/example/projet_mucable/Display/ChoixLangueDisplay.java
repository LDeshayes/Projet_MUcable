package com.example.projet_mucable.Display;

// Activité entre menu pcp et cahier pour choisir la langue du cahier

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.projet_mucable.Cahier;
import com.example.projet_mucable.R;

import static android.content.ContentValues.TAG;

public class ChoixLangueDisplay extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choixlangue_display);

        setupSpinner();
    }

    public void ActivityToCahierDisplay(View view) {

        Spinner mySpinner = (Spinner) findViewById(R.id.language_spinner);
        String language = mySpinner.getSelectedItem().toString();

        Intent i = new Intent ( this, CahierDisplay.class );
        i.putExtra( "LangueChoisie", language );
        startActivity( i );
        finish();

    }

    void setupSpinner() {

        Spinner spinner = (Spinner) findViewById(R.id.language_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.language_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }
}
