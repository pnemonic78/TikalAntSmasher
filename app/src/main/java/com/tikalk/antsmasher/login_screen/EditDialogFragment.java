package com.tikalk.antsmasher.login_screen;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tikalk.antsmasher.R;


/**
 * Created by motibartov on 15/11/2017.
 */

public class EditDialogFragment extends DialogFragment {

    private static final int COMMENT_MAX_LENGTH = 25;
    EditDialogEventListener eventListener;
    Button posButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        eventListener = (EditDialogEventListener) getActivity();
        return buildDialog(getActivity());
    }


    public AlertDialog buildDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(ActivityCompat.getDrawable(context, R.mipmap.ic_launcher));
        builder.setTitle(getArguments().getString("Title"));
        builder.setMessage(getArguments().getString("Message"));

        LinearLayout layout = new LinearLayout(context);
        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(parms);

        // Set an EditText view to get user input
        final EditText input = new EditText(context);
        final TextView chars = new TextView(context);

        input.setMaxLines(1);
        input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(
                COMMENT_MAX_LENGTH)});

        chars.setPadding(5, 0, 0, 2);
        String comment = getArguments().getString("Comment");
        if (comment != null) {  //This means that recording had a comment already..
            chars.setText(comment.length() + "/" + COMMENT_MAX_LENGTH);
            input.setText(comment);
        } else {
            chars.setText("0/" + COMMENT_MAX_LENGTH);
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
                    eventListener.onEditDone(value);
                    // Toast.makeText(context, value + " entered..",
                    // Toast.LENGTH_LONG).show();
                });


        builder.setTitle(getString(R.string.app_name));
        builder.setIcon(ActivityCompat.getDrawable(context, R.mipmap.ic_launcher));
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                posButton = ((AlertDialog)dialogInterface).getButton(AlertDialog.BUTTON_POSITIVE);
                posButton.setEnabled(false);
            }
        });


        input.addTextChangedListener(new TextWatcher() {
            // StringBuilder builder = new StringBuilder();
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String text = input.getText().toString().trim();
                chars.setText(text.length() + "/" + COMMENT_MAX_LENGTH);
                if(text.length() > 0){
                    posButton.setEnabled(true);
                }else {
                    posButton.setEnabled(false);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return dialog;
    }

    public interface EditDialogEventListener {
        void onEditDone(String comment);
    }

}
