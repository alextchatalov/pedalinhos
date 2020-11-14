package com.example.pedalinhos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.pedalinhos.database.AppDatabase;
import com.example.pedalinhos.database.Connection;
import com.example.pedalinhos.domain.Pedalinho;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ListarPedalinhosActivity extends AppCompatActivity {

    private ListView listView;
    private AppDatabase db;
    private List<Pedalinho> pedalinhos;
    private List<Pedalinho> pedalinhosFiltrados = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_pedalinhos);
        db = Connection.getConnection(getApplicationContext());
        listView = findViewById(R.id.lista_pedalinhos);
        pedalinhos = db.pedalinhoDAO().getAll();
        pedalinhosFiltrados.addAll(pedalinhos);
        ArrayAdapter<Pedalinho> arrayAdapter = new ArrayAdapter<Pedalinho>(getApplicationContext(),
                android.R.layout.simple_list_item_1, pedalinhosFiltrados);
        listView.setAdapter(arrayAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        pedalinhos = db.pedalinhoDAO().getAll();
        pedalinhosFiltrados.clear();
        pedalinhosFiltrados.addAll(pedalinhos);
        listView.invalidateViews();
    }
}