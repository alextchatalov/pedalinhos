package com.example.pedalinhos.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.pedalinhos.domain.Pedalinho;

import java.util.List;

@Dao
public interface PedalinhoDAO {

    @Query("SELECT * FROM pedalinho")
    List<Pedalinho> getAll();

    @Insert
    void insert(Pedalinho pedalinho);

    @Delete
    void delete(Pedalinho pedalinho);
}
