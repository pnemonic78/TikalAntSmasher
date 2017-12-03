package com.tikalk.antsmasher.board;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tikalk.antsmasher.R;
import com.tikalk.antsmasher.model.Team;

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
    public static final String EXTRA_SCORE = "score";
    public static final String EXTRA_NAME = "teamName";


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return buildDialog(getActivity());
    }

    private Dialog buildDialog(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.game_over_dialog, null);

        Bundle b = getArguments();
        Bundle teamsBundle = b.getBundle(EXTRA_TEAMS);

        Team teamA = new Team(0, teamsBundle.getBundle(EXTRA_TEAM1).getString(EXTRA_NAME));
        teamA.setScore(teamsBundle.getBundle(EXTRA_TEAM1).getInt(EXTRA_SCORE));

        Team teamB = new Team(0, teamsBundle.getBundle(EXTRA_TEAM2).getString(EXTRA_NAME));
        teamB.setScore(teamsBundle.getBundle(EXTRA_TEAM2).getInt(EXTRA_SCORE));

        Team teamC = new Team(0, teamsBundle.getBundle(EXTRA_TEAM3).getString(EXTRA_NAME));
        teamB.setScore(teamsBundle.getBundle(EXTRA_TEAM3).getInt(EXTRA_SCORE));

        final TextView tvTeamA = view.findViewById(R.id.teamA);
        final TextView tvTeamB = view.findViewById(R.id.teamB);
        final TextView tvTeamC = view.findViewById(R.id.teamC);

        tvTeamA.setText(teamA.getName() + ": " + teamA.getScore());
        tvTeamB.setText(teamB.getName() + ": " + teamB.getScore());
        tvTeamC.setText(teamC.getName() + ": " + teamC.getScore());

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setCancelable(false)
                .setIcon(ActivityCompat.getDrawable(context, R.mipmap.ic_launcher))
                .setTitle(getArguments().getString(EXTRA_TITLE, getString(R.string.app_name)))
                .setMessage(getArguments().getString(EXTRA_LABEL))
                .setView(view)
                .setPositiveButton(R.string.ok_button,
                        (dialog, whichButton) -> {
                        });

        AlertDialog dialog = builder.create();

        return dialog;
    }

}
