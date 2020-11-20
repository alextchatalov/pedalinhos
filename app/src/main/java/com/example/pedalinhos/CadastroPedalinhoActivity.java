package com.example.pedalinhos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.pedalinhos.database.AppDatabase;
import com.example.pedalinhos.database.Connection;
import com.example.pedalinhos.domain.Pedalinho;
import java.util.ArrayList;
import java.util.List;

public class CadastroPedalinhoActivity extends AppCompatActivity {
    
    public static final String PEDALINHO_PARAM = "pedalinho";
    private Spinner dropDown;
    private EditText numeroPedalinho;
    private String tipoPedalinho;
    private AppDatabase db;
    private Pedalinho pedalinho = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_pedalinho);
        dropDown = findViewById(R.id.spinner);
        db = Connection.getConnection(getApplicationContext());
        numeroPedalinho = findViewById(R.id.editNumeroPedalinho);
        comboBoxTipoPedalinho();

        Intent intent = getIntent();
        if (intent.hasExtra(PEDALINHO_PARAM)) {
            pedalinho = (Pedalinho) intent.getSerializableExtra(PEDALINHO_PARAM);
            numeroPedalinho.setText(Integer.toString(pedalinho.getNumeroPedalinho()));
            tipoPedalinho = pedalinho.getTipoPedalinho();
            selectValue(dropDown, tipoPedalinho);
        }
    }

    private void comboBoxTipoPedalinho() {


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
        String message = "";

        if (pedalinho == null) {

            pedalinho = new Pedalinho();
            pedalinho.setNumeroPedalinho(Integer.valueOf(numeroPedalinho.getText().toString()));
            pedalinho.setTipoPedalinho(tipoPedalinho);
            pedalinho.setUsando(false);
            db.pedalinhoDAO().insert(pedalinho);
            message = "Pedalinho: " + pedalinho + " Salvo com Sucesso!";
        } else {
            pedalinho.setNumeroPedalinho(Integer.valueOf(numeroPedalinho.getText().toString()));
            pedalinho.setTipoPedalinho(tipoPedalinho);
            db.pedalinhoDAO().update(pedalinho);
            message = "Pedalinho: " + pedalinho + " Alterado com Sucesso!";
        }

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    private void selectValue(Spinner spinner, Object value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(value)) {
                spinner.setSelection(i);
                break;
            }
        }
    }
}