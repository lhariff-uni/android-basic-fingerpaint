package com.example.fingerpainter.viewmodel;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.databinding.Bindable;
import androidx.lifecycle.MutableLiveData;

import com.example.fingerpainter.BR;
import com.example.fingerpainter.FingerPainterView;
import com.example.fingerpainter.model.Brush;
import com.example.fingerpainter.view.MainActivity;
import com.example.fingerpainter.view.SelectColour;

public class MainViewModel extends ObservableViewModel {
    // Model reference
    private Brush brush;

    // LiveData to 'communicate' to View to perform an action that can only be done in View.
    private MutableLiveData<Integer> activityID;

    public static final int COLOUR_REQUEST_CODE = 1;
    public static final int BRUSH_REQUEST_CODE = 2;

    public MainViewModel() {
        // Model constructor given DEFAULT parameters following FingerPainterView
        // Does not assign anything new to FingerPainterView (not notified)
        brush = new Brush(Color.BLACK,20, Paint.Cap.ROUND);
    }

    /*
     *  OnClick functions to navigate to a different activity. Navigation is done through
     *  LiveData variable (activityID). More information on how it's used in
     *  MainActivity.java
     */
    public void onColorClick() {
        setActivityID(COLOUR_REQUEST_CODE);
    }
    public void onBrushClick() {
        setActivityID(BRUSH_REQUEST_CODE);
    }

    /*
     *  Binded data to activity_main.xml
     *
     *  Reference can be found in FingerPainterView component, within a FrameLayout component.
     *  Used to update variables within FingerPainterView (without explicit reference within
     *  the ViewModel)
     */
    @Bindable
    public int getBrushColour() {
        return brush.getColour();
    }
    @Bindable
    public Paint.Cap getBrushCap() {
        return brush.getCap();
    }
    @Bindable
    public int getBrushWidth() {
        return brush.getWidth();
    }

    public MutableLiveData<Integer> getActivityID() {
        //in case of initial setup and it was never assigned data.
        if (activityID == null) {
            activityID = new MutableLiveData<Integer>();
        }
        return activityID;
    }

    public void setActivityID(int activityID) {
        this.activityID.setValue(activityID);
    }

    /*
     *  All setters notify change in order to update.
     */
    public void setBrushColour(int colour) {
        brush.setColour(colour);
        notifyPropertyChanged(BR.brushColour);
    }

    public void setBrushCap(Paint.Cap cap) {
        brush.setCap(cap);
        notifyPropertyChanged(BR.brushCap);
    }

    public void setBrushWidth(int width) {
        brush.setWidth(width);
        notifyPropertyChanged(BR.brushWidth);
    }


}
