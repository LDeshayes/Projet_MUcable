package com.example.projet_mucable;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;


public class DrawView extends View {

    Paint paint = new Paint();
    ArrayList<Line> lines = new ArrayList<Line>();

    int height;
    int width;

    double sepa;
    double sepa1;
    double sepa2;
    double sepa3;
    List<String> listAnswers = new ArrayList<>();

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint.setAntiAlias(true);
        paint.setStrokeWidth(6f);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);

    }

    @Override
    protected void onDraw(Canvas canvas) {

        height = findViewById(R.id.drawCanvas).getHeight();
        width = findViewById(R.id.drawCanvas).getWidth();

        sepa = ((height*0.25)/2);
        sepa1 = ((height*0.25)-sepa+20.0); // 80
        sepa2 = ((height*0.5)-sepa+20.0);  // 210
        sepa3 = ((height*0.75)-sepa+20.0); // 335


        for (Line l : lines) {
            canvas.drawLine(l.startX, l.startY, l.stopX, l.stopY, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            int newY;
            int newX;

            // Get nearest starting point
            // w:564 h:504
            // 0 + 20
            // height * 0.25 + 20
            // height * 0.50 + 20
            // height * 0.75 - 20
            // 80

            // Determine where the line should starts (when left)
            if(event.getX()<=100){

                newX = 50;

                if(event.getY()<=sepa1){
                    newY = 20;
                }
                else if(event.getY()>sepa1 && event.getY()<=sepa2){
                    newY = (int)(height*0.25) + 20;
                }
                else if(event.getY()>sepa2 && event.getY()<=sepa3){
                    newY = (int)(height*0.5) + 20;
                }
                else{
                    newY = (int)(height*0.75) + 20;
                }

                // If no other line starts at the same place we add the line
                ArrayList<Line> whereSame = whereSameStart(newX, newY);
                lines.removeAll(whereSame);
                //
                whereSame = whereSameEnd(newX, newY);
                lines.removeAll(whereSame);

                lines.add(new Line(newX, newY));
            }

            // Determine where the line should starts (when left)
            if(event.getX()>= width-100){

                newX =  width - 50;

                if(event.getY()<=sepa1){
                    newY = 20;
                }
                else if(event.getY()>sepa1 && event.getY()<=sepa2){
                    newY = (int)(height*0.25) + 20;
                }
                else if(event.getY()>sepa2 && event.getY()<=sepa3){
                    newY = (int)(height*0.5) + 20;
                }
                else{
                    newY = (int)(height*0.75) + 20;
                }

                // If no other line starts at the same place we add the line
                //ArrayList<Line> whereSame = whereSameStart(newX, newY);
                //lines.removeAll(whereSame);
                lines.add(new Line(newX, newY));

            }


            return true;
        }
        else if ((/*event.getAction() == MotionEvent.ACTION_MOVE ||*/ event.getAction() == MotionEvent.ACTION_UP) && lines.size() > 0) {

            int newY;
            int newX;

            if(event.getX()>= width-100){

                newX =  width - 50;

                if(event.getY()<=sepa1){
                    newY = 20;
                }
                else if(event.getY()>sepa1 && event.getY()<=sepa2){
                    newY = (int)(height*0.25) + 20;
                }
                else if(event.getY()>sepa2 && event.getY()<=sepa3){
                    newY = (int)(height*0.5) + 20;
                }
                else{
                    newY = (int)(height*0.75) + 20;
                }

                Line current = lines.get(lines.size() - 1);
                // We allow multiple line to en d on the same tick
                // (for example if the user is unsure about the correct answer)
                current.stopX = newX;
                current.stopY = newY;

                if(current.stopX == current.startX)
                    lines.remove(current);

                invalidate();

            }
            else if(event.getX()<=100){

                newX = 50;

                if(event.getY()<=sepa1){
                    newY = 20;
                }
                else if(event.getY()>sepa1 && event.getY()<=sepa2){
                    newY = (int)(height*0.25) + 20;
                }
                else if(event.getY()>sepa2 && event.getY()<=sepa3){
                    newY = (int)(height*0.5) + 20;
                }
                else{
                    newY = (int)(height*0.75) + 20;
                }

                Line current = lines.get(lines.size() - 1);
                // We allow multiple line to en d on the same tick
                // (for example if the user is unsure about the correct answer)

                ArrayList<Line> whereSame = whereSameEnd(newX, newY);
                lines.removeAll(whereSame);
                //
                whereSame = whereSameStart(newX, newY);
                lines.removeAll(whereSame);

                current.stopX = newX;
                current.stopY = newY;

                if(current.stopX == current.startX)
                    lines.remove(current);

                invalidate();

            }
            else{
                Line current = lines.get(lines.size() - 1);
                lines.remove(current);
            }

            return true;
        }
        else if(event.getAction() == MotionEvent.ACTION_MOVE && lines.size() > 0) {
            Line current = lines.get(lines.size() - 1);

            current.stopX = event.getX();
            current.stopY = event.getY();
            invalidate();

            return true;
        }
        else {
            return false;
        }
    }

    public ArrayList<Line> getLines(){
        return lines;
    }

    public void setLines(ArrayList<Line> nLines){
        lines = nLines;
    }

    public ArrayList<Line> whereSameStart(int startx, int starty){
        ArrayList<Line> whereSame = new ArrayList<>();
        for(Line l : lines){
            if(l.compareStart(new Line(startx, starty))){
                whereSame.add(l);
            }
        }
        return whereSame;
    }

    public ArrayList<Line> whereSameEnd(int stopx, int stopy){
        ArrayList<Line> whereSame = new ArrayList<>();
        for(Line l : lines){
            if(l.compareEnd(new Line(stopx, stopy))){
                whereSame.add(l);
            }
        }
        return whereSame;
    }

    public List<String> getAnswers(){

        listAnswers.clear();

        for (Line l : lines){

            int start = 0;
            int end = 0;

            int case1 = 20;
            int case2 = (int)(height*0.25) + 20;
            int case3 = (int)(height*0.5) + 20;
            int case4 = (int)(height*0.75) + 20;
            int custartY = (int)l.startY;
            int custopY = (int)l.stopY;
            int custartX = (int)l.startX;
            int custopX = (int)l.stopX;


            // We get for each line where the line begin and where the line end
            // in term of question/answer position
            if(custartY==case1)
                start=1;
            if(custartY==case2)
                start=2;
            if(custartY==case3)
                start=3;
            if(custartY==case4)
                start=4;


            if(custopY==case1)
                end=1;
            if(custopY==case2)
                end=2;
            if(custopY==case3)
                end=3;
            if(custopY==case4)
                end=4;

            int tmp;
            if(custartX>custopX){
                tmp = start;
                start = end;
                end = tmp;
            }
            end+=4;

            listAnswers.add(start+","+end);

        }

        return listAnswers;

    }



}