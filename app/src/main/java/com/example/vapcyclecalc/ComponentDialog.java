package com.example.vapcyclecalc;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Locale;

public class ComponentDialog extends AppCompatDialogFragment {
    private TextInputEditText name, input, output;
    private TextInputLayout nameLay, inputLay, outputLay;
    private Button okBtn, cancelBtn;
    private Dialog dialog;
    private componentDialogListener componentDialogListener;
    private RelativeLayout canvas;
    private View lastViewTouched;

    String inOutMsg = "Type a number";
    String nameMsg = "Choose a name!";

    public void setLastViewTouched(View lastViewTouched) {
        this.lastViewTouched = lastViewTouched;
    }

    public void setCanvas(RelativeLayout canvas) {
        this.canvas = canvas;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            componentDialogListener = (componentDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context + "must implement componentDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // TODO: customize the layout based on the component
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.properties_dialog, null);

        initViews(v);
        setProperties(lastViewTouched);
        setDialog(v);
        onClickListeners();

        return dialog;
    }

    private void setProperties(View v) {
        TextView nameComponent = v.findViewById(R.id.componentName);
        name.setText(nameComponent.getText().toString());

        TextView inputComponent = v.findViewById(R.id.inputTxtView);
        input.setText(inputComponent.getText().toString());

        TextView outputComponent = v.findViewById(R.id.outputTxtView);
        output.setText(outputComponent.getText().toString());
    }

    private void onClickListeners() {
        okBtn.setOnClickListener(v1 -> {
            if (isValid()) {
                String nameStr = name.getText().toString();
                String inStr = input.getText().toString();
                String outStr = output.getText().toString();

                componentDialogListener.getProperties(nameStr, inStr, outStr);
                dialog.dismiss();
            }
        });

        cancelBtn.setOnClickListener(v12 -> {
            dialog.dismiss();
        });
    }

    private boolean isValid() {
        boolean nameBool, inBool, outBool;

        nameBool = nameValidation();
        inBool = inputValidation();
        outBool = outputValidation();

        showError(nameBool, inBool, outBool);
        return !nameBool && !inBool && !outBool;
    }

    private boolean inputValidation() {
        if (input.getText().toString().equals("")) {
            inOutMsg = "Type a number";
            return true;
        }
        if (input.getText().toString().equalsIgnoreCase("IN")) {
            inOutMsg = "Replace this field with a number";
            return true;
        }
        if (input.getText().toString().equals(output.getText().toString())) {
            inOutMsg = "Input and output have the same number";
            return true;
        }

        int counter = 0;
        for (int i = 0 ; i < canvas.getChildCount() ; i++) {
            if (lastViewTouched.getId() == canvas.getChildAt(i).getId())
                continue;

            TextView childInputTxtView = canvas.getChildAt(i).findViewById(R.id.inputTxtView);
            if (childInputTxtView.getText().toString().equalsIgnoreCase("IN"))
                continue;

            int childInput = Integer.parseInt(childInputTxtView.getText().toString());
            if (Integer.parseInt(input.getText().toString()) == childInput) {
                counter++;
                inOutMsg = "Number already exists!";
            }
        }
        return counter != 0;
    }

    private boolean outputValidation() {
        if (output.getText().toString().equals("")) {
            inOutMsg = "Type a number";
            return true;
        }
        if (output.getText().toString().equalsIgnoreCase("OUT")) {
            inOutMsg = "Replace this field with a number";
            return true;
        }
        if (input.getText().toString().equals(output.getText().toString())) {
            inOutMsg = "Input and output have the same number";
            return true;
        }

        int counter = 0;
        for (int i = 0 ; i < canvas.getChildCount() ; i++) {
            if (lastViewTouched.getId() == canvas.getChildAt(i).getId())
                continue;

            TextView childOutputTxtView = canvas.getChildAt(i).findViewById(R.id.outputTxtView);
            if (childOutputTxtView.getText().toString().equalsIgnoreCase("OUT"))
                continue;

            int childOutput = Integer.parseInt(childOutputTxtView.getText().toString());
            if (Integer.parseInt(output.getText().toString()) == childOutput) {
                counter++;
                inOutMsg = "Number already exists!";
            }
        }
        return counter != 0;
    }

    private boolean nameValidation() {
        if (name.getText().toString().equals("")) {
            nameMsg = "Give the component a name";
            return true;
        }

        int counter = 0;
        for (int i = 0 ; i < canvas.getChildCount() ; i++) {
            if (lastViewTouched.getId() == canvas.getChildAt(i).getId())
                continue;

            TextView childNameTxtView = canvas.getChildAt(i).findViewById(R.id.componentName);
            if (name.getText().toString().equals(childNameTxtView.getText().toString())) {
                counter++;
                nameMsg = "This name already exists!";
            }
        }
        return counter != 0;
    }

    private void showError(boolean nameBool, boolean inBool, boolean outBool) {
        if (nameBool) {
            nameLay.setError(nameMsg);
            nameLay.setErrorEnabled(true);
        } else {
            nameLay.setErrorEnabled(false);
        }

        if (inBool) {
            inputLay.setError(inOutMsg);
            inputLay.setErrorEnabled(true);
        } else {
            inputLay.setErrorEnabled(false);
        }

        if (outBool) {
            outputLay.setError(inOutMsg);
            outputLay.setErrorEnabled(true);
        } else {
            outputLay.setErrorEnabled(false);
        }
    }

    private void setDialog(View v) {
        dialog.setContentView(v);
        dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.dialog_bg_parent));
        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
    }

    private void initViews(View view) {
        dialog = new Dialog(getActivity());
        name = view.findViewById(R.id.txtInputEditTxtName);
        input = view.findViewById(R.id.txtInputEditTxtInput);
        output = view.findViewById(R.id.txtInputEditTxtOutput);
        nameLay = view.findViewById(R.id.textInputLayoutName);
        inputLay = view.findViewById(R.id.textInputLayInput);
        outputLay = view.findViewById(R.id.textInputLayOutput);
        okBtn = view.findViewById(R.id.okBtnDialog);
        cancelBtn = view.findViewById(R.id.cancelBtnDialog);
    }

    public interface componentDialogListener {
        void getProperties(String name, String input, String output);
    }
}
