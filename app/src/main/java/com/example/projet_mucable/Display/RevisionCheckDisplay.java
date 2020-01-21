package com.example.projet_mucable.Display;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projet_mucable.CustomAdapter;
import com.example.projet_mucable.DicoSeri;
import com.example.projet_mucable.StringEqualityPercentCheckLevenshteinDistance;
import com.example.projet_mucable.R;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;



public class RevisionCheckDisplay extends AppCompatActivity {

    String language;
    Boolean sens;
    int nb_left;
    int taille_bd;
    boolean type;

    String question;
    String reponseUser;
    String reponse;

    ArrayList list_msgs = new ArrayList();
    DicoSeri monDico = new DicoSeri();


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revision_check_display);

        Intent this_i = getIntent();
        nb_left = this_i.getIntExtra("Nb_mots", 5);
        taille_bd = this_i.getIntExtra("Taille_bd", 5);
        language = this_i.getStringExtra("Langue");
        sens = this_i.getBooleanExtra("Sens", true);
        type = this_i.getBooleanExtra("Type", false);
        monDico = (DicoSeri)this_i.getSerializableExtra("Dico");


        question = this_i.getStringExtra("Question");
        reponseUser = this_i.getStringExtra("ReponseUser");
        reponse = this_i.getStringExtra("Reponse");

        monDico.put("Test1","Test2");

        TextView tVF = (TextView) findViewById(R.id.textViewVF);
        TextView tMP = (TextView) findViewById(R.id.textViewMotP);
        TextView tRU = (TextView) findViewById(R.id.textViewRU);

        tMP.setText("Le mot à traduire : "+question);
        tRU.setText("Votre traduction  : "+reponseUser);

        // Vérifie les réponses
        if(reponse.equals(reponseUser)){
            tVF.setText("Bonne réponse !");
            list_msgs.add("Parfait");
        }
        else{
            tVF.setText("Mauvaise réponse !");

            if(type){

                // Check taille réponse
                if(reponse.length() != reponseUser.length()){

                    if(reponse.length() > reponseUser.length()){
                        list_msgs.add("Réponse trop courte");
                    }
                    else{
                        list_msgs.add("Réponse trop longue");
                    }
                }
                // Check majuscules
                if(reponse.toLowerCase().equals(reponseUser.toLowerCase())){
                    list_msgs.add("Majuscules ?");
                }

                StringEqualityPercentCheckLevenshteinDistance howClose = new StringEqualityPercentCheckLevenshteinDistance();
                double percen = howClose.similarity(reponseUser, reponse)*100;
                //percen = new Double(new DecimalFormat(".##").format(percen));

                list_msgs.add("Votre réponse est à "+new DecimalFormat("##").format(percen)+"% correcte");
            }

        }

        monDico.put(question, String.join(", ", list_msgs));

        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_msgs);
        ListView listView = (ListView) findViewById(R.id.listViewMessages);
        listView.setAdapter(itemsAdapter);

        //Toast.makeText(getApplicationContext(),"TEST: "+nb_left+" !",Toast.LENGTH_LONG).show();
    }

    public void goToNext(View view) {

        final int random = new Random().nextInt(taille_bd);
        Intent i;

        if(type){
            i = new Intent ( this, ParCoeurActivity.class );

        }
        else{
            i = new Intent ( this, ParChoixActivity.class );
        }

        i.putExtra("Word_number", random);
        i.putExtra("Nb_mots", nb_left);
        i.putExtra("Sens", sens);
        i.putExtra("Langue", language);
        i.putExtra("Type", type);

        i.putExtra("Dico", monDico);

        if(nb_left>0){
            startActivity( i );
        }
        finish();

    }
}