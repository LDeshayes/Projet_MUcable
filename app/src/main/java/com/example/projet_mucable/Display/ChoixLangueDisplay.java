package com.example.projet_mucable.Display;

// Acitivity between main menu and cahierdisplay

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.projet_mucable.R;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/*

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor langueEdit = preferences.edit();
        langueEdit.putString("Langues", "Anglais;Allemand;Espagnol;");
        langueEdit.apply();

*/

public class ChoixLangueDisplay extends AppCompatActivity {

    List<String> listLangues = new ArrayList<>();
    List<String> tmpListLangue = new ArrayList<>();
    String prefLangues;
    SharedPreferences preferences;

    boolean supp = false;
    String langSupp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choixlangue_display);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        // We get all the languages used
        prefLangues = preferences.getString("Langues", "Anglais;Allemand;Espagnol;");

        // we get them ready to be used in adapter
        String[] langues = Arrays.copyOfRange(prefLangues.split(";"), 0, ((prefLangues.split(";")).length)); // -1 ?
        listLangues = Arrays.asList(langues);

        // Temporary list for adapter
        tmpListLangue.addAll(listLangues);
        tmpListLangue.add("Archives");


        setupSpinner();
    }

    // Setting up the language spinner
    void setupSpinner() {

        final Spinner spinner = findViewById(R.id.language_spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tmpListLangue);
        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.language_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Define sppinners behaviors
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Button butModif = findViewById(R.id.bModifLang);
                Button butSuppr = findViewById(R.id.bSuppLang);

                butModif.setEnabled(true);
                butSuppr.setEnabled(true);


                String language = spinner.getSelectedItem().toString();
                Log.d("testtest", language);


            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
                Button butModif = findViewById(R.id.bModifLang);
                Button butSuppr = findViewById(R.id.bSuppLang);

                butModif.setEnabled(false);
                butSuppr.setEnabled(false);

            }
        });

    }


    public void addLangue(View view){

        // Get the new language to add
        EditText edit = findViewById(R.id.editTextLang);
        String newLangue = edit.getText().toString();

        if(!newLangue.isEmpty() && tmpListLangue.indexOf(newLangue)==-1) {

            prefLangues += newLangue+";";
            //Log.d("testtest", prefLangues);

            // Temporary list for adapter
            //tmpListLangue.addAll(listLangues);
            tmpListLangue.add(newLangue);

            // Edit the preferences
            SharedPreferences.Editor langueEdit = preferences.edit();
            langueEdit.putString("Langues", prefLangues);
            langueEdit.apply();

            // Update the spinner
            final Spinner spinner = findViewById(R.id.language_spinner);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tmpListLangue);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);

            edit.setText("");
            spinner.setSelection(adapter.getCount()-1);

            // Remove potentiel tag of the language inside the word from prior achiving

            @SuppressLint("WrongConstant")
            SQLiteDatabase CDB = openOrCreateDatabase("CDB.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);

            for(int i = 1; i < 5; i++ ) {
                CDB.execSQL("UPDATE t_Mot SET Tag_"+i+"='NAN' WHERE Langue LIKE '"+newLangue+"' AND Tag_"+i+"='"+newLangue+"' ");
            }

        }

    }


    public void removeLangue(View view){

        final AlertDialog.Builder builder;


        ////////////////////////////////////////////////////////////////////////////////////////////
        // BUILDER SUPP
        builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogDarker));
        builder.setTitle("Etes-vous sûr de vouloir supprimer cette langue ?")
                .setCancelable(true)
                .setPositiveButton("Oui, archiver les mots", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        final Spinner spinner = findViewById(R.id.language_spinner);
                        langSupp = spinner.getSelectedItem().toString();

                        @SuppressLint("WrongConstant")
                        SQLiteDatabase CDB = openOrCreateDatabase("CDB.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);

                        // Check if exists
                        String tagExist = "SELECT * FROM t_TagColor WHERE Nom LIKE '"+langSupp+"' ";
                        Cursor mCount= CDB.rawQuery(tagExist, null);
                        // Add the tag in the DB
                        if(!(mCount.getCount()>0)){
                            ContentValues cvTagLang = new ContentValues();
                            cvTagLang.put("Nom", langSupp);
                            CDB.insert("t_TagColor", null, cvTagLang);
                        }
                        mCount.close();

                        // Add the language in one of the tag if possible
                        Cursor cursorToArchive = CDB.query(
                                /*"t_"+language,*/"t_Mot",
                                null,
                                "Langue LIKE '" + langSupp + "'",
                                null,
                                null,
                                null,
                                null,
                                null

                        );
                        cursorToArchive.moveToFirst();
                        while (!cursorToArchive.isAfterLast()) {
                            boolean inserted = false;
                            boolean alreadyHere = false;

                            // Check if tag is already here
                            for (int j = 6; j >= 3; j--) {
                                if (cursorToArchive.getString(j).equals(langSupp)) {
                                    alreadyHere = true;
                                }
                            }

                            // We insert the tag where we can
                            if (!alreadyHere){
                                for (int i = 6; i >= 3; i--) {
                                    if (cursorToArchive.getString(i).equals("NAN") && !inserted) {
                                        ContentValues cvTag = new ContentValues();
                                        cvTag.put("Tag_" + (i - 2), langSupp);
                                        CDB.update("t_Mot", cvTag, "Id_Word=" + cursorToArchive.getInt(0), null);
                                        inserted = true;
                                    }
                                }
                            }
                            cursorToArchive.moveToNext();
                        }

                        // AND we take out the label in the spinner


                        // Remove language from the lists
                        prefLangues = prefLangues.replaceAll(langSupp + ";", "");
                        tmpListLangue.remove(langSupp);

                        // Edit the preferences
                        SharedPreferences.Editor langueEdit = preferences.edit();
                        langueEdit.putString("Langues", prefLangues);
                        langueEdit.apply();

                        // Update the spinner
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, tmpListLangue);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);


                    }
                }).setNeutralButton("Non", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                    }
                })
                .setNegativeButton("Oui, supprimer les mots", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        // Get language of words to delete
                        final Spinner spinner = findViewById(R.id.language_spinner);
                        langSupp = spinner.getSelectedItem().toString();

                        // Delete words from concerned notebook
                        @SuppressLint("WrongConstant")
                        SQLiteDatabase CDB = openOrCreateDatabase("CDB.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
                        CDB.delete("t_Mot", "Langue='" + langSupp + "'", null);

                        // Remove language from the lists
                        prefLangues = prefLangues.replaceAll(langSupp + ";", "");
                        tmpListLangue.remove(langSupp);

                        // Edit the preferences
                        SharedPreferences.Editor langueEdit = preferences.edit();
                        langueEdit.putString("Langues", prefLangues);
                        langueEdit.apply();

                        // Update the spinner
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, tmpListLangue);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);
                    }
                });

        ////////////////////////////////////////////////////////////////////////////////////////////
        //// END OF BUILDERS /////
        //////////////////////////


        supp = false;
        final Spinner spinner = findViewById(R.id.language_spinner);
        String language = spinner.getSelectedItem().toString();
        // We do not delete archive
        if(!language.equals("Archives")) {


            AlertDialog alert = builder.create();
            alert.show();

            // Wait for alert dialog response

        }

    }

    public void modifLangue(View view){

        final EditText edit = findViewById(R.id.editTextLang);
        final String newLangue = edit.getText().toString();

        // if pas vide
        if(!newLangue.isEmpty()) {


            final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogDarker));
            builder.setMessage("Etes-vous sûr de vouloir modifier cette langue ?")
                    .setCancelable(true)
                    .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            final Spinner spinner = findViewById(R.id.language_spinner);
                            String language = spinner.getSelectedItem().toString();


                            // Modifiy language in lists
                            prefLangues = prefLangues.replaceAll(language, newLangue);
                            tmpListLangue.remove(language);
                            tmpListLangue.add(newLangue);

                            // Edit the preferences
                            SharedPreferences.Editor langueEdit = preferences.edit();
                            langueEdit.putString("Langues", prefLangues);
                            langueEdit.apply();

                            // Update list spinner
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, tmpListLangue);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner.setAdapter(adapter);

                            // Get on modified language
                            edit.setText("");
                            spinner.setSelection(adapter.getCount() - 1);

                            // Update concerned words with their new language
                            @SuppressLint("WrongConstant")
                            SQLiteDatabase CDB = openOrCreateDatabase("CDB.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);

                            ContentValues cv = new ContentValues();
                            cv.put("Langue", newLangue);
                            CDB.update("t_Mot", cv, "Langue='" + language + "'", null);


                        }
                    })
                    .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();


        }

    }


    public void activityToCahierDisplay(View view) {

        Spinner mySpinner = (Spinner) findViewById(R.id.language_spinner);
        String language = mySpinner.getSelectedItem().toString();

        Intent i = new Intent ( this, CahierDisplay.class );
        i.putExtra( "LangueChoisie", language );
        startActivity( i );
        finish();

    }

    // Full clean  DB & FST_LAUNCH value in SharedPreferences
    @SuppressLint("WrongConstant")
    public void cleanDB(View view) {

        SQLiteDatabase CDB = openOrCreateDatabase(
                "CDB.db"
                , SQLiteDatabase.CREATE_IF_NECESSARY
                , null
        );

        CDB.execSQL("DROP TABLE IF EXISTS t_Anglais");
        CDB.execSQL("DROP TABLE IF EXISTS t_Allemand");
        CDB.execSQL("DROP TABLE IF EXISTS t_Espagnol");

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor EDITOR = preferences.edit();
        EDITOR.putBoolean("FST_LAUNCH", true);
        EDITOR.putString("TAG_LIST", "EMPTY_NULL");
        EDITOR.putBoolean("NEED_UPD_01", true);
        EDITOR.apply();

        Intent i = new Intent ( this, LoadingScreenDisplay.class );
        startActivity( i );
        finish();

    }
}
