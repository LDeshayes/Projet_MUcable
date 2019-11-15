package com.example.projet_mucable.Display;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.projet_mucable.R;

public class RevisionDisplay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revision_display);
    }

    public void goToParCoeur(View view) {
        startActivity(new Intent(RevisionDisplay.this, ParCoeurActivity.class));
    }

    public void goToParChoix(View view) {
        startActivity(new Intent(RevisionDisplay.this, ParChoixActivity.class));
    }

    public void clicChoiceTag(View view) {
        Intent i = new Intent ( this, ChoixTagsDisplay.class );
        i.putExtra("Origin", "GestionTagsDisplay");
        startActivity( i );
        finish();
    }
}
