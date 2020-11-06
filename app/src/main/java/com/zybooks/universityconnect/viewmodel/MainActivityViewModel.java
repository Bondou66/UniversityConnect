package com.zybooks.universityconnect.viewmodel;


import androidx.lifecycle.ViewModel;

public class MainActivityViewModel extends ViewModel {

    private boolean isSigningIn;
    private boolean isSigningOut;
    private static MainActivityViewModel instance;

    private MainActivityViewModel() {
        isSigningIn = false;
        isSigningOut = false;
    }

    public static MainActivityViewModel getInstance() {
        if (instance == null) {
            instance = new MainActivityViewModel();
        }
        return instance;
    }

    public boolean isSigningIn() {
        return isSigningIn;
    }

    public void setSigningIn(boolean signingIn) {
        isSigningIn = signingIn;
    }

    public boolean isSigningOut() {
        return isSigningOut;
    }

    public void setSigningOut(boolean signingOut) {
        isSigningOut = signingOut;
    }
}
