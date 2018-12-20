package com.example.diesel.testtask;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class ThingItemDialogFragment extends DialogFragment {

    public interface ThingItemDialogListener {
        public void onDialogPositiveClick(ThingItemDialogFragment dialog);

        public void onDialogNegativeClick(ThingItemDialogFragment dialog);
    }

    public TextView title;
    public CheckBox checked;
    public TextView uid;

    ThingItemDialogListener mListener;

    void registerThingItemDialogListener(ThingItemDialogListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View layout;
        layout = inflater.inflate(R.layout.dialog_thing, null);
        title = layout.findViewById(R.id.dialog_title);
        title.setText(getArguments().getString("TITLE"));
        checked = layout.findViewById(R.id.dialog_checked);
        checked.setChecked(getArguments().getBoolean("CHECKED"));
        uid = layout.findViewById(R.id.dialog_uid);
        uid.setText(getArguments().getString("UID"));
        builder.setView(layout)
                .setMessage(R.string.hint_thing)
                .setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            if (!title.getText().toString()
                                    .equals(getArguments().getString("TITLE"))) {
                                Toast.makeText(getActivity(), "unsaved data!",
                                        Toast.LENGTH_LONG).show();
                                return true;
                            }
                        }
                        return false;
                    }
                })
                .setPositiveButton(R.string.thing_dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //save Thing props
                        mListener.onDialogPositiveClick(ThingItemDialogFragment.this);
                    }
                })
                .setNegativeButton(R.string.thing_dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //cancel saving
                        ThingItemDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}
