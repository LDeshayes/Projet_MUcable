package com.example.projet_mucable.Display;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.projet_mucable.R;
import java.util.ArrayList;
import java.util.Arrays;
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

    long startTime;
    long secondTime;

    TextView indiceTextView;
    EditText editMot;

    boolean showyet = false;
    boolean showyet2 = false;
    int clickedIndice = 0;
    boolean edited = true;

    String indiceMot = "";
    String newIndiceMot = "";

    // Runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {

            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);

            indiceTextView.setText(5-seconds+"");

            if(seconds>5 && clickedIndice==0 && !showyet){
                // Set button visible
                indiceTextView.setCompoundDrawablesWithIntrinsicBounds(0, android.R.drawable.ic_dialog_info, 0, 0);
                indiceTextView.setVisibility(View.VISIBLE);
                showyet = true;
                timerHandler.removeCallbacks(timerRunnable);

                Toast.makeText(getApplicationContext(), "Indice disponible", Toast.LENGTH_SHORT).show();
            }


            else if(clickedIndice==1 && (int)((System.currentTimeMillis()-secondTime)/1000)>5 && !showyet2){
                // Set button visible
                indiceTextView.setCompoundDrawablesWithIntrinsicBounds(0, android.R.drawable.ic_dialog_info, 0, 0);
                indiceTextView.setVisibility(View.VISIBLE);
                showyet2 = true;
                timerHandler.removeCallbacks(timerRunnable);

                Toast.makeText(getApplicationContext(), "Indice disponible", Toast.LENGTH_SHORT).show();
            }



            timerHandler.postDelayed(this, 100);
        }
    };

    @Override
    public void onPause() {
        super.onPause();

        timerHandler.removeCallbacks(timerRunnable);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong("TIMER", startTime);
        editor.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        timerHandler.postDelayed(timerRunnable, 0);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putLong("TIMER", startTime);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_par_coeur);

        startTime = System.currentTimeMillis();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong("TIMER", startTime);
        editor.commit();

        if (savedInstanceState != null){
            //Log.d("testtest","savedinstance: "+startTime+" / "+savedInstanceState.getLong("TIMER"));
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
        editMot = findViewById(R.id.editTextReponse);
        indiceTextView = findViewById(R.id.textViewIndice);


        timerHandler.postDelayed(timerRunnable, 0);


        // If it isnt the first time we go in revisionParCoeur
        if(this_i.getBooleanExtra("Not_First", true)){

            ArrayList<Integer> intList = this_i.getIntegerArrayListExtra("IndTab");
            indTab = intList.toArray(new Integer[0]);

            //monDico = (DicoSeri)this_i.getSerializableExtra("Dico");
            test_res = this_i.getStringExtra("String_res");
        }
        else{

            //boolean testEq;

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


                ArrayList<Integer> list1 = new ArrayList<>();
                for (int k=0; k<nbCoef0; k++) {
                    list1.add(k);
                }

                ArrayList<Integer> list2 = new ArrayList<>();
                for (int k=nbCoef0; k<nbCoef0+nbCoef1; k++) {
                    list2.add(k);
                }

                ArrayList<Integer> list3 = new ArrayList<>();
                for (int k=nbCoef0+nbCoef1; k<nbCoef0+nbCoef1+nbCoef2; k++) {
                    list3.add(k);
                }

                Collections.shuffle(list1);
                Collections.shuffle(list2);
                Collections.shuffle(list3);

                int doesitall = 0;

                int get0s = 0;
                int get1s = 0;
                int get2s = 0;


                while(  (   (doesitall<nb_left*0.6  && doesitall!=nbCoef0)  && nbCoef1+nbCoef2>=nb_left*0.4)
                        ||
                        (   doesitall!=(nb_left-(nbCoef1+nbCoef2))        && nbCoef1+nbCoef2<nb_left*0.4)  ){

                    indTab[doesitall]=list1.get(get0s);
                    get0s++;
                    doesitall++;
                }

                while(  (   doesitall<get0s+((nb_left-get0s)/2)             && nbCoef2>((nb_left-get0s)/2) )
                        ||
                        (   (doesitall<(nb_left-nbCoef2))                   && nbCoef2<=(nb_left-get0s)/2)  ){

                    indTab[doesitall]=list2.get(get1s);
                    get1s++;
                    doesitall++;
                }

                while(doesitall<nb_left){
                    indTab[doesitall]=list3.get(get2s);
                    get2s++;
                    doesitall++;
                }



                /*int i;
                int j;
                int resume;

                ///////////////////////////////////////////////////////// Coef0 /////////////////////////////////////////////////////////////////

                if(nbCoef0>(nb_left*0.6) ){
                // Use 60% coef0
                    ArrayList<Integer> list = new ArrayList<>();
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
                    //ArrayList<Integer> list = new ArrayList<Integer>();
                    for (i=0; i<nbCoef0; i++) {
                        indTab[i]=i;
                    }

                }

                resume = i;

                ///////////////////////////////////////////////////////// Coef1 /////////////////////////////////////////////////////////////////

                if(nbCoef1<(nb_left-resume)/2){
                // Use all coef1 possible
                    //ArrayList<Integer> list = new ArrayList<Integer>();
                    for (i=resume; i<resume+nbCoef1; i++) {
                        indTab[i]=i;
                    }
                }
                else{
                // Use rdm amount of Coef1 and Coef2
                    ArrayList<Integer> list = new ArrayList<>();
                    for (j=nbCoef0; j<nbCoef0+nbCoef1; j++) {
                        list.add(j);
                    }
                    Collections.shuffle(list);
                    for (i=resume, j=0; i<resume+(nb_left-resume)/2 && nbCoef2>(nb_left-resume)/2 || i<(nb_left-nbCoef2) && nbCoef2<=(nb_left-resume)/2; i++, j++) {
                        indTab[i]=list.get(j);
                    }
                }

                resume = i;

                ///////////////////////////////////////////////////////// Rest in Coef2 /////////////////////////////////////////////////////////
                ArrayList<Integer> list = new ArrayList<>();
                for (j=nbCoef1-1; j<nbCoef1+nbCoef2; j++) {
                    list.add(i);
                }
                Collections.shuffle(list);
                for (i=resume, j=0; i<nb_left; i++, j++) {
                    indTab[i]=list.get(j);
                    Log.d("testtest","whytho");
                }*/

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
            if(word_translation.charAt(i) == ' '){
                indiceMot+="  ";
                newIndiceMot+="s";

            }
            else{
                indiceMot+="_ ";
                newIndiceMot+="l";

            }
        }

        indiceTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if((tagsFilter==null || tagsFilter.isEmpty()) && clickedIndice==1){

                    secondTime = System.currentTimeMillis();
                    startTime = secondTime;

                    TextView indiceTag = findViewById(R.id.textViewIndiceTag);
                    indiceTag.setText("Tags du mots :  "+tags_list[indTab[word_number]]);
                    clickedIndice++;
                }
                else if(clickedIndice==0){
                    editMot.setHint(indiceMot);
                    indiceTextView.setText("-");
                    indiceTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    clickedIndice++;

                }

            }
        });



        editMot.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable e) {

                int position = editMot.getSelectionStart();
                String textFromEditView = e.toString();
                String textFromEditClean = textFromEditView.replaceAll("_ ","").replaceAll("  ","").replaceAll("_","").replaceAll(" ","");
                boolean antiTitouan = (textFromEditView.replaceAll("_","").replaceAll(" ","")).isEmpty();
                //boolean antiMartin = (position>editMot.length());
                String newText = textFromEditClean;
                //String newText = textFromEditView.split("_")[0];


                if(edited){
                    edited=false;
                    if(antiTitouan){
                        editMot.setText("");
                    }
                    else{

                        if (clickedIndice>0) {
                            if (textFromEditClean.length() < indiceMot.length()/2) {
                                for(int i = (textFromEditClean.length()); i<newIndiceMot.length(); i++){
                                    if(newIndiceMot.charAt(i)=='s'){
                                        newText+="  ";
                                    }
                                    else{
                                        newText+="_ ";
                                    }
                                }

                            }

                            // is only executed if the EditText was directly changed by the user
                            editMot.setText(newText);
                            //Log.d("fuckspace","pos: "+position+" - "+editMot.length());
                            editMot.setSelection(Math.min(position, editMot.length()));
                        }
                    }
                    edited=true;
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Toast.makeText(getApplicationContext(), "beforeTextChanged", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Toast.makeText(getApplicationContext(), "onTextChanged", Toast.LENGTH_SHORT).show();
            }
        });


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

    void loadDB() {

        Cursor cursor;
        final String SQL_COEF0;
        final String SQL_COEF1;
        final String SQL_COEF2;

        if(tagsFilter!=null && !tagsFilter.isEmpty()){
            cursor = CDB.query(
                    /*"t_"+language,*/"t_Mot",
                    null,
                    "Langue LIKE '"+language+"' AND (Tag_1 IN ("+tagsFilter+") OR Tag_2 IN ("+tagsFilter+") OR Tag_3 IN ("+tagsFilter+") OR Tag_4 IN ("+tagsFilter+")) AND CoefAppr IN (0,1,2,3,4)",
                    null,
                    null,
                    null,
                    "CoefAppr ASC"
            );
            SQL_COEF0 = "SELECT COUNT(*) FROM t_Mot WHERE Langue LIKE '"+language+"' AND CoefAppr IN (0,1,2) AND (Tag_1 IN ("+tagsFilter+") OR Tag_2 IN ("+tagsFilter+") OR Tag_3 IN ("+tagsFilter+") OR Tag_4 IN ("+tagsFilter+"))";
            SQL_COEF1 = "SELECT COUNT(*) FROM t_Mot WHERE Langue LIKE '"+language+"' AND CoefAppr=3 AND (Tag_1 IN ("+tagsFilter+") OR Tag_2 IN ("+tagsFilter+") OR Tag_3 IN ("+tagsFilter+") OR Tag_4 IN ("+tagsFilter+"))";
            SQL_COEF2 = "SELECT COUNT(*) FROM t_Mot WHERE Langue LIKE '"+language+"' AND CoefAppr=4 AND (Tag_1 IN ("+tagsFilter+") OR Tag_2 IN ("+tagsFilter+") OR Tag_3 IN ("+tagsFilter+") OR Tag_4 IN ("+tagsFilter+"))";
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

        Log.d("testtest","coef: "+nbCoef0+"-"+nbCoef1+"-"+nbCoef2);

        // Count total rows
        int rowCount = cursor.getCount();

        key_list = new int[rowCount];
        words_list = new String[rowCount];
        translations_list = new String[rowCount];
        tags_list = new String[rowCount];

        cursor.moveToFirst();
        for( int i = 0; i < rowCount; i++ ) {
            key_list[i] = cursor.getInt(0);
            words_list[i] = cursor.getString(1);
            translations_list[i] = cursor.getString(2);
            tags_list[i] = printNAN ( cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6) );
            cursor.moveToNext();
        }
        cursor.close();
    }

    String printNAN ( String tag1, String tag2, String tag3, String tag4 ) {

        String[] tabTag = { tag1, tag2, tag3, tag4 };

        StringBuilder tempTagBuilder = new StringBuilder();
        for (String s : tabTag) {
            if (!(s.equals("NAN"))) {
                tempTagBuilder.append(" - ").append(s);
            }
        }
        String tempTag = tempTagBuilder.toString();

        if ( tempTag.length() == 0 ) {
            return ( tempTag );
        } else {
            return ( tempTag.substring(3) );
        }

    }


    public void goToParCoeur(View view) {

        TextView t = findViewById(R.id.editTextReponse);
        String repo = t.getText().toString();

        // Stop timer
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
        i.putExtra("NbIndice", clickedIndice);

        ArrayList<Integer> intList = new ArrayList<Integer>(50);
        intList.addAll(Arrays.asList(indTab));
        i.putIntegerArrayListExtra("IndTab",intList);


        startActivity( i );
        finish();


    }
}