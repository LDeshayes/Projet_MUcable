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

import com.example.projet_mucable.R;

import java.util.Random;

public class ParCoeurActivity extends AppCompatActivity {


    String language;
    ListView words_listview;
    int[] key_list;
    String[] words_list;// = { "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "lo" };
    String[] translations_list;// = { "Un", "Deux", "Trois", "Quatre", "Cinq", "Six", "Sept", "Huit", "Neuf", "Dix", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "lo" };
    String[] tags_list;// = { "Nombre", "Nombre", "Nombre", "Nombre", "Nombre", "Nombre", "Nombre", "Nombre", "Nombre", "Nombre", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "lo" };
    int key = -1; // future key pour repérer le word à modifier
    View key_view;
    SQLiteDatabase CDB;

    String word = "Car";
    String word_translation;
    int word_number;
    int nb_left;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_par_coeur);

        getLanguage();
        setupDB();

        word_number = new Random().nextInt(words_list.length);

        Intent this_i = getIntent();
        word_number = this_i.getIntExtra("Word_number", 0);
        nb_left = this_i.getIntExtra("Nb_mots", 5);
        word = words_list[word_number];
        word_translation = translations_list[word_number];

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
        //language = i.getStringExtra("LangueChoisie");
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


    public void goToParCoeur(View view) {

        TextView t = (TextView) findViewById(R.id.editTextReponse);
        String rep = t.getText().toString();

        if(!(rep.equals(word_translation))){
            Toast.makeText(getApplicationContext(), "Mauvaise réponse !",Toast.LENGTH_LONG).show();
        }
        else{

            Toast.makeText(getApplicationContext(),"Bonne réponse !",Toast.LENGTH_LONG).show();

            final int random = new Random().nextInt(words_list.length);
            //Toast.makeText(getApplicationContext(),"nb :"+nb_left,Toast.LENGTH_LONG).show();

            Intent i = new Intent ( this, ParCoeurActivity.class );
            //i.putExtra("Word", words_list[random]);
            i.putExtra("Word_number", random);
            i.putExtra("Nb_mots", nb_left-1);

            if(nb_left>0){
                startActivity( i );
            }
            finish();

        }

    }
}
