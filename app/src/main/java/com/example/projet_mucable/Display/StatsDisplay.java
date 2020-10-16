package com.example.projet_mucable.Display;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import java.util.ArrayList;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import com.example.projet_mucable.CustomAdapter;
import com.example.projet_mucable.R;



public class StatsDisplay extends AppCompatActivity {

    String language = "";
    ListView w_words_listview;
    ListView m_words_listview;
    ListView b_words_listview;
    //Integer[][] key_list_final, key_list;
    String[][] list_ww;
    String[][] list_mw;
    String[][] list_bw;
    int[] rowCount = new int[3];
    int key = -1; // id of word of interest
    View key_view;
    SQLiteDatabase CDB;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats_dislay);

        getLanguage();
        setupDB();

    }

    void getLanguage() {

        // Get spinners
        final Spinner spinnerL = findViewById(R.id.spinnerLang);

        // Create language list
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Choisissez une langue");
        arrayList.add("Anglais");
        arrayList.add("Allemand");
        arrayList.add("Espagnol");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrayList);
        //ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.language_array, android.R.layout.simple_spinner_item);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerL.setAdapter(arrayAdapter);
        spinnerL.setSelection(0);

        // Define spinners behavior
        spinnerL.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                    language = parent.getItemAtPosition(position).toString();
                    loadDB(language);
                    final LinearLayout linlay = findViewById(R.id.linLay);
                    linlay.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });

    }

    void setupDB() {
        getDB();
        //loadDB(language);
    }

    @SuppressLint("WrongConstant")
    void getDB () {
        CDB = openOrCreateDatabase("CDB.db", SQLiteDatabase.CREATE_IF_NECESSARY, null );
    }

    String printNAN ( String tag1, String tag2, String tag3, String tag4 ) {

        String[] tabTag = { tag1, tag2, tag3, tag4 };
        StringBuilder tempTag = new StringBuilder();

        for (String s : tabTag) {
            if (!(s.equals("NAN"))) {
                tempTag.append(" - ").append(s);
            }
        }

        if ( tempTag.length() == 0 ) {
            return (tempTag.toString());
        } else {
            return ( tempTag.substring(3) );
        }

    }

    void loadDB(String lang) {

        //// Worst Words /////////////////////////////////////////////////////
        Cursor cursorWW = CDB.query(
                /*"t_"+lang,*/"t_Mot",
                null,
                "Langue LIKE '"+lang+"' AND CoefAppr IN ('0','1','2')",
                null,
                null,
                null,
                "CoefAppr ASC, Word ASC",
                null

        );
        rowCount[0] = cursorWW.getCount();
        list_ww = new String[4][rowCount[0]];
        cursorWW.moveToFirst();
        for ( int i=0; i < rowCount[0]; i++ ) {
            list_ww[0][i] = cursorWW.getString(1);
            list_ww[1][i] = cursorWW.getString(2);
            list_ww[2][i] = printNAN ( cursorWW.getString(3), cursorWW.getString(4), cursorWW.getString(5), cursorWW.getString(6) );
            list_ww[3][i] = cursorWW.getString(0);
            cursorWW.moveToNext();
        }
        cursorWW.close();

        //// Medium Words /////////////////////////////////////////////////////
        Cursor cursorMW = CDB.query(
                /*"t_"+lang,*/"t_Mot",
                null,
                "Langue LIKE '"+language+"' AND CoefAppr IN ('3','4')",
                null,
                null,
                null,
                "CoefAppr ASC, Word ASC",
                null

        );
        rowCount[1] = cursorMW.getCount();
        list_mw = new String[4][rowCount[1]];
        cursorMW.moveToFirst();
        for ( int i=0; i < rowCount[1]; i++ ) {
            list_mw[0][i] = cursorMW.getString(1);
            list_mw[1][i] = cursorMW.getString(2);
            list_mw[2][i] = printNAN ( cursorMW.getString(3), cursorMW.getString(4), cursorMW.getString(5), cursorMW.getString(6) );
            list_mw[3][i] = cursorMW.getString(0);
            cursorMW.moveToNext();
        }
        cursorMW.close();

        //// Best Words /////////////////////////////////////////////////////
        Cursor cursorBW = CDB.query(
                /*"t_"+lang,*/"t_Mot",
                null,
                "Langue LIKE '"+language+"' AND CoefAppr IN ('5')",
                null,
                null,
                null,
                "CoefAppr ASC, Word ASC",
                null

        );
        rowCount[2] = cursorBW.getCount();
        list_bw = new String[4][rowCount[2]];
        cursorBW.moveToFirst();
        for ( int i=0; i < rowCount[2]; i++ ) {
            list_bw[0][i] = cursorBW.getString(1);
            list_bw[1][i] = cursorBW.getString(2);
            list_bw[2][i] = printNAN ( cursorBW.getString(3), cursorBW.getString(4), cursorBW.getString(5), cursorBW.getString(6) );
            list_bw[3][i] = cursorBW.getString(0);
            cursorBW.moveToNext();
        }
        cursorBW.close();



        w_words_listview = findViewById(R.id.worst_words);
        m_words_listview = findViewById(R.id.medium_words);
        b_words_listview = findViewById(R.id.best_words);

        CustomAdapter customAdapter_w = new CustomAdapter(getApplicationContext(), list_ww[0], list_ww[1], list_ww[2]);
        CustomAdapter customAdapter_m = new CustomAdapter(getApplicationContext(), list_mw[0], list_mw[1], list_mw[2]);
        CustomAdapter customAdapter_b = new CustomAdapter(getApplicationContext(), list_bw[0], list_bw[1], list_bw[2]);

        w_words_listview.setAdapter(customAdapter_w);
        m_words_listview.setAdapter(customAdapter_m);
        b_words_listview.setAdapter(customAdapter_b);


        w_words_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int pos, long id) {
                if ( key != -1 ) {
                    key_view.setBackgroundResource(0);
                }
                key_view = v;
                key_view.setBackgroundResource(R.drawable.border);
                key = Integer.parseInt(list_ww[3][pos]);
            }
        });
        m_words_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int pos, long id) {
                if ( key != -1 ) {
                    key_view.setBackgroundResource(0);
                }
                key_view = v;
                key_view.setBackgroundResource(R.drawable.border);
                key = Integer.parseInt(list_mw[3][pos]);
            }
        });
        b_words_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int pos, long id) {
                if ( key != -1 ) {
                    key_view.setBackgroundResource(0);
                }
                key_view = v;
                key_view.setBackgroundResource(R.drawable.border);
                key = Integer.parseInt(list_bw[3][pos]);
            }
        });
    }

    public void resetTests(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogDarker));
        builder.setMessage("Êtes vous sur(e) de vouloir réinitialiser les tests ?")
                .setCancelable(true)
                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        @SuppressLint("WrongConstant") SQLiteDatabase CDB = openOrCreateDatabase("CDB.db", SQLiteDatabase.CREATE_IF_NECESSARY, null );
                        ContentValues cv = new ContentValues();
                        cv.put("CoefAppr",0);
                        //CDB.update("t_Anglais", cv, "", null);
                        //CDB.update("t_Allemand", cv, "", null);
                        CDB.update("t_Mot", cv, "Langue='"+language+"'", null);
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                    }
                })
                .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void toGraphs(View view) {
        if(!language.equals("")){
            Intent i = new Intent ( this, GraphMemDisplay.class );
            i.putExtra("Language",language);
            i.putExtra("Key",key);

            startActivity( i );
        }
    }


}
