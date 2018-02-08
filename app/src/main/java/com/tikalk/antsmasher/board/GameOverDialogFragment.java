package com.tikalk.antsmasher.board;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.tikalk.antsmasher.R;
import com.tikalk.antsmasher.model.Player;
import com.tikalk.antsmasher.model.Team;

import static com.tikalk.graphics.ImageUtils.tintImage;

/**
 * Created by motibartov on 15/11/2017.
 */

public class GameOverDialogFragment extends DialogFragment {

    private static final String TAG = "TAG_GameOverDialog";
    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_LABEL = "label";
    public static final String EXTRA_SCOREBOARD = "scoreBoard";
    public static final String EXTRA_TEAM1 = "team1";
    public static final String EXTRA_TEAM2 = "team2";
    public static final String EXTRA_TEAM3 = "team3";
    public static final String EXTRA_WINNER = "winner";

    private GameOverDialogListener gameOverDialogListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        gameOverDialogListener = (GameOverDialogListener) getActivity();
        return buildDialog(getActivity());
    }

    private Dialog buildDialog(Context context) {
        final Resources res = getResources();
        int antWidth = res.getDimensionPixelSize(R.dimen.ant_width);
        int antHeight = res.getDimensionPixelSize(R.dimen.ant_height);
        int iconPadding = res.getDimensionPixelSize(R.dimen.team_score_padding);

        View view = LayoutInflater.from(context).inflate(R.layout.game_over_dialog, null);

        final TextView tvTeamA = view.findViewById(R.id.teamA);
        final TextView tvTeamB = view.findViewById(R.id.teamB);
        final TextView tvTeamC = view.findViewById(R.id.teamC);
        final TextView tvWinner = view.findViewById(R.id.winner);

        Bundle args = getArguments();
        Bundle scoreBoard = args.getBundle(EXTRA_SCOREBOARD);

        Bitmap bitmapNormal = BitmapFactory.decodeResource(res, R.drawable.ant_normal);
        bitmapNormal = Bitmap.createScaledBitmap(bitmapNormal, antWidth, antHeight, false);

        if (scoreBoard != null) {
            Team teamA = scoreBoard.getParcelable(EXTRA_TEAM1);
            if (teamA != null) {
                Log.i(TAG, "buildDialog: teamA score: " + teamA.getScore());
                Bitmap bitmapA = tintImage(bitmapNormal, teamA.getAntSpecies().getTint());
                Drawable drawableA = new BitmapDrawable(res, bitmapA);
                drawableA.setBounds(0, 0, antWidth, antHeight);

                tvTeamA.setText(getString(R.string.team_score_final, teamA.getName(), teamA.getAntSpecies().getName(), teamA.getScore()));
                tvTeamA.setCompoundDrawablesRelative(drawableA, null, null, null);
                tvTeamA.setCompoundDrawablePadding(iconPadding);
            }

            Team teamB = scoreBoard.getParcelable(EXTRA_TEAM2);
            if (teamB != null) {
                Bitmap bitmapB = tintImage(bitmapNormal, teamB.getAntSpecies().getTint());
                Drawable drawableB = new BitmapDrawable(res, bitmapB);
                drawableB.setBounds(0, 0, antWidth, antHeight);

                tvTeamB.setText(getString(R.string.team_score_final, teamB.getName(), teamB.getAntSpecies().getName(), teamB.getScore()));
                tvTeamB.setCompoundDrawablesRelative(drawableB, null, null, null);
                tvTeamB.setCompoundDrawablePadding(iconPadding);
            }

            Team teamC = scoreBoard.getParcelable(EXTRA_TEAM3);
            if (teamC != null) {
                Bitmap bitmapC = tintImage(bitmapNormal, teamC.getAntSpecies().getTint());
                Drawable drawableC = new BitmapDrawable(res, bitmapC);
                drawableC.setBounds(0, 0, antWidth, antHeight);

                tvTeamC.setText(getString(R.string.team_score_final, teamC.getName(), teamC.getAntSpecies().getName(), teamC.getScore()));
                tvTeamC.setCompoundDrawablesRelative(drawableC, null, null, null);
                tvTeamC.setCompoundDrawablePadding(iconPadding);
            }

            Player winner = scoreBoard.getParcelable(EXTRA_WINNER);
            tvWinner.setText(getString(R.string.team_score_final, winner.getName(), winner.getTeamName(), winner.getScore()));

        }else {
            tvWinner.setVisibility(View.GONE);
            tvTeamA.setVisibility(View.GONE);
            tvTeamB.setVisibility(View.GONE);
            tvTeamC.setVisibility(View.GONE);
        }

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

        final AlertDialog dialog = builder.create();

        return dialog;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        gameOverDialogListener.onDialogClosed();
    }

    interface GameOverDialogListener {
        void onDialogClosed();
    }

}
