package com.example.pedalinhos.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.pedalinhos.domain.MarcaoUsoPedalinho;
import com.example.pedalinhos.domain.Pedalinho;

@Database(entities = {Pedalinho.class, MarcaoUsoPedalinho.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract PedalinhoDAO pedalinhoDAO();
    public abstract MarcacaoPedalinhoDAO marcacaoPedalinhoDAO();
}