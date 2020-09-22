package com.example.projet_mucable.Display;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projet_mucable.R;

public class LoadingScreenDisplay extends AppCompatActivity {

    String data_anglais[];
    String data_allemand[];
    String data_espagnol[];
    String tag_list = "Nombre;Chiffre";

    {
        data_anglais = new String[]{"'One', 'Un', 'Nombre', 'Chiffre', 'NAN', 'NAN'", "'Two', 'Deux', 'NAN', 'Chiffre', 'NAN', 'NAN'", "'Three', 'Trois', 'Nombre', 'NAN', 'NAN', 'NAN'"};
        data_allemand = new String[]{"'Ein', 'Un', 'Nombre', 'Chiffre', 'NAN', 'NAN'", "'Zwei', 'Deux', 'NAN', 'Chiffre', 'NAN', 'NAN'", "'Drei', 'Trois', 'Nombre', 'NAN', 'NAN', 'NAN'"};
        data_espagnol = new String[]{"'Una', 'Un', 'Nombre', 'Chiffre', 'NAN', 'NAN'", "'Dos', 'Deux', 'NAN', 'Chiffre', 'NAN', 'NAN'", "'Tres', 'Trois', 'Nombre', 'NAN', 'NAN', 'NAN'"};
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loadingscreen_display);

        createAndLoadDBAndsaveTags();
        //saveTags();
        updateDB();
        getScreenSize();

        Intent intent = new Intent(this, IntroMenuDisplay.class);
        startActivity(intent);
        finish();
    }

    @SuppressLint("WrongConstant")
    void createAndLoadDBAndsaveTags() {

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

            // Ajout des tags par défaut

            SharedPreferences.Editor NEW_TAGLIST = preferences.edit();
            NEW_TAGLIST.putString("TAG_LIST", tag_list);
            NEW_TAGLIST.commit();

            // CLOSE THE DB BETWEEN ACTIVITIES ?

            SharedPreferences.Editor DB_EXIST_EDIT = preferences.edit();
            DB_EXIST_EDIT.putBoolean("FST_LAUNCH", false);
            DB_EXIST_EDIT.commit();

        }


    }

    void saveTags() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor NEW_TAGLIST = preferences.edit();
        NEW_TAGLIST.putString("TAG_LIST", tag_list);
        NEW_TAGLIST.commit();

    }

    @SuppressLint("WrongConstant")
    void updateDB() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean need_upd_01 = preferences.getBoolean("NEED_UPD_01", true);
        if ( need_upd_01 ) {

            int rowCount, i;
            String delete, insert, lowered;
            Cursor cursor;
            SQLiteDatabase CDB = openOrCreateDatabase(
                    "CDB.db"
                    , SQLiteDatabase.CREATE_IF_NECESSARY
                    , null
            );

            // UPD Anglais
            cursor = CDB.query(
                    "t_Anglais",
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );
            rowCount = cursor.getCount();
            cursor.moveToFirst();

            for ( i = 0; i < rowCount; i++ ) {
                delete = "DELETE FROM t_Anglais WHERE Id_Word="+cursor.getInt(0);
                CDB.execSQL(delete);

                lowered = cursor.getInt(0)+", '"+cursor.getString(1).toLowerCase()+"', '"+cursor.getString(2).toLowerCase()+"', '"+cursor.getString(3)+"', '"+cursor.getString(4)+"', '"+cursor.getString(5)+"', '"+cursor.getString(6);
                insert = "INSERT INTO t_Anglais (Id_Word, Word, Translation, Tag_1, Tag_2, Tag_3, Tag_4) VALUES ("+lowered+"')";
                CDB.execSQL(insert);
                cursor.moveToNext();
            }

            // UPD Espagnol
            cursor = CDB.query(
                    "t_Espagnol",
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );
            rowCount = cursor.getCount();
            cursor.moveToFirst();

            for ( i = 0; i < rowCount; i++ ) {
                delete = "DELETE FROM t_Espagnol WHERE Id_Word="+cursor.getInt(0);
                CDB.execSQL(delete);

                lowered = cursor.getInt(0)+", '"+cursor.getString(1).toLowerCase()+"', '"+cursor.getString(2).toLowerCase()+"', '"+cursor.getString(3)+"', '"+cursor.getString(4)+"', '"+cursor.getString(5)+"', '"+cursor.getString(6);
                insert = "INSERT INTO t_Espagnol (Id_Word, Word, Translation, Tag_1, Tag_2, Tag_3, Tag_4) VALUES ("+lowered+"')";
                CDB.execSQL(insert);
                cursor.moveToNext();
            }

            // UPD Allemand
            cursor = CDB.query(
                    "t_Allemand",
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );
            rowCount = cursor.getCount();
            cursor.moveToFirst();

            for ( i = 0; i < rowCount; i++ ) {
                delete = "DELETE FROM t_Allemand WHERE Id_Word="+cursor.getInt(0);
                CDB.execSQL(delete);

                lowered = cursor.getInt(0)+", '"+cursor.getString(1)+"', '"+cursor.getString(2).toLowerCase()+"', '"+cursor.getString(3)+"', '"+cursor.getString(4)+"', '"+cursor.getString(5)+"', '"+cursor.getString(6);
                insert = "INSERT INTO t_Allemand (Id_Word, Word, Translation, Tag_1, Tag_2, Tag_3, Tag_4) VALUES ("+lowered+"')";
                CDB.execSQL(insert);
                cursor.moveToNext();
            }

            SharedPreferences.Editor DB_NEED_UPD = preferences.edit();
            DB_NEED_UPD.putBoolean("NEED_UPD_01", false);
            DB_NEED_UPD.commit();

        }

        // Coef update
        boolean need_upd_02 = preferences.getBoolean("NEED_UPD_02", true);
        if(need_upd_02){

            SQLiteDatabase CDB = openOrCreateDatabase(
                    "CDB.db"
                    , SQLiteDatabase.CREATE_IF_NECESSARY
                    , null
            );

            CDB.execSQL("ALTER TABLE t_Anglais ADD COLUMN CoefAppr INTEGER DEFAULT 0 CHECK (CoefAppr>=0 AND CoefAppr<4)");
            CDB.execSQL("ALTER TABLE t_Allemand ADD COLUMN CoefAppr INTEGER DEFAULT 0 CHECK (CoefAppr>=0 AND CoefAppr<4)");
            CDB.execSQL("ALTER TABLE t_Espagnol ADD COLUMN CoefAppr INTEGER DEFAULT 0 CHECK (CoefAppr>=0 AND CoefAppr<4)");

            SharedPreferences.Editor DB_NEED_UPD = preferences.edit();
            DB_NEED_UPD.putBoolean("NEED_UPD_02", false);
            DB_NEED_UPD.commit();

        }
        boolean need_upd_03 = preferences.getBoolean("NEED_UPD_03", true);
        if ( need_upd_03 ) {

            SQLiteDatabase CDB;
            CDB = openOrCreateDatabase(
                    "CDB.db"
                    , SQLiteDatabase.CREATE_IF_NECESSARY
                    , null
            );

            CDB.setForeignKeyConstraintsEnabled(true);
            CDB.execSQL("PRAGMA foreign_keys = ON;");

            /*CDB.execSQL("DROP TABLE t_Mot");
            CDB.execSQL("DROP TABLE t_Session");
            CDB.execSQL("DROP TABLE t_Stat");*/

            final String Create_table_Mot =
                    "CREATE TABLE t_Mot ("
                            + "Id_Word INTEGER PRIMARY KEY AUTOINCREMENT,"
                            + "Word TEXT,"
                            + "Translation TEXT,"
                            + "Tag_1 TEXT,"
                            + "Tag_2 TEXT,"
                            + "Tag_3 TEXT,"
                            + "Tag_4 TEXT,"
                            + "CoefAppr INTEGER DEFAULT 0 CHECK (CoefAppr>=0 AND CoefAppr<=5),"
                            + "Langue TEXT);";

            final String Create_table_STATS =
                    "CREATE TABLE t_Stat ("
                            + "Id_Stat INTEGER PRIMARY KEY AUTOINCREMENT,"
                            + "Temps DOUBLE,"
                            + "CoefAppr INTEGER,"
                            + "Resultat INTEGER,"
                            + "Id_Session INTEGER,"
                            + "Id_Word INTEGER,"
                            + "FOREIGN KEY (Id_Session) REFERENCES t_Session(Id_Session) ON DELETE CASCADE,"
                            + "FOREIGN KEY (Id_Word) REFERENCES t_Mot(Id_Word) ON DELETE CASCADE);";

            final String Create_table_Session =
                    "CREATE TABLE t_Session ("
                            + "Id_Session INTEGER PRIMARY KEY AUTOINCREMENT,"
                            + "Date INTEGER,"
                            + "Score INTEGER,"
                            + "Temps DOUBLE);";

            final String Create_table_TagColor =
                    "CREATE TABLE t_TagColor ("
                            + "Id_Tag INTEGER PRIMARY KEY AUTOINCREMENT,"
                            + "Nom STRING,"
                            + "Couleur STRING DEFAULT '#000000');";


            CDB.execSQL(Create_table_Mot);
            CDB.execSQL(Create_table_Session);
            CDB.execSQL(Create_table_STATS);
            CDB.execSQL(Create_table_TagColor);

            int rowCount, i;
            String delete, insert, lowered;
            Cursor cursor;

            // Remplissage des tables de mots

            cursor = CDB.query(
                    "t_Anglais",
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );
            rowCount = cursor.getCount();
            cursor.moveToFirst();
            for ( i = 0; i < rowCount; i++ ) {

                lowered = "'"+cursor.getString(1)+"', '"+cursor.getString(2).toLowerCase()+"', '"+cursor.getString(3)+"', '"+cursor.getString(4)+"', '"+cursor.getString(5)+"', '"+cursor.getString(6)+"', '"+cursor.getInt(7)+"'";
                insert = "INSERT INTO t_Mot (Word, Translation, Tag_1, Tag_2, Tag_3, Tag_4, CoefAppr, Langue) VALUES ("+lowered+", 'Anglais')";
                CDB.execSQL(insert);
                cursor.moveToNext();
            }

            cursor = CDB.query(
                    "t_Espagnol",
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );
            rowCount = cursor.getCount();
            cursor.moveToFirst();
            for ( i = 0; i < rowCount; i++ ) {

                lowered = "'"+cursor.getString(1)+"', '"+cursor.getString(2).toLowerCase()+"', '"+cursor.getString(3)+"', '"+cursor.getString(4)+"', '"+cursor.getString(5)+"', '"+cursor.getString(6)+"', '"+cursor.getInt(7)+"'";
                insert = "INSERT INTO t_Mot (Word, Translation, Tag_1, Tag_2, Tag_3, Tag_4, CoefAppr, Langue) VALUES ("+lowered+", 'Espagnol')";
                CDB.execSQL(insert);
                cursor.moveToNext();
            }

            cursor = CDB.query(
                    "t_Allemand",
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );
            rowCount = cursor.getCount();
            cursor.moveToFirst();
            for ( i = 0; i < rowCount; i++ ) {

                lowered = "'"+cursor.getString(1)+"', '"+cursor.getString(2).toLowerCase()+"', '"+cursor.getString(3)+"', '"+cursor.getString(4)+"', '"+cursor.getString(5)+"', '"+cursor.getString(6)+"', '"+cursor.getInt(7)+"'";
                insert = "INSERT INTO t_Mot (Word, Translation, Tag_1, Tag_2, Tag_3, Tag_4, CoefAppr, Langue) VALUES ("+lowered+", 'Allemand')";
                CDB.execSQL(insert);
                cursor.moveToNext();
            }

            // Remplissage table des tags
            String[] tags = preferences.getString("TAG_LIST", "EMPTY_NULL").split(";");
            for(String tag: tags){
                insert = "INSERT INTO t_TagColor (Nom) VALUES (tag)";
                CDB.execSQL(insert);
            }



            final String drop_anglais  = "DROP TABLE t_Anglais";
            final String drop_espagnol = "DROP TABLE t_Espagnol";
            final String drop_allemand = "DROP TABLE t_Allemand";
            CDB.execSQL(drop_anglais);
            CDB.execSQL(drop_espagnol);
            CDB.execSQL(drop_allemand);


            SharedPreferences.Editor DB_NEED_UPD = preferences.edit();
            DB_NEED_UPD.putBoolean("NEED_UPD_03", false);
            DB_NEED_UPD.commit();

        }

    }


    void getScreenSize() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor screenSize = preferences.edit();
        screenSize.putInt("screenHeight", Resources.getSystem().getDisplayMetrics().heightPixels);
        screenSize.putInt("screenWidth", Resources.getSystem().getDisplayMetrics().widthPixels);
        screenSize.commit();

    }

}
