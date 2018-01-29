package com.tikalk.antsmasher.board;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.tikalk.antsmasher.R;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by motibartov on 15/11/2017.
 */

public class ProgressDialogFragment extends DialogFragment {
    private static final String TAG = "ProgressDialogFragment";

    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_LABEL = "label";

    private ProgressDialogEventListener dialogClosedListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialogClosedListener = (ProgressDialogEventListener) getActivity();
        Dialog d = buildDialog(getActivity());
        d.setCanceledOnTouchOutside(false);
        return d;
    }

    private Dialog buildDialog(Context context) {
        final Resources res = getResources();

        View view = LayoutInflater.from(context).inflate(R.layout.progress_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setCancelable(true)
                .setView(view);


        final AlertDialog dialog = builder.create();

        return dialog;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        Log.i(TAG, "onCancel: ");
        dialogClosedListener.onProgressBarCanceled();
    }

    interface ProgressDialogEventListener {
        void onProgressDialogClosed();
        void onProgressBarCanceled();
    }

}
