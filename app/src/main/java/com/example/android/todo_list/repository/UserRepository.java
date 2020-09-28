package com.example.android.todo_list.repository;


import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.android.todo_list.databases.AppDatabase;
import com.example.android.todo_list.databases.dao.UserAccountDao;
import com.example.android.todo_list.entity.UserAccount;

public class UserRepository {


    private UserAccountDao userAccountDao;
    private LiveData<UserAccount> allData;

    public UserRepository(Application application) {

        AppDatabase db = AppDatabase.getInstance(application);
        userAccountDao = db.userAccountDao();
        allData = userAccountDao.getDetails();

    }

    public LiveData<UserAccount> getAllData() {
        return allData;
    }

    public void insertData(UserAccount data) {
        new LoginInsertion(userAccountDao).execute(data);
    }
    private static class LoginInsertion extends AsyncTask<UserAccount, Void, Void> {

        private UserAccountDao userDao;

        private LoginInsertion(UserAccountDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(UserAccount... userAccounts) {
            userDao.insert(userAccounts[0]);
            return null;
        }
    }

    public boolean isValidAccount(String username, String password) {

        UserAccount userAccount = userAccountDao.getAccount(username);

        if (userAccount == null) {
            return false;
        } else {
            System.out.println("User Loged in = " + userAccount.toString());
            return userAccount.getPassword().equals(password);
        }

    }

    public UserAccount getUserId(UserAccount userAccount) {
        UserAccount userAccountId = userAccountDao.getAccount(userAccount.getUserName());
        if (userAccountId != null) {
            return userAccountId;
        } else {
            return null;
        }
    }




}

