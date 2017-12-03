package com.tikalk.antsmasher.login_screen;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tikalk.antsmasher.R;

/**
 * Created by motibartov on 15/11/2017.
 */

public class EditDialogFragment extends DialogFragment {

    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_LABEL = "label";

    private static final int EDIT_MAX_LENGTH = 25;

    private EditDialogEventListener eventListener;
    private Button posButton;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        eventListener = (EditDialogEventListener) getActivity();
        return buildDialog(getActivity());
    }

    //FIXME move code to a layout xml
    private AlertDialog buildDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setIcon(ActivityCompat.getDrawable(context, R.mipmap.ic_launcher))
                .setTitle(getArguments().getString(EXTRA_TITLE, getString(R.string.app_name)))
                .setMessage(getArguments().getString(EXTRA_LABEL));

        LinearLayout layout = new LinearLayout(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(lp);

        // Set an EditText view to get user input
        final EditText input = new EditText(context);
        final TextView chars = new TextView(context);

        input.setMaxLines(1);
        input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(EDIT_MAX_LENGTH)});

        chars.setPadding(5, 0, 0, 2);
        String comment = getArguments().getString("Comment");
        // FIXME move this to strings.xml %1$d/%2$d
        if (comment != null) {  //This means that recording had a comment already..
            chars.setText(comment.length() + "/" + EDIT_MAX_LENGTH);
            input.setText(comment);
        } else {
            chars.setText("0/" + EDIT_MAX_LENGTH);
            input.setText("");
        }

        LinearLayout.LayoutParams tv1Params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        tv1Params.bottomMargin = 5;
        layout.addView(input, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.addView(chars, tv1Params);

        builder.setView(layout);

        builder.setPositiveButton(R.string.ok_button,
                (dialog, whichButton) -> {
                    String value = input.getText().toString().trim();
                    eventListener.onUserNameEntered(value);
                });

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(dialogInterface -> {
            String text = input.getText().toString().trim();
            posButton = ((AlertDialog) dialogInterface).getButton(AlertDialog.BUTTON_POSITIVE);
            posButton.setEnabled(text.length() > 0);
        });

        input.addTextChangedListener(new TextWatcher() {
            // StringBuilder builder = new StringBuilder();
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (posButton == null) {
                    return;
                }
                String text = input.getText().toString().trim();
                // FIXME move this to strings.xml %1$d/%2$d
                chars.setText(text.length() + "/" + EDIT_MAX_LENGTH);
                if (text.length() > 0) {
                    posButton.setEnabled(true);
                } else {
                    posButton.setEnabled(false);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        return dialog;
    }

    public interface EditDialogEventListener {
        void onUserNameEntered(String comment);
    }

}
