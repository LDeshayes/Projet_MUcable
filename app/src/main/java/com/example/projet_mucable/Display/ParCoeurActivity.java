package com.example.projet_mucable.Display;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projet_mucable.DicoSeri;
import com.example.projet_mucable.R;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ParCoeurActivity extends AppCompatActivity {


    String language;
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
    String word_translation;
    int word_number;
    int nb_left;
    int taille_bd;
    Integer[] indTab;

    int nbCoef0;
    int nbCoef1;
    int nbCoef2;

    Date debutTimer;
    Date finTimer;

    long startTime = System.currentTimeMillis();
    TextView indiceTextView;
    boolean showyet = false;
    boolean clickedIndice = false;
    String indiceMot = "";

    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);

            indiceTextView.setText(5-seconds+"");

            if(seconds>5 && !showyet){
                //mettre bouton indice disponible
                indiceTextView.setCompoundDrawablesWithIntrinsicBounds(0, android.R.drawable.ic_dialog_info, 0, 0);
                indiceTextView.setVisibility(View.VISIBLE);
                showyet = true;
                Toast.makeText(getApplicationContext(), "Indice disponible", Toast.LENGTH_SHORT).show();

                timerHandler.removeCallbacks(timerRunnable);
            }

            timerHandler.postDelayed(this, 100);
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putLong("TIMER", startTime);
        //declare values before saving the state
        super.onSaveInstanceState(savedInstanceState);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_par_coeur);

        if (savedInstanceState != null){
            //Do whatever you need with the string here, like assign it to variable.
            startTime = savedInstanceState.getLong("TIMER");
        }
        
        Intent this_i = getIntent();
        tagsFilter = this_i.getStringExtra("TagsFilter");

        getLanguage();
        setupDB();

        taille_bd = words_list.length;
        nb_left = this_i.getIntExtra("Nb_mots", 5);

        word_number = this_i.getIntExtra("Word_number", 0);
        indTab = new Integer[nb_left];
        final EditText editMot = findViewById(R.id.editTextReponse);
        indiceTextView = findViewById(R.id.textViewIndice);


        timerHandler.postDelayed(timerRunnable, 0);


        // Si c'est pas la premiere fois qu'on passe dans revisionParCoeur
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


                /*
                // Boucle sur nb_left
                for(int i=0; i<nb_left; i++){
                    testEq = false;
                    // Tan que tout les mots ne sont pas différents
                    while (!testEq) {
                        int rdm = (new Random()).nextInt(words_list.length);
                        testEq = true;
                        for(int j=0; j<nb_left; j++) {
                            if(indTab[j]==rdm) {
                                testEq = false;
                            }
                        }
                    }
                }
                */

                int i;
                int j;
                int resume;

                ///////////////////////////////////////////////////////// Coef0 /////////////////////////////////////////////////////////////////

                if(nbCoef0>(nb_left*0.6) /*&& nbCoef1+nbCoef2>=nb_left*0.6*/){
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

        sens = this_i.getBooleanExtra("Sens", true);
        if(sens){
            word = words_list[indTab[word_number]];
            word_translation = translations_list[indTab[word_number]];
        }
        else{
            word_translation = words_list[indTab[word_number]];
            word = translations_list[indTab[word_number]];
        }

        TextView t = findViewById(R.id.textViewQuestion);
        t.setText(word);

        ////////////////////////////////////////////////////////////////////////////////////////////

        //indiceTextView.setVisibility(View.INVISIBLE);
        for(int i = 0; i<word_translation.length(); i++){
            indiceMot+="_ ";
        }

        indiceTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editMot.setHint(indiceMot);
                clickedIndice = true;
            }
        });



        /*editMot.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable e) {
                String textFromEditView = e.toString();
                String newText = textFromEditView;

                String[] allChars = textFromEditView.split("");
                if(clickedIndice){
                    if(textFromEditView.length()<indiceMot.length()){
                        for(int i = 0; i < ((indiceMot.length()/2)-textFromEditView.length()); i++){
                            newText+="_ ";
                        }
                    }
                    //editMot.setText(newText);
                    //e.replace(0,e.length(),newText);
                    Toast.makeText(getApplicationContext(), "="+indiceMot.length()+"="+newText, Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(getApplicationContext(), "afterTextChanged", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Toast.makeText(getApplicationContext(), "beforeTextChanged", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Toast.makeText(getApplicationContext(), "onTextChanged", Toast.LENGTH_SHORT).show();
            }
        });*/


        // Start timer
        debutTimer = new Date();


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
                    "Langue LIKE '"+language+"' AND CoefAppr IN (0,1,2,3,4)",
                    null,
                    null,
                    null,
                    "CoefAppr ASC"
            );
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

        // Count total rows
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

        // Fin timer
        finTimer = new Date();
        Double chrono = (double)(finTimer.getTime()-debutTimer.getTime())/1000;


        Intent i = new Intent ( this, RevisionCheckDisplay.class );
        i.putExtra("Taille_bd", taille_bd);
        i.putExtra("Nb_mots", nb_left-1);
        i.putExtra("Word_number", word_number);
        i.putExtra("Sens", sens);
        i.putExtra("Langue", language);
        i.putExtra("Type", true);
        i.putExtra("TagsFilter", tagsFilter);

        //i.putExtra("Dico", monDico);
        i.putExtra("String_res", test_res);

        i.putExtra("Question", word);
        i.putExtra("ReponseUser", repo);
        i.putExtra("Reponse", word_translation);

        i.putExtra("Temps", chrono);

        ArrayList<Integer> intList = new ArrayList<Integer>(50);
        for (int k : indTab) intList.add(k);
        i.putIntegerArrayListExtra("IndTab",intList);


        startActivity( i );
        finish();


    }
}