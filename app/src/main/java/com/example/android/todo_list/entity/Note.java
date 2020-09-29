package com.example.android.todo_list.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
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
    private boolean checkedTodo;
    private String createdDate;

    public Note(String title, String description, int priority, int image, boolean checkedTodo, String createdDate) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.image = image;
        this.checkedTodo = checkedTodo;
        this.createdDate = createdDate;
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

    public boolean isCheckedTodo() {
        return checkedTodo;
    }
    public void setCheckedTodo(boolean checkedTodo) {
        this.checkedTodo = checkedTodo;
    }

    public String getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }


    //endregion
}
