package com.example.projet_mucable.Display;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projet_mucable.CustomAdapter;
import com.example.projet_mucable.DicoSeri;
import com.example.projet_mucable.R;
import com.example.projet_mucable.ResultatsAdapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ResultatRevisionDisplay extends AppCompatActivity {

    ArrayList list_msgs = new ArrayList();
    DicoSeri monDico = new DicoSeri();
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
        monDico = (DicoSeri)this_i.getSerializableExtra("Dico");
        String test_res = this_i.getStringExtra("String_res");
        String[] list_test_res = test_res.split(";");
        int size = list_test_res.length;
        int tot = 0;
        int goodRep = 0;

        String[] question_list = new String[size];
        String[] rep_list = new String[size];
        String[] attendu_list = new String[size];
        String[] mark_list = new String[size];

        /*String[] list_test_res = test_res.split(";");
        for(String line : list_test_res){
            list_msgs.add(line);
            if(line.charAt(line.length()-10)=='n'){
                goodRep+=1;
            }
            tot+=1;
        }*/

        // Counting t/f answers to order words in cahier
        Map<String,String> adjustWords =  new HashMap<String,String>();

        for(String line : list_test_res){

            String[] values = list_test_res[tot].split("°");

            question_list[tot] = values[0];
            rep_list[tot] = values[1];
            attendu_list[tot] = values[2];
            mark_list[tot] = values[3];

            if(adjustWords.get(values[0])==null){
                if(values[3].charAt(0)=='✓'){
                    adjustWords.put(values[0]+"_"+values[2],"y");
                }
                else{
                    adjustWords.put(values[0]+"_"+values[2],"n");
                }
            }
            else{
                if(values[3].charAt(0)!='✓'){
                    adjustWords.put(values[0]+"_"+values[2],"n");
                }
            }

            if(values[3].charAt(0)=='✓'){
                goodRep+=1;
            }
            tot+=1;
        }


        // textViewLangue
        TextView tLangue = (TextView) findViewById(R.id.textViewLangue);
        tLangue.setText("Révision en "+language);

        // textViewLangue
        TextView tScore = (TextView) findViewById(R.id.textViewScore);
        tScore.setText("Score : "+goodRep+"/"+tot);

        // textViewTag
        TextView tTag = (TextView) findViewById(R.id.textViewTags);
        if(tagsFilter==null || tagsFilter.isEmpty()){
            tagsFilter = "...";
        }
        tTag.setText("Tag : "+tagsFilter);

        //for(String key : ((HashMap<String,String>)monDico.getMap()).keySet()){
        //    list_msgs.add(/*key+"\n- "+*/monDico.get(key));
        //}

        /*
        ListView listView = (ListView) findViewById(R.id.words_listview_res);
        ArrayAdapter itemsAdapter = new ArrayAdapter<String>(this, R.layout.resultats_listview, list_msgs);
        listView.setAdapter(itemsAdapter);
        */

        ListView listView = (ListView) findViewById(R.id.words_listview_res);
        ResultatsAdapter itemsAdapter = new ResultatsAdapter(this, question_list, rep_list, attendu_list, mark_list);
        listView.setAdapter(itemsAdapter);


        // Access DB and change Coef value of words based on the fact that the user got them right
        @SuppressLint("WrongConstant") SQLiteDatabase CDB = openOrCreateDatabase("CDB.db", SQLiteDatabase.CREATE_IF_NECESSARY, null );

        /*
        * Cursor cursor = CDB.query(
                    "t_" + language,
                    new String[]{"key.split('*')[0]", "key.split('*')[1]"},
                    "Id_Word=" + key,
                    null,
                    null,
                    null,
                    null
            );
            cursor.moveToFirst();
            information_values = (cursor.getString(0) + ";" + cursor.getString(1) + ";" + cursor.getString(2) + ";" + cursor.getString(3) + ";" + cursor.getString(4) + ";" + cursor.getString(5)).split(";");
        * */

        String wordTmp;
        String translaTmp;
        int newcoef;

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
                    "t_" + language,
                    null,
                    selecTmp+"",
                    null,
                    null,
                    null,
                    null
            );
            cursor.moveToFirst();
            String[] information_values = (cursor.getString(0) + ";" + cursor.getString(7)).split(";");


            // If the user didn't get the answer wrong
            if(adjustWords.get(key)=="y"){
                // increase coef (max 3)
                newcoef = Integer.parseInt(information_values[1])+1;
                if(newcoef>3){
                    newcoef=3;
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
            CDB.update("t_"+language, cv, "Id_word="+information_values[0], null);

        }


    }
}

