package com.example.conoceturegion;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class Vercomentarios extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ComentarioAdapter comentarioAdapter;
    private DatabaseHelperUsuarios dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vercomentarios);

        dbHelper = new DatabaseHelperUsuarios(this);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<String> comentariosList = dbHelper.verComentarios();

        comentarioAdapter = new ComentarioAdapter(comentariosList);
        recyclerView.setAdapter(comentarioAdapter);
    }
}
