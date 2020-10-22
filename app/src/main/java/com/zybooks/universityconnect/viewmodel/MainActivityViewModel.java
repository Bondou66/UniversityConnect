package com.zybooks.universityconnect.viewmodel;


import androidx.lifecycle.ViewModel;

public class MainActivityViewModel extends ViewModel {

    private boolean isSigningIn;

    public MainActivityViewModel() {
        isSigningIn = false;
    }

    public boolean getIsSigningIn() {
        return isSigningIn;
    }

    public void setSigningIn(boolean signingIn) {
        isSigningIn = signingIn;
    }
}
