package com.example.android.todo_list.databases.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.android.todo_list.entity.UserAccount;

@Dao
public interface UserAccountDao {

    @Insert
    void insert(UserAccount account);

    @Query("SELECT * FROM useraccounts WHERE userName =:username")
    UserAccount getAccount(String username);

    @Query("SELECT * FROM useraccounts")
    LiveData<UserAccount> getDetails();

}
