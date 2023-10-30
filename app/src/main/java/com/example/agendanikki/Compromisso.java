package com.example.agendanikki;
public class Compromisso {
    private String descricao;
    private String data;
    private String hora;

    public Compromisso(String descricao, String data, String hora) {
        this.descricao = descricao;
        this.data = data;
        this.hora = hora;
    }
    public String getDescricao() {
        return descricao;
    }
    public String getData() {
        return data;
    }
    public String getHora() {
        return hora;
    }

}
