package com.example.android.todo_list.utils;

public class CheckInputLogin {
    public static Boolean validateRegistratonInput(String userName, String password) {
        if (userName.isEmpty() || password.isEmpty()) {
            return false;
        }
        return true;
    }
}
