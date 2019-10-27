package com.example.projet_mucable.Display;

// Ajout et suppression des tags, 4.4 cdc

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.projet_mucable.R;

import java.util.Arrays;

public class GestionTagsDisplay extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestiontags_display);

    }

    public void clicAjouterTag(View view) {

        final EditText editText_AddTag = findViewById(R.id.editText_AddTag);
        final String newTag = editText_AddTag.getText().toString();

        if ( newTag.length() != 0 ) {

            /*new AlertDialog.Builder(getApplicationContext())
                    //.setTitle("Delete entry")
                    .setMessage("Êtes vous sur(e) de vouloir ajouter ce tag "+newTag+" ?")

                    .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            saveTag( newTag );
                        }
                    })

                    .setNegativeButton("Non", null)
                    .show();*/

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Êtes vous sur(e) de vouloir ajouter ce tag "+newTag+" ?")
                    .setCancelable(true)
                    .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            saveTag( newTag );
                            editText_AddTag.setText("");
                        }
                    })
                    .setNegativeButton("Non", null );
            AlertDialog alert = builder.create();
            alert.show();

        } else {

            Toast.makeText(getApplicationContext(), "Le champ pour un nouveau tag est vide !", Toast.LENGTH_SHORT).show();

        }

    }

    void saveTag ( String tag ) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String tags = preferences.getString("TAG_LIST", "");

        String[] tag_list = tags.split(";");

        if (Arrays.asList(tag_list).contains(tag)) {
            Toast.makeText(getApplicationContext(), "Ce tag existe déjà dans la liste !", Toast.LENGTH_SHORT).show();
        } else {

            String tagList = tags + ";" + tag;

            SharedPreferences.Editor NEW_TAGLIST = preferences.edit();
            NEW_TAGLIST.putString("TAG_LIST", tagList);
            NEW_TAGLIST.commit();

        }

    }

}