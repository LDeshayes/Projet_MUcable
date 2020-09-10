package com.example.projet_mucable.Display;

import android.annotation.SuppressLint;
import android.content.Intent;
import java.util.ArrayList;
import java.util.Arrays;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projet_mucable.CustomAdapter;
import com.example.projet_mucable.R;



public class StatsActivity extends AppCompatActivity {

    String language = "Anglais";
    ListView w_words_listview;
    ListView m_words_listview;
    ListView b_words_listview;
    Integer[][] key_list_final, key_list;
    String[][] words_list, translations_list, tags_list;
    int rowCount;
    /*int key = -1; // future key pour repérer le word à modifier
    View key_view;*/
    SQLiteDatabase CDB;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats_dislay);

        getLanguage();
        setupDB();

    }

    void getLanguage() {

        // Recupération des spinners
        final Spinner spinnerL = findViewById(R.id.spinnerLang);

        // Créaqtion de la liste des différentes langues
        /*ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Anglais");
        arrayList.add("Allemand");
        arrayList.add("Espagnol");*/

        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayList);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.language_array, android.R.layout.simple_spinner_item);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerL.setAdapter(arrayAdapter);
        spinnerL.setSelection(0);

        // Definition des comportements des spinners
        spinnerL.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                language = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });


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

        int t1;
        if( (rowCount/3)%3 == 0){
            t1 = rowCount/3;
        }
        else{
            t1 = (int)Math.round((rowCount/3.0) + 0.5);
        }
        int t2 = (rowCount-t1)/2;
        int t3 = rowCount-t2-t1;

        key_list_final = new Integer[3][t1]; // too many lines
        key_list = new Integer[3][t1];
        words_list = new String[3][t1];
        translations_list = new String[3][t1];
        tags_list = new String[3][t1];

        cursor.moveToFirst();

        for ( int i=0; i < rowCount; i++ ) {


            Double dK = i / (rowCount / 3.0);
            int k = dK.intValue();

            Double dJ = i % (rowCount / 3.0);
            int j = dJ.intValue();

            //key_list_final[i/(rowCount/3)][j] = cursor.getInt(0);
            //key_list[i/(rowCount/3)][j] = cursor.getInt(0);
            words_list[k][j] = cursor.getString(1);
            translations_list[k][j] = cursor.getString(2);
            tags_list[k][j] = printNAN ( cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6) );
            cursor.moveToNext();

        }
        cursor.close();



        w_words_listview = (ListView) findViewById(R.id.worst_words);
        m_words_listview = (ListView) findViewById(R.id.medium_words);
        b_words_listview = (ListView) findViewById(R.id.best_words);

        CustomAdapter customAdapter_w = new CustomAdapter(getApplicationContext(), words_list[0], translations_list[0], tags_list[0]);
        CustomAdapter customAdapter_m = new CustomAdapter(getApplicationContext(), Arrays.copyOfRange(words_list[1],0, t3), Arrays.copyOfRange(translations_list[1],0,t3), Arrays.copyOfRange(tags_list[1],0,t3));
        CustomAdapter customAdapter_b = new CustomAdapter(getApplicationContext(), Arrays.copyOfRange(words_list[2],0, t2), Arrays.copyOfRange(translations_list[1],0,t2), Arrays.copyOfRange(tags_list[2],0,t2));

        w_words_listview.setAdapter(customAdapter_w);
        m_words_listview.setAdapter(customAdapter_m);
        b_words_listview.setAdapter(customAdapter_b);


        /*words_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int pos, long id) {
                if ( key != -1 ) {
                    key_view.setBackgroundResource(0);
                }
                key_view = v;
                key_view.setBackgroundResource(R.drawable.border);
                key = key_list[pos];
            }
        });*/
    }

    public void resetTests(View view) {

        /*@SuppressLint("WrongConstant") SQLiteDatabase CDB = openOrCreateDatabase("CDB.db", SQLiteDatabase.CREATE_IF_NECESSARY, null );
        ContentValues cv = new ContentValues();
        cv.put("CoefAppr",0);
        CDB.update("t_Anglais", cv, "", null);
        CDB.update("t_Allemand", cv, "", null);
        CDB.update("t_Espagnol", cv, "", null);*/

    }


}
