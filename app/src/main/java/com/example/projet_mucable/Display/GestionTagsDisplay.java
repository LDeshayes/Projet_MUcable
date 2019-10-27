package com.example.projet_mucable.Display;

// Ajout et suppression des tags, 4.4 cdc

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;

import com.example.projet_mucable.R;

public class GestionTagsDisplay extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestiontags_display);

    }

    public void clicAjouterTag(View view) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String tags = preferences.getString("TAG_LIST", "");

        EditText editText_AddTag = findViewById(R.id.editText_AddTag);
        String newTag = editText_AddTag.getText().toString();

        String tagList = tags + ";" + newTag;

        SharedPreferences.Editor NEW_TAGLIST = preferences.edit();
        NEW_TAGLIST.putString("TAG_LIST", tagList);
        NEW_TAGLIST.commit();

        editText_AddTag.setText("");

        //String[] tag_list = tags.split(";");


    }
}
