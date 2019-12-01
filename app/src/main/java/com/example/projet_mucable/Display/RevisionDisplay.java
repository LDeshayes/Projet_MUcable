package com.example.projet_mucable.Display;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revision_display);

        // Recupération des spinners
        final Spinner spinnerLD = findViewById(R.id.spinnerLDep);
        final Spinner spinnerLF = findViewById(R.id.spinnerLEnd);

        // Créaqtion de la liste des différentes langues
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Francais");
        arrayList.add("Anglais");
        arrayList.add("Allemand");
        arrayList.add("Espagnol");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerLD.setAdapter(arrayAdapter);
        spinnerLF.setAdapter(arrayAdapter);

        spinnerLF.setSelection(1);

        // Definition des comportements des spinners
        spinnerLD.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position>0){
                    langue = parent.getItemAtPosition(position).toString();
                    //Toast.makeText(parent.getContext(), "Selected: " + itemName, Toast.LENGTH_LONG).show();
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
                    //Toast.makeText(parent.getContext(), "Selected: " + itemName, Toast.LENGTH_LONG).show();
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

        Intent i = new Intent ( this, ParCoeurActivity.class );

        TextView t = (TextView) findViewById(R.id.editNBM);
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

        startActivity( i );
    }

    public void goToParChoix(View view) {

        Intent i = new Intent ( this, ParChoixActivity.class );

        TextView t = (TextView) findViewById(R.id.editNBM);
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

        startActivity( i );
    }

    public void clicChoiceTag(View view) {
        Intent i = new Intent ( this, ChoixTagsDisplay.class );
        startActivity( i );
    }
}