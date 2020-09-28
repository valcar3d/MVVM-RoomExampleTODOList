package com.example.android.todo_list.databases;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.android.todo_list.entity.Note;
import com.example.android.todo_list.databases.dao.NoteDao;


@Database(entities = Note.class, version = 1)
public abstract class NoteDatabase extends RoomDatabase {

    public static final String NOTE_DATABASE = "note_database";

    public abstract NoteDao noteDao();

    private static NoteDatabase instance;

    //region singleton implementation
    //method to synchronize the thread and get THIS instance of
    // the Database in this way we avoid using the same thread at the same time

    public static synchronized NoteDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, NoteDatabase.class, NOTE_DATABASE)
                    .fallbackToDestructiveMigration()
                    //.addCallback(roomCallback)
                    .build();
        }
        return instance;
    }
    //endregion



    //Create default data for users database
 /*   private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new NoteDatabase.PopulateDbAsyncTask(instance).execute();
        }
    };
    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private NoteDatabase noteDao;
        private PopulateDbAsyncTask(NoteDatabase db) {
            noteDao = db.noteDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            userDao.insert(new User("admin@mail.com", "admin123"));
            return null;
        }
    }*/

}
