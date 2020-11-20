package com.example.pedalinhos.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.pedalinhos.domain.MarcaoUsoPedalinho;
import com.example.pedalinhos.domain.Pedalinho;

import java.util.List;

@Dao
public interface MarcacaoPedalinhoDAO {

    @Insert
    void insert(MarcaoUsoPedalinho marcacao);

    @Delete
    void delete(MarcaoUsoPedalinho marcacao);

    @Update
    void update(MarcaoUsoPedalinho marcacao);
}
