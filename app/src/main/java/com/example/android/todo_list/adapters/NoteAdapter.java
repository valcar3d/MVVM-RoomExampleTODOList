package com.example.android.todo_list.adapters;

import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.todo_list.R;
import com.example.android.todo_list.entity.Note;


public class NoteAdapter extends ListAdapter<Note, NoteAdapter.ViewHolder> {

    //DiffUtil algoritm implementation
    //region diff util
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
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Note currentNote = getItem(i);
        viewHolder.title.setText(currentNote.getTitle());
        viewHolder.description.setText(currentNote.getDescription());
        viewHolder.priority.setText(String.valueOf(currentNote.getPriority()));
    }

    public Note getNoteAt(int position) {
        return getItem(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView description;
        TextView priority;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.note_item_title);
            description = itemView.findViewById(R.id.note_item_description);
            priority = itemView.findViewById(R.id.note_item_priority);


            itemView.setOnClickListener(new View.OnClickListener() {
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
