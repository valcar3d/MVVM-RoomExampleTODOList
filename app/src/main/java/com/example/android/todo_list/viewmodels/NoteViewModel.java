package com.example.android.todo_list.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.android.todo_list.entity.Note;
import com.example.android.todo_list.repository.NoteRepository;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {

    private NoteRepository noteRepository;
    private LiveData<List<Note>> allNotes;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        noteRepository = new NoteRepository(application);
        allNotes = noteRepository.getAllNotes();
    }

    //region CRUD calls to repository
    public void insert(Note note) {
        noteRepository.insert(note);
    }
    public void update(Note note) {
        noteRepository.update(note);
    }
    public void delete(Note note) {
        noteRepository.delete(note);
    }
    public void deleteAll() {
        noteRepository.deleteAll();
    }
    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }
    //endregion
}
