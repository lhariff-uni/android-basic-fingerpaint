package com.example.fingerpainter.viewmodel;

import android.graphics.Color;
import android.util.Log;

import androidx.databinding.Bindable;
import androidx.lifecycle.MutableLiveData;

import com.example.fingerpainter.BR;

public class ColourViewModel extends ObservableViewModel {
    // Binded variables to references in activity_select_colour.xml
    @Bindable
    private int colour;
    @Bindable
    private int textColour;
    @Bindable
    private String colourName;

    // LiveData to 'communicate' to View to perform an action that can only be done in View.
    private MutableLiveData<Boolean> selected;

    public ColourViewModel() {
        // Default values. Should always be overridden from View and existing data.
        colour = Color.BLACK; colourName = "black"; textColour = Color.WHITE;
    }

    // Updates LiveData (selected) to inform that it has been selected.
    public void onSelectClick() {
        setSelected(true);
    }

    /*
     *  Method called whenever a Button (referencing a colour) is clicked.
     *
     *  Parameters reference Button's background colour and text.
     *  Based on the colour given, a text colour is assigned for best text visibility.
     *  As many of text colour would be repeated, decided against adding it as a parameter.
     */
    public void onColourClick(int colour, String colourName) {
        setColour(colour);
        setColourName(colourName);

        if(this.colour == Color.BLACK || this.colour == Color.BLUE) {
            setTextColour(Color.WHITE);
        }
        else {
            setTextColour(Color.BLACK);
        }
    }

    // Setters notify change when called.
    public void setColour(int colour) {
        this.colour = colour;
        notifyPropertyChanged(BR.colour);
    }
    public void setColourName(String colourName) {
        this.colourName = colourName;
        notifyPropertyChanged(BR.colourName);
    }
    public void setTextColour(int textColour) {
        this.textColour = textColour;
        notifyPropertyChanged(BR.textColour);
    }
    public void setSelected(boolean selected) {
        this.selected.setValue(selected);
    }
    public MutableLiveData<Boolean> getSelected() {
        if(selected == null) {
            selected = new MutableLiveData<Boolean>();
        }
        return selected;
    }
    public String getColourName() {
        return colourName;
    }
    public int getColour() {
        return colour;
    }
    public int getTextColour() {
        return textColour;
    }

    /*
     *  Method to find the colour name based on a given colour.
     *  Used in rare cases where only a colour exists, and a name must be found.
     */
    public String findColourName(int colour) {
        switch(colour) {
            case Color.RED:
                return "red";
            case Color.GREEN:
                return "green";
            case Color.BLUE:
                return "blue";
            case Color.MAGENTA:
                return "magenta";
            case Color.YELLOW:
                return "yellow";
            case Color.CYAN:
                return "cyan";
            case Color.WHITE:
                return "white";
            case Color.GRAY:
                return "gray";
            case Color.BLACK:
                return "black";
            default:
                return "error";
        }
    }
}
