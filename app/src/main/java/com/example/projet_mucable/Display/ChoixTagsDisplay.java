package com.example.projet_mucable.Display;

// Offre la liste complète des tags actuels, permet d'en selectionner certains et renvoie les choix fait en intent

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projet_mucable.CustomAdapter;
import com.example.projet_mucable.R;

public class ChoixTagsDisplay extends Activity {


    ListView tags_listview;
    String[] tags_list;// = { "Nombre", "Nombre", "Nombre", "Nombre", "Nombre", "Nombre", "Nombre", "Nombre", "Nombre", "Nombre", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "lo" };
    int key; // future key pour repérer le word à modifier

    String tagReturn ="testTag";

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

        Intent i = new Intent ( this, GestionTagsDisplay.class );
        i.putExtra( "ChoiceTag", tagReturn );
        startActivity( i );
        finish();
    }


    void setupListView() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String tags = preferences.getString("TAG_LIST", "EMPTY_NULL");
        String[] tag_list = tags.split(";");

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
        CEST ICI QUE CA FAIT CACA DANS LA COLLE
         */
        tags_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),"TEST!",Toast.LENGTH_SHORT).show();
                /*Object o = tags_listview.getItemAtPosition(position);
                String str=(String)o;
                tagReturn = "yolo";*/
                Toast.makeText(getApplicationContext(),"TEST",Toast.LENGTH_SHORT).show();
            }
        });



    }

}
