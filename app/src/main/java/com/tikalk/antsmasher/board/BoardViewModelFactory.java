package com.tikalk.antsmasher.board;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;

import javax.inject.Inject;

/**
 * Created by motibartov on 25/11/2017.
 */

public class BoardViewModelFactory extends ViewModelProviders.DefaultFactory {

    private BoardViewModel viewModel;

    @Inject
    public BoardViewModelFactory(@NonNull Application application, BoardViewModel viewModel) {
        super(application);
        this.viewModel = viewModel;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (BoardViewModel.class.isAssignableFrom(modelClass)) {
            return (T) viewModel;
        }
        return super.create(modelClass);
    }
}
