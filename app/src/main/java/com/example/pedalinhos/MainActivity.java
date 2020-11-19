package com.example.pedalinhos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.pedalinhos.database.AppDatabase;
import com.example.pedalinhos.database.Connection;
import com.example.pedalinhos.domain.Pedalinho;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView listViewPedalinhosEmUso;
    private ListView listViewPedalinhosDisponiveis;
    private List<Pedalinho> disponiveis = new ArrayList<>();
    private List<Pedalinho> usando = new ArrayList<>();
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = Connection.getConnection(getApplicationContext());
        popularListasPedalinhos();

        listViewPedalinhosDisponiveis.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Pedalinho marcarPedalinhoComoUsando = (Pedalinho) listViewPedalinhosDisponiveis.getItemAtPosition(i);
                if (!usando.contains(marcarPedalinhoComoUsando)) {
                    usando.add(marcarPedalinhoComoUsando);
                    disponiveis.remove(marcarPedalinhoComoUsando);
                    atualizarListaPedalinhos();
                } else {
                    Toast.makeText(getApplicationContext(), "Pedalinho já sendo usando: " + marcarPedalinhoComoUsando.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void popularListasPedalinhos() {
        populaPedalinhosEmUso();
        popularListaPedalinhosDisponiveis();
    }

    private void popularListaPedalinhosDisponiveis() {
        listViewPedalinhosDisponiveis = findViewById(R.id.pedalinhosDisponiveis);
        atualizarListaPedalinhos();

        ArrayAdapter<Pedalinho> arrayAdapter = new ArrayAdapter<Pedalinho>(getApplicationContext(),
                android.R.layout.simple_list_item_1, disponiveis);
        listViewPedalinhosDisponiveis.setAdapter(arrayAdapter);
        registerForContextMenu(listViewPedalinhosDisponiveis);
    }

    private void populaPedalinhosEmUso() {
        listViewPedalinhosEmUso = findViewById(R.id.pedalinhosUsando);
        ArrayAdapter<Pedalinho> arrayAdapter = new ArrayAdapter<Pedalinho>(getApplicationContext(),
                android.R.layout.simple_list_item_1, usando);
        listViewPedalinhosEmUso.setAdapter(arrayAdapter);
        registerForContextMenu(listViewPedalinhosEmUso);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_principal, menu);
        return true;
    }



    public void lista(MenuItem menuItem) {
        Intent intent = new Intent(getApplicationContext(), ListarPedalinhosActivity.class);
        startActivity(intent);
    }

    public void cadastrar(MenuItem menuItem) {
        Intent intent = new Intent(getApplicationContext(), CadastroPedalinhoActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        atualizarListaPedalinhos();

    }
    
    private void atualizarListaPedalinhos() {
        disponiveis = db.pedalinhoDAO().getAll();
        listViewPedalinhosEmUso.invalidateViews();
        listViewPedalinhosDisponiveis.invalidateViews();
    }

}