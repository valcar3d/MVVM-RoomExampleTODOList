package com.example.android.todo_list.views;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.android.todo_list.R;
import com.example.android.todo_list.adapters.NoteAdapter;
import com.example.android.todo_list.databinding.ActivityNoteBinding;
import com.example.android.todo_list.entity.Note;
import com.example.android.todo_list.viewmodels.NoteViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class NoteActivity extends AppCompatActivity implements View.OnClickListener, NoteAdapter.onItemClickListener {

    NoteViewModel noteViewModel;
    NoteAdapter noteAdapter;
    FloatingActionButton floatingActionButton;
    private ActivityNoteBinding binding;

    public static final int ADD_REQUEST = 1;
    public static final int EDIT_REQUEST = 2;

    Handler handler = new Handler();
    long timeToHideDialogs = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Getting users ID and name to further usages
        Intent extras = getIntent();
        int userId = extras.getIntExtra("userId", 0);
        String userName = extras.getStringExtra("userName");

        //Toast.makeText(getApplicationContext(), "Used ID = " + userId + " Name = " + userName, Toast.LENGTH_SHORT).show();


        binding = DataBindingUtil.setContentView(this, R.layout.activity_note);
        floatingActionButton = findViewById(R.id.noteActivity_floatingButton);


        binding.noteActivityRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.noteActivityRecyclerView.setHasFixedSize(true);

        noteAdapter = new NoteAdapter();
        binding.noteActivityRecyclerView.setAdapter(noteAdapter);

        //region callback for swipes
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                //get current swipped note and use room delete interface
                noteViewModel.delete(noteAdapter.getNoteAt(viewHolder.getAdapterPosition()));

                final SweetAlertDialog singleNoteDeleted = new SweetAlertDialog(NoteActivity.this, SweetAlertDialog.WARNING_TYPE);
                singleNoteDeleted.setTitleText("Note deleted");
                singleNoteDeleted.setContentText("The note was completely deleted");
                singleNoteDeleted.hideConfirmButton();
                singleNoteDeleted.show();

                handler.postDelayed(new Runnable() {
                    public void run() {
                        singleNoteDeleted.dismiss();
                    }
                }, timeToHideDialogs);


            }
        }).attachToRecyclerView(binding.noteActivityRecyclerView);
        //endregion


        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                // update Recycler View
                noteAdapter.submitList(notes);
            }
        });


        floatingActionButton.setOnClickListener(this);
        noteAdapter.setOnClickRecyclerview(this);
    }

    //region menu configurations
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_delete_all, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.notes_delete) {
            noteViewModel.deleteAll();

            //SweetAlert New Note Added
            final SweetAlertDialog allNotesDeleted = new SweetAlertDialog(NoteActivity.this, SweetAlertDialog.WARNING_TYPE);
            allNotesDeleted.setTitleText("Notes deleted");
            allNotesDeleted.setContentText("All notes were deleted");
            allNotesDeleted.hideConfirmButton();
            allNotesDeleted.show();

            handler.postDelayed(new Runnable() {
                public void run() {
                    allNotesDeleted.dismiss();
                }
            }, timeToHideDialogs);
            //End SweetAlert New Note Added

        }

        if (id == R.id.logout) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        return true;
    }
    //endregion

    //Add note activity with an activity result of ADD_REQUEST
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.noteActivity_floatingButton:
                Intent intent = new Intent(NoteActivity.this, AddEditNoteActivity.class);
                startActivityForResult(intent, ADD_REQUEST);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //icon can be fetched from anyplace, internet, locally.
        //this is here just for demonstrations purposes
        int note_icon = R.drawable.notepad;

        if (requestCode == ADD_REQUEST && resultCode == RESULT_OK) {
            String note_title = data.getStringExtra("note_title");
            String note_description = data.getStringExtra("note_description");
            int note_priority = data.getIntExtra("note_priority", 0);
            note_priority = data.getIntExtra("note_priority", 0);

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            String currentDateandTime = sdf.format(new Date());

            String currentDate = currentDateandTime;

            boolean note_done = data.getBooleanExtra("note_done", false);


            noteViewModel.insert(new Note(note_title, note_description, note_priority, note_icon, note_done, currentDate));

            //SweetAlert New Note Added
            final SweetAlertDialog noteAddedDialog = new SweetAlertDialog(NoteActivity.this, SweetAlertDialog.SUCCESS_TYPE);
            noteAddedDialog.setTitleText("New Note added");
            noteAddedDialog.setContentText("The note was stored");
            noteAddedDialog.hideConfirmButton();
            noteAddedDialog.show();

            handler.postDelayed(new Runnable() {
                public void run() {
                    noteAddedDialog.dismiss();
                }
            }, timeToHideDialogs);
            //End SweetAlert New Note Added

        } else if (requestCode == EDIT_REQUEST && resultCode == RESULT_OK) {
            String note_title = data.getStringExtra("note_title");
            String note_description = data.getStringExtra("note_description");
            int note_priority = data.getIntExtra("note_priority", 0);
            int note_id = data.getIntExtra("id", 0);

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            String currentDateandTime = sdf.format(new Date());

            String currentDate = currentDateandTime;

            Note note = new Note(note_title, note_description, note_priority, note_icon, false, currentDate);
            note.setId(note_id);
            noteViewModel.update(note);

            //SweetAlert Note Updated
            final SweetAlertDialog noteUpdatedDialog = new SweetAlertDialog(NoteActivity.this, SweetAlertDialog.SUCCESS_TYPE);
            noteUpdatedDialog.setTitleText("Note updated");
            noteUpdatedDialog.setContentText("The note was updated");
            noteUpdatedDialog.hideConfirmButton();
            noteUpdatedDialog.show();

            handler.postDelayed(new Runnable() {
                public void run() {
                    noteUpdatedDialog.dismiss();
                }
            }, timeToHideDialogs);

        } else {
            new SweetAlertDialog(NoteActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Note not saved")
                    .setContentText("The note was NOT saved")
                    .show();
        }
    }

    //Listener from the Adapter to check which item was selected and send to
    //Edit note activity with an activity result of EDIT_REQUEST
    @Override
    public void onRecyclerViewItemSelected(Note note) {
        Intent intent = new Intent(NoteActivity.this, AddEditNoteActivity.class);
        intent.putExtra("id", note.getId());
        intent.putExtra("title", note.getTitle());
        intent.putExtra("description", note.getDescription());
        intent.putExtra("priority", note.getPriority());
        intent.putExtra("checked", note.isCheckedTodo());
        startActivityForResult(intent, EDIT_REQUEST);
    }

    //Binding for ImageView src from the note_item.xml
    @BindingAdapter({"android:src"})
    public static void setImageViewResource(ImageView imageView, int resource) {
        imageView.setImageResource(resource);
    }


}


