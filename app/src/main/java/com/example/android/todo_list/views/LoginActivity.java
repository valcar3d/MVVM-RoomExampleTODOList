package com.example.android.todo_list.views;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.android.todo_list.R;
import com.example.android.todo_list.databinding.ActivityLoginBinding;
import com.example.android.todo_list.databinding.Listener;
import com.example.android.todo_list.entity.UserAccount;
import com.example.android.todo_list.viewmodels.UserViewModel;

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
            binding.username.setError("Please a valid E-mail Address");
        } else if (TextUtils.isEmpty(strPassword)) {
            binding.password.setError("Please Enter a Password");
        } else {

            data.setUserName(strEmail);
            data.setPassword(strPassword);

            UserAccount itExists = userViewModel.getUserId(data);

            if (itExists != null) {
                if (itExists.getUserName().equals(data.getUserName())) {
                    Toast.makeText(getApplicationContext(), "User already taken, please use another", Toast.LENGTH_SHORT).show();
                }
            } else {

                userViewModel.insert(data);
                Toast.makeText(getApplicationContext(), "User added, you can login", Toast.LENGTH_SHORT).show();

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
            binding.username.setError("Please Enter Your E-mail Address");
        } else if (TextUtils.isEmpty(strPassword)) {
            binding.password.setError("Please Enter Your Password");
        } else {

            boolean isValid = userViewModel.checkValidLogin(strEmail, strPassword);


            if (isValid) {
                data.setUserName(strEmail);
                data.setPassword(strPassword);

                Toast.makeText(getBaseContext(), "Successfully Logged In!", Toast.LENGTH_LONG).show();
                binding.username.setText("");
                binding.password.setText("");

                UserAccount userId = userViewModel.getUserId(data);

                Intent intent = new Intent(this, NoteActivity.class);

                startActivity(intent);
                finish();

            } else {
                Toast.makeText(getBaseContext(), "Invalid Login!", Toast.LENGTH_SHORT).show();
            }
        }

    }
}