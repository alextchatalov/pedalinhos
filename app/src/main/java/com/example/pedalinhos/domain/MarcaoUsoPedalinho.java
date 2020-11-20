package com.example.pedalinhos.domain;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class MarcaoUsoPedalinho {

    @PrimaryKey(autoGenerate = true)
    private Long id;
    private long pedalinho_id;

    @ColumnInfo(name = "tempo")
    private Date tempo;

    public MarcaoUsoPedalinho() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getTempo() {
        return tempo;
    }

    public void setTempo(Date tempo) {
        this.tempo = tempo;
    }

    public long getPedalinho_id() {
        return pedalinho_id;
    }

    public void setPedalinho_id(long pedalinho_id) {
        this.pedalinho_id = pedalinho_id;
    }
}
