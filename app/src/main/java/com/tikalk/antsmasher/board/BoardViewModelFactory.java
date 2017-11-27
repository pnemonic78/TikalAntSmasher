package com.tikalk.antsmasher.board;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import javax.inject.Inject;

/**
 * Created by motibartov on 25/11/2017.
 */

public class BoardViewModelFactory implements ViewModelProvider.Factory {

    private BoardViewModel mViewModel;

    @Inject
    public BoardViewModelFactory(BoardViewModel viewModel) {
        this.mViewModel = viewModel;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(BoardViewModel.class)) {
            return (T) mViewModel;
        }
        throw new IllegalArgumentException("Unknown class name");
    }
}
