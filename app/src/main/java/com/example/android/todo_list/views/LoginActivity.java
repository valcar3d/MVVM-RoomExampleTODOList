package com.example.android.todo_list.views;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import com.example.android.todo_list.R;
import com.example.android.todo_list.databinding.ActivityLoginBinding;
import com.example.android.todo_list.databinding.Listener;
import com.example.android.todo_list.entity.UserAccount;
import com.example.android.todo_list.viewmodels.UserViewModel;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LoginActivity extends AppCompatActivity implements Listener {

    private UserViewModel userViewModel;
    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        binding = DataBindingUtil.setContentView(LoginActivity.this, R.layout.activity_login);
        binding.setClickListener(this);
        userViewModel = ViewModelProviders.of(LoginActivity.this).get(UserViewModel.class);


    }

    @Override
    public void onClickSignUp(View view) {

        String strEmail = binding.username.getText().toString().trim();
        String strPassword = binding.password.getText().toString().trim();

        UserAccount data = new UserAccount();

        if (TextUtils.isEmpty(strEmail)) {
            binding.username.setError("Please write a valid E-mail Address");
        } else if (TextUtils.isEmpty(strPassword)) {
            binding.password.setError("Please write a Password");
        } else {

            data.setUserName(strEmail);
            data.setPassword(strPassword);

            UserAccount itExists = userViewModel.getUserId(data);

            if (itExists != null) {
                if (itExists.getUserName().equals(data.getUserName())) {

                    new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Username taken")
                            .setContentText("User already taken, please use another")
                            .setConfirmText("Got it")
                            .show();
                }
            } else {

                userViewModel.insert(data);

                new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("New User added")
                        .setContentText("you can login now with your newly created credentials")
                        .show();

                binding.username.setText("");
                binding.password.setText("");
            }

        }

    }

    @Override
    public void onClickLogin(View view) {

        String strEmail = binding.username.getText().toString().trim();
        String strPassword = binding.password.getText().toString().trim();

        UserAccount data = new UserAccount();

        if (TextUtils.isEmpty(strEmail)) {
            binding.username.setError("Please write a valid E-mail Address");
        } else if (TextUtils.isEmpty(strPassword)) {
            binding.password.setError("Please write a Password");
        } else {

            boolean isValid = userViewModel.checkValidLogin(strEmail, strPassword);
            if (isValid) {
                data.setUserName(strEmail);
                data.setPassword(strPassword);

                binding.username.setText("");
                binding.password.setText("");

                final UserAccount userId = userViewModel.getUserId(data);
                final Intent intent = new Intent(this, NoteActivity.class);

                final SweetAlertDialog alertSuccessLogin = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
                alertSuccessLogin.setTitleText("Welcome " + strEmail);
                alertSuccessLogin.hideConfirmButton();
                alertSuccessLogin.show();


                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {

                        intent.putExtra("userId", userId.getUserId());
                        intent.putExtra("userName", userId.getUserName());
                        alertSuccessLogin.dismiss();
                        startActivity(intent);
                        finish();
                    }
                }, 1000);

            } else {

                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error")
                        .setContentText("Your Email or Password are incorrect, try again")
                        .show();
            }
        }

    }
}