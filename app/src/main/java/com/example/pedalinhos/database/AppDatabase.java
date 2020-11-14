package com.example.pedalinhos.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.pedalinhos.domain.Pedalinho;

@Database(entities = {Pedalinho.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PedalinhoDAO pedalinhoDAO();
}