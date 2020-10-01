package com.example.projet_mucable.Display;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projet_mucable.CahierAdapter;
import com.example.projet_mucable.CustomAdapter;
import com.example.projet_mucable.R;

import java.util.HashMap;
import java.util.Map;

public class ChoixMotsDisplay extends AppCompatActivity {

    View word_view;
    ListView words_listview;
    String wordsChosen = "";
    String wordsChosen2 = "";
    String language;
    int[] key_list;
    String[] words_list, translations_list, tags_list;
    Map<String,String> motsSelected =  new HashMap<String,String>();
    Map<String,String> mots =  new HashMap<String,String>();
    SQLiteDatabase CDB;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choixmots_display);

        Intent i = getIntent();
        language = i.getStringExtra("LangueChoisie");
        CDB = openOrCreateDatabase("CDB.db", SQLiteDatabase.CREATE_IF_NECESSARY, null );
        loadDB();
        setupListView();
    }

    void loadDB() {


        Cursor cursor = CDB.query(
                "t_Mot",
                null,
                "Langue LIKE '"+language+"'",
                null,
                null,
                null,
                "Word ASC",
                null
        );

        int rowCount = cursor.getCount();

        /*key_list_final = new int[rowCount];*/
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
        cursor.close();

    }

    Map<String, String> getTagsColor(){

        Map<String,String> tagColMap = new HashMap<>();

        // On every tag lines
        for(String tl : tags_list){
            // We get every tag of the line
            String[] tmpTags = tl.split(" - ");
            for(String t : tmpTags){
                // We check they arent null
                if(t != null && !t.equals("") && !t.equals("NAN") && !t.equals("NAN_NULL")){
                    tagColMap.put(t,"");
                }
            }
        }


        // Of all the tag, we get the corresponding color
        for(String tag: tagColMap.keySet()){
            Cursor c = CDB.rawQuery("SELECT Couleur FROM t_TagColor WHERE Nom LIKE '"+tag+"'", null);
            c.moveToFirst();
            //Log.d("testtest",c.getString(0));
            tagColMap.put(tag,c.getString(0));
        }

        return tagColMap;
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

    void setupListView() {

        /*words_listview = (ListView) findViewById(R.id.mots_listview);
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), words_list, translations_list, tags_list);
        words_listview.setAdapter(customAdapter);*/

        words_listview = (ListView) findViewById(R.id.mots_listview);
        CahierAdapter customAdapter = new CahierAdapter(getApplicationContext(), words_list, translations_list, tags_list, getTagsColor());
        words_listview.setAdapter(customAdapter);


        words_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                word_view = view;

                if(motsSelected.get(key_list[position]+"")!=null){
                    if(motsSelected.get(key_list[position]+"").equals("true")){
                        motsSelected.put(key_list[position]+"","false");
                        mots.put(key_list[position]+"",words_list[position]+"");
                        word_view.setBackgroundResource(0);
                        //Toast.makeText(getApplicationContext(),"T->F: "+key_list[position],Toast.LENGTH_LONG).show();
                    }
                    else if(motsSelected.get(key_list[position]+"").equals("false")){
                        motsSelected.put(key_list[position]+"","true");
                        mots.put(key_list[position]+"",words_list[position]+"");
                        word_view.setBackgroundResource(R.drawable.border);
                        //Toast.makeText(getApplicationContext(),"F->T: "+key_list[position],Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    motsSelected.put(key_list[position]+"","true");
                    mots.put(key_list[position]+"",words_list[position]+"");
                    word_view.setBackgroundResource(R.drawable.border);
                    //Toast.makeText(getApplicationContext(),"NULL->T: "+key_list[position],Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    public void onClickChoiceMots(View view) {

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        wordsChosen = "";
        wordsChosen2 = "";
        int i =0;
        for (Map.Entry<String, String> entry : motsSelected.entrySet()) {

            if(entry.getValue().equals("true")){
                if(i>0){
                    wordsChosen = wordsChosen+",";
                    wordsChosen2 = wordsChosen2+",";
                }
                wordsChosen = wordsChosen +"'"+mots.get(entry.getKey())+"'";
                wordsChosen2 = wordsChosen2 +"'"+entry.getKey()+"'";
                i+=1;
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogDarker));
        final SharedPreferences.Editor NEW_MOTSLIST = preferences.edit();
        NEW_MOTSLIST.putString("RESTARTFROMMOTS", "true");
        NEW_MOTSLIST.putString("RESTARTFROMTAGS", "false");


        if ( !wordsChosen.equals("") ) {
            builder.setMessage("Êtes vous sur(e) de vouloir choisir ces mots?")
                    .setCancelable(true)
                    .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            NEW_MOTSLIST.putString("WORDS_CHOSEN", wordsChosen);
                            NEW_MOTSLIST.putString("WORDS_CHOSEN2", wordsChosen2);
                            NEW_MOTSLIST.commit();
                            finish();
                        }
                    })
                    .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            SharedPreferences.Editor NEW_MOTSLIST = preferences.edit();
                            NEW_MOTSLIST.putString("WORDS_CHOSEN", "");
                            NEW_MOTSLIST.putString("WORDS_CHOSEN2", "");
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

        } else {
            builder.setMessage("Vous avez choisit aucun mot !")
                    .setCancelable(true)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            SharedPreferences.Editor NEW_MOTSLIST = preferences.edit();
                            NEW_MOTSLIST.putString("WORDS_CHOSEN", "");
                            NEW_MOTSLIST.putString("WORDS_CHOSEN2", "");
                            NEW_MOTSLIST.commit();
                            finish();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

        }

    }




}