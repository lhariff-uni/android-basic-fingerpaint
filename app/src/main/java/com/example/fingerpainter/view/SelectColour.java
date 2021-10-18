package com.example.fingerpainter.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.fingerpainter.R;
import com.example.fingerpainter.databinding.ActivitySelectBrushBinding;
import com.example.fingerpainter.databinding.ActivitySelectColourBinding;
import com.example.fingerpainter.viewmodel.BrushViewModel;
import com.example.fingerpainter.viewmodel.ColourViewModel;

public class SelectColour extends AppCompatActivity {
    // Variables to reference UI components and ViewModel only.
    Button previewButton = null;
    ColourViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_colour);

        model = new ViewModelProvider(this).get(ColourViewModel.class);
        ActivitySelectColourBinding v = ActivitySelectColourBinding.inflate(LayoutInflater.from(this));
        setContentView(v.getRoot());
        v.setViewmodel(model);

        // Holding reference to UI components
        previewButton = findViewById(R.id.preview_colour);

        // Assigning existing data to ViewModel
        Bundle bundle = getIntent().getExtras();
        model.setColour(bundle.getInt("colour"));
        model.setColourName(model.findColourName(model.getColour()));
        model.setTextColour(model.getColour() == Color.BLACK || model.getColour() == Color.BLUE ?
                Color.WHITE : Color.BLACK);

        // Load any saved data
        if(savedInstanceState != null) {
            model.setColour(savedInstanceState.getInt("colour"));
            model.setColourName(savedInstanceState.getString("name"));
            model.setTextColour(savedInstanceState.getInt("textColour"));
        }

        // Observer to pointing to appropriate function handler.
        final Observer<Boolean> selectObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean bool) {
                Log.d("psylha", "(SelectColour) selectObserver: " + bool);
                if(bool) {
                    handleSelected();
                }
            }
        };
        // Assigning LiveData (selected) the Observer.
        model.getSelected().observe(this, selectObserver);
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        state.putInt("colour", model.getColour());
        state.putString("name", model.getColourName());
        state.putInt("textColour", model.getTextColour());
        super.onSaveInstanceState(state);
    }

    /*
     *  Handles termination of the activity and preparing for results of the activity.
     *  Called when LiveData (selected) is changed.
     */
    private void handleSelected() {
        Intent intent = new Intent();
        intent.putExtra("colour", model.getColour());
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}