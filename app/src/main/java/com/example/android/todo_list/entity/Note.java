package com.example.android.todo_list.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;


@Entity(tableName = "note_table")
public class Note {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "noteId")
    int id;
    private String title;
    private String description;
    int priority;
    private int image;

    //constructor to allow new implementations of the notes icons
    //we can fetch them from anywhere not only from the resources
    public Note(String title, String description, int priority, int image) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.image = image;
    }

    //region Getters and Setters

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public int getPriority() {
        return priority;
    }
    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getImage() {
        return image;
    }
    public void setImage(int image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }
    public void setId(@NonNull int id) {
        this.id = id;
    }

    //endregion
}
