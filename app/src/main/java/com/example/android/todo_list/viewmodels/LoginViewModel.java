package com.example.android.todo_list.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.view.View;

import com.example.android.todo_list.models.User;

public class LoginViewModel extends ViewModel {

    public MutableLiveData<String> emailAdress = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();

    private MutableLiveData<User> userMutableLiveData;

    public MutableLiveData<User> getUser() {

        if (userMutableLiveData == null) {
            userMutableLiveData = new MutableLiveData<>();
        }
        return userMutableLiveData;

    }

    public void onClickLogin(View view) {

        User loginUser = new User(emailAdress.getValue(), password.getValue());
        userMutableLiveData.setValue(loginUser);

        System.out.println("Clicked on login!");

    }

    public void onClickNewUser(View view) {
        System.out.println("Clicked on new User!");
    }

    public void onClickForgot(View view) {
        System.out.println("Clicked on Forgot!");
    }

}
