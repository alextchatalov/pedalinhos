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
import com.example.pedalinhos.domain.PedalinhoMarcao;
import com.example.pedalinhos.domain.MarcaoUsoPedalinho;
import com.example.pedalinhos.domain.Pedalinho;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView listViewPedalinhosEmUso;
    private ListView listViewPedalinhosDisponiveis;
    private List<PedalinhoMarcao> disponiveis = new ArrayList<>();
    private List<PedalinhoMarcao> usando = new ArrayList<>();
    private ArrayAdapter<PedalinhoMarcao> adapterPedalinhoDisponiveis;
    private ArrayAdapter<PedalinhoMarcao> adapterPedalinhoUsando;
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
                PedalinhoMarcao marcao = (PedalinhoMarcao) listViewPedalinhosEmUso.getItemAtPosition(i);
                Pedalinho pedalinhoEmUso = marcao.pedalinho;
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
                PedalinhoMarcao marcacaoPedalinhoDisponivel = (PedalinhoMarcao) listViewPedalinhosDisponiveis.getItemAtPosition(i);
                Pedalinho marcarPedalinhoComoUsando = marcacaoPedalinhoDisponivel.pedalinho;
                if (!usando.contains(marcarPedalinhoComoUsando)) {
                    marcarPedalinhoComoUsando.setUsando(true);
                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.MINUTE,5);
                    marcarPedalinhoComoUsando.setDataInicioUso(calendar.getTime());
                    MarcaoUsoPedalinho marcacao = new MarcaoUsoPedalinho();
                    marcacao.setPedalinho_id(marcarPedalinhoComoUsando.getId());
                    marcacao.setTempo(calendar.getTime());
                    db.marcacaoPedalinhoDAO().insert(marcacao);
                    db.pedalinhoDAO().update(marcarPedalinhoComoUsando);
                    atualizarListaPedalinhos();
                    Toast.makeText(getApplicationContext(), "Marcação: " + marcacao.getTempo(), Toast.LENGTH_SHORT).show();
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