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
    String[] tempInfo;
    String[] newInfo;

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
            tempInfo = "Mot;Traduction; ; ; ; ".split(";");
        } else if ( mode.equals("Modify") ) {
            key = i.getIntExtra("key", -1);
            String Origin = i.getStringExtra("Origin");
            if ( Origin.equals("CahierDisplay") ) {
                getKeyRow();
            } else if ( Origin.equals("ChoixTagsDisplay") ) {
                tempInfo = i.getStringExtra("informations").split(";");
            }
        }
    }

    void getKeyRow() {

        if ( mode.equals("Add") ) {
            tempInfo = "Mot;Traduction; ; ; ; ".split(";");
        } else if ( mode.equals("Modify") ) {
            Cursor cursor = CDB.query(
                    "t_"+language,
                    new String[]{"Word", "Translation", "Tag_1", "Tag_2", "Tag_3", "Tag_4"},
                    "Id_Word="+key,
                    null,
                    null,
                    null,
                    null
            );
            cursor.moveToFirst();
            tempInfo = (cursor.getString(0)+";"+cursor.getString(1)+";"+cursor.getString(2)+";"+cursor.getString(3)+";"+cursor.getString(4)+";"+cursor.getString(5)).split(";");
        }
    }

    void getNewValues() {
        String tempNewInfo;
        String tempValue;

        EditText tempEditText;
        Button tempButton;

        tempEditText = findViewById(R.id.editText_word);
        tempValue = tempEditText.getText().toString();
        if ( tempValue.length() == 0 ) {
            tempNewInfo = tempInfo[0];
        } else {
            tempNewInfo = tempValue;
        }

        tempEditText = findViewById(R.id.editText_translation);
        tempValue = tempEditText.getText().toString();
        if ( tempValue.length() == 0 ) {
            tempNewInfo = tempNewInfo + ";" + tempInfo[1];
        } else {
            tempNewInfo = tempNewInfo + ";" + tempValue;
        }

        tempButton = findViewById(R.id.button_Tag_1);
        tempValue = tempButton.getText().toString();
        if ( tempValue.equals(" ") ) {
            tempNewInfo = tempNewInfo + ";NAN";
        } else {
            tempNewInfo = tempNewInfo + ";" + tempButton.getText().toString();
        }


        tempButton = findViewById(R.id.button_Tag_2);
        tempValue = tempButton.getText().toString();
        if ( tempValue.equals(" ") ) {
            tempNewInfo = tempNewInfo + ";NAN";
        } else {
            tempNewInfo = tempNewInfo + ";" + tempButton.getText().toString();
        }

        tempButton = findViewById(R.id.button_Tag_3);
        tempValue = tempButton.getText().toString();
        if ( tempValue.equals(" ") ) {
            tempNewInfo = tempNewInfo + ";NAN";
        } else {
            tempNewInfo = tempNewInfo + ";" + tempButton.getText().toString();
        }

        tempButton = findViewById(R.id.button_Tag_4);
        tempValue = tempButton.getText().toString();
        if ( tempValue.equals(" ") ) {
            tempNewInfo = tempNewInfo + ";NAN";
        } else {
            tempNewInfo = tempNewInfo + ";" + tempButton.getText().toString();
        }

        newInfo = tempNewInfo.split(";");
    }

    void setupDisplayValues() {
        TextView tempTextView;
        EditText tempEditText;
        Button tempButton;

        tempTextView = findViewById(R.id.textView_mode);
        tempTextView.setText(mode);

        tempEditText = findViewById(R.id.editText_word);
        tempEditText.setHint(tempInfo[0]);

        tempEditText = findViewById(R.id.editText_translation);
        tempEditText.setHint(tempInfo[1]);

        tempButton = findViewById(R.id.button_Tag_1);
        tempButton.setText(tempInfo[2]);

        tempButton = findViewById(R.id.button_Tag_2);
        tempButton.setText(tempInfo[3]);

        tempButton = findViewById(R.id.button_Tag_3);
        tempButton.setText(tempInfo[4]);

        tempButton = findViewById(R.id.button_Tag_4);
        tempButton.setText(tempInfo[5]);

        tempButton = findViewById(R.id.button_delete);
        if ( mode.equals("Modify") ) {
            tempButton.setVisibility(View.VISIBLE);
        } else if ( mode.equals("Add") ) {
            tempButton.setVisibility(View.GONE);
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
        getKeyRow();

        EditText tempEditText;
        Button tempButton;

        tempEditText = findViewById(R.id.editText_word);
        tempEditText.setText("");
        tempEditText.setHint(tempInfo[0]);

        tempEditText = findViewById(R.id.editText_translation);
        tempEditText.setText("");
        tempEditText.setHint(tempInfo[1]);

        tempButton = findViewById(R.id.button_Tag_1);
        tempButton.setText(tempInfo[2]);

        tempButton = findViewById(R.id.button_Tag_2);
        tempButton.setText(tempInfo[3]);

        tempButton = findViewById(R.id.button_Tag_3);
        tempButton.setText(tempInfo[4]);

        tempButton = findViewById(R.id.button_Tag_4);
        tempButton.setText(tempInfo[5]);
    }

    public void onClicClear(View view) {
        clear();
    }

    public void onClicValidation(View view) {
        String statement = "";

        getNewValues();
        if ( mode.equals("Modify") ) {
            statement = "UPDATE t_"+language+" SET Word='"+newInfo[0]+"', Translation='"+newInfo[1]+"', Tag_1='"+newInfo[2]+"', Tag_2='"+newInfo[3]+"', Tag_3='"+newInfo[4]+"', Tag_4='"+newInfo[5]+"' WHERE Id_Word="+key;
            CDB.execSQL(statement);
            clear();
        } else if ( mode.equals("Add") ) {
            statement = "INSERT INTO t_"+language+" (Word, Translation, Tag_1, Tag_2, Tag_3, Tag_4) VALUES ('"+newInfo[0]+"', '"+newInfo[1]+"', '"+newInfo[2]+"', '"+newInfo[3]+"', '"+newInfo[4]+"', '"+newInfo[5]+"')";
            CDB.execSQL(statement);

            Intent i = new Intent ( this, CahierDisplay.class );
            i.putExtra( "LangueChoisie", language );
            startActivity( i );
            finish();
        }
    }

    public void onClicDelete(View view) {
        if ( mode.equals("Modify") ) {
            String delete = "DELETE FROM t_"+language+" WHERE Id_Word="+key;
            CDB.execSQL(delete);

            Intent i = new Intent ( this, CahierDisplay.class );
            i.putExtra( "LangueChoisie", language );
            startActivity( i );
            finish();
        }
    }

    // TODO
    // Gestion clictagbutton

}
