package com.tikalk.antsmasher.board;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.tikalk.antsmasher.model.Game;

/**
 * Board presenter.
 */

public class BoardViewModel extends ViewModel {

    public interface View {
    }

    private View view;
    private MutableLiveData<Game> game;

    public void setView(View view) {
        this.view = view;
    }

    public LiveData<Game> getGame() {
        if (game == null) {
            game = new MutableLiveData<>();
            loadGame();
        }
        return game;
    }

    private void loadGame() {
        // TODO Do an asynchronous operation to fetch teams.
        game.setValue(new Game());
    }

}
