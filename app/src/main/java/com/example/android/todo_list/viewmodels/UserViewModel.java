package com.example.android.todo_list.viewmodels;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.android.todo_list.entity.UserAccount;
import com.example.android.todo_list.repository.UserRepository;

public class UserViewModel extends AndroidViewModel {

    private UserRepository repository;
    private LiveData<UserAccount> getAllData;

    public UserViewModel(@NonNull Application application) {
        super(application);
        repository = new UserRepository(application);
        getAllData = repository.getAllData();
    }

    public void insert(UserAccount data) {
        repository.insertData(data);
    }

    public LiveData<UserAccount> getGetAllData() {
        return getAllData;
    }

    public boolean checkValidLogin(String username, String password) {

        return repository.isValidAccount(username, password);
    }

    public UserAccount getUserId(UserAccount userAccount) {
        return repository.getUserId(userAccount);
    }

}


