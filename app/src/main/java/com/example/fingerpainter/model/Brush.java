package com.example.fingerpainter.model;

import android.graphics.Paint;

public class Brush {
    int colour, width;
    Paint.Cap cap;
    public Brush(int colour, int width, Paint.Cap cap) {
        setColour(colour);
        setCap(cap);
        setWidth(width);
    }

    public int getColour() {
        return colour;
    }

    public Paint.Cap getCap() {
        return cap;
    }

    public int getWidth() {
        return width;
    }

    public void setColour(int colour) {
        this.colour = colour;
    }

    public void setCap(Paint.Cap cap) {
        this.cap = cap;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
