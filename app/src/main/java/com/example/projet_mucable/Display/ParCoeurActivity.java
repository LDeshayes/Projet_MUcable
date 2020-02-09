package com.example.projet_mucable.Display;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projet_mucable.DicoSeri;
import com.example.projet_mucable.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ParCoeurActivity extends Activity {


    String language;
    Boolean sens;
    //ListView words_listview;
    int[] key_list;
    String[] words_list;// = { "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "lo" };
    String[] translations_list;// = { "Un", "Deux", "Trois", "Quatre", "Cinq", "Six", "Sept", "Huit", "Neuf", "Dix", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "lo" };
    String[] tags_list;// = { "Nombre", "Nombre", "Nombre", "Nombre", "Nombre", "Nombre", "Nombre", "Nombre", "Nombre", "Nombre", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "lo" };
    String tagsFilter;

    SQLiteDatabase CDB;

    DicoSeri monDico = new DicoSeri();
    String test_res = "";

    String word = "Car";
    String word_translation;
    int word_number;
    int nb_left;
    int taille_bd;
    Integer[] indTab;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_par_coeur);


        Intent this_i = getIntent();
        tagsFilter = this_i.getStringExtra("TagsFilter");

        getLanguage();
        setupDB();
        taille_bd = words_list.length;
        nb_left = this_i.getIntExtra("Nb_mots", 5);
        indTab = new Integer[nb_left];
        word_number = this_i.getIntExtra("Word_number", new Random().nextInt(words_list.length));

        // Si cest pas la premeire fois qu'on passe dans revisionParCoeur
        if(this_i.getBooleanExtra("Not_First", true)){

            ArrayList<Integer> intList = this_i.getIntegerArrayListExtra("IndTab");
            indTab = intList.toArray(new Integer[0]);

            monDico = (DicoSeri)this_i.getSerializableExtra("Dico");
            test_res = this_i.getStringExtra("String_res");
        }
        else{

            boolean testEq;

            if(taille_bd>nb_left){

                for(int i=0; i<nb_left; i++){
                    indTab[i] = 0;
                }

                // Boucle sur nb_left
                for(int i=0; i<nb_left; i++){
                    testEq = false;
                    while (!testEq) {
                        int rdm = (new Random()).nextInt(words_list.length);
                        testEq = true;
                        for(int j=0; j<nb_left; j++) {
                            if (indTab[j]==rdm) {
                                testEq = false;
                            }
                        }
                    }
                }
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



        sens = this_i.getBooleanExtra("Sens", true);
        if(sens){
            word = words_list[word_number];
            word_translation = translations_list[word_number];
        }
        else{
            word_translation = words_list[word_number];
            word = translations_list[word_number];
        }

        TextView t = (TextView) findViewById(R.id.textViewQuestion);
        t.setText(word);



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

    // FUTURE : ADD FILTER HERE
    void loadDB() {

        Cursor cursor;

        if(tagsFilter!=null && !tagsFilter.isEmpty()){
            cursor = CDB.query(
                    "t_"+language,
                    null,
                    "Tag_1 IN ('"+tagsFilter+"') OR Tag_2 IN ('"+tagsFilter+"') OR Tag_3 IN ('"+tagsFilter+"') OR Tag_4 IN ('"+tagsFilter+"')",
                    null,
                    null,
                    null,
                    null
            );
        }
        else{
            cursor = CDB.query(
                    "t_"+language,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );
        }

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


    public void goToParCoeur(View view) {

        TextView t = (TextView) findViewById(R.id.editTextReponse);
        String repo = t.getText().toString();


        Intent i = new Intent ( this, RevisionCheckDisplay.class );
        i.putExtra("Taille_bd", taille_bd);
        i.putExtra("Nb_mots", nb_left-1);
        i.putExtra("Sens", sens);
        i.putExtra("Langue", language);
        i.putExtra("Type", true);
        i.putExtra("TagsFilter", tagsFilter);

        i.putExtra("Dico", monDico);
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