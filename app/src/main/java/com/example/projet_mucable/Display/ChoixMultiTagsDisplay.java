package com.example.projet_mucable.Display;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.projet_mucable.R;
import java.util.HashMap;
import java.util.Map;

public class ChoixMultiTagsDisplay extends AppCompatActivity {

    View tag_view;
    String tagsChosen = "";
    Map<String,String> tagsSelected =  new HashMap<String,String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choixmultitags_display);
        setupListView();
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
            int[] to = new int[] { R.id.tag};

            // création de l'objet SimpleCursorAdapter...
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.tag_listview, matrixCursor, from, to, 0);


            // ...qui va remplir l'objet ListView
            final ListView tags_listview = (ListView) findViewById(R.id.tags_listview);
            tags_listview.setAdapter(adapter);

            tags_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    tag_view = view;

                    if(tagsSelected.get(tag_list[position])!=null){
                        if(tagsSelected.get(tag_list[position]).equals("true")){
                            tagsSelected.put(tag_list[position],"false");
                            tag_view.setBackgroundResource(0);
                            //Toast.makeText(getApplicationContext(),"T->F: ",Toast.LENGTH_LONG).show();
                        }
                        else if(tagsSelected.get(tag_list[position]).equals("false")){
                            tagsSelected.put(tag_list[position],"true");
                            tag_view.setBackgroundResource(R.drawable.border);
                            //Toast.makeText(getApplicationContext(),"F->T: ",Toast.LENGTH_LONG).show();
                        }
                    }
                    else{
                        tagsSelected.put(tag_list[position],"true");
                        tag_view.setBackgroundResource(R.drawable.border);
                    }
                    //tagsChosen = tagsChosen + tag_list[position];
                }
            });
        }
    }

    public void onClickChoiceTags(View view) {

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        tagsChosen = "";
        int i =0;
        for (Map.Entry<String, String> entry : tagsSelected.entrySet()) {

            if(entry.getValue().equals("true")){
                if(i>0)
                    tagsChosen = tagsChosen+",";
                tagsChosen = tagsChosen +"'"+entry.getKey()+"'";
                i+=1;
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogDarker));
        final SharedPreferences.Editor NEW_TAGSLIST = preferences.edit();
        NEW_TAGSLIST.putString("RESTARTFROMTAGS", "true");
        NEW_TAGSLIST.putString("RESTARTFROMMOTS", "false");


        if ( !tagsChosen.equals("") ) {
            builder.setMessage("Êtes vous sur(e) de vouloir choisir ces tags?")
                    .setCancelable(true)
                    .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            NEW_TAGSLIST.putString("TAG_CHOSEN", tagsChosen);
                            NEW_TAGSLIST.apply();
                            finish();
                        }
                    })
                    .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            SharedPreferences.Editor NEW_TAGSLIST = preferences.edit();
                            NEW_TAGSLIST.putString("TAG_CHOSEN", "");
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

        } else {
            builder.setMessage("Vous avez choisit aucun tag !")
                    .setCancelable(true)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            SharedPreferences.Editor NEW_TAGSLIST = preferences.edit();
                            NEW_TAGSLIST.putString("TAG_CHOSEN", "");
                            NEW_TAGSLIST.apply();
                            finish();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

        }

    }

}