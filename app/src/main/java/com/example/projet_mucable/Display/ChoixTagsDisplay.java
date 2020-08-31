package com.example.projet_mucable.Display;

// Offre la liste complète des tags actuels, permet d'en selectionner certains et renvoie les choix fait en intent

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ContextThemeWrapper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projet_mucable.CustomAdapter;
import com.example.projet_mucable.GestionMot;
import com.example.projet_mucable.R;

import org.w3c.dom.Text;
import java.util.ArrayList;
import java.util.Arrays;

public class ChoixTagsDisplay extends AppCompatActivity {


    View tag_view;
    String tagReturn = "NAN";
    String[] wordInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choixtags_display);

        setupListView();
    }

    @Override
    /**
     * action when back button is pressed
     */
    public void onBackPressed() {
        sendTagReturn();
    }

    public void sendTagReturn () {

        Intent i;
        Intent pred = getIntent();
        String origin = pred.getStringExtra("Origin");

        if ( origin.equals("GestionTagsDisplay") ) {
            i = new Intent ( this, GestionTagsDisplay.class );
            i.putExtra( "ChoiceTag", tagReturn );
        }

        // TODO Rework it with an onRestart
        /* else if ( origin.equals("GestionMotDisplay") ) {
            String[] wordInfo = pred.getStringExtra("informations").split(";");
            int tagChanged_number = pred.getIntExtra("tag_number", -1);
            String newWordInfo = wordInfo[0]+";"+wordInfo[1];
            for ( int ind = 0; ind < 4; ind ++ ) {
                if ( ind == tagChanged_number ) {
                    newWordInfo = newWordInfo+";"+tagReturn;
                } else {
                    newWordInfo = newWordInfo+";"+wordInfo[ind+2];
                }
            }

            i = new Intent ( this, GestionMotDisplay.class );
            i.putExtra("LangueChoisie", pred.getStringExtra("LangueChoisie") );
            i.putExtra("mode", pred.getStringExtra("mode") );
            i.putExtra("Origin", "ChoixTagsDisplay");
            i.putExtra("key", pred.getStringExtra("key"));
            i.putExtra("informations", newWordInfo);
            i.putExtra("hints", pred.getStringExtra("hints"));
        }*/ else {
            i = new Intent ( this, IntroMenuDisplay.class );
        }
        startActivity( i );
        finish();
    }

    void setupListView() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String tags = preferences.getString("TAG_LIST", "EMPTY_NULL");
        final String[] tag_list = tags.split(";");

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
            int[] to = new int[] { R.id.textViewCol1};

            // création de l'objet SimpleCursorAdapter...
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.tag_listview, matrixCursor, from, to, 0);


            // ...qui va remplir l'objet ListView
            final ListView tags_listview = (ListView) findViewById(R.id.tags_listview);
            tags_listview.setAdapter(adapter);


            /*
            CEST BON MAIS FAUT RENDRE LE TAG PLUS FONCE QUAND ON A CLIQUE DESSUS
            */
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

    public void onClickChoiceTag(View view) {

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        if ( !tagChosen.equals("NAN") ) {

            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogDarker));
            builder.setMessage("Êtes vous sur(e) de vouloir choisir le tag "+tagChosen+" ?")
                    .setCancelable(true)
                    .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            SharedPreferences.Editor NEW_TAGLIST = preferences.edit();
                            NEW_TAGLIST.putString("TAG_CHOSEN", tagChosen);
                            NEW_TAGLIST.commit();
                            finish();
                        }
                    })
                    .setNegativeButton("Non", null );
            AlertDialog alert = builder.create();
            alert.show();

        } else {

            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogDarker));
            builder.setMessage("Vous n'avez choisit aucun tag !")
                    .setCancelable(true)
                    .setPositiveButton("Ok", null);
            AlertDialog alert = builder.create();
            alert.show();


        }

    }

}
