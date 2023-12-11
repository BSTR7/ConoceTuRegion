package com.example.conoceturegion;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder> {

    private ArrayList<String> usuariosList;
    private OnItemClickListener onItemClickListener;

    public UsuarioAdapter(ArrayList<String> usuariosList, OnItemClickListener onItemClickListener) {
        this.usuariosList = usuariosList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public UsuarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_usuario, parent, false);
        return new UsuarioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuarioViewHolder holder, int position) {
        String usuario = usuariosList.get(position);
        holder.textViewUsuario.setText(usuario);
    }

    @Override
    public int getItemCount() {
        return usuariosList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(String usuario);
    }

    public class UsuarioViewHolder extends RecyclerView.ViewHolder {
        TextView textViewUsuario;

        public UsuarioViewHolder(View itemView) {
            super(itemView);
            textViewUsuario = itemView.findViewById(R.id.textViewUsuario);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    String usuario = usuariosList.get(position);
                    onItemClickListener.onItemClick(usuario);
                }
            });
        }
    }

    public void actualizarLista(ArrayList<String> nuevaLista) {
        this.usuariosList.clear();
        this.usuariosList.addAll(nuevaLista);
        notifyDataSetChanged();
    }

}
