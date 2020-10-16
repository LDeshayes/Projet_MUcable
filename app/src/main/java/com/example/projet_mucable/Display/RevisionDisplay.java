package com.example.projet_mucable.Display;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.projet_mucable.R;
import java.util.ArrayList;


public class RevisionDisplay extends AppCompatActivity {

    String langue = "Anglais";
    Boolean quel_sens = false;
    //String tag;
    String tags;

    int taille_bd=0;

    SQLiteDatabase CDB;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revision_display);

        CDB = openOrCreateDatabase("CDB.db", SQLiteDatabase.CREATE_IF_NECESSARY, null );


        // Get spinners
        final Spinner spinnerLD = findViewById(R.id.spinnerLDep);
        final Spinner spinnerLF = findViewById(R.id.spinnerLEnd);

        // Create language list
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Francais");
        arrayList.add("Anglais");
        arrayList.add("Allemand");
        arrayList.add("Espagnol");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerLD.setAdapter(arrayAdapter);
        spinnerLF.setAdapter(arrayAdapter);

        spinnerLF.setSelection(1);

        // Define sppinners behaviors
        spinnerLD.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position>0){
                    langue = parent.getItemAtPosition(position).toString();
                    spinnerLF.setSelection(0);
                    quel_sens = true;
                }
                if(position==0 && spinnerLF.getSelectedItem()=="Francais"){
                    spinnerLF.setSelection(1);
                    langue = "Anglais";
                    quel_sens = false;
                }
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });
        spinnerLF.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position>0){
                    langue = parent.getItemAtPosition(position).toString();
                    spinnerLD.setSelection(0);
                    quel_sens = false;
                }
                if(position==0 && spinnerLD.getSelectedItem()=="Francais"){
                    spinnerLD.setSelection(1);
                    langue = "Anglais";
                    quel_sens = true;
                }
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });


    }

    public void goToParCoeur(View view) {

        Cursor cursor;

        if(tags!=null && !tags.isEmpty()){
            cursor = CDB.query(
                    /*"t_"+langue,*/"t_Mot",
                    null,
                    "Langue LIKE '"+langue+"' AND (Tag_1 IN ("+tags+") OR Tag_2 IN ("+tags+") OR Tag_3 IN ("+tags+") OR Tag_4 IN ("+tags+")) AND CoefAppr IN (0,1,2,3,4)",
                    null,
                    null,
                    null,
                    "CoefAppr ASC"
            );
        }
        else{
            cursor = CDB.query(
                    /*"t_"+langue,*/"t_Mot",
                    null,
                    "Langue LIKE '"+langue+"' AND CoefAppr IN (0,1,2,3,4)",
                    null,
                    null,
                    null,
                    "CoefAppr ASC"
            );
        }

        taille_bd =  cursor.getCount();
        cursor.close();

        Intent i = new Intent ( this, ParCoeurActivity.class );

        TextView t = findViewById(R.id.editNBM);
        String nb_m_s = t.getText().toString();
        if(nb_m_s.length()<1){
            nb_m_s="5";
        }
        int nb_m = Integer.parseInt(nb_m_s);

        i.putExtra("Nb_mots", nb_m);
        i.putExtra("Langue", langue);
        i.putExtra("Sens", quel_sens);
        i.putExtra("Type", true);
        i.putExtra("Not_First", false);
        i.putExtra("TagsFilter", tags);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor NEW_TAGSLIST = preferences.edit();
        NEW_TAGSLIST.putString("RESTARTFROMTAGS", "false");
        NEW_TAGSLIST.apply();

        if(taille_bd<1){
            Toast.makeText(getApplicationContext(),"Il faut plus de mots pour ces conditions.",Toast.LENGTH_LONG).show();
        }
        else{
            startActivity( i );
        }
    }

    @SuppressLint("WrongConstant")
    public void goToParChoix(View view) {

        Cursor cursor;

        if(tags!=null && !tags.isEmpty()){
            cursor = CDB.query(
                    /*"t_"+langue,*/"t_Mot",
                    null,
                    "Langue LIKE '"+langue+"' AND (Tag_1 IN ("+tags+") OR Tag_2 IN ("+tags+") OR Tag_3 IN ("+tags+") OR Tag_4 IN ("+tags+")) AND CoefAppr IN (0,1,2,3,4)",
                    null,
                    null,
                    null,
                    "CoefAppr ASC"
            );
        }
        else{
            cursor = CDB.query(
                    /*"t_"+langue,*/"t_Mot",
                    null,
                    "Langue LIKE '"+langue+"' AND CoefAppr IN (0,1,2,3,4)",
                    null,
                    null,
                    null,
                    "CoefAppr ASC"
            );
        }

        taille_bd =  cursor.getCount();
        cursor.close();

        Intent i = new Intent ( this, ParChoixActivity.class );

        TextView t = findViewById(R.id.editNBM);
        String nb_m_s = t.getText().toString();
        if(nb_m_s.length()<1){
            nb_m_s="5";
        }
        int nb_m = Integer.parseInt(nb_m_s);

        i.putExtra("Nb_mots", nb_m);
        i.putExtra("Langue", langue);
        i.putExtra("Sens", quel_sens);
        i.putExtra("Type", false);
        i.putExtra("Not_First", false);
        i.putExtra("TagsFilter", tags);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor NEW_TAGSLIST = preferences.edit();
        NEW_TAGSLIST.putString("RESTARTFROMTAGS", "false");
        NEW_TAGSLIST.apply();

        if(taille_bd<4){
            Toast.makeText(getApplicationContext(),"Il faut plus de mots pour ces conditions.",Toast.LENGTH_LONG).show();
        }
        else{
            startActivity( i );
        }

    }

    public void goToParLiens(View view) {

        Intent i = new Intent ( this, LiensDisplay.class );
        Cursor cursor;

        cursor = CDB.query(
                /*"t_"+langue,*/"t_Mot",
                null,
                "Langue LIKE '"+langue+"' AND CoefAppr IN (0,1,2,3,4)",
                null,
                null,
                null,
                "CoefAppr ASC"
        );
        taille_bd =  cursor.getCount();
        cursor.close();

        i.putExtra("Langue", langue);
        if(taille_bd<4){
            Toast.makeText(getApplicationContext(),"Il faut plus de mots pour ces conditions.",Toast.LENGTH_LONG).show();
        }
        else{
            startActivity( i );
        }
    }


    public void onClicTag(View view) {
        //Intent i = new Intent ( this, ChoixTagsDisplay.class );
        Intent i = new Intent ( this, ChoixMultiTagsDisplay.class );
        startActivity( i );
    }

    public void onRestart() {
        super.onRestart();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        //tags = preferences.getString("TAG_CHOSEN", null);

        TextView listeTags = findViewById(R.id.textViewTags);
        String comefromtags  = preferences.getString("RESTARTFROMTAGS", "false");
        //Log.d("testtest", " comfrom :"+comefromtags);


        if(comefromtags.equals("true")){
            tags = preferences.getString("TAGS_CHOSEN", "");
            if(tags.equals("")){
                listeTags.setText("aucuns");
            }
            else{
                listeTags.setText(tags.replaceAll("'","").replaceAll(",",", "));
            }
        }

        /*Button button_Tag_1 = findViewById(R.id.buttonTagsSelec);
        if(tags!=null && !tags.isEmpty()){
            button_Tag_1.setText(tags);
        }*/

    }

}