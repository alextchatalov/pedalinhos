package com.example.pedalinhos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
}