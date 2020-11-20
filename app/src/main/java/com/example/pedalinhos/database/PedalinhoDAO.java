package com.example.pedalinhos.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.pedalinhos.domain.Pedalinho;

import java.util.List;

@Dao
public interface PedalinhoDAO {

    @Query("SELECT * FROM pedalinho")
    List<Pedalinho> getAll();

    @Query("SELECT * FROM pedalinho where usando = :usando  ")
    List<Pedalinho> buscarTodosOsPeladinhos(boolean usando);

    @Query("SELECT * FROM pedalinho where notificado = :notificado  ")
    List<Pedalinho> buscarTodosOsPedalinhosNotificados(boolean notificado);

    @Insert
    void insert(Pedalinho pedalinho);

    @Delete
    void delete(Pedalinho pedalinho);

    @Update
    void update(Pedalinho pedalinho);
}
