package com.example.projet_mucable.Display;

// Ajout et suppression des tags, 4.4 cdc

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projet_mucable.CustomAdapter;
import com.example.projet_mucable.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;

public class GestionTagsDisplay extends AppCompatActivity {

    View tag_view;
    String tagChosen = "NAN";
    SQLiteDatabase CDB;
    String[] tag_listDB;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestiontags_display);

        CDB = openOrCreateDatabase("CDB.db", SQLiteDatabase.CREATE_IF_NECESSARY, null );
        Cursor cTag = CDB.query(
                "t_TagColor",
                null,
                null,
                null,
                null,
                null,
                null
        );
        int tailleReq = cTag.getCount();
        tag_listDB = new String[tailleReq];
        int i = 0;

        cTag.moveToFirst();
        while(!cTag.isAfterLast()){
            //Log.d("testtest", " tag :"+c2.getString(1));
            tag_listDB[i] = cTag.getString(1);
            i++;
            cTag.moveToNext();
        }

        setupElements();
    }

    void setupElements() {
        setupListView();
    }

    void setupListView() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String tags = preferences.getString("TAG_LIST", "EMPTY_NULL");
        //final String[] tag_list = tags.split(";");
        final String[] tag_list = tag_listDB;



        if ( !tag_list[0].equals("EMPTY_NULL") ) {

            // Définition des colonnes
            // NB : SimpleCursorAdapter a besoin obligatoirement d'un ID nommé "_id"
            String[] columns = new String[] { "_id", "col1" };

            // Définition des données du tableau
            MatrixCursor matrixCursor= new MatrixCursor(columns);

            for ( int i = 0; ( i < tag_list.length ) ; i++ ) {
                matrixCursor.addRow(new Object[] { 1,tag_list[i]});
            }

            // on prendra les données des colonnes 1 et 2...
            String[] from = new String[] {"col1"};

            // ...pour les placer dans les TextView définis dans "tag_listview.xml"
            int[] to = new int[] { R.id.tag};

            // création de l'objet SimpleCursorAdapter...
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.tag_listview, matrixCursor, from, to, 0);


            // ...qui va remplir l'objet ListView
            final ListView tags_listview = (ListView) findViewById(R.id.tags_listview);
            tags_listview.setAdapter(adapter);

            tags_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if ( !tagChosen.equals("NAN") ) {
                        tag_view.setBackgroundResource(0);
                    }
                    tag_view = view;
                    tag_view.setBackgroundResource(R.drawable.border);
                    tagChosen = tag_list[position];
                }
            });
        }
    }

    void saveTag ( String addTag ) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String tags = preferences.getString("TAG_LIST", "EMPTY_NULL");

        String[] tag_list = tags.split(";");

        if (Arrays.asList(tag_list).contains(addTag)) {
            Toast.makeText(getApplicationContext(), "Ce tag existe déjà dans la liste !", Toast.LENGTH_SHORT).show();
        } else {

            String tagList;

            if ( tag_list[0].equals("EMPTY_NULL") || tag_list[0].equals("") ) {
                tagList = addTag;
            } else {
                tagList = tags + ";" + addTag;
            }

            SharedPreferences.Editor NEW_TAGLIST = preferences.edit();
            NEW_TAGLIST.putString("TAG_LIST", tagList);
            NEW_TAGLIST.commit();

        }

    }

    public void onClickAddTag(View view) {

        final EditText editText_AddTag = findViewById(R.id.editText_tag);
        final String newTag = editText_AddTag.getText().toString();

        final ContentValues valTag = new ContentValues();
        valTag.put("Nom",newTag);

        if ( newTag.length() != 0 ) {

            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogDarker));
            builder.setMessage("Êtes vous sur(e) de vouloir ajouter ce tag "+newTag+" ?")
                    .setCancelable(true)
                    .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            saveTag( newTag );
                            editText_AddTag.setText("");
                            finish();

                            CDB.insert("t_TagColor","",valTag);

                            overridePendingTransition(0, 0);
                            startActivity(getIntent());
                            overridePendingTransition(0, 0);
                        }
                    })
                    .setNegativeButton("Non", null );
            AlertDialog alert = builder.create();
            alert.show();

        } else {

            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogDarker));
            builder.setMessage("Le champ pour un nouveau tag est vide !")
                    .setCancelable(true)
                    .setPositiveButton("Ok", null);
            AlertDialog alert = builder.create();
            alert.show();

        }

    }

    @SuppressLint("WrongConstant")
    void delTag( String delTag ) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String tags = preferences.getString("TAG_LIST", "EMPTY_NULL");

        String[] tag_list = tags.split(";");

        if (Arrays.asList(tag_list).contains(delTag)) {

            // Suppression du tag de la liste des tags en mémoire
            int pos = new ArrayList<String>(Arrays.asList(tag_list)).indexOf(delTag);

            String new_TAGLIST = "";
            int i, j;

            for (i = 0; i < tag_list.length; i++ ) {
                if(i != pos){
                    new_TAGLIST = new_TAGLIST + ";" + tag_list[i];
                }
            }

            SharedPreferences.Editor NEW_TAGLIST = preferences.edit();
            if ( new_TAGLIST.length() == 0 ) {
                NEW_TAGLIST.putString("TAG_LIST", "EMPTY_NULL");
            } else {
                NEW_TAGLIST.putString("TAG_LIST", new_TAGLIST.substring(1));
            }
            NEW_TAGLIST.commit();

            // Suppression des occurrences du tag dans la DB
            SQLiteDatabase CDB = openOrCreateDatabase("CDB.db", SQLiteDatabase.CREATE_IF_NECESSARY, null );
            String[] language_array = getResources().getStringArray(R.array.language_array);;
            //for ( i = 0; i < language_array.length; i++ ) {
                for ( j = 1; j < 5; j++ ) {
                    CDB.execSQL("UPDATE t_Mot SET Tag_"+j+"='NAN' WHERE Tag_"+j+"='"+delTag+"' ");
                }
            //}

        } else {

            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogDarker));
            builder.setMessage("Ce tag n'existe pas dans la liste !")
                    .setCancelable(true)
                    .setPositiveButton("Ok", null);
            AlertDialog alert = builder.create();
            alert.show();

        }

    }

    public void onClickDelTag(View view) {

        if ( tagChosen.equals("NAN") ) {

            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogDarker));
            builder.setMessage("Choisissez un tag à supprimer !")
                    .setCancelable(true)
                    .setPositiveButton("Ok", null);
            AlertDialog alert = builder.create();
            alert.show();

        } else {

            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogDarker));
            builder.setMessage("Êtes vous sur(e) de vouloir supprimer ce tag "+tagChosen+" ?")
                    .setCancelable(true)
                    .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            delTag(tagChosen);
                            finish();

                            CDB.delete("t_TagColor","Nom=?",new String[]{tagChosen});

                            overridePendingTransition(0, 0);
                            startActivity(getIntent());
                            overridePendingTransition(0, 0);
                        }
                    })
                    .setNegativeButton("Non", null );
            AlertDialog alert = builder.create();
            alert.show();

        }

    }

}