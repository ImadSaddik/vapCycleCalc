package com.example.vapcyclecalc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener,
        ComponentDialog.componentDialogListener {

    private Button settingsBtn, duplicateBtn, removeBtn, confirmBtn;
    private RelativeLayout canvasRelLay;
    private ImageView boilerImg, condenserImg, pumpImg, turbineImg;
    private float dX, dY;
    private GestureDetector gd;
    private View lastViewTouched;
    private static int boilerId = 100;
    private static int condenserId = 200;
    private static int turbineId = 300;
    private static int pumpId = 400;
    private RightBox rightBox;
    private LeftBox leftBox;
    private boolean isVisible = false;
    private boolean firstTouch = true;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v != lastViewTouched) {
            removeSelection();
        }
        lastViewTouched = v;
        tapEvents();
        gd.onTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (firstTouch) {
                    showBordersFirstTouch();
                }
                dX = v.getX() - event.getRawX();
                dY = v.getY() - event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (isViewInside(canvasRelLay, (int) event.getRawX(), (int) event.getRawY())) {
                    v.animate()
                            .x(event.getRawX() + dX)
                            .y(event.getRawY() + dY)
                            .setDuration(0)
                            .start();
                }
                break;
            default:
                return false;
        }
        return true;
    }

    private void showBordersFirstTouch() {
        leftBox = lastViewTouched.findViewById(R.id.leftSelection);
        rightBox = lastViewTouched.findViewById(R.id.rightSelection);

        leftBox.setVisibility(View.VISIBLE);
        rightBox.setVisibility(View.VISIBLE);

        firstTouch = false;
    }

    private void showRemoveBorders() {
        leftBox = lastViewTouched.findViewById(R.id.leftSelection);
        rightBox = lastViewTouched.findViewById(R.id.rightSelection);

        if (isVisible) {
            leftBox.setVisibility(View.GONE);
            rightBox.setVisibility(View.GONE);
            isVisible = false;
            firstTouch = true;
        } else {
            leftBox.setVisibility(View.VISIBLE);
            rightBox.setVisibility(View.VISIBLE);
            isVisible = true;
            firstTouch = false;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initLists();
        initViews();
        createComponent();
        operations();
    }

    private void operations() {
        removeBtn.setOnClickListener(v -> {
            if (lastViewTouched != null) {
                canvasRelLay.removeView(lastViewTouched);
                lastViewTouched = null;
            }
            isVisible = false;
            firstTouch = true;
        });
        duplicateBtn.setOnClickListener(v -> {
            duplicateView();
            firstTouch = false;
        });
        settingsBtn.setOnClickListener(v -> {
            // TODO : Complete the settings operation
        });
        confirmBtn.setOnClickListener(v -> {
            // TODO : Complete the confirm operation
        });
    }

    private void duplicateView() {
        View aView;

        if (lastViewTouched != null) {
            int id = lastViewTouched.getId();
            removeSelection();

            if (id >= 100 && id < 200) {
                aView = getLayoutInflater().inflate(R.layout.boiler_layout, null, false);
                aView.setId(boilerId++);
            } else if (id >= 200 && id < 300) {
                aView = getLayoutInflater().inflate(R.layout.condenser_layout, null, false);
                aView.setId(condenserId++);
            } else if (id >= 300 && id < 400) {
                aView = getLayoutInflater().inflate(R.layout.turbine_layout, null, false);
                aView.setId(turbineId++);
            } else if (id >= 400 && id < 500) {
                aView = getLayoutInflater().inflate(R.layout.pump_layout, null, false);
                aView.setId(pumpId++);
            } else {
                aView = null;
            }
            if (aView != null) {
                aView.setOnTouchListener(this);
                addComponent(aView);
                lastViewTouched = aView;
                showRemoveBorders();
            }
        }
    }

    private void createComponent() {
        boilerImg.setOnClickListener(v -> {
            removeSelection();
            View boiler = getLayoutInflater().inflate(R.layout.boiler_layout, null, false);
            boiler.setOnTouchListener(this);
            boiler.setId(boilerId++);
            addComponent(boiler);
        });
        turbineImg.setOnClickListener(v -> {
            removeSelection();
            View turbine = getLayoutInflater().inflate(R.layout.turbine_layout, null, false);
            turbine.setOnTouchListener(this);
            turbine.setId(turbineId++);
            addComponent(turbine);
        });
        pumpImg.setOnClickListener(v -> {
            removeSelection();
            View pump = getLayoutInflater().inflate(R.layout.pump_layout, null, false);
            pump.setOnTouchListener(this);
            pump.setId(pumpId++);
            addComponent(pump);
        });
        condenserImg.setOnClickListener(v -> {
            removeSelection();
            View condenser = getLayoutInflater().inflate(R.layout.condenser_layout, null, false);
            condenser.setOnTouchListener(this);
            condenser.setId(condenserId++);
            addComponent(condenser);
        });
    }

    private void removeSelection() {
        if (lastViewTouched != null) {
            leftBox = lastViewTouched.findViewById(R.id.leftSelection);
            leftBox.setVisibility(View.GONE);

            rightBox = lastViewTouched.findViewById(R.id.rightSelection);
            rightBox.setVisibility(View.GONE);
        }
        isVisible = false;
        firstTouch = true;
    }

    private void tapEvents() {
        gd.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
            @Override
            public boolean onSingleTapConfirmed(@NonNull MotionEvent e) {
                showRemoveBorders();
                return false;
            }

            @Override
            public boolean onDoubleTap(@NonNull MotionEvent e) {
                // TODO: Display the properties in the dialogue after typing a component
                openDialog();
                return false;
            }

            @Override
            public boolean onDoubleTapEvent(@NonNull MotionEvent e) {
                return false;
            }
        });
    }

    private void openDialog() {
        ComponentDialog dialog = new ComponentDialog();
        dialog.setCanvas(canvasRelLay);
        dialog.setLastViewTouched(lastViewTouched);
        dialog.show(getSupportFragmentManager(), "Component dialog");

    }

    private boolean isViewInside(View v, int rx, int ry) {
        int margin = 100;
        int[] l = new int[2];
        v.getLocationOnScreen(l);

        Rect rect = new Rect(
                l[0] + margin,
                l[1] + margin,
                l[0] + v.getWidth() - margin,
                l[1] + v.getHeight() - margin);
        return rect.contains(rx, ry);
    }

    private void addComponent(View v) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(canvasRelLay.getWidth()/2, canvasRelLay.getHeight()/3, 0, 0);

        v.setLayoutParams(params);
        v.setScaleX(1.3f);
        v.setScaleY(1.3f);
        canvasRelLay.addView(v);
    }

    private void initViews() {
        gd = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener());
        settingsBtn = findViewById(R.id.settingsBtn);
        duplicateBtn = findViewById(R.id.duplicateBtn);
        removeBtn = findViewById(R.id.removeBtn);
        confirmBtn = findViewById(R.id.confirmBtn);
        canvasRelLay = findViewById(R.id.canvasRelLay);
        boilerImg = findViewById(R.id.boilerImg);
        condenserImg = findViewById(R.id.condenserImg);
        pumpImg = findViewById(R.id.pumpImg);
        turbineImg = findViewById(R.id.turbineImg);
    }

    private void initLists() {
        TablesDB.setSatWaterTemperatureList();
        TablesDB.setSatWaterPressureList();
        TablesDB.setSuperheatedWaterVaporList();
    }

    @Override
    public void getProperties(String name, String input, String output, String efficiency) {
        TextView inputTxtView = lastViewTouched.findViewById(R.id.inputTxtView);
        inputTxtView.setText(input);

        TextView outputTxtView = lastViewTouched.findViewById(R.id.outputTxtView);
        outputTxtView.setText(output);

        TextView componentName = lastViewTouched.findViewById(R.id.componentName);
        componentName.setText(name);
    }
}