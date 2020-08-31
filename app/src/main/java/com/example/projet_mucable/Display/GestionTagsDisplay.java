package com.example.projet_mucable.Display;

// Ajout et suppression des tags, 4.4 cdc

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.projet_mucable.CustomAdapter;

import com.example.projet_mucable.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;

public class GestionTagsDisplay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestiontags_display);

        setupElements();
    }

    void setupElements() {
        setChoiceTag();
    }

    void setChoiceTag() {

        Button button_selection = findViewById(R.id.button_DelChoice);

        Intent i = getIntent();
        String choiceTag = i.getStringExtra("ChoiceTag");

        if ( choiceTag == null || choiceTag.equals("NAN") ) {
            button_selection.setText("Choix");
        } else {
            button_selection.setText(choiceTag);
        }

    }

    public void clicAddTag(View view) {

        final EditText editText_AddTag = findViewById(R.id.editText_AddTag);
        final String newTag = editText_AddTag.getText().toString();

        if ( newTag.length() != 0 ) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Êtes vous sur(e) de vouloir ajouter ce tag "+newTag+" ?")
                    .setCancelable(true)
                    .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            saveTag( newTag );
                            editText_AddTag.setText("");
                        }
                    })
                    .setNegativeButton("Non", null );
            AlertDialog alert = builder.create();
            alert.show();

        } else {

            Toast.makeText(getApplicationContext(), "Le champ pour un nouveau tag est vide !", Toast.LENGTH_SHORT).show();

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

    void saveTag ( String tag ) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String tags = preferences.getString("TAG_LIST", "EMPTY_NULL");

        String[] tag_list = tags.split(";");

        if (Arrays.asList(tag_list).contains(tag)) {
            Toast.makeText(getApplicationContext(), "Ce tag existe déjà dans la liste !", Toast.LENGTH_SHORT).show();
        } else {

            String tagList;

            if ( tag_list[0].equals("EMPTY_NULL") || tag_list[0].equals("") ) {
                tagList = tag;
            } else {
                tagList = tags + ";" + tag;
            }

            SharedPreferences.Editor NEW_TAGLIST = preferences.edit();
            NEW_TAGLIST.putString("TAG_LIST", tagList);
            NEW_TAGLIST.commit();

        }

    }

    public void clicChoiceTag(View view) {
        Intent i = new Intent ( this, ChoixTagsDisplay.class );
        i.putExtra("Origin", "GestionTagsDisplay");
        startActivity( i );
        finish();
    }

    public void clicDelTag(View view) {

        final Button button_selection = findViewById(R.id.button_DelChoice);
        final String delTag = (String) button_selection.getText();

        if ( delTag.equals("Choix") ) {
            Toast.makeText(getApplicationContext(), "Choisissez un tag à supprimer !", Toast.LENGTH_SHORT).show();
        } else {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Êtes vous sur(e) de vouloir supprimer ce tag "+delTag+" ?")
                    .setCancelable(true)
                    .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            delTag(delTag);
                            button_selection.setText("Choix");
                        }
                    })
                    .setNegativeButton("Non", null );
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
            for ( i = 0; i < language_array.length; i++ ) {
                for ( j = 1; j < 5; j++ ) {
                    CDB.execSQL("UPDATE t_"+language_array[i]+" SET Tag_"+j+"='NAN' WHERE Tag_"+j+"='"+delTag+"' ");
                }
            }

        } else {
            Toast.makeText(getApplicationContext(), "Ce tag n'existe pas dans la liste !", Toast.LENGTH_SHORT).show();
        }

    }

}