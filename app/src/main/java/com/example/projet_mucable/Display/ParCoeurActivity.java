package com.example.projet_mucable.Display;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.projet_mucable.R;

public class ParCoeurActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_par_coeur);
    }


    public void goToParCoeur(View view) {

        Intent i = new Intent ( this, ChoixTagsDisplay.class );
        i.putExtra("Origin", "GestionTagsDisplay");
        startActivity( i );
        finish();

    }
}
