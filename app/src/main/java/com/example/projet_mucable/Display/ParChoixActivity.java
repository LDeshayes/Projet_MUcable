package com.example.projet_mucable.Display;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.projet_mucable.R;

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
    int key = -1; // future key pour repérer le word à modifier
    //View key_view;
    SQLiteDatabase CDB;

    String word = "Car";
    String a1, a2, a3, a4;
    String word_translation;
    int word_number;
    int nb_left;
    int taille_bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_par_choix);

        getLanguage();
        setupDB();

        taille_bd = words_list.length;

        word_number = (new Random()).nextInt(words_list.length);

        Intent this_i = getIntent();
        word_number = this_i.getIntExtra("Word_number", 0);
        nb_left = this_i.getIntExtra("Nb_mots", 5);

        int[] iAnswers = {0, 0, 0, 0};
        while (iAnswers[0]==iAnswers[1] || iAnswers[0]==iAnswers[2] || iAnswers[0]==iAnswers[3] || iAnswers[1]==iAnswers[2] || iAnswers[1]==iAnswers[3] || iAnswers[2]==iAnswers[3]){
            for(int i=0; i<4; i++){
                iAnswers[i] = (new Random()).nextInt(words_list.length);
            }
        }

        sens = this_i.getBooleanExtra("Sens", true);
        if(sens){
            word = words_list[word_number];
            word_translation = translations_list[word_number];
            a1 = translations_list[iAnswers[0]];
            a2 = translations_list[iAnswers[1]];
            a3 = translations_list[iAnswers[2]];
            a4 = translations_list[iAnswers[3]];
        }
        else{
            word_translation = words_list[word_number];
            word = translations_list[word_number];
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

        tq1.setText(a1);
        tq1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                repo = a1;
                tq1.setEnabled(false);
                tq2.setEnabled(true);
                tq3.setEnabled(true);
                tq4.setEnabled(true);
            }
        });

        tq2.setText(a2);
        tq2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                repo = a2;
                tq1.setEnabled(true);
                tq2.setEnabled(false);
                tq3.setEnabled(true);
                tq4.setEnabled(true);
            }
        });

        tq3.setText(a3);
        tq3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                repo = a3;
                tq1.setEnabled(true);
                tq2.setEnabled(true);
                tq3.setEnabled(false);
                tq4.setEnabled(true);
            }
        });


        tq4.setText(a4);
        tq4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                repo = a4;
                tq1.setEnabled(true);
                tq2.setEnabled(true);
                tq3.setEnabled(true);
                tq4.setEnabled(false);
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

    // FUTURE : ADD FILTER HERE
    void loadDB() {

        Cursor cursor = CDB.query(
                "t_"+language,
                null,
                null,
                null,
                null,
                null,
                null
        );

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
        i.putExtra("Sens", sens);
        i.putExtra("Langue", language);
        i.putExtra("Type", false);

        i.putExtra("Question", word);
        i.putExtra("ReponseUser", repo);
        i.putExtra("Reponse", word_translation);
        startActivity( i );
        finish();



    }
}
