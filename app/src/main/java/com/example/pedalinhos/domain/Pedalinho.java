package com.example.pedalinhos.domain;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

@Entity
public class Pedalinho implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(name = "numero_pedalinho")
    private Integer numeroPedalinho;

    @ColumnInfo(name = "tipo_pedalinho")
    private String tipoPedalinho;

    @Ignore
    private Date dataInicioUso;

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

    public Date getDataInicioUso() {
        return dataInicioUso;
    }

    public void setDataInicioUso(Date dataInicioUso) {
        this.dataInicioUso = dataInicioUso;
    }

    @Override
    public String toString() {
        return numeroPedalinho + " - " + tipoPedalinho;
    }
}
