package com.example.projet_mucable.Display;

import androidx.appcompat.app.AppCompatActivity;
import com.example.projet_mucable.R;
import com.example.projet_mucable.ResultatsAdapter;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ResultatRevisionDisplay extends AppCompatActivity {

    //ArrayList list_msgs = new ArrayList();
    //DicoSeri monDico = new DicoSeri();
    String language;
    String tagsFilter = "...";
    Boolean sens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultat_revision_display);


        Intent this_i = getIntent();
        language = this_i.getStringExtra("Langue");
        sens = this_i.getBooleanExtra("Sens", true);
        tagsFilter = this_i.getStringExtra("TagsFilter");
        String test_res = this_i.getStringExtra("String_res");
        String[] list_test_res = test_res.split(";");

        String XX = "#";

        int size = list_test_res.length;
        int tot = 0;
        double tpsTot = 0;
        int goodRep = 0;
        Date date = new Date();
        Date date2sem;
        long id_session;

        String[] question_list = new String[size];
        String[] rep_list = new String[size];
        String[] attendu_list = new String[size];
        String[] mark_list = new String[size];
        //String[] requests = new String[size];

        String wordTmp;
        String translaTmp;
        int newcoef;
        int i = 0;


        // Access DB and change Coef value of words based on the fact that the user got them right
        @SuppressLint("WrongConstant") SQLiteDatabase CDB = openOrCreateDatabase("CDB.db", SQLiteDatabase.CREATE_IF_NECESSARY, null );
        CDB.execSQL("PRAGMA foreign_keys = ON;");

        // textViewLangue
        TextView tLangue = findViewById(R.id.textViewLangue);
        tLangue.setText("Révision en "+language);

        // textViewTag
        TextView tTag = findViewById(R.id.textViewTags);
        if(tagsFilter==null || tagsFilter.isEmpty()){
            tagsFilter = "...";
        }
        tTag.setText("Tag : "+tagsFilter);

        for(String line : list_test_res){
            if(line.split(XX)[3].charAt(0)=='✓'){
                goodRep+=1;
            }
            tpsTot += Double.parseDouble(line.split(XX)[4]);
            tot+=1;
        }

        // Insert new session
        //String newSession = "INSERT INTO t_Session (date, score, temps) VALUES ('"+date.getTime()+"','"+(goodRep/tot)*20.0+"','"+tpsTot+"');";
        ContentValues valuesSess = new ContentValues();
        valuesSess.put("Date",date.getTime());
        valuesSess.put("Score",(goodRep/tot)*20.0);
        valuesSess.put("Temps",tpsTot);
        //The insert method returns the id of row just inserted or -1 if there was an error during insertion.
        id_session = CDB.insert("t_Session","",valuesSess);


        // Get 2-week-prior date
        date2sem = getDateBeforeTwoWeeks(date);


        // Counting t/f answers to order words in cahier
        Map<String,String> adjustWords = new HashMap<>();

        for(String line : list_test_res){
            String[] values = line.split(XX);

            question_list[i] = values[0];
            rep_list[i] = values[1];
            attendu_list[i] = values[2];
            mark_list[i] = values[3];
            double tps = Double.parseDouble(values[4]);
            double percen = Double.parseDouble(values[5]);
            int nbIndice = Integer.parseInt(values[6]);

            String que = values[0].replaceAll("'","''");
            //String rep = values[1];
            String attendu = values[2].replaceAll("'","''");
            String mark = values[3];


            if(sens){
                wordTmp = que;
                translaTmp = attendu;
            }
            else{
                wordTmp = attendu;
                translaTmp = que;
            }
            String selecTmp = "Word='"+wordTmp+"' AND Translation='"+translaTmp+"'";

            Cursor cursor = CDB.query(
                    /*"t_" + language,*/"t_Mot",
                    null,
                    "Langue LIKE '"+language+"' AND "+selecTmp+"",
                    null,
                    null,
                    null,
                    null
            );
            cursor.moveToFirst();
            String[] inf_val = (cursor.getString(0) + ";" + cursor.getString(7)).split(";");
            cursor.close();

            //requests[i] = "INSERT INTO t_Stat (Temps, CoefAppr, Resultat, Id_Session, Id_Word) VALUES ('"+tps+"','"+inf_val[1]+"','"+percen+"','"+id_session+"','"+inf_val[0]+"');";
            ContentValues valuesStat = new ContentValues();
            valuesStat.put("Temps",tps);
            valuesStat.put("CoefAppr",inf_val[1]);
            valuesStat.put("Resultat",percen);
            valuesStat.put("Id_Session",id_session);
            valuesStat.put("Id_Word",inf_val[0]);
            valuesStat.put("NbIndice", nbIndice);
            //The insert method returns the id of row just inserted or -1 if there was an error during insertion.
            CDB.insert("t_Stat","",valuesStat);

            i+=1;

            if(adjustWords.get(que)==null){
                if(mark.charAt(0)=='✓'){
                    adjustWords.put(que+"_"+attendu,"y");
                }
                else{
                    adjustWords.put(que+"_"+attendu,"n");
                }
            }
            else{
                if(mark.charAt(0)!='✓'){
                    adjustWords.put(que+"_"+attendu,"n");
                }
            }

            /*if(mark.charAt(0)=='✓'){
                goodRep+=1;
            }
            tot+=1;*/

        }


        // textViewScore
        TextView tScore = findViewById(R.id.textViewScore);
        tScore.setText("Score : "+goodRep+"/"+tot);

        ListView listView = findViewById(R.id.words_listview_res);
        ResultatsAdapter itemsAdapter = new ResultatsAdapter(this, question_list, rep_list, attendu_list, mark_list);
        listView.setAdapter(itemsAdapter);


        for (String key : adjustWords.keySet()) {
            ContentValues cv = new ContentValues();

            if(sens){
                wordTmp = key.split("_")[0];
                translaTmp = key.split("_")[1];
            }
            else{
                wordTmp = key.split("_")[1];
                translaTmp = key.split("_")[0];
            }
            String selecTmp = "Word='"+wordTmp+"' AND Translation='"+translaTmp+"'";

            Cursor cursor = CDB.query(
                    "t_Mot",
                    null,
                    "Langue LIKE '"+language+"' AND "+selecTmp+"",
                    null,
                    null,
                    null,
                    null
            );
            cursor.moveToFirst();
            String[] information_values = (cursor.getString(0) + ";" + cursor.getString(7)).split(";");
            cursor.close();


            // If the user didn't get the answer wrong
            if(adjustWords.get(key).equals("y")){
                // increase coef (max 3)
                newcoef = Integer.parseInt(information_values[1])+1;
                if(newcoef>5){
                    newcoef=5;
                }
                cv.put("CoefAppr",newcoef);
            }
            else{
                // decrease coef (min 0)
                newcoef = Integer.parseInt(information_values[1])-1;
                if(newcoef<0){
                    newcoef=0;
                }
                cv.put("CoefAppr",newcoef);
            }
            //CDB.update("t_"+language, cv, "Id_word="+information_values[0], null);
            CDB.update("t_Mot", cv, "Langue LIKE '"+language+"' AND Id_word="+information_values[0], null);


        }

        // Recupérer la derniere stats de chaques mots
        // si session de stat du mot < sessionActu-4
        //      => coefAppr
        final Cursor cursorStats = CDB.rawQuery("" +
                " SELECT st.*" +
                " FROM t_Stat st" +
                " INNER JOIN" +
                "    (SELECT Id_Word, MAX(Id_Session) AS MaxSession" +
                "    FROM t_Stat" +
                "    GROUP BY Id_Word) groupedst " +
                " ON st.Id_Word = groupedst.Id_Word " +
                " AND st.Id_Session = groupedst.MaxSession", null);

        ContentValues cvStat = new ContentValues();

        cursorStats.moveToFirst();
        while(!cursorStats.isAfterLast()){

            final Cursor cursorSess = CDB.rawQuery("SELECT Date FROM t_Session WHERE Id_session="+cursorStats.getInt(4),null);
            cursorSess.moveToFirst();
            int dateFormerSession = cursorSess.getInt(0);
            cursorSess.close();

            //Log.d("testtest","session mot:"+cursorStats.getInt(4)+"  Last session:"+id_session +"   coef:"+cursorStats.getInt(2)+"   id:"+cursorStats.getInt(5));

            if( (cursorStats.getInt(4)<=id_session-3  || dateFormerSession<date2sem.getTime()) && cursorStats.getInt(2)==5){
                cvStat.put("CoefAppr",4);
                CDB.update("t_Mot", cvStat, "Langue LIKE '"+language+"' AND Id_word="+cursorStats.getInt(5), null);

            }
            cursorStats.moveToNext();
        }
        cursorStats.close();

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        /*Cursor c1 = CDB.query(
                "t_Stat",
                null,
                null,
                null,
                null,
                null,
                null
        );
        c1.moveToFirst();
        while(!c1.isAfterLast()){
            Log.d("TABLES69420", " STATS :"+c1.getInt(4)+" - "+c1.getInt(5)+" - "+c1.getDouble(1)+" - "+c1.getDouble(2)+" - "+c1.getDouble(3));
            c1.moveToNext();
        }

        Cursor c2 = CDB.query(
                "t_Session",
                null,
                null,
                null,
                null,
                null,
                null
        );
        c2.moveToFirst();
        while(!c2.isAfterLast()){
            Log.d("TABLES69420", " SESSIONS :"+c2.getInt(0)+" - "+c2.getInt(1)+" - "+c2.getInt(2)+" - "+c2.getDouble(3));
            c2.moveToNext();
        }

        Log.d("TABLES69420", "---------------------------------------------- ");*/

    }

    public Date getDateBeforeTwoWeeks(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -14); //2 weeks
        return calendar.getTime();
    }
}