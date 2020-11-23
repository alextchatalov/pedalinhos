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
import java.util.Collections;
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
        monitorarTempoPedalinhosThread().start();
    }

    private Thread monitorarTempoPedalinhosThread() {
       return  new Thread( new Runnable() {
            @Override
            public void run() {
                while (true) {

                    for (PedalinhoMarcao pedalinhoEmUso : usando) {

                        if (pedalinhoEmUso.isPedalinhoEncerrado() || pedalinhoEmUso.isPedalinhoNaoNotificado()) {
                            //marcarPealinhoComoNotificadoouEncerrado();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    listViewPedalinhosEmUso.invalidateViews();
                                    listViewPedalinhosDisponiveis.invalidateViews();
                                }
                            });
                            //TODO ALTERAR A COR DO BACKGROUND DA LISTA DO ITEM PARA NOTIFICAR
                            //TODO OU EXIBIR UMA MENSAGEM PARA NOTIFICAR O PEDALINHO SOBRE O TEMPO
                        }
                        //TODO CASO O PEDALINHO JÁ FOI NOTIFICADO OU O TEMPO JÁ ENCERROU HORA ATUAL > HORARIO TERMINO DO PEDALINHO
                        //TODO EXIBIR OU MUDAR O BACKGROUD DE COR PARA AVISAR QUE O TEMPO ENCERROU
                        //TODO CASO NÃO FOR NENHUM DESSES CASOS NÃO FAZER NADA
                    }
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void marcarPealinhoComoNotificadoouEncerrado() {
        for (int position = 1; position < listViewPedalinhosEmUso.getCount(); position++) {
            PedalinhoMarcao pedalinhoMarcao = (PedalinhoMarcao) listViewPedalinhosEmUso.getAdapter().getItem(position);
            View view = listViewPedalinhosEmUso.getAdapter().getView(position, null, null);

            if (pedalinhoMarcao.isPedalinhoEncerrado()) {
                System.out.println("Pedalinho encerrado: " + pedalinhoMarcao);
                view.setBackgroundColor(Color.RED);
            } else if (pedalinhoMarcao.isPedalinhoNaoNotificado()) {
                System.out.println("Pedalinho a notificar: " + pedalinhoMarcao);
                view.setBackgroundColor(Color.YELLOW);
            }
            view.refreshDrawableState();
        }
    }

    private void onClickUsando() {
        listViewPedalinhosEmUso.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, final View view, int i, long l) {
                PedalinhoMarcao marcao = (PedalinhoMarcao) listViewPedalinhosEmUso.getItemAtPosition(i);
                Pedalinho pedalinhoEmUso = marcao.pedalinho;

                if (marcao.isPedalinhoNaoNotificado()){
                    System.out.println("NOTIFICADO pedalinho: " + pedalinhoEmUso);
                    pedalinhoEmUso.setNotificado(true);
                    view.setBackgroundColor(Color.YELLOW);
                } else if (marcao.isPedalinhoEncerrado()) {
                    System.out.println("ENCERRANDO pedalinho: " + pedalinhoEmUso);
                    pedalinhoEmUso.setNotificado(false);
                    pedalinhoEmUso.setUsando(false);
                    view.setBackgroundColor(Color.TRANSPARENT);
                }else {
                    System.out.println("CANCELANDO pedalinho: " + pedalinhoEmUso);
                    pedalinhoEmUso.setNotificado(false);
                    pedalinhoEmUso.setUsando(false);
                    view.setBackgroundColor(Color.TRANSPARENT);
                    //TODO CRIAR UMA MENSAGEM DE VALIDAÇÃO
//                    AlertDialog confirmar = new AlertDialog.Builder(getApplicationContext())
//                            .setTitle("Atenção!")
//                            .setMessage("Pedalinho no tempo, deseja cancelar?")
//                            .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    pedalinhoEmUso.setNotificado(false);
//                                    pedalinhoEmUso.setUsando(false);
//                                    view.setBackgroundColor(Color.TRANSPARENT);
//                                }
//                            })
//                            .setNegativeButton("Não", null)
//                            .create();
//                    confirmar.show();
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
                    calendar.add(Calendar.MINUTE,1);
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
        Collections.sort(usando);
    }

    private void atualizarThread() {
        monitorarTempoPedalinhosThread().interrupt();
        monitorarTempoPedalinhosThread().start();
    }

}