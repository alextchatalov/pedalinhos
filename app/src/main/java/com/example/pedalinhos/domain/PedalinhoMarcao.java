package com.example.pedalinhos.domain;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.pedalinhos.Configuracao;

import java.util.Calendar;

public class PedalinhoMarcao implements Comparable<PedalinhoMarcao> {

    @Embedded
    public Pedalinho pedalinho;

    @Relation(parentColumn = "id", entityColumn = "pedalinho_id")
    public MarcaoUsoPedalinho marcaoUsoPedalinho;

    public boolean isPedalinhoNaoNotificado() {
        if (this.marcaoUsoPedalinho != null && this.marcaoUsoPedalinho.getTempo() != null) {
            Calendar notificar = Calendar.getInstance();
            notificar.setTime(this.marcaoUsoPedalinho.getTempo());
            notificar.add(Calendar.MINUTE, -Configuracao.TEMPO_NOTIFICAR);
            return this.pedalinho.isUsando() &&
                    !this.pedalinho.isNotificado() &&
                    notificar.getTime().before(Calendar.getInstance().getTime());
        }
        return false;
    }

    public boolean isPedalinhoEncerrado() {
        if (this.marcaoUsoPedalinho != null && this.marcaoUsoPedalinho.getTempo() != null) {
            return this.pedalinho.isUsando() &&
                    this.marcaoUsoPedalinho.getTempo().before(Calendar.getInstance().getTime());
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder exibicao = new StringBuilder();
        if (this.marcaoUsoPedalinho != null && this.marcaoUsoPedalinho.getTempo() != null && pedalinho.isUsando()) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(this.marcaoUsoPedalinho.getTempo());
            exibicao.append(" ( Termino em: ");
            exibicao.append(calendar.get(Calendar.HOUR_OF_DAY));
            exibicao.append(":");
            String minutos = String.valueOf(calendar.get(Calendar.MINUTE));
            exibicao.append(minutos.length() < 2 ? "0" : "").append(minutos);
            exibicao.append(" )");
        }

//        if (this.isPedalinhoEncerrado()) {
//            exibicao.append(" TEMPO ENCERRADO");
//        } else if (this.isPedalinhoNaoNotificado()) {
//            exibicao.append(" - NOTIFICAR");
//        }
        return pedalinho.getNumeroPedalinho() + " - " + pedalinho.getTipoPedalinho() + exibicao.toString();
    }

    @Override
    public int compareTo(PedalinhoMarcao o) {
        if (marcaoUsoPedalinho == null || marcaoUsoPedalinho.getTempo() == null || o.marcaoUsoPedalinho == null || o.marcaoUsoPedalinho.getTempo() == null) {
            return 0;
        }
        return marcaoUsoPedalinho.getTempo().compareTo(o.marcaoUsoPedalinho.getTempo());
    }
}
