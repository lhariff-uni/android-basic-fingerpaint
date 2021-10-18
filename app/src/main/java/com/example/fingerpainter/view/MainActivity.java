package com.example.fingerpainter.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import com.example.fingerpainter.FingerPainterView;
import com.example.fingerpainter.R;
import com.example.fingerpainter.viewmodel.MainViewModel;
import com.example.fingerpainter.databinding.ActivityMainBinding;

/*
 *  LiveData was used instead of BindingAdapters to allow 'communication' from ViewModel to View.
 *
 *  Communication was required in cases where it was required to work with instances of View
 *  and it is not allowed in ViewModel (in best practice of MVVM).
 *
 *  While BindingAdapters worked to establish 'communication', there was a limiting factor
 *  in that the method should be static (at least for simplicity's sake). Being static, it did
 *  not allow referencing non-static items. This made it difficult to start an activity for result.
 *  (It's only possible to startActivity() in a static method)
 *
 *  LiveData allowed communication and Observable's onChanged method is non-static, making it
 *  easier to call startActivityForResult() and continue applying MVVM.
 */

public class MainActivity extends AppCompatActivity {
    // Variables to reference UI components and ViewModel only.
    FingerPainterView fingerPainterView;
    MainViewModel model;

    public static final int COLOUR_REQUEST_CODE = 1;
    public static final int BRUSH_REQUEST_CODE = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new ViewModelProvider(this).get(MainViewModel.class);
        ActivityMainBinding v = ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(v.getRoot());
        v.setViewmodel(model);

        // Holding reference to UI components
        fingerPainterView = (FingerPainterView) findViewById(R.id.fingerPainterView);

        // Load image into canvas
        if(getIntent().getData() != null) {
            fingerPainterView.load(getIntent().getData());
        }

        // Load any saved data
        if(savedInstanceState != null) {
            model.setBrushColour(savedInstanceState.getInt("colour"));
            model.setBrushWidth(savedInstanceState.getInt("width"));
            model.setBrushCap(savedInstanceState.getString("cap").equals("round")
                    ? Paint.Cap.ROUND : Paint.Cap.SQUARE);
            model.setActivityID(0);
        }

        // Observer to pointing to appropriate function handler.
        final Observer<Integer> actObserver = new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                Log.d("psylha", "(MainActivity) actObserver: " + integer);
                navigateActivity(integer);
            }
        };
        // Assigning LiveData (activityID) the Observer.
        model.getActivityID().observe(this, actObserver);
    }

    /*
     *  Override to allow modification of activityID on resuming MainActivity.
     *
     *  Necessary as it prevents unexpected/repeated activity calls.
     *  As it is only onResume, it is only called once the user returns from other activities.
     *  0 can be seen as the MainActivity ID/Code.
     */
    @Override
    public void onResume() {
        super.onResume();
        model.setActivityID(0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_CANCELED) {
        }
        else {
            // Updates Model data based on activity results (which then update FingerPainterView)
            if(requestCode == BRUSH_REQUEST_CODE) {
                model.setBrushWidth(data.getIntExtra("width", 20));
                model.setBrushCap(data.getStringExtra("cap").equals("round") ?
                        Paint.Cap.ROUND : Paint.Cap.SQUARE);
            }
            else if(requestCode == COLOUR_REQUEST_CODE) {
                model.setBrushColour(data.getIntExtra("colour", Color.BLACK));
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        state.putInt("colour", model.getBrushColour());
        state.putInt("width", model.getBrushWidth());
        state.putString("cap", model.getBrushCap() == Paint.Cap.ROUND ? "round" : "square");
        super.onSaveInstanceState(state);
    }

    /*
     *  Method to start different activities.
     *  Called when LiveData (activityID) is changed.
     *
     *  Acts as a way to navigate to other activities based on their request code and the
     *  activityID.
     */
    private void navigateActivity(int activityID) {
        if(activityID == COLOUR_REQUEST_CODE) {
            Intent intent = new Intent(this, SelectColour.class);
            Bundle bundle = new Bundle();
            bundle.putInt("colour", model.getBrushColour());
            intent.putExtras(bundle);

            this.startActivityForResult(intent, COLOUR_REQUEST_CODE);
        }
        else if(activityID == BRUSH_REQUEST_CODE) {
            Intent intent = new Intent(this, SelectBrush.class);
            Bundle bundle = new Bundle();
            bundle.putInt("width", model.getBrushWidth());
            bundle.putString("cap", model.getBrushCap() == Paint.Cap.ROUND ? "round" : "square");
            intent.putExtras(bundle);

            this.startActivityForResult(intent, BRUSH_REQUEST_CODE);
        }
    }

}