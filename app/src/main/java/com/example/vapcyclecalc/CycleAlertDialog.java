package com.example.vapcyclecalc;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.content.ContextCompat;

public class CycleAlertDialog extends AppCompatDialogFragment {
    private TextView errorDesc;
    private String errorMsg;
    private Button okBtn;
    private Dialog dialog;
    LayoutInflater inflater;
    View v;

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        init();
        errorDesc.setText(errorMsg);
        setDialog();
        okBtn.setOnClickListener(v1 -> dialog.dismiss());

        return dialog;
    }

    private void setDialog() {
        dialog.setContentView(v);
        dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.dialog_bg_parent));
        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
    }

    private void init() {
        dialog = new Dialog(getActivity());
        inflater = getActivity().getLayoutInflater();
        v = inflater.inflate(R.layout.cycle_alert_dialog, null);
        errorDesc = v.findViewById(R.id.error_desc);
        okBtn = v.findViewById(R.id.okBtnCycleDialog);
    }
}
