package com.example.agendanikki;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText et_descricao;
    private Button bt_data, bt_hora, bt_salvar;
    private String horas, data;
    private RecyclerView recyclerView;
    private CompromissoAdapter compromissoAdapter;
    private List<Compromisso> compromissos = new ArrayList<>();

    private int lastUsedId = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //parte da interface
        et_descricao = findViewById(R.id.et_descricao);
        bt_data = findViewById(R.id.bt_data);
        bt_hora = findViewById(R.id.bt_hora);
        bt_salvar = findViewById(R.id.bt_salvar);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        compromissos = new ArrayList<>();
        compromissoAdapter = new CompromissoAdapter(this, compromissos);
        recyclerView.setAdapter(compromissoAdapter);

        BancoDados.abrirBanco(this);
        BancoDados.abrirTabelaCompromissos(this);

        //escolher data
        bt_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate();
            }
        });

        //escolher hora
        bt_hora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTime();
            }
        });

        //obtém a descrição, data e hora do compromisso e insere no banco de dados
        //e atualiza a lista de compromissos.
        bt_salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inserirCompromisso();
            }
        });
        //carrega os compromissos armazenados no banco de dados e adiciona na lista compromissos
        carregarCompromissosDoBanco();
        BancoDados.fecharDB();

        //usado para deletar compromissos individualmente, cada compromisso
        //tem seu próprio botão de deletar
        compromissoAdapter.setOnDeleteClickListener(new CompromissoAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(int position) {
                Compromisso compromisso = compromissos.get(position);
                int id = compromisso.getId(); // Obtenha o ID do compromisso da lista
                BancoDados.excluirCompromisso(id, MainActivity.this);

                compromissos.remove(position);
                compromissoAdapter.notifyItemRemoved(position);
            }

        });

    }
    public void selectTime() {
        final Calendar calendar = Calendar.getInstance();
        int hora = calendar.get(Calendar.HOUR_OF_DAY);
        int minuto = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (timePicker, hourOfDay, minute) -> {
                    String horaSelecionada = String.format("%02d:%02d", hourOfDay, minute);
                    bt_hora.setText(horaSelecionada);
                    horas = horaSelecionada;
                }, hora, minuto, true);
        timePickerDialog.show();
    }
    //funcao para pegar a data no formato de calendario na hora da seleção
    public void selectDate() {
        final Calendar calendar = Calendar.getInstance();
        int ano = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (datePicker, year, month, day) -> {
                    String dataSelecionada = day + "/" + (month + 1) + "/" + year;
                    bt_data.setText(dataSelecionada);
                    data = dataSelecionada;
                }, ano, mes, dia);
        datePickerDialog.show();
    }
    //funcao para pegar a hora no formato de um relogio na hora da seleção
    private void inserirCompromisso() {
        String st_descricao = et_descricao.getText().toString();
        int newId = ++lastUsedId; // Incrementa o ID
        Compromisso compromisso = new Compromisso(newId, st_descricao, data, horas);

        BancoDados.inserirCompromisso(st_descricao, data, horas, this);
        compromissos.add(compromisso);
        compromissoAdapter.notifyDataSetChanged();
        et_descricao.setText("");
    }

    //carrega os compromissos armazenados no banco de dados e coloca na lista de compromissos
    private void carregarCompromissosDoBanco() {
        compromissos.clear();
        Cursor cursor = BancoDados.buscarDados(this);
        if (cursor != null && cursor.moveToFirst()) {
            int descricaoIndex = cursor.getColumnIndex("descricao");
            int dataIndex = cursor.getColumnIndex("data");
            int horaIndex = cursor.getColumnIndex("hora");
            int idIndex = cursor.getColumnIndex("id");

            do {
                if (descricaoIndex >= 0 && dataIndex >= 0 && horaIndex >= 0) {
                    String descricao = cursor.getString(descricaoIndex);
                    String data = cursor.getString(dataIndex);
                    String hora = cursor.getString(horaIndex);
                    int id = cursor.getInt(idIndex);

                    Compromisso compromisso = new Compromisso(id, descricao, data, hora);
                    compromissos.add(compromisso);
                    lastUsedId = Math.max(lastUsedId, id); // Atualiza o lastUsedId
                }
            } while (cursor.moveToNext());
            cursor.close();
            compromissoAdapter.notifyDataSetChanged();
        }
    }






}
