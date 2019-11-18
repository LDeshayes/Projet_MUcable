package com.example.projet_mucable.Display;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.projet_mucable.R;

public class LoadingScreenDisplay extends Activity {

    String data_anglais[];
    String data_allemand[];
    String data_espagnol[];
    String tag_list = "Nombre;Chiffre";

    {
        data_anglais = new String[]{"'One', 'Un', 'Nombre', 'Chiffre', 'NAN', 'NAN'", "'Two', 'Deux', 'Nombre', 'NAN', 'NAN', 'NAN'", "'Three', 'Trois', 'Nombre', 'NAN', 'NAN', 'NAN'"};
        data_allemand = new String[]{"'Ein', 'Un', 'Nombre', 'Chiffre', 'NAN', 'NAN'", "'Zwei', 'Deux', 'Nombre', 'NAN', 'NAN', 'NAN'", "'Drei', 'Trois', 'Nombre', 'NAN', 'NAN', 'NAN'"};
        data_espagnol = new String[]{"'Una', 'Un', 'Nombre', 'Chiffre', 'NAN', 'NAN'", "'Dos', 'Deux', 'Nombre', 'NAN', 'NAN', 'NAN'", "'Tres', 'Trois', 'Nombre', 'NAN', 'NAN', 'NAN'"};
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loadingscreen_display);

        createAndLoadDB();
        saveTags();

        Intent intent = new Intent(this, IntroMenuDisplay.class);
        startActivity(intent);
        finish();
    }

    @SuppressLint("WrongConstant")
    void createAndLoadDB() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean fst_launch = preferences.getBoolean("FST_LAUNCH", true);
        if (fst_launch) {

            // CREATION OF DB
            SQLiteDatabase CDB = openOrCreateDatabase(
                    "CDB.db"
                    , SQLiteDatabase.CREATE_IF_NECESSARY
                    , null
            );

            final String Create_table_ANGLAIS =
                    "CREATE TABLE t_Anglais ("
                            + "Id_Word INTEGER PRIMARY KEY AUTOINCREMENT,"
                            + "Word TEXT,"
                            + "Translation TEXT,"
                            + "Tag_1 TEXT,"
                            + "Tag_2 TEXT,"
                            + "Tag_3 TEXT,"
                            + "Tag_4 TEXT);";

            final String Create_table_ALLEMAND =
                    "CREATE TABLE t_Allemand ("
                            + "Id_Word INTEGER PRIMARY KEY AUTOINCREMENT,"
                            + "Word TEXT,"
                            + "Translation TEXT,"
                            + "Tag_1 TEXT,"
                            + "Tag_2 TEXT,"
                            + "Tag_3 TEXT,"
                            + "Tag_4 TEXT);";

            final String Create_table_ESPAGNOL =
                    "CREATE TABLE t_Espagnol ("
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
            int i;
            String Insert_Data;
            for ( i = 0; i < data_anglais.length; i++ ) {
                Insert_Data = "INSERT INTO t_Anglais VALUES (NULL,"+data_anglais[i]+")";
                CDB.execSQL(Insert_Data);
            }
            for ( i = 0; i < data_allemand.length; i++ ) {
                Insert_Data = "INSERT INTO t_Allemand VALUES (NULL,"+data_allemand[i]+")";
                CDB.execSQL(Insert_Data);
            }
            for ( i = 0; i < data_espagnol.length; i++ ) {
                Insert_Data = "INSERT INTO t_Espagnol VALUES (NULL,"+data_espagnol[i]+")";
                CDB.execSQL(Insert_Data);
            }

            // CLOSE THE DB BETWEEN ACTIVITIES ?

            SharedPreferences.Editor DB_EXIST_EDIT = preferences.edit();
            DB_EXIST_EDIT.putBoolean("FST_LAUNCH", false);
            DB_EXIST_EDIT.commit();

            Log.i("ChoixLangueDisplay", "DB HAS BEEN CREATED");

        }

        Log.i("ChoixLangueDisplay", "DB HAS BEEN TESTED");

    }

    void saveTags() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor NEW_TAGLIST = preferences.edit();
        NEW_TAGLIST.putString("TAG_LIST", tag_list);
        NEW_TAGLIST.commit();

    }

}
