package com.example.projet_mucable.Display;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projet_mucable.DrawView;
import com.example.projet_mucable.Line;
import com.example.projet_mucable.R;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class LiensDisplay extends AppCompatActivity  /*implements View.OnTouchListener*/ {


    TextView[] allTextViews;
    ViewGroup _root;
    //ArrayList<Line> lines = new ArrayList<Line>();
    DrawView drawView;
    List<String> listAnswered = new ArrayList<>();
    boolean checked = false;


    int[] key_list;
    String[] words_list;// = { "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "lo" };
    String[] translations_list;// = { "Un", "Deux", "Trois", "Quatre", "Cinq", "Six", "Sept", "Huit", "Neuf", "Dix", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "la", "lo" };

    SQLiteDatabase CDB;
    String language;
    int taille_bd;
    Integer[] indTab = new Integer[4];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liens_display);

        _root = (ViewGroup)findViewById(R.id.root);
        LinearLayout ls = findViewById(R.id.leftSide);
        LinearLayout rs = findViewById(R.id.leftSide);
        drawView = findViewById(R.id.drawCanvas);


        language = getIntent().getStringExtra("Langue");

        //ls.setOnTouchListener(this);

        loadDB();
        taille_bd = words_list.length;
        setUpSides();

    }


    @SuppressLint("RtlHardcoded")
    public void setUpSides(){


        // Get 4 random indice from possible words
        ArrayList<Integer> list = new ArrayList<>();
        for (int j=0; j<taille_bd; j++) {
            list.add(j);
        }
        Collections.shuffle(list);
        for (int i=0; i<4; i++) {
            indTab[i]=list.get(i);
        }

        // Initialize some vars
        LinearLayout leftSide = findViewById(R.id.leftSide);
        LinearLayout rightSide = findViewById(R.id.rightSide);

        allTextViews = new TextView[8];

        ArrayList<String> textsQ = new ArrayList<>();
        ArrayList<String> textsR = new ArrayList<>();


        // Add tot he lsit the words to display
        for(int i = 0; i<4; i++){
            textsQ.add(words_list[indTab[i]]);
            textsR.add(translations_list[indTab[i]]);
        }

        // We shuffle the list so that the answer is not in front of the question
        Collections.shuffle(textsR);


        for(int i = 0; i<4; i++){
            listAnswered.add((textsQ.indexOf(words_list[indTab[i]])+1)+","+(textsR.indexOf(translations_list[indTab[i]])+5));
        }

        // indexof

        for(int i=0; i<4; i++){
            allTextViews[i] = new TextView(getApplicationContext());
            allTextViews[i].setText(textsQ.get(i)+" •");

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

            LinearLayout.LayoutParams test = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            test.gravity = Gravity.RIGHT;

            leftSide.addView(allTextViews[i], test);
        }

        for(int i=0+4; i<4+4; i++){
            allTextViews[i] = new TextView(getApplicationContext());
            allTextViews[i].setText("• "+textsR.get(i-4));


            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

            LinearLayout.LayoutParams test = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            test.gravity = Gravity.LEFT;

            rightSide.addView(allTextViews[i], test);
        }


    }


    /*@Override
    public boolean onTouch(View view, MotionEvent event) {
        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                //Log.d("testtest",""+X+"-"+Y);

                break;
            case MotionEvent.ACTION_UP:
                //Log.d("testtest",""+X+"-"+Y);

                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                //Log.d("testtest",""+X+"-"+Y);

                break;
            case MotionEvent.ACTION_POINTER_UP:
                //Log.d("testtest",""+X+"-"+Y);

                break;
            case MotionEvent.ACTION_MOVE:
                //Log.d("testtest",""+X+"-"+Y);

                break;
        }
        _root.invalidate();
        return true;
    }*/

    public void check(View view){

        List<String> listAnswers = drawView.getAnswers();
        List<String> listGoodAns = new ArrayList<>();
        List<String> listBadAns;
        List<Integer> whatsGood = new ArrayList<>();
        List<Integer> whatsBad = new ArrayList<>();


        // We get the good answers
        for(String s1: listAnswers){
            // Log.d("testtest",s1+"");
            for(String s2: listAnswered){
                if(s1.equals(s2))
                    listGoodAns.add(s1);
            }
        }

        // We get the bad answers
        listBadAns =  new ArrayList<>(listAnswered);
        listBadAns.removeAll(listGoodAns);

        // We get the integers corresponding to the Q/A correctly answered
        for(String s : listGoodAns)
            for(String s2 : s.split(","))
                whatsGood.add(Integer.parseInt(s2));
        // & otherwise
        for(String s : listBadAns)
            for(String s2 : s.split(","))
                whatsBad.add(Integer.parseInt(s2));


        for(Integer i : whatsGood)
            allTextViews[i-1].setTextColor(Color.rgb(23, 145, 31));
        for(Integer i : whatsBad)
            allTextViews[i-1].setTextColor(Color.rgb(230, 92, 92));


        // Next series of questions/rep
        if(whatsGood.size()==8 && checked)
            goToParLiens();

        if(whatsGood.size()==8)
            checked = true;






    }


    public void goToParLiens() {

        Intent i = new Intent ( this, LiensDisplay.class );
        Cursor cursor;

        cursor = CDB.query(
                /*"t_"+langue,*/"t_Mot",
                null,
                "Langue LIKE '"+language+"' AND CoefAppr IN (0,1,2,3,4)",
                null,
                null,
                null,
                "CoefAppr ASC"
        );
        taille_bd =  cursor.getCount();
        cursor.close();

        i.putExtra("Langue", language);
        if(taille_bd<4){
            Toast.makeText(getApplicationContext(),"Il faut plus de mots pour ces conditions.",Toast.LENGTH_LONG).show();
        }
        else{
            startActivity( i );
        }
    }






    @SuppressLint("WrongConstant")
    void loadDB() {

        Cursor cursor;
        CDB = openOrCreateDatabase("CDB.db", SQLiteDatabase.CREATE_IF_NECESSARY, null );

        cursor = CDB.query(
                    /*"t_"+language,*/"t_Mot",
                    null,
                    "Langue LIKE '"+language+"' AND CoefAppr IN (0,1,2,3,4)",
                    null,
                    null,
                    null,
                    "CoefAppr ASC"
        );


        int rowCount = cursor.getCount();

        key_list = new int[rowCount];
        words_list = new String[rowCount];
        translations_list = new String[rowCount];

        cursor.moveToFirst();
        for ( int i = 0; i < rowCount; i++ ) {
            key_list[i] = cursor.getInt(0);
            words_list[i] = cursor.getString(1);
            translations_list[i] = cursor.getString(2);
            cursor.moveToNext();
        }
        cursor.close();

    }


}