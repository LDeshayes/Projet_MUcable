package com.example.projet_mucable.Display;

// Offre la liste complète des tags actuels, permet d'en selectionner certains et renvoie les choix fait en intent

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.projet_mucable.R;

public class ChoixTagsDisplay extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choixtags_display);
    }

    @Override
    /**
     * action when back button is pressed
     */
    public void onBackPressed() {
        Intent i = new Intent ( this, GestionTagsDisplay.class );
        startActivity( i );
        finish();
    }

}
