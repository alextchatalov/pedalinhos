package com.example.pedalinhos.database;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.pedalinhos.domain.MarcaoUsoPedalinho;
import com.example.pedalinhos.domain.Pedalinho;

public class PedalinhoMarcao {

    @Embedded
    public Pedalinho pedalinho;

    @Relation(parentColumn = "id", entityColumn = "pedalinho_id")
    public MarcaoUsoPedalinho marcaoUsoPedalinho;

}
