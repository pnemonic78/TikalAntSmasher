package com.tikalk.antsmasher.teams;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import javax.inject.Inject;


public class ViewModelFactory implements ViewModelProvider.Factory{

    private TeamViewModel mViewModel;

    @Inject
    public ViewModelFactory(TeamViewModel viewModel){
        this.mViewModel = viewModel;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if(modelClass.isAssignableFrom(TeamViewModel.class)){
            return (T) mViewModel;
        }
        throw new IllegalArgumentException("Unknown class name");
    }
}
