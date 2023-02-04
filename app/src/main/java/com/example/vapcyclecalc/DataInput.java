package com.example.vapcyclecalc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class DataInput extends AppCompatActivity {
    private AutoCompleteTextView condenserDDAutoComplete;

    private TextInputLayout condenserTempPressDDLay, boilerTempLay, boilerPressLay;
    private TextInputLayout turbineEffLay, turbineOutputLay, pumpEffLay, condenserTempPressLay;

    private TextInputEditText boilerTempEditTxt, boilerPressEditTxt, condenserTempPressEditTxt;
    private TextInputEditText turbineEffEditText, turbineOutputEditText, pumpEffEditText;

    private Button calculateBtn, backBtn;

    private TextView enthalpyTitle1, enthalpyTitle2, enthalpyTitle3, enthalpyTitle4;
    private TextView enthalpyValue1, enthalpyValue2, enthalpyValue3, enthalpyValue4;
    private TextView turbinePowerTitle, pumpPowerTitle, boilerQTitle, condenserQTitle;
    private TextView turbinePowerValue, pumpPowerValue, boilerQValue, condenserQValue;
    private TextView effTitle, massTitle, effValue, massValue;

    private RelativeLayout canvas;

    private int[] cyclePoints = new int[4];
    private float entropy1, titre, entropyL = -1, entropyV = -1, enthalpyL = -1, enthalpyV = -1;
    private float specificVolumeL, pressure, mass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_input);
        init();

        calculateBtn.setOnClickListener(v -> {
            if (formIsValid()) {
                setCyclePoints();
                setCardsTitle();
                setEnthalpyOneValue();
                setEnthalpyTwoValue();
                setEnthalpyThreeFourValue();
                calculateTheEfficiency();
                calculateMass();
                calculatePowers();
            }
        });

        backBtn.setOnClickListener(v -> finish());
    }

    private void calculateMass() {
        float h1 = Float.parseFloat(enthalpyValue1.getText().toString());
        float h2 = Float.parseFloat(enthalpyValue2.getText().toString());
        float h3 = Float.parseFloat(enthalpyValue3.getText().toString());
        float h4 = Float.parseFloat(enthalpyValue4.getText().toString());
        float w = Float.parseFloat(turbineOutputEditText.getText().toString());

        mass = (float) (w * Math.pow(10, 3) / (h1 - h2 - h4 + h3));
        String s = (Math.round(mass * 100.0) / 100.0) + " kg/s";
        massValue.setText(s);
    }

    private void calculatePowers() {
        float h1 = Float.parseFloat(enthalpyValue1.getText().toString());
        float h2 = Float.parseFloat(enthalpyValue2.getText().toString());
        float h3 = Float.parseFloat(enthalpyValue3.getText().toString());
        float h4 = Float.parseFloat(enthalpyValue4.getText().toString());
        String s;

        // Turbine
        s = (Math.round((mass * (h1 - h2) / Math.pow(10, 3)) * 100.0) / 100.0) + " MW";
        turbinePowerValue.setText(s);

        // Boiler
        s = (Math.round((mass * (h1 - h4) / Math.pow(10, 3)) * 100.0) / 100.0) + " MW";
        boilerQValue.setText(s);

        // Condenser
        s = (Math.round((mass * (h3 - h2) / Math.pow(10, 3)) * 100.0) / 100.0) + " MW";
        condenserQValue.setText(s);

        // Pump
        s = (Math.round((mass * (h4 - h3) / Math.pow(10, 3)) * 100.0) / 100.0) + " MW";
        pumpPowerValue.setText(s);
    }

    private void setEnthalpyThreeFourValue() {
        String s = String.valueOf(Math.round(enthalpyL * 100.0) / 100.0);
        enthalpyValue3.setText(s);

        float boilerPress = Float.parseFloat(boilerPressEditTxt.getText().toString());
        float h4 = enthalpyL + (boilerPress - pressure) * specificVolumeL;
        s = String.valueOf(Math.round(h4 * 100.0) / 100.0);
        enthalpyValue4.setText(s);
    }

    private void setEnthalpyTwoValue() {
        float enthalpyIsentropic, finalEnthalpy;
        boolean valueExist = false;

        // Setting the entropy and enthalpy values
        if (condenserDDAutoComplete.getText().toString().equalsIgnoreCase("T")) {
            for (SatWater obj : TablesDB.satWaterTemperatureList) {
                if (obj.getTemperature() == Float.parseFloat(condenserTempPressEditTxt.getText().toString())) {
                    setEntropyEnthalpy(obj);
                    valueExist = true;
                }
            }

            // If we didn't find the value in the table so we need to do linear interpolation
            if (!valueExist) {
                SatWater interpolationObj = temperatureInterpolation();
                setEntropyEnthalpy(interpolationObj);
            }
        } else {
            for (SatWater obj : TablesDB.satWaterPressureList) {
                if (obj.getPressure() == Float.parseFloat(condenserTempPressEditTxt.getText().toString())) {
                    setEntropyEnthalpy(obj);
                    valueExist = true;
                }
            }

            // If we didn't find the value in the table so we need to do linear interpolation
            if (!valueExist) {
                SatWater interpolationObj = pressureInterpolation();
                setEntropyEnthalpy(interpolationObj);
            }
        }

        // Calculating the titre and isentropic enthalpy
        titre = (entropy1 - entropyL) / (entropyV - entropyL);
        enthalpyIsentropic = enthalpyL + titre * (enthalpyV - enthalpyL);

        // Calculating the real enthalpy and setting the TextView
        float h1 = Float.parseFloat(enthalpyValue1.getText().toString());
        finalEnthalpy = h1 + Float.parseFloat(turbineEffEditText.getText().toString()) * (enthalpyIsentropic - h1);
        String s = String.valueOf(Math.round(finalEnthalpy * 100.0) / 100.0);
        enthalpyValue2.setText(s);
    }

    private void setEntropyEnthalpy(SatWater obj) {
        entropyL = obj.getSatLiquidEntropy();
        entropyV = obj.getSatVaporEntropy();
        enthalpyL = obj.getSatLiquidEnthalpy();
        enthalpyV = obj.getSatVaporEnthalpy();
        specificVolumeL = obj.getSatLiquidSpecificVolume();
        pressure = obj.getPressure();
    }

    private SatWater temperatureInterpolation() {
        SatWater lowerBound = null, upperBound = null, newObj = new SatWater();
        String s = condenserTempPressEditTxt.getText().toString();

        for (int i = 0; i < TablesDB.satWaterPressureList.size(); i++) {
            if (Float.parseFloat(s) < TablesDB.satWaterPressureList.get(i).getPressure()) {
                lowerBound = TablesDB.satWaterTemperatureList.get(i - 1);
                upperBound = TablesDB.satWaterTemperatureList.get(i);
                break;
            }
        }
        if (lowerBound != null && upperBound != null) {
            populateObj(newObj, lowerBound, upperBound);
        }
        return newObj;
    }

    private SatWater pressureInterpolation() {
        SatWater lowerBound = null, upperBound = null, newObj = new SatWater();
        String s = condenserTempPressEditTxt.getText().toString();

        for (int i = 0; i < TablesDB.satWaterPressureList.size(); i++) {
            if (Float.parseFloat(s) < TablesDB.satWaterPressureList.get(i).getPressure()) {
                lowerBound = TablesDB.satWaterPressureList.get(i - 1);
                upperBound = TablesDB.satWaterPressureList.get(i);
                break;
            }
        }
        if (lowerBound != null && upperBound != null) {
            populateObj(newObj, lowerBound, upperBound);
        }
        return newObj;
    }

    private void populateObj(SatWater newObj, SatWater lowerBound, SatWater upperBound) {
        String s = condenserTempPressEditTxt.getText().toString();

        newObj.setPressure(Float.parseFloat(s));
        newObj.setSatLiquidSpecificVolume((newObj.getPressure() - lowerBound.getPressure()) *
                (upperBound.getSatLiquidSpecificVolume() - lowerBound.getSatLiquidSpecificVolume()) /
                (upperBound.getPressure() - lowerBound.getPressure()) +
                lowerBound.getSatLiquidSpecificVolume());
        newObj.setSatVaporSpecificVolume((newObj.getPressure() - lowerBound.getPressure()) *
                (upperBound.getSatVaporSpecificVolume() - lowerBound.getSatVaporSpecificVolume()) /
                (upperBound.getPressure() - lowerBound.getPressure()) +
                lowerBound.getSatVaporSpecificVolume());
        newObj.setSatLiquidEnthalpy((newObj.getPressure() - lowerBound.getPressure()) *
                (upperBound.getSatLiquidEnthalpy() - lowerBound.getSatLiquidEnthalpy()) /
                (upperBound.getPressure() - lowerBound.getPressure()) +
                lowerBound.getSatLiquidEnthalpy());
        newObj.setSatVaporEnthalpy((newObj.getPressure() - lowerBound.getPressure()) *
                (upperBound.getSatVaporEnthalpy() - lowerBound.getSatVaporEnthalpy()) /
                (upperBound.getPressure() - lowerBound.getPressure()) +
                lowerBound.getSatVaporEnthalpy());
        newObj.setSatLiquidEntropy((newObj.getPressure() - lowerBound.getPressure()) *
                (upperBound.getSatLiquidEntropy() - lowerBound.getSatLiquidEntropy()) /
                (upperBound.getPressure() - lowerBound.getPressure()) +
                lowerBound.getSatLiquidEntropy());
        newObj.setSatVaporEntropy((newObj.getPressure() - lowerBound.getPressure()) *
                (upperBound.getSatVaporEntropy() - lowerBound.getSatVaporEntropy()) /
                (upperBound.getPressure() - lowerBound.getPressure()) +
                lowerBound.getSatVaporEntropy());
    }

    private void setEnthalpyOneValue() {
        for (SuperheatedWaterVapor obj : TablesDB.superheatedWaterVaporList) {
            if (obj.getTemperature() == Float.parseFloat(boilerTempEditTxt.getText().toString())) {
                String s = String.valueOf(Math.round(obj.getEnthalpy() * 100.0) / 100.0);
                enthalpyValue1.setText(s);
                entropy1 = obj.getEntropy();
            }
        }
    }

    private void calculateTheEfficiency() {
        float h1 = Float.parseFloat(enthalpyValue1.getText().toString());
        float h2 = Float.parseFloat(enthalpyValue2.getText().toString());
        float h3 = Float.parseFloat(enthalpyValue3.getText().toString());
        float h4 = Float.parseFloat(enthalpyValue4.getText().toString());

        float eff = ((h1 - h2 - h4 + h3) / (h1 - h4)) * 100;
        String s = (Math.round(eff * 100.0) / 100.0) + " %";
        effValue.setText(s);
    }

    private void setCardsTitle() {
        enthalpyTitle1.setText(Html.fromHtml("h<sub>" + cyclePoints[0] + "</sub>", Html.FROM_HTML_MODE_LEGACY));
        enthalpyTitle2.setText(Html.fromHtml("h<sub>" + cyclePoints[1] + "</sub>", Html.FROM_HTML_MODE_LEGACY));
        enthalpyTitle3.setText(Html.fromHtml("h<sub>" + cyclePoints[2] + "</sub>", Html.FROM_HTML_MODE_LEGACY));
        enthalpyTitle4.setText(Html.fromHtml("h<sub>" + cyclePoints[3] + "</sub>", Html.FROM_HTML_MODE_LEGACY));
        turbinePowerTitle.setText(Html.fromHtml("W<sub>t</sub>", Html.FROM_HTML_MODE_LEGACY));
        pumpPowerTitle.setText(Html.fromHtml("W<sub>p</sub>", Html.FROM_HTML_MODE_LEGACY));
        boilerQTitle.setText(Html.fromHtml("Q<sub>c</sub>", Html.FROM_HTML_MODE_LEGACY));
        condenserQTitle.setText(Html.fromHtml("Q<sub>f</sub>", Html.FROM_HTML_MODE_LEGACY));
        massTitle.setText("m");
        effTitle.setText("\uD835\uDF02");
    }

    private void setCyclePoints() {
        View boilerView, condenserView, turbineView, pumpView;

        for (int i = 0; i < canvas.getChildCount(); i++) {
            int id = canvas.getChildAt(i).getId();

            if (id >= 100 && id < 200) {
                boilerView = canvas.getChildAt(i);
                TextView output = boilerView.findViewById(R.id.outputTxtView);
                cyclePoints[0] = Integer.parseInt(output.getText().toString());
            } else if (id >= 200 && id < 300) {
                condenserView = canvas.getChildAt(i);
                TextView output = condenserView.findViewById(R.id.outputTxtView);
                cyclePoints[2] = Integer.parseInt(output.getText().toString());
            } else if (id >= 300 && id < 400) {
                turbineView = canvas.getChildAt(i);
                TextView output = turbineView.findViewById(R.id.outputTxtView);
                cyclePoints[1] = Integer.parseInt(output.getText().toString());
            } else if (id >= 400 && id < 500) {
                pumpView = canvas.getChildAt(i);
                TextView output = pumpView.findViewById(R.id.outputTxtView);
                cyclePoints[3] = Integer.parseInt(output.getText().toString());
            }
        }
    }

    private boolean formIsValid() {
        boolean boilerT, boilerP, condenserTP, condenserDD, turbineP, turbineEff, pumpEff;

        // boiler
        String s = boilerTempEditTxt.getText().toString();
        boilerT = (s.equals("") || Float.parseFloat(s) <= 0);
        s = boilerPressEditTxt.getText().toString();
        boilerP = (s.equals("") || Float.parseFloat(s) <= 0);

        // Condenser
        s = condenserTempPressEditTxt.getText().toString();
        condenserTP = (s.equals("") || Float.parseFloat(s) <= 0);
        condenserDD = condenserDDAutoComplete.getText().toString().equals("");

        // Turbine
        s = turbineEffEditText.getText().toString();
        turbineEff = (s.equals("") || Float.parseFloat(s) <= 0 || Float.parseFloat(s) > 1);
        s = turbineOutputEditText.getText().toString();
        turbineP = (s.equals("") || Float.parseFloat(s) <= 0);

        // Pump
        s = pumpEffEditText.getText().toString();
        pumpEff = (s.equals("") || Float.parseFloat(s) <= 0 || Float.parseFloat(s) > 1);

        showError(boilerT, boilerP, condenserTP, condenserDD, turbineP, turbineEff, pumpEff);
        return !boilerT && !boilerP && !condenserTP && !condenserDD && !turbineP && !turbineEff && !pumpEff;
    }

    private void showError(boolean boilerT, boolean boilerP, boolean condenserTP, boolean condenserDD,
                           boolean turbineP, boolean turbineEff, boolean pumpEff) {

        String valueErrorMsg = "Choose a value > 0!";
        String effErrorMsg = "The efficiency is between 0 and 1!";
        String ddErrorMsg = "This field can't be empty!";

        if (boilerT) {
            boilerTempLay.setError(valueErrorMsg);
            boilerTempLay.setErrorEnabled(true);
        } else {
            boilerTempLay.setErrorEnabled(false);
        }

        if (boilerP) {
            boilerPressLay.setError(valueErrorMsg);
            boilerPressLay.setErrorEnabled(true);
        } else {
            boilerPressLay.setErrorEnabled(false);
        }

        if (condenserTP) {
            condenserTempPressLay.setError(valueErrorMsg);
            condenserTempPressLay.setErrorEnabled(true);
        } else {
            condenserTempPressLay.setErrorEnabled(false);
        }

        if (condenserDD) {
            condenserTempPressDDLay.setError(ddErrorMsg);
            condenserTempPressDDLay.setErrorEnabled(true);
        } else {
            condenserTempPressDDLay.setErrorEnabled(false);
        }

        if (turbineP) {
            turbineOutputLay.setError(valueErrorMsg);
            turbineOutputLay.setErrorEnabled(true);
        } else {
            turbineOutputLay.setErrorEnabled(false);
        }

        if (turbineEff) {
            turbineEffLay.setError(effErrorMsg);
            turbineEffLay.setErrorEnabled(true);
        } else {
            turbineEffLay.setErrorEnabled(false);
        }

        if (pumpEff) {
            pumpEffLay.setError(effErrorMsg);
            pumpEffLay.setErrorEnabled(true);
        } else {
            pumpEffLay.setErrorEnabled(false);
        }
    }

    private void init() {
        canvas = MainActivity.canvasRelLay;
        effTitle = findViewById(R.id.globalEffTitle);
        massTitle = findViewById(R.id.massTitle);
        effValue = findViewById(R.id.globalEffVal);
        massValue = findViewById(R.id.massValue);
        turbinePowerValue = findViewById(R.id.turbineOutputValue);
        pumpPowerValue = findViewById(R.id.pumpPowerValue);
        boilerQValue = findViewById(R.id.boilerQValue);
        condenserQValue = findViewById(R.id.condenserQValue);
        turbinePowerTitle = findViewById(R.id.turbineOutputTitle);
        pumpPowerTitle = findViewById(R.id.pumpPowerTitle);
        boilerQTitle = findViewById(R.id.boilerQTitle);
        condenserQTitle = findViewById(R.id.condenserQTitle);
        enthalpyValue1 = findViewById(R.id.enthalpyOneValue);
        enthalpyValue2 = findViewById(R.id.enthalpyTwoValue);
        enthalpyValue3 = findViewById(R.id.enthalpyThreeValue);
        enthalpyValue4 = findViewById(R.id.enthalpyFourValue);
        enthalpyTitle1 = findViewById(R.id.enthalpyOneTitle);
        enthalpyTitle2 = findViewById(R.id.enthalpyTwoTitle);
        enthalpyTitle3 = findViewById(R.id.enthalpyThreeTitle);
        enthalpyTitle4 = findViewById(R.id.enthalpyFourTitle);
        pumpEffEditText = findViewById(R.id.pumpEffEditTxt);
        pumpEffLay = findViewById(R.id.pumpEffLay);
        turbineOutputEditText = findViewById(R.id.turbineOutEditTxt);
        turbineOutputLay = findViewById(R.id.turbineOutLay);
        turbineEffEditText = findViewById(R.id.turbineEffEditTxt);
        turbineEffLay = findViewById(R.id.turbineEffLay);
        condenserTempPressLay = findViewById(R.id.condenserTempPressLay);
        condenserTempPressEditTxt = findViewById(R.id.condenserTempPressEditTxt);
        condenserDDAutoComplete = findViewById(R.id.condenserTempPressDropDownMenu);
        condenserTempPressDDLay = findViewById(R.id.condenserTempPressDropDownMenuLay);
        backBtn = findViewById(R.id.backBtn);
        calculateBtn = findViewById(R.id.calculateBtn);
        boilerTempLay = findViewById(R.id.boilerTempLay);
        boilerTempEditTxt = findViewById(R.id.boilerTempEditTxt);
        boilerPressLay = findViewById(R.id.boilerPressLay);
        boilerPressEditTxt = findViewById(R.id.boilerPressEditTxt);

    }
}