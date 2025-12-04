package com.haoht.ex4.adapters;

import android.content.Context;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.haoht.ex4.model.NotesModel;

import java.util.List;

public class NotesAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<NotesModel> notesList;

    public NotesAdapter(Context context, int layout, List<NotesModel> notesList) {
        this.context = context;
        this.layout = layout;
        this.notesList = notesList;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public int getLayout() {
        return layout;
    }

    public void setLayout(int layout) {
        this.layout = layout;
    }

    public List<NotesModel> getNotesList() {
        return notesList;
    }

    public void setNotesList(List<NotesModel> notesList) {
        this.notesList = notesList;
    }

    @Override
    public int getCount() {
        return notesList.size();
    }

    @Override
    public Object getItem(int position) {
        return notesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView textViewNotes;
        ImageView imageViewEdit, imageViewDelete;
    }

    @Override
    public View getView(int position, View convertView, android.view.ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, layout, null);
            holder.textViewNotes = convertView.findViewById(com.haoht.ex4.R.id.textViewNotes);
            holder.imageViewEdit = convertView.findViewById(com.haoht.ex4.R.id.imageViewEdit);
            holder.imageViewDelete = convertView.findViewById(com.haoht.ex4.R.id.imageViewDelete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        NotesModel notes = notesList.get(position);
        holder.textViewNotes.setText(notes.getNameNotes());

        return convertView;
    }



}
