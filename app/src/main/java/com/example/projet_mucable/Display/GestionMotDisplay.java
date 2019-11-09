package com.example.projet_mucable.Display;

// Add un mot au cahier ou le modifie, 4.2 cdc image du milieu

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.projet_mucable.R;

public class GestionMotDisplay extends Activity {

    String language;
    String mode;
    int key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestionmot_display);

        getIntentValues();
    }

    void getIntentValues() {
        Intent i = getIntent();
        language = i.getStringExtra("LangueChoisie");
        mode = i.getStringExtra("mode");
        if ( mode.equals("Modify") ) {
            key = i.getIntExtra("key", -1);
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent ( this, CahierDisplay.class );
        i.putExtra( "LangueChoisie", language );
        startActivity( i );
        finish();
    }

}
