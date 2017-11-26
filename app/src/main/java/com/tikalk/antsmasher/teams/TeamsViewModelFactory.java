package com.tikalk.antsmasher.teams;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;

import javax.inject.Inject;


public class TeamsViewModelFactory extends ViewModelProviders.DefaultFactory {

    private final TeamViewModel viewModel;

    @Inject
    public TeamsViewModelFactory(Application application, TeamViewModel viewModel) {
        super(application);
        this.viewModel = viewModel;
    }

    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (TeamViewModel.class.isAssignableFrom(modelClass)) {
            return (T) viewModel;
        }
        return super.create(modelClass);
    }
}
