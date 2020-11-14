package com.example.pedalinhos.domain;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Pedalinho implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(name = "numero_pedalinho")
    private Integer numeroPedalinho;

    @ColumnInfo(name = "tipo_pedalinho")
    private String tipoPedalinho;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumeroPedalinho() {
        return numeroPedalinho;
    }

    public void setNumeroPedalinho(Integer numeroPedalinho) {
        this.numeroPedalinho = numeroPedalinho;
    }

    public String getTipoPedalinho() {
        return tipoPedalinho;
    }

    public void setTipoPedalinho(String tipoPedalinho) {
        this.tipoPedalinho = tipoPedalinho;
    }

    @Override
    public String toString() {
        return numeroPedalinho + " - " + tipoPedalinho;
    }
}
