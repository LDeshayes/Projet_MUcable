package com.example.projet_mucable;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.widget.SeekBar;
import android.widget.TextView;

import android.os.Bundle;

public class ColorPicker extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    //Reference the seek bars
    SeekBar SeekA;
    SeekBar SeekR;
    SeekBar SeekG;
    SeekBar SeekB;
    //Reference the TextView
    TextView ShowColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_picker);

        //Get a reference to the seekbars
        SeekA= findViewById(R.id.seekA);
        SeekR= findViewById(R.id.seekR);
        SeekG= findViewById(R.id.seekG);
        SeekB= findViewById(R.id.seekB);

        //Reference the TextView
        ShowColor= findViewById(R.id.textView);

        //This activity implements SeekBar OnSeekBarChangeListener
        SeekA.setOnSeekBarChangeListener(this);
        SeekR.setOnSeekBarChangeListener(this);
        SeekG.setOnSeekBarChangeListener(this);
        SeekB.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

        //get current ARGB values
        int A=SeekA.getProgress();
        int R=SeekR.getProgress();
        int G=SeekG.getProgress();
        int B=SeekB.getProgress();

        //Reference the value changing
        //int id=seekBar.getId();

        //Build and show the new color
        ShowColor.setBackgroundColor(Color.argb(A,R,G,B));

        //show the color value
        ShowColor.setText("0x"+String.format("%02x", A)+String.format("%02x", R)+String.format("%02x", G)+String.format("%02x", B));

        //some math so text shows (needs improvement for greys)
        ShowColor.setTextColor(Color.argb(0xff,255-R,255-G,255-B));

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor COLOR_PICKED = preferences.edit();
        COLOR_PICKED.putString("COLOR_PICKED", "#"+String.format("%02x", A)+String.format("%02x", R)+String.format("%02x", G)+String.format("%02x", B));
        COLOR_PICKED.apply();

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}