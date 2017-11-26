package com.tikalk.antsmasher.teams;

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
import com.tikalk.antsmasher.data.PrefsHelper;


/**
 * Created by motibartov on 15/11/2017.
 */

public class IpDialogFragment extends DialogFragment {

    private static final int INPUT_MAX_LENGTH = 16;
    EditDialogEventListener eventListener;
    Button posButton;
    EditText etAntIp;
    EditText etAdminIp;
    EditText etSmashIp;

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

        filters[0] = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       android.text.Spanned dest, int dstart, int dend) {
                if (end > start) {
                    String destTxt = dest.toString();
                    String resultingTxt = destTxt.substring(0, dstart)
                            + source.subSequence(start, end)
                            + destTxt.substring(dend);
                    if (!resultingTxt
                            .matches("^\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3})?)?)?)?)?)?")) {
                        return "";
                    } else {
                        String[] splits = resultingTxt.split("\\.");
                        for (int i = 0; i < splits.length; i++) {
                            if (Integer.valueOf(splits[i]) > 255) {
                                return "";
                            }
                        }
                    }
                }
                return null;
            }

        };

        // Set an EditText view to get user input
        etAntIp = new EditText(context);
        etAntIp.setMaxLines(1);
        etAntIp.setFilters(new InputFilter[]{new InputFilter.LengthFilter(
                INPUT_MAX_LENGTH), filters[0]});

        etAntIp.setHint(getString(R.string.antIpHint));

        final TextView antsChars = new TextView(context);
        antsChars.setPadding(5, 0, 0, 2);

        etAdminIp = new EditText(context);
        etAdminIp.setFilters(new InputFilter[]{new InputFilter.LengthFilter(
                INPUT_MAX_LENGTH), filters[0]});

        etAdminIp.setHint(getString(R.string.adminIpHint));

        final TextView adminChars = new TextView(context);
        adminChars.setPadding(5, 0, 0, 2);

        etSmashIp = new EditText(context);
        etSmashIp.setFilters(new InputFilter[]{new InputFilter.LengthFilter(
                INPUT_MAX_LENGTH), filters[0]});
        etSmashIp.setHint(getString(R.string.smashIpHint));

        final TextView smashChars = new TextView(context);
        smashChars.setPadding(5, 0, 0, 2);


        String antsPrefIp = getArguments().getString(PrefsHelper.ANTPUBLISH_SOCKET_URL);
        String adminPrefIp = getArguments().getString(PrefsHelper.ADMIN_IP);
        String smashPrefIp = getArguments().getString(PrefsHelper.SMASH_SOCKET_URL);

        // FIXME move this to strings.xml %1$d/%2$d
        if (antsPrefIp != null) {  //This means that recording had a comment already..
            antsChars.setText(antsPrefIp.length() + "/" + INPUT_MAX_LENGTH);
            etAntIp.setText(antsPrefIp);
        } else {
            antsChars.setText("0/" + INPUT_MAX_LENGTH);
            etAntIp.setText("");
        }


        if (adminPrefIp != null) {  //This means that recording had a comment already..
            adminChars.setText(adminPrefIp.length() + "/" + INPUT_MAX_LENGTH);
            etAdminIp.setText(adminPrefIp);
        } else {
            adminChars.setText("0/" + INPUT_MAX_LENGTH);
            etAdminIp.setText("");
        }

        if (smashPrefIp != null) {  //This means that recording had a comment already..
            smashChars.setText(adminPrefIp.length() + "/" + INPUT_MAX_LENGTH);
            etSmashIp.setText(adminPrefIp);
        } else {
            smashChars.setText("0/" + INPUT_MAX_LENGTH);
            etSmashIp.setText("");
        }

        LinearLayout.LayoutParams tv1Params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        tv1Params.bottomMargin = 5;
        layout.addView(etAntIp, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.addView(antsChars, tv1Params);

        layout.addView(etAdminIp, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.addView(adminChars, tv1Params);

        layout.addView(etSmashIp, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.addView(smashChars, tv1Params);

        builder.setView(layout);

        builder.setPositiveButton(R.string.ok_button,
                (dialog, whichButton) -> {

                    eventListener.onEditDone(etAntIp.getText().toString().trim(), etAdminIp.getText().toString().trim(), etSmashIp.getText().toString().trim());
                    // Toast.makeText(context, value + " entered..",
                    // Toast.LENGTH_LONG).show();
                });


        builder.setTitle(getString(R.string.app_name));
        builder.setIcon(ActivityCompat.getDrawable(context, R.mipmap.ic_launcher));
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                posButton = ((AlertDialog) dialogInterface).getButton(AlertDialog.BUTTON_POSITIVE);
                posButton.setEnabled(false);
            }
        });


        etAntIp.addTextChangedListener(new TextWatcher() {
            // StringBuilder builder = new StringBuilder();
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String text = etAntIp.getText().toString().trim();
                // FIXME move this to strings.xml %1$d/%2$d
                antsChars.setText(text.length() + "/" + INPUT_MAX_LENGTH);
                updateButtonState();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        etAdminIp.addTextChangedListener(new TextWatcher() {
            // StringBuilder builder = new StringBuilder();
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String text = etAdminIp.getText().toString().trim();
                // FIXME move this to strings.xml %1$d/%2$d
                adminChars.setText(text.length() + "/" + INPUT_MAX_LENGTH);
                updateButtonState();

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etSmashIp.addTextChangedListener(new TextWatcher() {
            // StringBuilder builder = new StringBuilder();
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String text = etSmashIp.getText().toString().trim();
                // FIXME move this to strings.xml %1$d/%2$d
                smashChars.setText(text.length() + "/" + INPUT_MAX_LENGTH);
                updateButtonState();
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

    void updateButtonState() {
        if ((etAntIp.getText().length() > 0) && (etAdminIp.getText().length() > 0) && (etSmashIp.getText().length() > 0)) {
            posButton.setEnabled(true);
        } else {
            posButton.setEnabled(false);
        }
    }

    public interface EditDialogEventListener {
        void onEditDone(String enteredAntIp, String enteredAdminIp, String enteredSmashIp);
    }


    InputFilter[] filters = new InputFilter[1];


}
