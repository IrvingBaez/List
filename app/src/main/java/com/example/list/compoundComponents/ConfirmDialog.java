package com.example.list.compoundComponents;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.list.R;

public class ConfirmDialog extends AppCompatDialogFragment {
    private ConfirmDialogListener listener;
    private final int title;
    private final int message;
    private final int negative;
    private final int positive;

    public ConfirmDialog(int title, int message, int negative, int positive) {
        this.title = title;
        this.message = message;
        this.negative = negative;
        this.positive = positive;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(this.title).setMessage(this.message)
                .setNegativeButton(this.negative, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton(this.positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onYesClicked();
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (ConfirmDialogListener) context;
    }

    public interface ConfirmDialogListener{
        void onYesClicked();
    }
}