package com.example.pedalinhos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pedalinhos.database.AppDatabase;
import com.example.pedalinhos.database.Connection;
import com.example.pedalinhos.domain.Pedalinho;

import java.util.ArrayList;
import java.util.List;

public class CadastroPedalinhoActivity extends AppCompatActivity {

    private Spinner dropDown;
    private EditText numeroPedalinho;
    private String tipoPedalinho;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_pedalinho);

        db = Connection.getConnection(getApplicationContext());
        numeroPedalinho = findViewById(R.id.editNumeroPedalinho);
        comboBoxTipoPedalinho();
    }

    private void comboBoxTipoPedalinho() {
        dropDown = findViewById(R.id.spinner);

        List<String> list = new ArrayList<>();
        list.add("Selecione o Tipo do Pedalinho");
        list.add("Cisne");
        list.add("Barco");
        list.add("Caiaque");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropDown.setAdapter(adapter);
        dropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tipoPedalinho = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(CadastroPedalinhoActivity.this, "Selecionado: " + tipoPedalinho, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void salvar(View view) {

        if (numeroPedalinho.getText().toString() != null &&
            (tipoPedalinho != null || "".equals(tipoPedalinho.trim()))
           ) {
            Pedalinho pedalinho = new Pedalinho();
            pedalinho.setNumeroPedalinho(Integer.valueOf(numeroPedalinho.getText().toString()));
            pedalinho.setTipoPedalinho(tipoPedalinho);
            db.pedalinhoDAO().insert(pedalinho);
            List<Pedalinho> all = db.pedalinhoDAO().getAll();
            Toast.makeText(this, "Pedalinho com ID inserido: " + all.size(), Toast.LENGTH_SHORT).show();
        }

        Intent intent = new Intent(getApplicationContext(), ListarPedalinhosActivity.class);
        startActivity(intent);
    }
}