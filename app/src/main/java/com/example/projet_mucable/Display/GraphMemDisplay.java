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

    // Values of the word we want the stats of
    SQLiteDatabase CDB;
    int key;
    String word;
    String translation;
    String tag_1;
    String tag_2;
    String tag_3;
    String tag_4;
    int CoefAppr_Word ;


    // dictio id_session:date_session
    Map<String,String> mapSession = new HashMap<>();
    // Regrouped all session in a day interval
    String[] regroupedSessh;
    String[] regroupedStats;
    String[] sesshDates;


    // All stats gathered in the specified period
    //int[] id_session;
    Map<Integer, Integer> mapId_session = new HashMap<>();

    //Double[] temps;
    Map<Integer, Double> mapTemps = new HashMap<>();

    //int[] coefAppr;
    Map<Integer, Integer> mapCoefAppr = new HashMap<>();

    //int[] resultat;
    Map<Integer, Integer> mapResultat = new HashMap<>();

    //int[] nbIndice;
    Map<Integer, Integer> mapNbIndice = new HashMap<>();



    // We gather stats from session that are between now and the end date
    Date dateNow = new Date();
    Date dateLimite;
    // Number of weeks of the interval
    int periodeSpinner;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_mem_display);

        Intent i = getIntent();
        key = i.getIntExtra("Key", -1);
        CDB = openOrCreateDatabase("CDB.db", SQLiteDatabase.CREATE_IF_NECESSARY, null );
        getKeyRow();


        /////////////////////////////////////////////////////
        /////////////////// Manage spinner //////////////////
        final Spinner spinnerP = findViewById(R.id.spinnerPeriode);
        ArrayList<String> listPeriode = new ArrayList<>();
        listPeriode.add("2 semaines");
        listPeriode.add("4 semaines");
        listPeriode.add("6 semaines");
        listPeriode.add("8 semaines");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listPeriode);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerP.setAdapter(arrayAdapter);

        spinnerP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //Log.d("testtest", "pos: "+position);
                periodeSpinner = (position+1)*2;
                generateStuff();
                separateSession();
                separateStats();
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
                periodeSpinner = -1;
            }
        });
        spinnerP.setSelection(0);

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        final Spinner spinnerStatType = findViewById(R.id.spinnerStatType);
        ArrayList<String> listStatType = new ArrayList<>();
        listStatType.add("Temps de réponse");
        listStatType.add("X");
        listStatType.add("Y");
        listStatType.add("Z");
        ArrayAdapter<String> arrayAdapterType = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listStatType);
        arrayAdapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatType.setAdapter(arrayAdapterType);

        spinnerStatType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
                periodeSpinner = -1;
            }
        });
        spinnerStatType.setSelection(0);



        ///////////////////// end spinner ///////////////////
        /////////////////////////////////////////////////////


    }


    // Get infos about current word
    void getKeyRow() {

        if(key!=-1){
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

    }

    // Get date from X weeks
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
        //int nbRows;

        Cursor sessh = CDB.rawQuery("" +
                        " SELECT Id_Session, Date" +
                        " FROM t_Session " +
                        " WHERE Date>"+dateLimite.getTime()+" ORDER BY Date ASC"
                ,null);

        sessh.moveToFirst();
        while(!sessh.isAfterLast()){
            mapSession.put(sessh.getInt(0)+"", sessh.getString(1));
            sessh.moveToNext();
        }
        sessh.close();


        /*Log.d("testtest",  " SELECT *" +
                " FROM t_Stat " +
                " WHERE Id_Word="+key+
                " AND Id_Session IN "+
                " (SELECT Id_Session from t_Session WHERE Date>"+dateLimite.getTime()+" ORDER BY Date ASC)");*/

        Cursor stats;

        if(key!=-1){
            // Get stats concerning word
            stats = CDB.rawQuery("" +
                            " SELECT *" +
                            " FROM t_Stat " +
                            " WHERE Id_Word="+key+
                            " AND Id_Session IN "+
                            " (SELECT Id_Session from t_Session WHERE Date>"+dateLimite.getTime()+" ORDER BY Date ASC)"
                    ,null);
        }
        else{
            // Get stats concerning word
            stats = CDB.rawQuery("" +
                            " SELECT *" +
                            " FROM t_Stat " +
                            " WHERE Id_Session IN "+
                            " (SELECT Id_Session from t_Session WHERE Date>"+dateLimite.getTime()+" ORDER BY Date ASC)"
                    ,null);
        }



        //nbRows = stats.getCount();
        /*id_session= new int[nbRows];
        temps = new Double[nbRows];
        coefAppr = new int[nbRows];
        resultat = new int[nbRows];
        nbIndice = new int[nbRows];*/

        stats.moveToFirst();
        //int i = 0;
        while(!stats.isAfterLast()){
            Log.d("testtest", "allsessh: "+stats.getInt(4));

            mapId_session.put(stats.getInt(0), stats.getInt(4));
            //id_session[i] = stats.getInt(4);

            mapTemps.put(stats.getInt(0), stats.getDouble(1));
            //temps[i] = stats.getDouble(1);

            mapCoefAppr.put(stats.getInt(0), stats.getInt(2));
            //coefAppr[i] = stats.getInt(2);

            mapResultat.put(stats.getInt(0), stats.getInt(3));
            //resultat[i] = stats.getInt(3);

            mapNbIndice.put(stats.getInt(0), stats.getInt(6));
            //nbIndice[i] = stats.getInt(6);
            //i++;
            stats.moveToNext();
        }
        stats.close();

    }


    // Regroup stats in sessions and session in group of sessions
    // example: two session the same day
    public void separateSession(){

        int size = periodeSpinner*7;
        regroupedSessh = new String[size];
        sesshDates = new String[size];
        //Map<String,String> sesshDays = new HashMap<>();


        int i = 0;
        int j = 0;
        Date tmp = dateNow;

        for(String id_sess: mapSession.keySet()){

            Date current = new Date(Long.parseLong(mapSession.get(id_sess)));

            // if current session and previous session are the same day, regroup them
            if(j>0 && current.getTime()-tmp.getTime()<current.getTime()-getDateBeforeXDays(current, 1).getTime()){
                if(regroupedSessh[i]==null || regroupedSessh[i].equals("") || regroupedSessh[i].isEmpty()){
                    regroupedSessh[i]=id_sess;
                    //Log.d("testtest","if: "+regroupedSessh[i]);

                }
                else{
                    regroupedSessh[i]=regroupedSessh[i]+","+id_sess;
                    //Log.d("testtest","else: "+regroupedSessh[i]);

                }
            }
            else{
                if(j==0){
                    regroupedSessh[i]=id_sess;
                    sesshDates[i]=current.getTime()+"";
                    //Log.d("testtest","i0: "+regroupedSessh[i]);
                    j++;
                }
                else{
                    i++;
                }
                tmp = current;
                sesshDates[i]=current.getTime()+"";
            }
        }

    }

    public  void separateStats(){

        int i = 0;
        regroupedStats = new String[regroupedSessh.length];

        //Log.d("testtest",""+regroupedSessh[0]);

        while (regroupedSessh[i]!= null){

            // Link stats with grouped sessions
            // get all stats i's together
            //Log.d("testtest","Id_Word="+key+" AND Id_Session IN ('"+regroupedSessh[i].replaceAll(",","','")+"')");


            Cursor cursorStats;
            if(key!=-1){
                cursorStats = CDB.query(
                        "t_Stat",
                        null,
                        "Id_Word="+key+" AND Id_Session IN ('"+regroupedSessh[i].replaceAll(",","','")+"')",
                        null,
                        null,
                        null,
                        null,
                        null

                );
            }
            else{
                cursorStats = CDB.query(
                        "t_Stat",
                        null,
                        "Id_Session IN ('"+regroupedSessh[i].replaceAll(",","','")+"')",
                        null,
                        null,
                        null,
                        null,
                        null

                );
            }


            cursorStats.moveToFirst();
            while(!cursorStats.isAfterLast()){

                // Build stats in groups
                if(regroupedStats[i]==null || regroupedStats[i].equals("") || regroupedStats[i].isEmpty()){
                    regroupedStats[i]=cursorStats.getInt(0)+"";
                    //Log.d("testtest","if: "+regroupedSessh[i]);
                }
                else{
                    regroupedStats[i]=regroupedStats[i]+","+cursorStats.getInt(0);
                    //Log.d("testtest","else: "+regroupedSessh[i]);
                }

                cursorStats.moveToNext()
;            }
            cursorStats.close();
            i++;
        }
        //Log.d("testtest",""+regroupedStats[0]);
    }


    // Returns the average time used to answer separated whether the answer was correct or not
    public Double[][] averageTimeAnswer(){

        int size = regroupedSessh.length;
        Double[][] fused = new Double[3][size];

        // Run through every session 'day'
        for(int i = 0; i<size; i++){

            Double tpsGood = 0.0;
            Double tpsBad = 0.0;
            int nbGood = 0;
            int nbBad = 0;

            // Get stats concerned
            for( String s : regroupedStats[i].split(",")){
                // If answer is 100% (correct)
                if((mapResultat.get(Integer.parseInt(s)))==100){
                    tpsGood += mapTemps.get(Integer.parseInt(s));
                    nbGood++;
                }
                else{
                    tpsBad += mapTemps.get(Integer.parseInt(s));
                    nbBad++;
                }
            }
            for(int j = i; j<size; j++){
                fused[1][j] = tpsGood/nbGood;
                fused[0][j] = tpsBad/nbBad;
                fused[2][j] = (tpsBad+tpsGood)/(nbBad+nbGood);
            }

        }
        return fused;
    }


    // returns the average number of hints asked by the user
    public int[][] averageNbIndice(){

        int size = regroupedSessh.length;
        int[][] fused = new int[3][size];

        // Run through every session 'day'
        for(int i = 0; i<size; i++){

            int nbWhenGood = 0;
            int nbWhenBad = 0;

            // Get stats concerned
            for( String s : regroupedStats[i].split(",")){
                // If answer is 100% (correct)
                if((mapResultat.get(Integer.parseInt(s)))==100){
                    nbWhenGood += mapNbIndice.get(Integer.parseInt(s));

                }
                else{
                    nbWhenBad += mapNbIndice.get(Integer.parseInt(s));

                }
            }
            for(int j = i; j<size; j++){
                fused[1][j] = nbWhenGood;
                fused[0][j] = nbWhenBad;
                fused[2][j] = nbWhenGood+nbWhenBad;
            }

        }
        return fused;
    }


    // average accuracy of wrong answer
    public int[] precisionReponse(){

        int size = regroupedSessh.length;
        int[] percents = new int[size];

        // Run through every session 'day'
        for(int i = 0; i<size; i++){

            int percen = 0;
            int count = 0;

            // Get stats concerned
            for( String s : regroupedStats[i].split(",")){
                // If answer is 100% (correct)
                if((mapResultat.get(Integer.parseInt(s)))!=100){
                    percen+=mapResultat.get(Integer.parseInt(s));
                    count++;
                }
            }

            percen = percen/count;
            percents[i] = percen;

        }

        return percents;
    }


    public void dosomethingelse(){





    }


}