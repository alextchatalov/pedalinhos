package com.example.pedalinhos;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
import com.example.pedalinhos.utils.Toaster;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ListView listViewPedalinhosEmUso;
    private ListView listViewPedalinhosDisponiveis;
    private List<PedalinhoMarcao> disponiveis = new ArrayList<>();
    private List<PedalinhoMarcao> usando = new ArrayList<>();
    private ArrayAdapter<PedalinhoMarcao> adapterPedalinhoDisponiveis;
    private ArrayAdapter<PedalinhoMarcao> adapterPedalinhoUsando;
    private AppDatabase db;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Date hoje = new Date();
        fecharAppNaDataLimite(hoje);
        db = Connection.getConnection(getApplicationContext());
        listViewPedalinhosDisponiveis = findViewById(R.id.pedalinhosDisponiveis);
        listViewPedalinhosEmUso = findViewById(R.id.pedalinhosUsando);
        popularListasPedalinhos();

        onClickDisponiveis();
        onClickUsando();
        monitorarTempoPedalinhosThread().start();
    }

    private void fecharAppNaDataLimite(Date hoje) {
        Date dataLimite;
        try {
            dataLimite = new SimpleDateFormat("dd/MM/yyyy").parse("04/03/2021");
            if (hoje.after(dataLimite)) {
                Toaster.makeLongToast(getApplicationContext(),  "O tempo limite de utilizar o aplicativo se expirou em: " + simpleDateFormat.format(dataLimite), 8000);
                this.finishAffinity();
            } else {
                Toaster.makeLongToast(getApplicationContext(),"Você poderá utilizar o aplicativo até: " + simpleDateFormat.format(dataLimite), 8000);
            }

        } catch (ParseException e) {
            Toast.makeText(MainActivity.this, "Erro ao abrir o Aplicativo: " + e.getMessage(), Integer.valueOf(6)).show();
        }
    }

    private Thread monitorarTempoPedalinhosThread() {
       return  new Thread( new Runnable() {
            @Override
            public void run() {
                while (true) {

                    for (PedalinhoMarcao pedalinhoEmUso : usando) {

                        if (pedalinhoEmUso.isPedalinhoEncerrado() || pedalinhoEmUso.isPedalinhoNaoNotificado()) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    listViewPedalinhosEmUso.invalidateViews();
                                    listViewPedalinhosDisponiveis.invalidateViews();
                                }
                            });
                        }
                    }
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        Log.v(TAG, "Ocorreu um erro inesperado: " + e.getMessage());
                        Thread.currentThread().interrupt();
                    }
                }
            }
        });
    }

    private void onClickUsando() {
        listViewPedalinhosEmUso.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, final View view, int i, long l) {
                PedalinhoMarcao marcao = (PedalinhoMarcao) listViewPedalinhosEmUso.getItemAtPosition(i);
                Pedalinho pedalinhoEmUso = marcao.pedalinho;

                if (marcao.isPedalinhoNaoNotificado()){
                    Log.v(TAG,"NOTIFICADO pedalinho: " + pedalinhoEmUso);
                    pedalinhoEmUso.setNotificado(true);
                    view.setBackgroundColor(Color.YELLOW);
                } else if (marcao.isPedalinhoEncerrado()) {
                    Log.v(TAG,"ENCERRANDO pedalinho: " + pedalinhoEmUso);
                    pedalinhoEmUso.setNotificado(false);
                    pedalinhoEmUso.setUsando(false);
                    view.setBackgroundColor(Color.TRANSPARENT);
                }else {
                    Log.v(TAG,"CANCELANDO pedalinho: " + pedalinhoEmUso);
                    pedalinhoEmUso.setNotificado(false);
                    pedalinhoEmUso.setUsando(false);
                    view.setBackgroundColor(Color.TRANSPARENT);
                }
                db.pedalinhoDAO().update(pedalinhoEmUso);
                atualizarListaPedalinhos();
                atualizarThread();
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
                    calendar.add(Calendar.MINUTE,2);
                    MarcaoUsoPedalinho marcacao = new MarcaoUsoPedalinho();
                    marcacao.setPedalinho_id(marcarPedalinhoComoUsando.getId());
                    marcacao.setTempo(calendar.getTime());
                    db.marcacaoPedalinhoDAO().insert(marcacao);
                    db.pedalinhoDAO().update(marcarPedalinhoComoUsando);
                    atualizarListaPedalinhos();
                    Toast.makeText(getApplicationContext(), "Marcação: " + marcacao.getTempo(), Toast.LENGTH_SHORT).show();
                    atualizarThread();
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

        adapterPedalinhoDisponiveis = new PedalinhoArrayAdapter(getApplicationContext(), disponiveis);
        listViewPedalinhosDisponiveis.setAdapter(adapterPedalinhoDisponiveis);
        registerForContextMenu(listViewPedalinhosDisponiveis);
    }

    private void associarPedalinhosEmUsoAListView() {
        adapterPedalinhoUsando = new PedalinhoArrayAdapter(getApplicationContext(), usando);
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
        Collections.sort(usando);
    }

    private void atualizarThread() {
        monitorarTempoPedalinhosThread().interrupt();
        monitorarTempoPedalinhosThread().start();
    }

}