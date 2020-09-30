package com.example.projet_mucable.Display;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.projet_mucable.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GraphMemDisplay extends AppCompatActivity {

    SQLiteDatabase CDB;
    int key;
    String word;
    String translation;
    String tag_1;
    String tag_2;
    String tag_3;
    String tag_4;
    int CoefAppr_Word ;


    Map<String,String> mapSession = new HashMap<>();


    int[] id_session;
    Double[] Temps;
    int[] CoefAppr;
    int[] Resultat;
    int[] nbIndice;


    Date dateNow = new Date();
    int periodeSpinner;
    Date dateLimite;


    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_mem_display);

        Intent i = getIntent();
        key = i.getIntExtra("Key", -1);
        CDB = openOrCreateDatabase("CDB.db", SQLiteDatabase.CREATE_IF_NECESSARY, null );


        /////////////////////////////////////////////////////
        ////////////////// Gestion spinner //////////////////
        /////////////////////////////////////////////////////
        final Spinner spinnerP = findViewById(R.id.spinnerPeriode);
        ArrayList<String> listPeriode = new ArrayList<String>();
        listPeriode.add("2 semaines");
        listPeriode.add("4 semaines");
        listPeriode.add("6 semaines");
        listPeriode.add("8 semaines");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listPeriode);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerP.setAdapter(arrayAdapter);

        spinnerP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //Log.d("testtest", "pos: "+position);
                periodeSpinner = (position+1)*2;
                generateStuff();
                separateSession();
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
                periodeSpinner = -1;
            }
        });
        spinnerP.setSelection(0);

        getKeyRow();


    }


    // Recupere les infos du mots
    void getKeyRow() {

        Cursor cursor = CDB.query(
                "t_Mot",
                null,
                "Id_Word=" + key,
                null,
                null,
                null,
                null
        );

        cursor.moveToFirst();
        word = cursor.getString(1);
        translation = cursor.getString(2);
        tag_1 = cursor.getString(3);
        tag_2 = cursor.getString(4);
        tag_3 = cursor.getString(5);
        tag_4 = cursor.getString(6);
        CoefAppr_Word = cursor.getInt(7);
        cursor.close();
    }

    // Recupere la date d'il ya X semaines
    public Date getDateBeforeXWeeks(Date date, int x) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -(x*7) ); // x weeks
        return calendar.getTime();
    }

    public Date getDateBeforeXDays(Date date, int x) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -(x) ); // x weeks
        return calendar.getTime();
    }

    public void generateStuff(){

        dateLimite = getDateBeforeXWeeks(dateNow, periodeSpinner);
        int nbRows;

        Cursor sessh = CDB.rawQuery("" +
                        " SELECT Id_Session, Date" +
                        " FROM t_Session " +
                        " WHERE Date<"+dateLimite.getTime()+" ORDER BY Date ASC"
                ,null);
        sessh.moveToFirst();
        while(!sessh.isAfterLast()){
            mapSession.put(sessh.getInt(0)+"", sessh.getString(1));
            sessh.moveToNext();
        }
        sessh.close();


        // Get stats concerning word
        Cursor stats = CDB.rawQuery("" +
                " SELECT *" +
                " FROM t_Stat " +
                " WHERE Id_Word="+key+
                " AND Id_Session IN "+
                " (SELECT Id_Session from t_Session WHERE Date<"+dateLimite.getTime()+" ORDER BY Date ASC)"
                ,null);

        nbRows = stats.getCount();
        id_session= new int[nbRows];
        Temps = new Double[nbRows];
        CoefAppr = new int[nbRows];
        Resultat = new int[nbRows];
        nbIndice = new int[nbRows];

        stats.moveToFirst();
        int i = 0;
        while(!stats.isAfterLast()){
            id_session[i] = stats.getInt(4);
            Temps[i] = stats.getDouble(1);
            CoefAppr[i] = stats.getInt(2);
            Resultat[i] = stats.getInt(3);
            nbIndice[i] = stats.getInt(6);
            i++;
            stats.moveToNext();
        }
        stats.close();

    }


    // Regrouper les stats en sessions et les sessions en groupes de sessions
    // exemple: deux sessions le meme jour
    public void separateSession(){

        Log.d("testtest","Day: "+getDateBeforeXDays(dateNow, 2));

        Map<String,String> sesshDays = new HashMap<>();

        for(String id_sess: mapSession.keySet()){




        }


    }


}