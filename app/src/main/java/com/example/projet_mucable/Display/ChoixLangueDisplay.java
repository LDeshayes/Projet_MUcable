package com.example.projet_mucable.Display;

// Activité entre menu pcp et cahier pour choisir la langue du cahier

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.example.projet_mucable.R;

public class ChoixLangueDisplay extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupElements();

        setContentView(R.layout.activity_choixlangue_display);
    }

    public void ActivityToCahierDisplay(View view) {
    }

    void setupElements() {
    }
}
