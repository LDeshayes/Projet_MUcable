package com.example.projet_mucable.Display;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projet_mucable.DicoSeri;
import com.example.projet_mucable.R;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class ResultatRevisionDisplay extends AppCompatActivity {

    ArrayList list_msgs = new ArrayList();
    DicoSeri monDico = new DicoSeri();
    String language;
    Boolean sens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultat_revision_display);

        Intent this_i = getIntent();
        language = this_i.getStringExtra("Langue");
        sens = this_i.getBooleanExtra("Sens", true);
        monDico = (DicoSeri)this_i.getSerializableExtra("Dico");

        // textViewLangue

        TextView tLangue = (TextView) findViewById(R.id.textViewLangue);
        tLangue.setText("Révision en "+language);

        for(String key : ((HashMap<String,String>)monDico.getMap()).keySet()){
            list_msgs.add(/*key+"\n- "+*/monDico.get(key));
        }


        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_msgs);
        ListView listView = (ListView) findViewById(R.id.words_listview_res);
        listView.setAdapter(itemsAdapter);


    }
}