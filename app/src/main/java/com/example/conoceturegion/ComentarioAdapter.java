package com.example.conoceturegion;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ComentarioAdapter extends RecyclerView.Adapter<ComentarioAdapter.ComentarioViewHolder> {
    private ArrayList<String> comentariosList;
    public ComentarioAdapter(ArrayList<String> comentariosList) {
        this.comentariosList = comentariosList;
    }
    @NonNull
    @Override
    public ComentarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comentario, parent, false);
        return new ComentarioViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ComentarioViewHolder holder, int position) {
        String comentario = comentariosList.get(position);
        holder.bind(comentario);
    }
    @Override
    public int getItemCount() {
        return comentariosList.size();
    }
    static class ComentarioViewHolder extends RecyclerView.ViewHolder {
        private TextView comentarioTextView;
        public ComentarioViewHolder(@NonNull View itemView) {
            super(itemView);
            comentarioTextView = itemView.findViewById(R.id.textComentario);
        }
        public void bind(String comentario) {
            comentarioTextView.setText(comentario);
        }
    }
}

