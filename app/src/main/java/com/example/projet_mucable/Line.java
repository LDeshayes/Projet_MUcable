package com.example.projet_mucable;

public class Line {
    float startX, startY, stopX, stopY;

    public Line(float startX, float startY, float stopX, float stopY) {
        this.startX = startX;
        this.startY = startY;
        this.stopX = stopX;
        this.stopY = stopY;
    }
    public Line(float startX, float startY) { // for convenience
        this(startX, startY, startX, startY);
    }

    public boolean compareStart(Line l){
        return (startX==l.startX && startY==l.startY);
    }
    public boolean compareEnd(Line l){
        return (stopX==l.stopX && stopY==l.stopY);
    }
}
