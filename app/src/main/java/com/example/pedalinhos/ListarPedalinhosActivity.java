package com.example.pedalinhos;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.example.pedalinhos.database.AppDatabase;
import com.example.pedalinhos.database.Connection;
import com.example.pedalinhos.domain.Pedalinho;
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
        registerForContextMenu(listView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        pedalinhos = db.pedalinhoDAO().getAll();
        pedalinhosFiltrados.clear();
        pedalinhosFiltrados.addAll(pedalinhos);
        listView.invalidateViews();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_contexto, menu);
    }

    public void excluir(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final Pedalinho selecionado = pedalinhosFiltrados.get(menuInfo.position);
        AlertDialog confirmar = new AlertDialog.Builder(this)
                .setTitle("Atenção!")
                .setMessage("Deseja Realmente Excluir o Pedalinho: " + selecionado)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        pedalinhosFiltrados.remove(selecionado);
                        pedalinhos.remove(selecionado);
                        db.pedalinhoDAO().delete(selecionado);
                        listView.invalidateViews();
                    }
                })
                .setNegativeButton("Não", null)
                .create();
        confirmar.show();
    }

    public void atualizar(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final Pedalinho selecionado = pedalinhosFiltrados.get(menuInfo.position);
        Intent intent = new Intent(this, CadastroPedalinhoActivity.class);
        intent.putExtra(CadastroPedalinhoActivity.PEDALINHO_PARAM, selecionado);
        startActivity(intent);
    }
}