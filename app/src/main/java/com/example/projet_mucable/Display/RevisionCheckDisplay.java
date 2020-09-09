package com.example.projet_mucable.Display;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projet_mucable.CustomAdapter;
import com.example.projet_mucable.DicoSeri;
import com.example.projet_mucable.StringEqualityPercentCheckLevenshteinDistance;
import com.example.projet_mucable.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
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
    int word_num;
    boolean type;
    String tagsFilter;

    String question;
    String reponseUser;
    String reponse;

    ArrayList list_msgs = new ArrayList();
    DicoSeri monDico = new DicoSeri();
    String test_res;
    Integer[] indTab;

    SQLiteDatabase CDB;


    @SuppressLint("WrongConstant")
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
        tagsFilter = this_i.getStringExtra("TagsFilter");
        monDico = (DicoSeri)this_i.getSerializableExtra("Dico");

        indTab = new Integer[nb_left];
        ArrayList<Integer> intList = this_i.getIntegerArrayListExtra("IndTab");
        indTab = intList.toArray(new Integer[0]);



        word_num = this_i.getIntExtra("Word_number", 0);

        test_res = this_i.getStringExtra("String_res");

        question = this_i.getStringExtra("Question");
        reponseUser = this_i.getStringExtra("ReponseUser");
        reponse = this_i.getStringExtra("Reponse");

        if(reponseUser.equals(""))
            reponseUser=" ";


        TextView tVF = (TextView) findViewById(R.id.textViewVF);
        TextView tMP = (TextView) findViewById(R.id.textViewMotP);
        TextView tRU = (TextView) findViewById(R.id.textViewRU);

        tMP.setText("Le mot à traduire : "+question);
        tRU.setText("Votre traduction  : "+reponseUser);


        // Vérifie les réponses
        if(reponse.equals(reponseUser)){
            tVF.setText("Bonne réponse !");
            list_msgs.add("Parfait");
            monDico.put(reponse,question+" : "+reponseUser+"("+reponse+") Bonne réponse");
            test_res = test_res+question+"°"+reponseUser+"°"+reponse+"°✓;";
        }
        else{

            CDB = openOrCreateDatabase("CDB.db", SQLiteDatabase.CREATE_IF_NECESSARY, null );
            String SQL_EXI = "SELECT COUNT(*) FROM t_"+language+" WHERE Word='"+question+"' AND Translation='"+reponseUser+"'";

            Cursor mCount= CDB.rawQuery(SQL_EXI, null);
            mCount.moveToFirst();
            int nbExi = mCount.getInt(0);

            if(nbExi>0){
                tVF.setText("Bonne réponse !");
                list_msgs.add("Parfait");
                monDico.put(reponse,question+" : "+reponseUser+"("+reponse+") Bonne réponse");
                test_res = test_res+question+"°"+reponseUser+"°"+reponse+"°✓;";
            }
            else{

                tVF.setText("Mauvaise réponse !");
                monDico.put(reponse,question+" : "+reponseUser+"("+reponse+") Mauvaise réponse");
                test_res = test_res+question+"°"+reponseUser+"°"+reponse+"°✗;";

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

        }

        //monDico.put(question, String.join(", ", list_msgs));

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

        i.putExtra("Word_number", word_num+1);
        i.putExtra("Nb_mots", nb_left);
        i.putExtra("Sens", sens);
        i.putExtra("Langue", language);
        i.putExtra("Type", type);
        i.putExtra("TagsFilter", tagsFilter);

        i.putExtra("Dico", monDico);
        i.putExtra("String_res", test_res);

        ArrayList<Integer> intList = new ArrayList<Integer>(50);
        for (int k : indTab) intList.add(k);
        i.putIntegerArrayListExtra("IndTab",intList);


        if(nb_left>0){
            startActivity( i );
        }
        else{
            i = new Intent ( this, ResultatRevisionDisplay.class );
            i.putExtra("Dico", monDico);
            i.putExtra("String_res", test_res);
            i.putExtra("Sens", sens);
            i.putExtra("Langue", language);
            i.putExtra("TagsFilter", tagsFilter);
            startActivity( i );
        }
        finish();

    }
}