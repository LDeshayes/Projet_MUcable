package com.example.projet_mucable.Display;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
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
import com.example.projet_mucable.TagAdapter;

import java.util.HashMap;
import java.util.Map;

public class ChoixMultiTagsDisplay extends AppCompatActivity {

    View tag_view;
    String tagsChosen = "";
    Map<String,String> tagsSelected =  new HashMap<String,String>();
    SQLiteDatabase CDB;
    String[] tag_listDB;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choixmultitags_display);

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
        cTag.close();

        setupListView();
    }

    Map<String, String> getTagsColor(){

        Map<String,String> tagColMap = new HashMap<>();

        // on prends toutes les lignes de tags
        for(String tl : tag_listDB){
            // on recupere les tags de chaque lignes
            String[] tmpTags = tl.split(" - ");
            for(String t : tmpTags){
                // on verifier qu'ils sont pas nuls
                if(t != null && !t.equals("") && !t.equals("NAN") && !t.equals("NAN_NULL")){
                    tagColMap.put(t,"");
                }
            }
        }

        // de tout les tags recupérés on récupere la couleur
        for(String tag: tagColMap.keySet()){
            Cursor c = CDB.rawQuery("SELECT Couleur FROM t_TagColor WHERE Nom LIKE '"+tag+"'", null);
            c.moveToFirst();
            //Log.d("testtest",c.getString(0));
            tagColMap.put(tag,c.getString(0));
            c.close();
        }

        return tagColMap;
    }

    void setupListView() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String tags = preferences.getString("TAG_LIST", "EMPTY_NULL");
        final String[] tag_list = tag_listDB;

        if ( !tag_list[0].equals("EMPTY_NULL") ) {


            // création de l'objet TagAdapter...
            TagAdapter adapter = new TagAdapter(getApplicationContext(), tag_listDB, getTagsColor());

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
                            NEW_TAGSLIST.putString("TAGS_CHOSEN", tagsChosen);
                            NEW_TAGSLIST.apply();
                            finish();
                        }
                    })
                    .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            SharedPreferences.Editor NEW_TAGSLIST = preferences.edit();
                            NEW_TAGSLIST.putString("TAGS_CHOSEN", "");
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
                            NEW_TAGSLIST.putString("TAGS_CHOSEN", "");
                            NEW_TAGSLIST.apply();
                            finish();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

        }

    }

}