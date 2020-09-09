package com.example.projet_mucable.Display;

// Menu de partage de voc, 4.3 cdc

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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
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
import android.app.Activity;


public class PartageDisplay extends AppCompatActivity {

    private static final int RESULT = 1;
    private static final int CREATE_FILE = 1;
    private static final int PICK_FILE = 2;
    String langue;
    String tagsChosen;
    String wordsChosen;
    String wordsChosen2;


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

        // Recupération des spinners
        final Spinner spinnerL = findViewById(R.id.spinnerL);

        // Créaqtion de la liste des différentes langues
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Anglais");
        arrayList.add("Allemand");
        arrayList.add("Espagnol");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerL.setAdapter(arrayAdapter);
        spinnerL.setSelection(0);

        // Definition des comportements des spinners
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

        Cursor cursor;

        if(tagsChosen=="" && wordsChosen==""){
            cursor = CDB.query(
                    "t_"+langue,
                    null,
                    null,
                    null,
                    null,
                    null,
                    "CoefAppr ASC, Word ASC",
                    null

            );
        }
        else {
            cursor = CDB.query(
                    "t_" + langue,
                    null,
                    "Tag_1 IN ("+tagsChosen+") OR Tag_2 IN ("+tagsChosen+") OR Tag_3 IN ("+tagsChosen+") OR Tag_4 IN ("+tagsChosen+") OR Id_Word IN ("+wordsChosen2+")",
                    null,
                    null,
                    null,
                    "CoefAppr ASC, Word ASC",
                    null

            );
        }

        cursor.moveToFirst();
        for ( int i = 0; i < cursor.getCount(); i++ ) {
            text =text+ cursor.getString(1)+","+ cursor.getString(2)+","+
                        cursor.getString(3)+","+cursor.getString(4)+","+
                        cursor.getString(5)+","+cursor.getString(6)+","+cursor.getInt(7)+"\n";
            cursor.moveToNext();
        }

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

        try {
            inputStream = getContentResolver().openInputStream(uri);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                String[] parts = line.split(",");

                String SQL_exist = "SELECT COUNT(*) FROM t_"+langue+" WHERE Word='"+parts[0]+"' AND Translation='"+parts[1]+"'";
                //Toast.makeText(getApplicationContext(),"T: "+SQL_exist,Toast.LENGTH_LONG).show();

                // Count nn rows with each CoefAppr
                Cursor mCount= CDB.rawQuery(SQL_exist, null);
                int count = mCount.getCount();
                Toast.makeText(getApplicationContext(),"n° : "+count,Toast.LENGTH_LONG).show();
                if(!(count > 0)){
                    String insert = "INSERT INTO t_"+langue+" (Word, Translation, Tag_1, Tag_2, Tag_3, Tag_4, CoefAppr) VALUES ('"+parts[0]+"', '"+parts[1]+"', '"+parts[2]+"', '"+parts[3]+"', '"+parts[4]+"', '"+parts[5]+"', '"+parts[6]+"')";
                    CDB.execSQL(insert);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

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
        String comefrom  = preferences.getString("RESTARTFROM", "");


        if(comefrom == "tags"){
            tagsChosen = preferences.getString("TAG_CHOSEN", "");
            if(tagsChosen==""){
                listeTags.setText("aucun");
            }
            else{
                listeTags.setText(tagsChosen);
            }
        }
        else if(comefrom != ""){
            wordsChosen = preferences.getString("WORDS_CHOSEN", "");
            wordsChosen2 = preferences.getString("WORDS_CHOSEN2", "");
            if(wordsChosen==""){
                listeMots.setText("aucun");
            }
            else{
                listeMots.setText(wordsChosen);
            }
        }

        //Toast.makeText(getApplicationContext(),"T: "+tagsChosen,Toast.LENGTH_LONG).show();

    }

}
