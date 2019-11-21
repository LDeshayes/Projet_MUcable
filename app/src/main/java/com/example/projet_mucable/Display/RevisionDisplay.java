package com.example.projet_mucable.Display;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.projet_mucable.R;

public class RevisionDisplay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revision_display);
    }

    public void goToParCoeur(View view) {

        Intent i = new Intent ( this, ParCoeurActivity.class );

        TextView t = (TextView) findViewById(R.id.editNBM);
        int nb_m = Integer.parseInt(t.getText().toString());
        i.putExtra("Nb_mots", nb_m);

        startActivity( i );
    }

    public void goToParChoix(View view) {
        startActivity(new Intent(RevisionDisplay.this, ParChoixActivity.class));
    }

    public void clicChoiceTag(View view) {
        Intent i = new Intent ( this, ChoixTagsDisplay.class );
        i.putExtra("Origin", "RevisionDisplay");
        startActivity( i );
        finish();
    }
}
