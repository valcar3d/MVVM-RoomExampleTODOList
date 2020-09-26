package com.example.android.todo_list.adapters;

import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.todo_list.databinding.NoteItemBinding;
import com.example.android.todo_list.entity.Note;


public class NoteAdapter extends ListAdapter<Note, NoteAdapter.ViewHolder> {

    //DiffUtil algoritm implementation
    //region diffUtil
    public NoteAdapter() {
        super(DIFF_CALLBACK);
    }

    public static final DiffUtil.ItemCallback<Note> DIFF_CALLBACK = new DiffUtil.ItemCallback<Note>() {
        @Override
        public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {

            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getDescription().equals(newItem.getDescription()) &&
                    oldItem.getPriority() == newItem.getPriority();
        }
    };
    //endregion

    //region Callbacks
    //setup callback for itemClick
    public interface onItemClickListener {
        void onRecyclerViewItemSelected(Note note);
    }

    private onItemClickListener listener;

    //setter for listener
    public void setOnClickRecyclerview(onItemClickListener listener) {
        this.listener = listener;
    }
    //endregion

    //region recyclerview configurations
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        NoteItemBinding noteItemBinding = NoteItemBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(noteItemBinding);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Note currentNote = getItem(i);
        viewHolder.noteItemBinding.setNoteModel(currentNote);
        viewHolder.noteItemBinding.executePendingBindings();//instantaneous binding to current user
    }

    public Note getNoteAt(int position) {
        return getItem(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        NoteItemBinding noteItemBinding;

        public ViewHolder(@NonNull NoteItemBinding noteItemBinding) {
            super(noteItemBinding.getRoot());
            this.noteItemBinding = noteItemBinding;

            noteItemBinding.rvContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onRecyclerViewItemSelected(getItem(position));//trigger the callback
                    }
                }
            });
        }

    }
    //endregion

}
