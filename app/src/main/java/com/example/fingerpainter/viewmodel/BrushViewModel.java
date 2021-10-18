package com.example.fingerpainter.viewmodel;

import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;

import androidx.databinding.Bindable;
import androidx.lifecycle.MutableLiveData;

import com.example.fingerpainter.BR;

public class BrushViewModel extends ObservableViewModel {
    // Binded variables to references in activity_select_brush.xml
    @Bindable
    private int width;
    @Bindable
    private boolean roundCap;
    @Bindable
    private boolean squareCap;
    // Using binded booleans instead to more easily communicate to XML's radio buttons
    private Paint.Cap cap;

    // LiveData to 'communicate' to View to perform an action that can only be done in View.
    private MutableLiveData<Boolean> selected;

    public BrushViewModel() {
        // Default values. Should always be overridden from View and existing data.
        width = 0; cap = Paint.Cap.BUTT;
    }

    // Updates LiveData (selected) to inform that it has been selected.
    public void onSelectClick() {
        setSelected(true);
    }

    /*
     *  Method called when Buttons associated to width are pressed.
     *
     *  Button's text is set as a parameter.
     *  The amount the width is changed by depends on the text.
     */
    public void onWidthClick(String button) {
        int change = 0;
        switch (button) {
            case "+1":
                change = 1;
                break;
            case "+10":
                change = 10;
                break;
            case "-1":
                change = -1;
                break;
            case "-10":
                change = -10;
                break;
        }
        setWidth(getWidth() + change);
    }

    /*
     *  Method called when Radio Buttons associated to Brush Cap is clicked.
     *
     *  Radio Button's text is set as a parameter.
     *  The cap is set depending on the text.
     */
    public void onRadioClick(String radioButton) {
        Paint.Cap cap = null;
        if(radioButton.equalsIgnoreCase("round"))
            cap = Paint.Cap.ROUND;
        else
            cap = Paint.Cap.SQUARE;
        setCap(cap);
    }

    // Setters notify change when called.
    public void setWidth(int width) {
        // Sets minimum value allowed to be 1
        this.width = width > 0 ? width : 1;
        notifyPropertyChanged(BR.width);
    }

    public void setCap(Paint.Cap cap) {
        this.cap = cap;

        // Updates booleans depending on new Paint.Cap
        roundCap = cap == Paint.Cap.ROUND;
        squareCap = cap == Paint.Cap.SQUARE;
        notifyPropertyChanged(BR.roundCap);
        notifyPropertyChanged(BR.squareCap);
    }

    public void setSelected(boolean selected) {
        this.selected.setValue(selected);
    }

    public MutableLiveData<Boolean> getSelected() {
        if (selected == null) {
            selected = new MutableLiveData<Boolean>();
        }
        return selected;
    }

    public int getWidth() {
        return width;
    }
    public Paint.Cap getCap() {
        return cap;
    }
    public boolean isRoundCap() {
        return roundCap;
    }
    public boolean isSquareCap() {
        return squareCap;
    }
}
