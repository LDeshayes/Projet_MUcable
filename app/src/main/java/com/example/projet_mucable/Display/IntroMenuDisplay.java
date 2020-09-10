package com.example.projet_mucable.Display;

// Menu principal de l'application, 4.1 cdc

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

    public void goToRevision(View view) {
        startActivity(new Intent(IntroMenuDisplay.this, RevisionDisplay.class));
    }

    public void resetTests(View view) {

        /*@SuppressLint("WrongConstant") SQLiteDatabase CDB = openOrCreateDatabase("CDB.db", SQLiteDatabase.CREATE_IF_NECESSARY, null );
        ContentValues cv = new ContentValues();
        cv.put("CoefAppr",0);
        CDB.update("t_Anglais", cv, "", null);
        CDB.update("t_Allemand", cv, "", null);
        CDB.update("t_Espagnol", cv, "", null);*/

        startActivity(new Intent(IntroMenuDisplay.this, StatsActivity.class));

    }

    public void activityToGestionTagsDisplay(View view) {
        startActivity(new Intent(IntroMenuDisplay.this, GestionTagsDisplay.class));
    }

    public void goToShareDisplay(View view) {
        startActivity(new Intent(IntroMenuDisplay.this, PartageDisplay.class));
    }

}
