package com.tikalk.antsmasher.login_screen;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tikalk.antsmasher.R;

/**
 * Created by motibartov on 15/11/2017.
 */

public class IpDialogFragment extends DialogFragment {

    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_LABEL = "label";
    public static final String EXTRA_VALUE = "value";

    private IpDialogEventListener eventListener;
    private Button posButton;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        eventListener = (IpDialogEventListener) getActivity();
        return buildDialog(getActivity());
    }

    private Dialog buildDialog(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.edit_authority, null);

        posButton = null;
        final EditText input = view.findViewById(android.R.id.edit);
        final TextView count = view.findViewById(android.R.id.text1);
        final int maxLength = getResources().getInteger(R.integer.max_username_length);

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString().trim();
                count.setText(getString(R.string.edit_count, text.length(), maxLength));
                if (posButton != null) {
                    posButton.setEnabled(text.length() > 0);
                }
            }
        });
        input.setText(getArguments().getString(EXTRA_VALUE));

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setCancelable(false)
                .setIcon(ActivityCompat.getDrawable(context, R.mipmap.ic_launcher))
                .setTitle(getArguments().getString(EXTRA_TITLE, getString(R.string.app_name)))
                .setMessage(getArguments().getString(EXTRA_LABEL))
                .setView(view)
                .setPositiveButton(R.string.ok_button,
                        (dialog, whichButton) -> {
                            String value = input.getText().toString().trim();
                            eventListener.onIpEntered(value);
                        });

        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener(dialogInterface -> {
            String text = input.getText().toString().trim();
            posButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            posButton.setEnabled(text.length() > 0);
        });

        return dialog;
    }

    public interface IpDialogEventListener {
        void onIpEntered(String comment);
    }
}
