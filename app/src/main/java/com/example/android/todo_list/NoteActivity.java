package com.example.android.todo_list;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.style.BackgroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.todo_list.adapters.NoteAdapter;
import com.example.android.todo_list.databinding.ActivityNoteBinding;
import com.example.android.todo_list.entity.Note;
import com.example.android.todo_list.viewmodel.NoteViewModel;

import java.util.List;

public class NoteActivity extends AppCompatActivity implements View.OnClickListener, NoteAdapter.onItemClickListener {

    NoteViewModel noteViewModel;
    NoteAdapter noteAdapter;
    FloatingActionButton floatingActionButton;
    private ActivityNoteBinding binding;

    public static final int ADD_REQUEST = 1;
    public static final int EDIT_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_note);
        floatingActionButton = findViewById(R.id.noteActivity_floatingButton);

        binding.noteActivityRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.noteActivityRecyclerView.setHasFixedSize(true);

        noteAdapter = new NoteAdapter();
        binding.noteActivityRecyclerView.setAdapter(noteAdapter);

        //TODO check swipe functionality
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
                Toast.makeText(NoteActivity.this, R.string.txtNoteDeleted, Toast.LENGTH_SHORT).show();
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
            Toast.makeText(NoteActivity.this, R.string.txtAllNotesDeleted, Toast.LENGTH_SHORT).show();
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

            noteViewModel.insert(new Note(note_title, note_description, note_priority, note_icon));

            Toast.makeText(this, R.string.txtNoteSaved, Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_REQUEST && resultCode == RESULT_OK) {
            String note_title = data.getStringExtra("note_title");
            String note_description = data.getStringExtra("note_description");
            int note_priority = data.getIntExtra("note_priority", 0);
            int note_id = data.getIntExtra("id", 0);


            Note note = new Note(note_title, note_description, note_priority, note_icon);
            note.setId(note_id);
            noteViewModel.update(note);

            Toast.makeText(this, R.string.txtNoteUpdated, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.txtNoteNotSaved, Toast.LENGTH_SHORT).show();
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
        startActivityForResult(intent, EDIT_REQUEST);
    }

    //Binding for ImageView src from the note_item.xml
    @BindingAdapter({"android:src"})
    public static void setImageViewResource(ImageView imageView, int resource) {
        imageView.setImageResource(resource);
    }


}


