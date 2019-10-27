package com.example.projet_mucable.Display;

// Activité entre menu pcp et cahier pour choisir la langue du cahier

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

import com.example.projet_mucable.R;

public class ChoixLangueDisplay extends Activity {

    String data_anglais[];
    String data_allemand[];
    String data_espagnol[];

    {
        data_anglais = new String[]{"'One', 'Un', 'Nombre', 'NAN', 'NAN', 'NAN'", "'Two', 'Deux', 'Nombre', 'NAN', 'NAN', 'NAN'", "'Three', 'Trois', 'Nombre', 'NAN', 'NAN', 'NAN'"};
        data_allemand = new String[]{"'Ein', 'Un', 'Nombre', 'NAN', 'NAN', 'NAN'", "'Zwei', 'Deux', 'Nombre', 'NAN', 'NAN', 'NAN'", "'Drei', 'Trois', 'Nombre', 'NAN', 'NAN', 'NAN'"};
        data_espagnol = new String[]{"'Una', 'Un', 'Nombre', 'NAN', 'NAN', 'NAN'", "'Dos', 'Deux', 'Nombre', 'NAN', 'NAN', 'NAN'", "'Tres', 'Trois', 'Nombre', 'NAN', 'NAN', 'NAN'"};
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choixlangue_display);

        setupSpinner();
    }

    void setupSpinner() {

        Spinner spinner = (Spinner) findViewById(R.id.language_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.language_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }

    public void ActivityToCahierDisplay(View view) {

        Spinner mySpinner = (Spinner) findViewById(R.id.language_spinner);
        String language = mySpinner.getSelectedItem().toString();

        createAndLoadDB();

        Intent i = new Intent ( this, CahierDisplay.class );
        i.putExtra( "LangueChoisie", language );
        startActivity( i );
        finish();

    }

    @SuppressLint("WrongConstant")
    void createAndLoadDB() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean exist = preferences.getBoolean("FST_LAUNCH", false);
        if (!exist) {

            // CREATION OF DB
            SQLiteDatabase CDB = openOrCreateDatabase(
                    "CDB.db"
                    , SQLiteDatabase.CREATE_IF_NECESSARY
                    , null
            );

            final String Create_table_ANGLAIS =
                    "CREATE TABLE t_ANGLAIS ("
                            + "Id_Word INTEGER PRIMARY KEY AUTOINCREMENT,"
                            + "Word TEXT,"
                            + "Translation TEXT,"
                            + "Tag_1 TEXT,"
                            + "Tag_2 TEXT,"
                            + "Tag_3 TEXT,"
                            + "Tag_4 TEXT);";

            final String Create_table_ALLEMAND =
                    "CREATE TABLE t_ALLEMAND ("
                            + "Id_Word INTEGER PRIMARY KEY AUTOINCREMENT,"
                            + "Word TEXT,"
                            + "Translation TEXT,"
                            + "Tag_1 TEXT,"
                            + "Tag_2 TEXT,"
                            + "Tag_3 TEXT,"
                            + "Tag_4 TEXT);";

            final String Create_table_ESPAGNOL =
                    "CREATE TABLE t_ESPAGNOL ("
                            + "Id_Word INTEGER PRIMARY KEY AUTOINCREMENT,"
                            + "Word TEXT,"
                            + "Translation TEXT,"
                            + "Tag_1 TEXT,"
                            + "Tag_2 TEXT,"
                            + "Tag_3 TEXT,"
                            + "Tag_4 TEXT);";

            CDB.execSQL(Create_table_ANGLAIS);
            CDB.execSQL(Create_table_ALLEMAND);
            CDB.execSQL(Create_table_ESPAGNOL);

            // FIRST LOAD OF DB
            int incr;
            String Insert_Data;
            for ( incr = 0; incr < data_anglais.length; incr++ ) {
                Insert_Data = "INSERT INTO t_ANGLAIS VALUES (NULL,"+data_anglais[incr]+")";
                CDB.execSQL(Insert_Data);
            }
            for ( incr = 0; incr < data_allemand.length; incr++ ) {
                Insert_Data = "INSERT INTO t_ALLEMAND VALUES (NULL,"+data_allemand[incr]+")";
                CDB.execSQL(Insert_Data);
            }
            for ( incr = 0; incr < data_espagnol.length; incr++ ) {
                Insert_Data = "INSERT INTO t_ESPAGNOL VALUES (NULL,"+data_espagnol[incr]+")";
                CDB.execSQL(Insert_Data);
            }

            // CLOSE THE DB BETWEEN ACTIVITIES ?

            SharedPreferences.Editor DB_EXIST_EDIT = preferences.edit();
            DB_EXIST_EDIT.putBoolean("FST_LAUNCH", true);
            DB_EXIST_EDIT.commit();

            Log.i("ChoixLangueDisplay", "DB HAS BEEN CREATED");

        }

        Log.i("ChoixLangueDisplay", "DB HAS BEEN TESTED");

    }

}
