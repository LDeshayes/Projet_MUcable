package com.example.projet_mucable.Display;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projet_mucable.DicoSeri;
import com.example.projet_mucable.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ParChoixActivity extends AppCompatActivity {


    String language;
    String repo;
    Boolean sens;
    //ListView words_listview;
    int[] key_list;
    String[] words_list;// = { "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "lo" };
    String[] translations_list;// = { "Un", "Deux", "Trois", "Quatre", "Cinq", "Six", "Sept", "Huit", "Neuf", "Dix", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "lo" };
    String[] tags_list;// = { "Nombre", "Nombre", "Nombre", "Nombre", "Nombre", "Nombre", "Nombre", "Nombre", "Nombre", "Nombre", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "lo" };
    String tagsFilter;

    SQLiteDatabase CDB;

    //DicoSeri monDico = new DicoSeri();
    String test_res = "";

    String word;
    String a1, a2, a3, a4;
    String word_translation;
    int word_number;
    int nb_left;
    int taille_bd;
    Integer[] indTab;

    int nbCoef0;
    int nbCoef1;
    int nbCoef2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_par_choix);

        Intent this_i = getIntent();
        tagsFilter = this_i.getStringExtra("TagsFilter");

        getLanguage();
        setupDB();

        taille_bd = words_list.length;
        nb_left = this_i.getIntExtra("Nb_mots", 5);

        word_number = this_i.getIntExtra("Word_number", 0);
        indTab = new Integer[nb_left];


        // if it's not the first time we go in revisionParCoeur
        if(this_i.getBooleanExtra("Not_First", true)){

            ArrayList<Integer> intList = this_i.getIntegerArrayListExtra("IndTab");
            indTab = intList.toArray(new Integer[0]);

            //monDico = (DicoSeri)this_i.getSerializableExtra("Dico");
            test_res = this_i.getStringExtra("String_res");
        }
        else{

            boolean testEq;

            if(taille_bd>=nb_left){

                for(int i=0; i<nb_left; i++){
                    indTab[i] = 0;
                }


                int i;
                int j;
                int resume;

                ///////////////////////////////////////////////////////// Coef0 /////////////////////////////////////////////////////////////////

                if(nbCoef0>(nb_left*0.6)){
                // Use 60% coef0
                    ArrayList<Integer> list = new ArrayList<Integer>();
                    for (j=0; j<nbCoef0; j++) {
                        list.add(j);
                    }
                    Collections.shuffle(list);
                    for (i=0; i<(int)(nb_left*0.6)-1 && (nb_left*0.4<(nbCoef2+nbCoef1)) || i<nb_left-(nbCoef1+nbCoef2) && (nb_left*0.4>=nbCoef2+nbCoef1); i++) {
                        indTab[i]=list.get(i);
                    }
                }
                else{
                // Use all coef 0
                    ArrayList<Integer> list = new ArrayList<Integer>();
                    for (i=0; i<nbCoef0; i++) {
                        indTab[i]=i;
                    }

                }

                resume = i;

                ///////////////////////////////////////////////////////// Coef1 /////////////////////////////////////////////////////////////////

                if(nbCoef1<(nb_left-resume)/2){
                // Use all coef1 possible
                    ArrayList<Integer> list = new ArrayList<Integer>();
                    for (i=resume; i<resume+nbCoef1; i++) {
                        indTab[i]=i;
                    }
                }
                else{
                // Use rdm amount of Coef1 and Coef2
                    ArrayList<Integer> list = new ArrayList<Integer>();
                    for (j=nbCoef0-1; j<nbCoef0+nbCoef1; j++) {
                        list.add(i);
                    }
                    Collections.shuffle(list);
                    for (i=resume, j=0; i<resume+(nb_left-resume)/2 && nbCoef2>(nb_left-resume)/2 || i<(nb_left-nbCoef2) && nbCoef2<=(nb_left-resume)/2; i++, j++) {
                        indTab[i]=list.get(j);
                    }
                }

                resume = i;

                ///////////////////////////////////////////////////////// Rest in Coef2 /////////////////////////////////////////////////////////
                ArrayList<Integer> list = new ArrayList<Integer>();
                for (j=nbCoef1-1; j<nbCoef1+nbCoef2; j++) {
                    list.add(i);
                }
                Collections.shuffle(list);
                for (i=resume, j=0; i<nb_left; i++, j++) {
                    indTab[i]=list.get(j);
                }

                List<Integer> intList = Arrays.asList(indTab);
                //indTab = new Integer[nb_left];
                Collections.shuffle(intList);
                intList.toArray(indTab);


            }
            else{

                for(int i=0; i<nb_left; i++){
                    if(i<taille_bd){
                        indTab[i] = i;
                    }
                    else {
                        indTab[i] = i%taille_bd;
                    }
                }

                List<Integer> intList = Arrays.asList(indTab);
                Collections.shuffle(intList);
                intList.toArray(indTab);

            }

        }

        Integer[] iAnswers = {word_number, 0, 0, 0};

        /*while (iAnswers[0]==iAnswers[1] || iAnswers[0]==iAnswers[2] || iAnswers[0]==iAnswers[3] || iAnswers[1]==iAnswers[2] || iAnswers[1]==iAnswers[3] || iAnswers[2]==iAnswers[3]){
            for(int i=1; i<4; i++){
                iAnswers[i] = (new Random()).nextInt(words_list.length);
            }
        }*/

        ArrayList<Integer> listAns = new ArrayList<Integer>();
        for (int k=0; k<words_list.length; k++) {
            if(k!=word_number)
                listAns.add(k);
        }
        Collections.shuffle(listAns);
        for(int l=1; l<4; l++) {
            iAnswers[l]=listAns.get(l-1);
        }

        List<Integer> listAnswers = Arrays.asList(iAnswers);
        Collections.shuffle(listAnswers);
        listAnswers.toArray(iAnswers);

        sens = this_i.getBooleanExtra("Sens", true);
        if(sens){
            word = words_list[indTab[word_number]];
            word_translation = translations_list[indTab[word_number]];
            a1 = translations_list[iAnswers[0]];
            a2 = translations_list[iAnswers[1]];
            a3 = translations_list[iAnswers[2]];
            a4 = translations_list[iAnswers[3]];
        }
        else{
            word_translation = words_list[indTab[word_number]];
            word = translations_list[indTab[word_number]];
            a1 = words_list[iAnswers[0]];
            a2 = words_list[iAnswers[1]];
            a3 = words_list[iAnswers[2]];
            a4 = words_list[iAnswers[3]];
        }

        TextView t = (TextView) findViewById(R.id.textViewQuestion);
        t.setText(word);

        final Button tq1 =  findViewById(R.id.buttonQ1);
        final Button tq2 =  findViewById(R.id.buttonQ2);
        final Button tq3 =  findViewById(R.id.buttonQ3);
        final Button tq4 =  findViewById(R.id.buttonQ4);
        final Button bVal =  findViewById(R.id.buttonValider);
        bVal.setEnabled(false);

        tq1.setText(a1);
        tq1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                repo = a1;
                //tq1.setEnabled(false);
                tq1.setTextColor(Color.BLACK);
                tq2.setTextColor(Color.WHITE);
                tq3.setTextColor(Color.WHITE);
                tq4.setTextColor(Color.WHITE);

                bVal.setEnabled(true);
            }
        });

        tq2.setText(a2);
        tq2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                repo = a2;
                tq1.setTextColor(Color.WHITE);
                tq2.setTextColor(Color.BLACK);
                tq3.setTextColor(Color.WHITE);
                tq4.setTextColor(Color.WHITE);
                bVal.setEnabled(true);
            }
        });

        tq3.setText(a3);
        tq3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                repo = a3;
                tq1.setTextColor(Color.WHITE);
                tq2.setTextColor(Color.WHITE);
                tq3.setTextColor(Color.BLACK);
                tq4.setTextColor(Color.WHITE);
                bVal.setEnabled(true);
            }
        });


        tq4.setText(a4);
        tq4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                repo = a4;
                tq1.setTextColor(Color.WHITE);
                tq2.setTextColor(Color.WHITE);
                tq3.setTextColor(Color.WHITE);
                tq4.setTextColor(Color.BLACK);
                bVal.setEnabled(true);
            }
        });

    }

    void setupDB() {
        getDB();
        loadDB();
    }

    void getLanguage() {
        Intent i = getIntent();
        language = "Anglais";
        language = i.getStringExtra("Langue");
    }

    @SuppressLint("WrongConstant")
    void getDB () {
        CDB = openOrCreateDatabase("CDB.db", SQLiteDatabase.CREATE_IF_NECESSARY, null );
    }

    void loadDB() {

        Cursor cursor;
        final String SQL_COEF0;
        final String SQL_COEF1;
        final String SQL_COEF2;

        if(tagsFilter!=null && !tagsFilter.isEmpty()){
            cursor = CDB.query(
                    /*"t_"+language,*/"t_Mot",
                    null,
                    "Langue LIKE '"+language+"' AND Tag_1 IN ("+tagsFilter+") OR Tag_2 IN ("+tagsFilter+") OR Tag_3 IN ("+tagsFilter+") OR Tag_4 IN ("+tagsFilter+") AND CoefAppr IN (0,1,2,3,4)",
                    null,
                    null,
                    null,
                    "CoefAppr ASC"
            );
            SQL_COEF0 = "SELECT COUNT(*) FROM t_Mot WHERE Langue LIKE '"+language+"' AND CoefAppr IN (0,1,2) AND Tag_1 IN ("+tagsFilter+") OR Tag_2 IN ("+tagsFilter+") OR Tag_3 IN ("+tagsFilter+") OR Tag_4 IN ("+tagsFilter+")";
            SQL_COEF1 = "SELECT COUNT(*) FROM t_Mot WHERE Langue LIKE '"+language+"' AND CoefAppr=3 AND Tag_1 IN ("+tagsFilter+") OR Tag_2 IN ("+tagsFilter+") OR Tag_3 IN ("+tagsFilter+") OR Tag_4 IN ("+tagsFilter+")";
            SQL_COEF2 = "SELECT COUNT(*) FROM t_Mot WHERE Langue LIKE '"+language+"' AND CoefAppr=4 AND Tag_1 IN ("+tagsFilter+") OR Tag_2 IN ("+tagsFilter+") OR Tag_3 IN ("+tagsFilter+") OR Tag_4 IN ("+tagsFilter+")";
        }
        else{
            cursor = CDB.query(
                    /*"t_"+language,*/"t_Mot",
                    null,
                    "Langue LIKE '"+language+"' AND  CoefAppr IN (0,1,2,3,4)",
                    null,
                    null,
                    null,
                    "CoefAppr ASC"
            );
            /*SQL_COEF0 = "SELECT COUNT(*) FROM t_"+language+" WHERE CoefAppr=0";*/
            SQL_COEF0 = "SELECT COUNT(*) FROM t_Mot WHERE Langue LIKE '"+language+"' AND CoefAppr IN (0,1,2)";
            SQL_COEF1 = "SELECT COUNT(*) FROM t_Mot WHERE Langue LIKE '"+language+"' AND CoefAppr=3";
            SQL_COEF2 = "SELECT COUNT(*) FROM t_Mot WHERE Langue LIKE '"+language+"' AND CoefAppr=4";
        }


        // Count nn rows with each CoefAppr
        Cursor mCount= CDB.rawQuery(SQL_COEF0, null);
        mCount.moveToFirst();
        nbCoef0 = mCount.getInt(0);

        mCount= CDB.rawQuery(SQL_COEF1, null);
        mCount.moveToFirst();
        nbCoef1= mCount.getInt(0);

        mCount= CDB.rawQuery(SQL_COEF2, null);
        mCount.moveToFirst();
        nbCoef2= mCount.getInt(0);

        mCount.close();

        int rowCount = cursor.getCount();

        key_list = new int[rowCount];
        words_list = new String[rowCount];
        translations_list = new String[rowCount];
        tags_list = new String[rowCount];

        cursor.moveToFirst();
        for ( int i = 0; i < rowCount; i++ ) {
            key_list[i] = cursor.getInt(0);
            words_list[i] = cursor.getString(1);
            translations_list[i] = cursor.getString(2);
            tags_list[i] = printNAN ( cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6) );
            cursor.moveToNext();
        }

    }

    String printNAN ( String tag1, String tag2, String tag3, String tag4 ) {

        String[] tabTag = { tag1, tag2, tag3, tag4 };
        String tempTag = "";

        for ( int i = 0; i < tabTag.length; i++ ) {
            if ( !( tabTag[i].equals("NAN") ) ) {
                tempTag = tempTag + " - " + tabTag[i];
            }
        }

        if ( tempTag.length() == 0 ) {
            return ( tempTag );
        } else {
            return ( tempTag.substring(3) );
        }

    }


    public void goToParChoix(View view) {

        /*Intent i = new Intent ( this, ParChoixActivity.class );

        i.putExtra("Nb_mots", taille_bd);
        i.putExtra("Langue", language);
        i.putExtra("Sens", sens);
        startActivity( i );*/

        //TextView t = (TextView) findViewById(R.id.editTextReponse);
        //String repo = t.getText().toString();


        Intent i = new Intent ( this, RevisionCheckDisplay.class );
        i.putExtra("Taille_bd", taille_bd);
        i.putExtra("Nb_mots", nb_left-1);
        i.putExtra("Word_number", word_number);
        i.putExtra("Sens", sens);
        i.putExtra("Langue", language);
        i.putExtra("Type", false);
        i.putExtra("TagsFilter", tagsFilter);

        //i.putExtra("Dico", monDico);
        i.putExtra("String_res", test_res);

        i.putExtra("Question", word);
        i.putExtra("ReponseUser", repo);
        i.putExtra("Reponse", word_translation);

        ArrayList<Integer> intList = new ArrayList<Integer>(50);
        for (int k : indTab) intList.add(k);
        i.putIntegerArrayListExtra("IndTab",intList);

        startActivity( i );
        finish();



    }
}
