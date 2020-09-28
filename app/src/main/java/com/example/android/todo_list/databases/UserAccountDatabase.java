package com.example.android.todo_list.databases;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.android.todo_list.databases.dao.UserAccountDao;
import com.example.android.todo_list.entity.UserAccount;

@Database(entities = {UserAccount.class}, version = 1)
public abstract class UserAccountDatabase extends RoomDatabase {

    public abstract UserAccountDao userAccountDao();

    public static UserAccountDatabase INSTANCE;

    public static UserAccountDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), UserAccountDatabase.class, "user-database").allowMainThreadQueries().build();

        }

        return INSTANCE;

    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

}

