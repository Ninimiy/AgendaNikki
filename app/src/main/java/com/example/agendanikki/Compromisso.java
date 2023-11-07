package com.example.agendanikki;

//armazena os dados de descricao, data e hora
public class Compromisso {
    private int id;
    private String descricao;
    private String data;
    private String hora;

//construtor da classe compromisso
public Compromisso(int id, String descricao, String data, String hora) {
    this.id = id;
    this.descricao = descricao;
    this.data = data;
    this.hora = hora;
}
    public int getId() {
        return id;
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
