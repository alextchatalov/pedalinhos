package com.example.pedalinhos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
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
    private ArrayAdapter<Pedalinho> adapterPedalinhoDisponiveis;
    private ArrayAdapter<Pedalinho> adapterPedalinhoUsando;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = Connection.getConnection(getApplicationContext());
        listViewPedalinhosDisponiveis = findViewById(R.id.pedalinhosDisponiveis);
        listViewPedalinhosEmUso = findViewById(R.id.pedalinhosUsando);
        popularListasPedalinhos();

        onClickDisponiveis();
        onClickUsando();
    }

    private void onClickUsando() {
        listViewPedalinhosEmUso.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Pedalinho pedalinhoEmUso = (Pedalinho) listViewPedalinhosEmUso.getItemAtPosition(i);
                if (pedalinhoEmUso.isNotificado()) {
                    pedalinhoEmUso.setNotificado(false);
                    pedalinhoEmUso.setUsando(false);
                    view.setBackgroundColor(Color.TRANSPARENT);
                } else {
                    pedalinhoEmUso.setNotificado(true);
                    view.setBackgroundColor(Color.YELLOW);
                }
                db.pedalinhoDAO().update(pedalinhoEmUso);
                atualizarListaPedalinhos();
            }
        });
    }

    private void onClickDisponiveis() {
        listViewPedalinhosDisponiveis.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Pedalinho marcarPedalinhoComoUsando = (Pedalinho) listViewPedalinhosDisponiveis.getItemAtPosition(i);
                if (!usando.contains(marcarPedalinhoComoUsando)) {
                    marcarPedalinhoComoUsando.setUsando(true);
                    db.pedalinhoDAO().update(marcarPedalinhoComoUsando);
                    atualizarListaPedalinhos();
                } else {
                    Toast.makeText(getApplicationContext(), "Pedalinho já sendo usando: " + marcarPedalinhoComoUsando.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void popularListasPedalinhos() {
        popularListas();
        associarPedalinhosEmUsoAListView();
        associarPedalinhosDisponiveisAListView();
    }

    private void associarPedalinhosDisponiveisAListView() {

        adapterPedalinhoDisponiveis = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_list_item_1, disponiveis);
        listViewPedalinhosDisponiveis.setAdapter(adapterPedalinhoDisponiveis);
        registerForContextMenu(listViewPedalinhosDisponiveis);
    }

    private void associarPedalinhosEmUsoAListView() {

        adapterPedalinhoUsando = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_list_item_1, usando);
        listViewPedalinhosEmUso.setAdapter(adapterPedalinhoUsando);
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
        disponiveis.clear();
        usando.clear();
        popularListas();
        adapterPedalinhoDisponiveis.notifyDataSetChanged();
        adapterPedalinhoUsando.notifyDataSetChanged();
        listViewPedalinhosEmUso.invalidateViews();
        listViewPedalinhosDisponiveis.invalidateViews();
    }

    private void popularListas() {
        disponiveis.addAll(db.pedalinhoDAO().buscarTodosOsPeladinhos(false));
        usando.addAll(db.pedalinhoDAO().buscarTodosOsPeladinhos(true));
    }

}