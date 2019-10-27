package com.example.projet_mucable.Display;

// Menu principal de l'application, 4.1 cdc

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projet_mucable.Cahier;
import com.example.projet_mucable.R;

public class IntroMenuDisplay extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intromenu_display);

    }

    public void goToCahiers(View view) {
        startActivity(new Intent(IntroMenuDisplay.this, ChoixLangueDisplay.class));
    }


    public void activityToGestionTagsDisplay(View view) {
        startActivity(new Intent(IntroMenuDisplay.this, GestionTagsDisplay.class));
    }
}
