package com.example.projet_mucable.Display;

// Affiche une liste de vocabulaire d'une langue choisie, 4.2

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projet_mucable.CustomAdapter;
import com.example.projet_mucable.R;

import java.util.List;

public class CahierDisplay extends AppCompatActivity {

    String language;
    ListView words_listview;
    int[] key_list_final, key_list;
    String[] words_list, translations_list, tags_list;
    int rowCount;
    int key = -1; // future key pour repérer le word à modifier
    View key_view;
    boolean switchCol = false;
    boolean bsearch = false;
    SQLiteDatabase CDB;

    String[] words_list_temp;
    String[] translations_list_temp;
    String[] tags_list_temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cahier_display);

        getLanguage();

        setupDB();

        setupElements();

        setupSearchBar();

    }

    void getLanguage() {
        Intent i = getIntent();
        language = i.getStringExtra("LangueChoisie");
    }

    void setupDB() {
        getDB();
        loadDB();
    }

    @SuppressLint("WrongConstant")
    void getDB () {
        CDB = openOrCreateDatabase("CDB.db", SQLiteDatabase.CREATE_IF_NECESSARY, null );
    }

    // FUTURE : ADD FILTER HERE
    String printNAN ( String tag1, String tag2, String tag3, String tag4 ) {

        String[] tabTag = { tag1, tag2, tag3, tag4 };
        StringBuilder tempTag = new StringBuilder();

        for (String s : tabTag) {
            if (!(s.equals("NAN"))) {
                tempTag.append(" - ").append(s);
            }
        }

        if ( tempTag.length() == 0 ) {
            return (tempTag.toString());
        } else {
            return ( tempTag.substring(3) );
        }

    }

    void loadDB() {

        Cursor cursor = CDB.query(
                "t_"+language,
                null,
                null,
                null,
                null,
                null,
                "CoefAppr ASC, Word ASC",
                null

        );

        rowCount = cursor.getCount();

        key_list_final = new int[rowCount];
        key_list = new int[rowCount];
        words_list = new String[rowCount];
        translations_list = new String[rowCount];
        tags_list = new String[rowCount];

        cursor.moveToFirst();
        for ( int i = 0; i < rowCount; i++ ) {
            // comment here
            /*AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogDarker));
            builder.setMessage("TEST: "+cursor.getString(1)+" - "+cursor.getInt(7)+" !")
                    .setCancelable(true)
                    .setPositiveButton("Ok", null);
            AlertDialog alert = builder.create();
            alert.show();*/
            key_list_final[i] = cursor.getInt(0);
            key_list[i] = cursor.getInt(0);
            words_list[i] = cursor.getString(1);
            translations_list[i] = cursor.getString(2);
            tags_list[i] = printNAN ( cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6) );
            cursor.moveToNext();
        }

    }

    void setupElements() {
        setupLanguage();
        setupListView();
    }

    void setupLanguage() {
        TextView textView = (TextView) findViewById(R.id.language);
        textView.setText(language+" ");
    }

    void setupListView() {
        words_listview = (ListView) findViewById(R.id.words_listview);
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), words_list, translations_list, tags_list);
        words_listview.setAdapter(customAdapter);

        words_listview = (ListView) findViewById(R.id.words_listview);

        words_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int pos, long id) {
                if ( key != -1 ) {
                    key_view.setBackgroundResource(0);
                }
                key_view = v;
                key_view.setBackgroundResource(R.drawable.border);
                key = key_list[pos];
            }
        });
    }

    public void onClicModify(View view) {
        if ( key == -1 ) {
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogDarker));
            builder.setMessage("Veuillez sélectionner une entrée à modifier !")
                    .setCancelable(true)
                    .setPositiveButton("Ok", null);
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            Intent i = new Intent ( this, GestionMotDisplay.class );
            i.putExtra("key",key);
            launchGestionMots("Modify", i);
        }

    }

    public void onClicAdd(View view) {
        launchGestionMots("Add", new Intent ( this, GestionMotDisplay.class ));
    }

    void launchGestionMots ( String mode, Intent i ) {
        i.putExtra( "mode", mode );
        i.putExtra( "LangueChoisie", language );
        startActivity( i );
        finish();
    }

    public void setupSearchBar() {

        EditText editText_search = findViewById(R.id.editText_search);

        editText_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String search = s.toString().toLowerCase();
                CustomAdapter customAdapter;

                if (search.length() != 0) {

                    key = -1;
                    bsearch = true;

                    int counter = 0;
                    int[] searchOK = new int[rowCount];
                    String[] tempTags;

                    for (int i = 0; i < rowCount; i++) {
                        searchOK[i] = -1;
                        if (words_list[i].toLowerCase().contains(search) || translations_list[i].toLowerCase().contains(search)) {
                            counter++;
                            searchOK[i] = 1;
                        }
                        tempTags = tags_list[i].split(" - ");
                        for (int j = 0; ( j < tempTags.length ) && ( searchOK[i] != 1 ); j++) {
                            if (tempTags[j].toLowerCase().contains(search)) {
                                counter++;
                                searchOK[i] = 1;
                            }
                        }
                    }

                    key_list = new int[counter];

                    words_list_temp = new String[counter];
                    translations_list_temp = new String[counter];
                    tags_list_temp = new String[counter];

                    int incr = 0;
                    for (int i = 0; i < rowCount; i++) {
                        if (searchOK[i] == 1) {
                            key_list[incr] = key_list_final[i];
                            words_list_temp[incr] = words_list[i];
                            translations_list_temp[incr] = translations_list[i];
                            tags_list_temp[incr] = tags_list[i];
                            incr++;
                        }
                    }

                    if(!switchCol){
                        customAdapter = new CustomAdapter(getApplicationContext(), words_list_temp, translations_list_temp, tags_list_temp);
                    }
                    else{
                        customAdapter = new CustomAdapter(getApplicationContext(), translations_list_temp,  words_list_temp, tags_list_temp);
                    }
                } else {
                    bsearch = false;

                    if(!switchCol){
                        customAdapter = new CustomAdapter(getApplicationContext(), words_list, translations_list, tags_list);
                    }
                    else{
                        customAdapter = new CustomAdapter(getApplicationContext(), translations_list, words_list, tags_list);
                    }

                }
                words_listview.setAdapter(customAdapter);


            }

            @Override
            public void afterTextChanged(Editable s) {}
        });


    }

    public void onClickSwitch(View view){

        CustomAdapter customAdapter;
        switchCol = !switchCol;
        words_listview = (ListView) findViewById(R.id.words_listview);

        if(!switchCol){

            if(!bsearch){
                customAdapter = new CustomAdapter(getApplicationContext(), words_list, translations_list, tags_list);
            }
            else{
                customAdapter = new CustomAdapter(getApplicationContext(), words_list_temp, translations_list_temp, tags_list_temp);
            }

        }
        else{

            if(!bsearch){
                customAdapter = new CustomAdapter(getApplicationContext(), translations_list, words_list, tags_list);
            }
            else{
                customAdapter = new CustomAdapter(getApplicationContext(), translations_list_temp ,words_list_temp , tags_list_temp);
            }
        }

        words_listview.setAdapter(customAdapter);

    }

}
