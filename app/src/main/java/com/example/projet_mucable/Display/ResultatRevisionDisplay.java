package com.example.projet_mucable.Display;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projet_mucable.CustomAdapter;
import com.example.projet_mucable.DicoSeri;
import com.example.projet_mucable.R;
import com.example.projet_mucable.ResultatsAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
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

        for(String line : list_test_res){

            String[] values = list_test_res[tot].split("°");

            question_list[tot] = values[0];
            rep_list[tot] = values[1];
            attendu_list[tot] = values[2];
            mark_list[tot] = values[3];


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





    }
}