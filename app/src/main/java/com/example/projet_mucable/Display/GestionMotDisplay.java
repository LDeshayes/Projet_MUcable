package com.example.projet_mucable.Display;

// Add un mot au cahier ou le modifie, 4.2 cdc image du milieu

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projet_mucable.R;

public class GestionMotDisplay extends Activity {

    String language;
    String mode;
    int key;
    String[] information_values;
    String[] new_information_values;

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
        tempTextView.setText(mode);

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
            Cursor cursor = CDB.query(
                    "t_" + language,
                    new String[]{"Word", "Translation", "Tag_1", "Tag_2", "Tag_3", "Tag_4"},
                    "Id_Word=" + key,
                    null,
                    null,
                    null,
                    null
            );
            cursor.moveToFirst();
            information_values = (cursor.getString(0) + ";" + cursor.getString(1) + ";" + cursor.getString(2) + ";" + cursor.getString(3) + ";" + cursor.getString(4) + ";" + cursor.getString(5)).split(";");
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

        tempEditText = findViewById(R.id.editText_word);
        tempEditText.setText("");
        tempEditText.setHint(information_values[0]);

        tempEditText = findViewById(R.id.editText_translation);
        tempEditText.setText("");
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
    }

    public void getNewValues() {
        EditText tempEditText;
        Button tempButton;

        String tempNewInfo;
        String tempStr;

        tempEditText = findViewById(R.id.editText_word);
        tempStr = tempEditText.getText().toString();
        if ( tempStr.equals("") ) {
            tempNewInfo = information_values[0];
        } else {
            tempNewInfo = tempStr;
        }

        tempEditText = findViewById(R.id.editText_translation);
        tempStr = tempEditText.getText().toString();
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

        new_information_values = tempNewInfo.split(";");
    }

    public void onClicClear(View view) {
        clear();
    }

    public void deleteKey() {
        String delete = "DELETE FROM t_"+language+" WHERE Id_Word="+key;
        CDB.execSQL(delete);
    }

    public void onClicValidation(View view) {
        String insert;

        getNewValues();

        if ( mode.equals("Modify") ) {
            deleteKey();
            insert = "INSERT INTO t_"+language+" (Id_Word, Word, Translation, Tag_1, Tag_2, Tag_3, Tag_4) VALUES ("+key+", '"+new_information_values[0]+"', '"+new_information_values[1]+"', '"+new_information_values[2]+"', '"+new_information_values[3]+"', '"+new_information_values[4]+"', '"+new_information_values[5]+"')";
            CDB.execSQL(insert);
            information_values = (new_information_values[0]+";"+new_information_values[1]+";"+new_information_values[2]+";"+new_information_values[3]+";"+new_information_values[4]+";"+new_information_values[5]).split(";");
            clear();
        } else if ( mode.equals("Add") ) {
            String word;
            String translation;
            EditText tempEditText;

            tempEditText = findViewById(R.id.editText_word);
            word = tempEditText.getText().toString();
            tempEditText = findViewById(R.id.editText_translation);
            translation = tempEditText.getText().toString();

            if ( ( word.length() == 0 ) || ( translation.length() == 0 ) ) {
                Toast.makeText(getApplicationContext(), "L'ajout demande au moins le mot et sa traduction !", Toast.LENGTH_SHORT).show();
            } else {
                insert = "INSERT INTO t_"+language+" (Word, Translation, Tag_1, Tag_2, Tag_3, Tag_4) VALUES ('"+new_information_values[0]+"', '"+new_information_values[1]+"', '"+new_information_values[2]+"', '"+new_information_values[3]+"', '"+new_information_values[4]+"', '"+new_information_values[5]+"')";
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
        onClicTag(0);
    }

    public void onClicTag2(View view) {
        onClicTag(1);
    }

    public void onClicTag3(View view) {
        onClicTag(2);
    }

    public void onClicTag4(View view) {
        onClicTag(3);
    }

    void onClicTag( int tag_number ) {
        Toast.makeText(getApplicationContext(),"Il n'est pas encore possible d'ajouter ou de modifier des tags!", Toast.LENGTH_LONG).show();

        // TODO Reword it with an onRestart
        /*getNewValues();

        Intent i = new Intent ( this, ChoixTagsDisplay.class );
        i.putExtra("LangueChoisie", language);
        i.putExtra("mode", mode);
        i.putExtra("key", key);
        i.putExtra("informations", newInfoString);
        i.putExtra("Origin", "GestionMotDisplay");
        i.putExtra("tag_number", tag_number);
        i.putExtra("hints", tempHint[0]+";"+tempHint[1]);

        startActivity( i );
        finish();*/
    }

}
