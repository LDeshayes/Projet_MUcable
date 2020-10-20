package com.example.projet_mucable.Display;

// Share word list

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.projet_mucable.R;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.widget.Toast;


public class PartageDisplay extends AppCompatActivity {

    private static final int RESULT = 1;
    private static final int CREATE_FILE = 1;
    private static final int PICK_FILE = 2;
    String langue;
    String tagsChosen = "";
    String wordsChosen = "";
    String wordsChosen2;

    List<String> listLangues = new ArrayList<>();
    String prefLangues;
    SharedPreferences preferences;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partage_display);
        //Intent this_i = getIntent();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, RESULT);
        }

        findViewById(R.id.buttonDL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createFile();
            }
        });

        findViewById(R.id.buttonIMP).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFile();
            }
        });

        // Get spinners
        final Spinner spinnerL = findViewById(R.id.spinnerL);

        // Get lang
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        // We get all the languages used
        prefLangues = preferences.getString("Langues", "Anglais;Allemand;Espagnol;");
        // we get them ready to be used in adapter
        String[] langues = Arrays.copyOfRange(prefLangues.split(";"), 0, ((prefLangues.split(";")).length)); // -1 ?
        listLangues = Arrays.asList(langues);

        // Create language list
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.addAll(listLangues);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayList);
        //ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.language_array, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerL.setAdapter(arrayAdapter);
        spinnerL.setSelection(0);

        // Define spinners behavior
        spinnerL.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                langue = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor prefEdit = preferences.edit();
        prefEdit.putString("WORDS_CHOSEN", "");
        prefEdit.putString("TAGS_CHOSEN", "");
        prefEdit.apply();

        /*if(tagsChosen!=null && !tagsChosen.equals("")){
            TextView listeTags = findViewById(R.id.textViewListeTags);
            listeTags.setText(tagsChosen);
        }*/

    }


    private void createFile(/*Uri pickerInitialUri*/) {

        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TITLE, "wordsexp.txt");

        // Optionally, specify a URI for the directory that should be opened in
        // the system file picker when your app creates the document.
        //intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri);

        startActivityForResult(intent, CREATE_FILE);
    }

    private void openFile(/*Uri pickerInitialUri*/) {

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");

        // Optionally, specify a URI for the file that should appear in the
        // system file picker when it loads.
        //intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri);

        startActivityForResult(intent, PICK_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREATE_FILE) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    if (data != null
                            && data.getData() != null) {
                        writeInFile(data.getData(), "");
                    }
                    break;
                case Activity.RESULT_CANCELED:
                    break;
            }
        }
        if (requestCode == PICK_FILE) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    if (data != null && data.getData() != null) {
                        readInFile(data.getData());
                    }
                    break;
                case Activity.RESULT_CANCELED:
                    break;
            }
        }
    }

    private void writeInFile(@NonNull Uri uri, @NonNull String text) {
        OutputStream outputStream;
        @SuppressLint("WrongConstant") SQLiteDatabase CDB = openOrCreateDatabase("CDB.db", SQLiteDatabase.CREATE_IF_NECESSARY, null );


        // Adding the color corresponding to the tag that will be exported
        HashMap<String, String> mapColor = new HashMap<>();
        // Run trough CDB, fill the map
        Cursor cursorTag = CDB.query(
                "t_TagColor", null, null, null, null, null, "Nom ASC", null);
        cursorTag.moveToFirst();
        while(!cursorTag.isAfterLast()){
            if(!cursorTag.getString(1).equals("NAN") && !cursorTag.getString(1).equals("NAN_NULL"))
                mapColor.put(cursorTag.getString(1), cursorTag.getString(2));
            Log.d("testtest", cursorTag.getString(1)+"-"+cursorTag.getString(2));

            cursorTag.moveToNext();

        }
        cursorTag.close();

        Cursor cursor;
        if((tagsChosen==null || tagsChosen.equals("")) && (wordsChosen==null || wordsChosen.equals(""))){
            cursor = CDB.query(
                    /*"t_"+langue,*/"t_Mot",
                    null,
                    "Langue LIKE '"+langue+"'",
                    null,
                    null,
                    null,
                    "CoefAppr ASC, Word ASC",
                    null

            );
        }
        else {
            cursor = CDB.query(
                    /*"t_" + langue,*/"t_Mot",
                    null,
                    "Langue LIKE '"+langue+"' AND (Tag_1 IN ("+tagsChosen+") OR Tag_2 IN ("+tagsChosen+") OR Tag_3 IN ("+tagsChosen+") OR Tag_4 IN ("+tagsChosen+")) OR Id_Word IN ("+wordsChosen2+")",
                    null,
                    null,
                    null,
                    "CoefAppr ASC, Word ASC",
                    null

            );
        }


        cursor.moveToFirst();
        for ( int i = 0; i < cursor.getCount(); i++ ) {
            text =text+ cursor.getString(1)+";"+cursor.getString(2)+";"+
                        cursor.getString(3)+";"+cursor.getString(4)+";"+
                        cursor.getString(5)+";"+cursor.getString(6)+";"+
                        mapColor.get(cursor.getString(3))+";"+mapColor.get(cursor.getString(4))+";"+
                        mapColor.get(cursor.getString(5))+";"+mapColor.get(cursor.getString(6))+";"+
                        "\n";
            cursor.moveToNext();
        }
        cursor.close();

        try {
            outputStream = getContentResolver().openOutputStream(uri);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));
            bw.write(text/*"Ten,Dix,Nombre,Chiffre,NAN,NAN,0"*/);
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void readInFile(@NonNull Uri uri) {
        InputStream inputStream;
        @SuppressLint("WrongConstant") SQLiteDatabase CDB = openOrCreateDatabase("CDB.db", SQLiteDatabase.CREATE_IF_NECESSARY, null );

        HashMap<String, String> mapColor = new HashMap<>();

        try {
            inputStream = getContentResolver().openInputStream(uri);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            Map<String, String> mapTags = new HashMap<>();

            while ((line = br.readLine()) != null) {
                System.out.println(line);
                String line2 = line;
                line = line.replaceAll("'","''");
                String[] parts = line.split(";");

                //String SQL_exist = "SELECT COUNT(*) FROM t_"+langue+" WHERE Word='"+parts[0]+"' AND Translation='"+parts[1]+"'";
                String SQL_exist = "SELECT * FROM t_Mot WHERE Langue='"+langue+"' AND Word='"+parts[0]+"' AND Translation='"+parts[1]+"'";

                // Count nn rows with each CoefAppr
                Cursor mCount = CDB.rawQuery(SQL_exist, null);
                int count = mCount.getCount();
                mCount.close();
                if(!(count > 0)){
                    //String insert = "INSERT INTO t_"+langue+" (Word, Translation, Tag_1, Tag_2, Tag_3, Tag_4, CoefAppr) VALUES ('"+parts[0]+"', '"+parts[1]+"', '"+parts[2]+"', '"+parts[3]+"', '"+parts[4]+"', '"+parts[5]+"', '"+parts[6]+"')";
                    String insert = "INSERT INTO t_Mot (Word, Translation, Tag_1, Tag_2, Tag_3, Tag_4, CoefAppr, Langue) VALUES ('"+parts[0]+"', '"+parts[1]+"', '"+parts[2]+"', '"+parts[3]+"', '"+parts[4]+"', '"+parts[5]+"', '"+0+"', '"+langue+"')";
                    CDB.execSQL(insert);
                }

                // Add corresponding tag
                for(int i=2; i<6; i++ ){

                    if(!parts[i].equals("NAN") && !parts[i].equals("NAN_NULL")) {
                            //String insert = "INSERT INTO t_"+langue+" (Word, Translation, Tag_1, Tag_2, Tag_3, Tag_4, CoefAppr) VALUES ('"+parts[0]+"', '"+parts[1]+"', '"+parts[2]+"', '"+parts[3]+"', '"+parts[4]+"', '"+parts[5]+"', '"+parts[6]+"')";
                            mapTags.put(parts[i], "");
                            mapColor.put(parts[i], parts[i+4]);
                    }
                }

            }
            for(String t : mapTags.keySet()){

                String SQL_exist_T = "SELECT * FROM t_TagColor WHERE Nom='"+t+"'";

                Cursor mCount_t = CDB.rawQuery(SQL_exist_T, null);
                int count_t = mCount_t.getCount();
                mCount_t.close();

                if(!(count_t > 0)){
                    String insert = "INSERT INTO t_TagColor (Nom, Couleur) VALUES ('"+t+"', '"+mapColor.get(t)+"')";
                    //Log.d("testtest",""+t);
                    CDB.execSQL(insert);
                }


            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        Toast.makeText(getApplicationContext(), "Importation effectuée ", Toast.LENGTH_SHORT).show();


    }

    public void onClicTag(View view) {
        Intent i = new Intent ( this, ChoixMultiTagsDisplay.class);
        startActivity( i );
    }

    public void onClicMot(View view) {
        Intent i = new Intent ( this, ChoixMotsDisplay.class);
        i.putExtra("LangueChoisie", langue);
        startActivity( i );
    }

    public void onRestart() {
        super.onRestart();

        TextView listeTags = findViewById(R.id.textViewListeTags);
        TextView listeMots = findViewById(R.id.textViewMots);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String comefromT  = preferences.getString("RESTARTFROMTAGS", "false");
        String comefromM  = preferences.getString("RESTARTFROMMOTS", "false");


        if(comefromT.equals("true")){
            tagsChosen = preferences.getString("TAGS_CHOSEN", "");
            if(tagsChosen.equals("")){
                listeTags.setText("aucun");
            }
            else{
                listeTags.setText(tagsChosen.replaceAll("'","").replaceAll(",",", "));
            }
        }

        if(comefromM.equals("true")){
            wordsChosen = preferences.getString("WORDS_CHOSEN", "");
            wordsChosen2 = preferences.getString("WORDS_CHOSEN2", "");
            if(wordsChosen.equals("")){
                listeMots.setText("aucun");
            }
            else{
                listeMots.setText(wordsChosen.replaceAll("'","").replaceAll(",",", "));
            }
        }

    }

}
