package com.example.pedalinhos.domain;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.Calendar;

public class PedalinhoMarcao {

    @Embedded
    public Pedalinho pedalinho;

    @Relation(parentColumn = "id", entityColumn = "pedalinho_id")
    public MarcaoUsoPedalinho marcaoUsoPedalinho;

    @Override
    public String toString() {
        StringBuilder exibicao = new StringBuilder();
        if (this.marcaoUsoPedalinho.getTempo() != null && pedalinho.isUsando()) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(this.marcaoUsoPedalinho.getTempo());
            exibicao.append("( Termino em: ");
            exibicao.append(calendar.get(Calendar.HOUR_OF_DAY));
            exibicao.append(":");
            exibicao.append(calendar.get(Calendar.MINUTE));
            exibicao.append(" )");

        }
        return pedalinho.getNumeroPedalinho() + " - " + pedalinho.getTipoPedalinho() + exibicao.toString();
    }
}
