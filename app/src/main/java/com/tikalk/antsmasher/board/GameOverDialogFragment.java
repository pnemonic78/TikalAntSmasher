package com.tikalk.antsmasher.board;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.tikalk.antsmasher.R;
import com.tikalk.antsmasher.model.Team;

import static com.tikalk.graphics.ImageUtils.tintImage;

/**
 * Created by motibartov on 15/11/2017.
 */

public class GameOverDialogFragment extends DialogFragment {

    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_LABEL = "label";
    public static final String EXTRA_VALUE = "value";
    public static final String EXTRA_TEAMS = "teams";
    public static final String EXTRA_TEAM1 = "team1";
    public static final String EXTRA_TEAM2 = "team2";
    public static final String EXTRA_TEAM3 = "team3";
    public static final String EXTRA_WINNER = "winner";
    public static final String EXTRA_SCORE = "score";
    public static final String EXTRA_NAME = "name";

    int antWidth;
    int antHeight;
    GameOverDialogListener gameOverDialogListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        antWidth = getActivity().getResources().getDimensionPixelSize(R.dimen.ant_width);
        antHeight = getActivity().getResources().getDimensionPixelSize(R.dimen.ant_height);

        gameOverDialogListener = (GameOverDialogListener) getActivity();
        return buildDialog(getActivity());
    }

    private Dialog buildDialog(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.game_over_dialog, null);

        Bundle b = getArguments();

        Bundle teamsBundle = b.getBundle(EXTRA_TEAMS);

        Team teamA = teamsBundle.getParcelable(EXTRA_TEAM1);
        Bitmap bitmap = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.ant_normal);

        bitmap = Bitmap.createScaledBitmap(bitmap, (int) (antWidth), (int) (antHeight), false);
        bitmap = tintImage(bitmap, teamA.getAntSpecies().getTint());
        Drawable drawable = new BitmapDrawable(getActivity().getResources(), bitmap);
        drawable.setBounds(0, 0, antWidth, antHeight);


        Team teamB = teamsBundle.getParcelable(EXTRA_TEAM2);
        bitmap = tintImage(bitmap, teamB.getAntSpecies().getTint());
        Drawable drawableB = new BitmapDrawable(getActivity().getResources(), bitmap);
        drawableB.setBounds(0, 0, antWidth, antHeight);

        Team teamC = teamsBundle.getParcelable(EXTRA_TEAM3);
        bitmap = tintImage(bitmap, teamC.getAntSpecies().getTint());
        Drawable drawableC = new BitmapDrawable(getActivity().getResources(), bitmap);
        drawableC.setBounds(0, 0, antWidth, antHeight);

        final TextView tvTeamA = view.findViewById(R.id.teamA);
        final TextView tvTeamB = view.findViewById(R.id.teamB);
        final TextView tvTeamC = view.findViewById(R.id.teamC);
        final TextView tvWinner = view.findViewById(R.id.tvWinner);

        tvTeamA.setText(teamA.getName() + " -  " + teamA.getAntSpecies().getName() + ": " + teamA.getScore());
        tvTeamA.setCompoundDrawables(drawable, null, null, null);
        tvTeamA.setCompoundDrawablePadding((int) getActivity().getResources().getDimension(R.dimen.tiny_padding));

        tvTeamB.setText(teamB.getName() + " - " + teamB.getAntSpecies().getName() + ": " + teamB.getScore());
        tvTeamB.setCompoundDrawables(drawableB, null, null, null);
        tvTeamB.setCompoundDrawablePadding((int) getActivity().getResources().getDimension(R.dimen.tiny_padding));

        tvTeamC.setText(teamC.getName() + " - " + teamC.getAntSpecies().getName() + ": " + teamC.getScore());
        tvTeamC.setCompoundDrawables(drawableC, null, null, null);
        tvTeamC.setCompoundDrawablePadding((int) getActivity().getResources().getDimension(R.dimen.tiny_padding));

        Bundle winnerBundle = teamsBundle.getBundle(EXTRA_WINNER);
        tvWinner.setText(winnerBundle.getString("name") + ":" + winnerBundle.getString("teamName") + " - " + winnerBundle.getInt("score"));

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setIcon(ActivityCompat.getDrawable(context, R.mipmap.ic_launcher))
                .setTitle(getArguments().getString(EXTRA_TITLE, getString(R.string.app_name)))
                .setMessage(getArguments().getString(EXTRA_LABEL))
                .setCancelable(false)
                .setView(view)
                .setPositiveButton(R.string.ok_button,
                        (dialog, whichButton) -> {
                            gameOverDialogListener.onDialogClosed();
                        });

        AlertDialog dialog = builder.create();

        return dialog;
    }

    interface GameOverDialogListener {
        void onDialogClosed();
    }

}
