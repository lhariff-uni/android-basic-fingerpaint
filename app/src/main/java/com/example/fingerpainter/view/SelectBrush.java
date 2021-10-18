package com.example.fingerpainter.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.fingerpainter.R;
import com.example.fingerpainter.databinding.ActivityMainBinding;
import com.example.fingerpainter.databinding.ActivitySelectBrushBinding;
import com.example.fingerpainter.viewmodel.BrushViewModel;
import com.example.fingerpainter.viewmodel.MainViewModel;

public class SelectBrush extends AppCompatActivity {
    // Variables to reference UI components and ViewModel only.
    RadioButton roundRadio = null;
    RadioButton squareRadio = null;
    TextView textWidth = null;
    BrushViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new ViewModelProvider(this).get(BrushViewModel.class);
        ActivitySelectBrushBinding v = ActivitySelectBrushBinding.inflate(LayoutInflater.from(this));
        setContentView(v.getRoot());
        v.setViewmodel(model);

        // Holding reference to UI components
        roundRadio = (RadioButton) findViewById(R.id.roundRadioButton);
        squareRadio = (RadioButton) findViewById(R.id.squareRadioButton);
        textWidth = (TextView) findViewById(R.id.widthNumber);

        // Assigning existing data to ViewModel
        Bundle bundle = getIntent().getExtras();
        model.setWidth(bundle.getInt("width"));
        model.setCap(bundle.getString("cap").equals("round") ? Paint.Cap.ROUND : Paint.Cap.SQUARE);

        // Load any saved data
        if(savedInstanceState != null) {
            model.setWidth(savedInstanceState.getInt("width"));
            model.setCap(savedInstanceState.getString("cap").equals("round") ?
                    Paint.Cap.ROUND : Paint.Cap.SQUARE);
        }

        // Observer to pointing to appropriate function handler.
        final Observer<Boolean> selectObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean bool) {
                Log.d("psylha", "(SelectBrush) selectObserver: " + bool.booleanValue());
                if(bool.booleanValue()) {
                    handleSelected();
                }
            }
        };
        // Assigning LiveData (selected) the Observer.
        model.getSelected().observe(this, selectObserver);
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        state.putInt("width", model.getWidth());
        state.putString("cap", model.getCap() == Paint.Cap.ROUND ? "round" : "square");
        super.onSaveInstanceState(state);
    }

    /*
     *  Handles termination of the activity and preparing for results of the activity.
     *  Called when LiveData (selected) is changed.
     */
    private void handleSelected() {
        Intent intent = new Intent();
        intent.putExtra("width", model.getWidth());
        intent.putExtra("cap", model.getCap() == Paint.Cap.ROUND ? "round" : "square");
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}