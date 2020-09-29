package com.example.android.todo_list.databases;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.android.todo_list.databases.dao.NoteDao;
import com.example.android.todo_list.databases.dao.UserAccountDao;
import com.example.android.todo_list.entity.Note;
import com.example.android.todo_list.entity.UserAccount;


@Database(entities = {Note.class, UserAccount.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public static final String NOTE_DATABASE = "todo_database";

    public abstract NoteDao noteDao();
    public abstract UserAccountDao userAccountDao();

    private static AppDatabase instance;

    //region singleton implementation
    //method to synchronize the thread and get THIS instance of
    // the Database in this way we avoid using the same thread at the same time

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, AppDatabase.class, NOTE_DATABASE).allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
    //endregion


}
