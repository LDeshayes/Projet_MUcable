package com.example.projet_mucable.Display;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projet_mucable.DicoSeri;
import com.example.projet_mucable.R;

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

public class ResultatRevisionDisplay extends Activity {

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
        int tot = 0;
        int goodRep = 0;

        String[] list_test_res = test_res.split(";");
        for(String line : list_test_res){
            list_msgs.add(line);
            if(line.charAt(line.length()-10)=='n'){
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

        Toast.makeText(getApplicationContext(),"TEST: "+tagsFilter,Toast.LENGTH_LONG).show();

        // textViewTag
        TextView tTag = (TextView) findViewById(R.id.textViewTags);
        if(tagsFilter==null || tagsFilter.isEmpty()){
            tagsFilter = "...";
        }
        tTag.setText("Tag : "+tagsFilter);

        //for(String key : ((HashMap<String,String>)monDico.getMap()).keySet()){
        //    list_msgs.add(/*key+"\n- "+*/monDico.get(key));
        //}


        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_msgs);
        ListView listView = (ListView) findViewById(R.id.words_listview_res);
        listView.setAdapter(itemsAdapter);


    }
}