package com.example.projet_mucable.Display;

// Add a word to cahier or modify it

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.example.projet_mucable.R;

public class GestionMotDisplay extends AppCompatActivity {

    String language;
    String mode;
    int key;
    String[] information_values;
    String[] new_information_values;
    int tag_changed;

    SQLiteDatabase CDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestionmot_display);

        getDB();
        getIntentValues();
        setupDisplayValues();
    }

    @SuppressLint("WrongConstant")
    void getDB() {
        CDB = openOrCreateDatabase("CDB.db", SQLiteDatabase.CREATE_IF_NECESSARY, null );
        CDB.execSQL("PRAGMA foreign_keys = ON;");
    }

    void getIntentValues() {
        Intent i = getIntent();
        language = i.getStringExtra("LangueChoisie");
        mode = i.getStringExtra("mode");
        if ( mode.equals("Add") ) {
            information_values = "Mot;Traduction;NAN;NAN;NAN;NAN".split(";");
        } else if ( mode.equals("Modify") ) {
            key = i.getIntExtra("key", -1);
            getKeyRow();
        }
    }

    void setupDisplayValues() {
        TextView tempTextView;
        EditText tempEditText;
        Button tempButton;

        tempTextView = findViewById(R.id.textView_mode);
        if ( mode.equals("Modify") ) {
            tempTextView.setText("Modifier mot");
        } else {
            tempTextView.setText("Ajouter mot");
        }

        tempEditText = findViewById(R.id.editText_word);
        tempEditText.setHint(information_values[0]);

        tempEditText = findViewById(R.id.editText_translation);
        tempEditText.setHint(information_values[1]);

        tempButton = findViewById(R.id.button_Tag_1);
        if ( information_values[2].equals("NAN") ) {
            tempButton.setText(" ");
        } else {
            tempButton.setText(information_values[2]);
        }

        tempButton = findViewById(R.id.button_Tag_2);
        if ( information_values[3].equals("NAN") ) {
            tempButton.setText(" ");
        } else {
            tempButton.setText(information_values[3]);
        }

        tempButton = findViewById(R.id.button_Tag_3);
        if ( information_values[4].equals("NAN") ) {
            tempButton.setText(" ");
        } else {
            tempButton.setText(information_values[4]);
        }

        tempButton = findViewById(R.id.button_Tag_4);
        if ( information_values[5].equals("NAN") ) {
            tempButton.setText(" ");
        } else {
            tempButton.setText(information_values[5]);
        }

        tempButton = findViewById(R.id.button_delete);
        if ( mode.equals("Modify") ) {
            tempButton.setVisibility(View.VISIBLE);
        } else if ( mode.equals("Add") ) {
            tempButton.setVisibility(View.GONE);
        }
    }

    void getKeyRow() {
        if (mode.equals("Modify")) {

            Cursor cursor;
            if(language.equals("Archives")){

                StringBuilder allNonArch = new StringBuilder();

                // We get all the languages used
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                String[] prefLangues = preferences.getString("Langues", "Anglais;Allemand;Espagnol;").split(";");

                // We get a string like: 'Anglais','Espagnol','Allemand'
                boolean first = true;
                for(String s : prefLangues){
                    if(!s.equals("Archives") && first){
                        allNonArch.append("'").append(s).append("'");
                        first = false;
                    }
                    else{
                        allNonArch.append(",'").append(s).append("'");
                    }
                }

                // Get all words that are not from non archived languages
                cursor = CDB.query(
                        /*"t_"+language,*/"t_Mot",
                        new String[]{"Word", "Translation", "Tag_1", "Tag_2", "Tag_3", "Tag_4", "Langue"},
                        "Langue NOT IN ("+ allNonArch.toString() +") AND Id_Word=" + key,
                        null,
                        null,
                        null,
                        null,
                        null
                );
            }
            else {
                cursor = CDB.query(
                        "t_Mot",
                        new String[]{"Word", "Translation", "Tag_1", "Tag_2", "Tag_3", "Tag_4", "Langue"},
                        "Langue LIKE '"+language+"' AND Id_Word=" + key,
                        null,
                        null,
                        null,
                        null
                );
            }
            cursor.moveToFirst();
            information_values = (cursor.getString(0) + ";" + cursor.getString(1) + ";" + cursor.getString(2) + ";" + cursor.getString(3) + ";" + cursor.getString(4) + ";" + cursor.getString(5)).split(";");
            language = cursor.getString(6);
            cursor.close();


        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent ( this, CahierDisplay.class );
        i.putExtra( "LangueChoisie", language );
        startActivity( i );
        finish();
    }

    void clear() {
        EditText tempEditText;
        Button tempButton;
        String[] inf_shown = information_values;

        int i=0;
        for(String s : inf_shown){
            inf_shown[i] = s.replaceAll("''", "'");
            i++;
        }


        tempEditText = findViewById(R.id.editText_word);
        tempEditText.setText("");
        tempEditText.setHint(inf_shown[0]);

        tempEditText = findViewById(R.id.editText_translation);
        tempEditText.setText("");
        tempEditText.setHint(inf_shown[1]);

        tempButton = findViewById(R.id.button_Tag_1);
        if ( information_values[2].equals("NAN") ) {
            tempButton.setText(" ");
        } else {
            tempButton.setText(information_values[2]);
        }

        tempButton = findViewById(R.id.button_Tag_2);
        if ( information_values[3].equals("NAN") ) {
            tempButton.setText(" ");
        } else {
            tempButton.setText(information_values[3]);
        }

        tempButton = findViewById(R.id.button_Tag_3);
        if ( information_values[4].equals("NAN") ) {
            tempButton.setText(" ");
        } else {
            tempButton.setText(information_values[4]);
        }

        tempButton = findViewById(R.id.button_Tag_4);
        if ( information_values[5].equals("NAN") ) {
            tempButton.setText(" ");
        } else {
            tempButton.setText(information_values[5]);
        }
    }

    public void getNewValues() {
        EditText tempEditText;
        Button tempButton;

        String tempNewInfo;
        String tempStr;

        tempEditText = findViewById(R.id.editText_word);
        tempStr = tempEditText.getText().toString();
        if ( !language.equals("Allemand") ) {
            tempStr = tempStr.toLowerCase();
        }
        if ( tempStr.equals("") ) {
            tempNewInfo = information_values[0];
        } else {
            tempNewInfo = tempStr;
        }

        tempEditText = findViewById(R.id.editText_translation);
        tempStr = tempEditText.getText().toString().toLowerCase();
        if ( tempStr.equals("") ) {
            tempNewInfo = tempNewInfo + ";" + information_values[1];
        } else {
            tempNewInfo = tempNewInfo + ";" + tempStr;
        }

        tempButton = findViewById(R.id.button_Tag_1);
        tempStr = tempButton.getText().toString();
        if ( tempStr.equals(" ") ) {
            tempNewInfo = tempNewInfo + ";NAN";
        } else {
            tempNewInfo = tempNewInfo + ";" + tempStr;
        }

        tempButton = findViewById(R.id.button_Tag_2);
        tempStr = tempButton.getText().toString();
        if ( tempStr.equals(" ") ) {
            tempNewInfo = tempNewInfo + ";NAN";
        } else {
            tempNewInfo = tempNewInfo + ";" + tempStr;
        }

        tempButton = findViewById(R.id.button_Tag_3);
        tempStr = tempButton.getText().toString();
        if ( tempStr.equals(" ") ) {
            tempNewInfo = tempNewInfo + ";NAN";
        } else {
            tempNewInfo = tempNewInfo + ";" + tempStr;
        }

        tempButton = findViewById(R.id.button_Tag_4);
        tempStr = tempButton.getText().toString();
        if ( tempStr.equals(" ") ) {
            tempNewInfo = tempNewInfo + ";NAN";
        } else {
            tempNewInfo = tempNewInfo + ";" + tempStr;
        }

        tempNewInfo = tempNewInfo.replace("'","''");
        new_information_values = tempNewInfo.split(";");
    }

    public void onClicClear(View view) {
        clear();
    }

    public void deleteKey() {
        //String delete = "DELETE FROM t_"+language+" WHERE Id_Word="+key;
        String delete = "DELETE FROM t_Mot WHERE Langue LIKE '"+language+"' AND Id_Word="+key;
        CDB.execSQL(delete);
    }

    public void onClicValidation(View view) {
        String insert;

        getNewValues();

        if ( mode.equals("Modify") ) {
            deleteKey();
            //insert = "INSERT INTO t_"+language+" (Id_Word, Word, Translation, Tag_1, Tag_2, Tag_3, Tag_4) VALUES ("+key+", '"+new_information_values[0]+"', '"+new_information_values[1]+"', '"+new_information_values[2]+"', '"+new_information_values[3]+"', '"+new_information_values[4]+"', '"+new_information_values[5]+"')";
            insert = "INSERT INTO t_Mot (Id_Word, Word, Translation, Tag_1, Tag_2, Tag_3, Tag_4, Langue) VALUES ("+key+", '"+new_information_values[0]+"', '"+new_information_values[1]+"', '"+new_information_values[2]+"', '"+new_information_values[3]+"', '"+new_information_values[4]+"', '"+new_information_values[5]+"', '"+language+"')";
            CDB.execSQL(insert);
            information_values = (new_information_values[0]+";"+new_information_values[1]+";"+new_information_values[2]+";"+new_information_values[3]+";"+new_information_values[4]+";"+new_information_values[5]).split(";");
            clear();
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogDarker));
            builder.setMessage("Modifications réussies !")
                    .setCancelable(true)
                    .setPositiveButton("Ok", null);
            AlertDialog alert = builder.create();
            alert.show();
        } else if ( mode.equals("Add") ) {
            String word;
            String translation;
            EditText tempEditText;

            tempEditText = findViewById(R.id.editText_word);
            word = tempEditText.getText().toString();
            tempEditText = findViewById(R.id.editText_translation);
            translation = tempEditText.getText().toString();

            if ( ( word.length() == 0 ) || ( translation.length() == 0 ) ) {
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogDarker));
                builder.setMessage("L'ajout demande au moins le mot et sa traduction !")
                        .setCancelable(true)
                        .setPositiveButton("Ok", null);
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                insert = "INSERT INTO t_Mot (Word, Translation, Tag_1, Tag_2, Tag_3, Tag_4, Langue) VALUES ('"+new_information_values[0]+"', '"+new_information_values[1]+"', '"+new_information_values[2]+"', '"+new_information_values[3]+"', '"+new_information_values[4]+"', '"+new_information_values[5]+"', '"+language+"')";
                CDB.execSQL(insert);

                Intent i = new Intent ( this, CahierDisplay.class );
                i.putExtra( "LangueChoisie", language );
                startActivity( i );
                finish();
            }
        }
    }

    public void onClicDelete(View view) {
        if ( mode.equals("Modify") ) {
            deleteKey();

            Intent i = new Intent ( this, CahierDisplay.class );
            i.putExtra( "LangueChoisie", language );
            startActivity( i );
            finish();
        }
    }

    public void onClicTag1(View view) {
        onClicTag(1);
    }

    public void onClicTag2(View view) {
        onClicTag(2);
    }

    public void onClicTag3(View view) {
        onClicTag(3);
    }

    public void onClicTag4(View view) {
        onClicTag(4);
    }

    void onClicTag( int tag_number ) {

        tag_changed = tag_number;

        Intent i = new Intent ( this, ChoixTagsDisplay.class );
        startActivity( i );
    }

    public void onRestart() {
        super.onRestart();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String tag = preferences.getString("TAG_CHOSEN", "EMPTY_NULL");

        Button button_Tag_1 = findViewById(R.id.button_Tag_1);
        String tag_1 = button_Tag_1.getText().toString();
        Button button_Tag_2 = findViewById(R.id.button_Tag_2);
        String tag_2 = button_Tag_2.getText().toString();
        Button button_Tag_3 = findViewById(R.id.button_Tag_3);
        String tag_3 = button_Tag_3.getText().toString();
        Button button_Tag_4 = findViewById(R.id.button_Tag_4);
        String tag_4 = button_Tag_4.getText().toString();

        assert tag != null;
        if ( tag.equals(tag_1) || tag.equals(tag_2) || tag.equals(tag_3) || tag.equals(tag_4) ) {
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogDarker));
            builder.setMessage("Ce tag est déjà sélectionné pour ce mot !")
                    .setCancelable(true)
                    .setPositiveButton("Ok", null);
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            if ( tag_changed == 1 ) {
                button_Tag_1.setText(tag);
            } else if ( tag_changed == 2 ) {
                button_Tag_2.setText(tag);
            } else if ( tag_changed == 3 ) {
                button_Tag_3.setText(tag);
            } else if ( tag_changed == 4 ) {
                button_Tag_4.setText(tag);
            }
        }
    }

    public void onClicTagClear1(View view) {
        Button button_Tag = findViewById(R.id.button_Tag_1);
        button_Tag.setText(" ");
    }

    public void onClicTagClear2(View view) {
        Button button_Tag = findViewById(R.id.button_Tag_2);
        button_Tag.setText(" ");
    }

    public void onClicTagClear3(View view) {
        Button button_Tag = findViewById(R.id.button_Tag_3);
        button_Tag.setText(" ");
    }

    public void onClicTagClear4(View view) {
        Button button_Tag = findViewById(R.id.button_Tag_4);
        button_Tag.setText(" ");
    }

}
