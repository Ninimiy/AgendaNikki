package com.example.agendanikki;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        BancoDados.fecharDB();

        bt_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate();
            }
        });

        bt_hora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTime();
            }
        });

        bt_salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inserirCompromisso();
            }
        });
        carregarCompromissosDoBanco();
        compromissoAdapter.setOnDeleteClickListener(new CompromissoAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(int position) {
                Compromisso compromisso = compromissos.get(position);
                compromissos.remove(position);
                compromissoAdapter.notifyItemRemoved(position);
                BancoDados.abrirBanco(MainActivity.this);
                BancoDados.fecharDB();
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
    private void inserirCompromisso() {
        String st_descricao = et_descricao.getText().toString();
        Compromisso compromisso = new Compromisso(st_descricao, data, horas);
        BancoDados.inserirCompromisso(st_descricao, data, horas, this);
        compromissos.add(compromisso);
        compromissoAdapter.notifyDataSetChanged();
        et_descricao.setText("");
    }
    private void carregarCompromissosDoBanco() {
        compromissos.clear();
        Cursor cursor = BancoDados.buscarDados(this);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String descricao = cursor.getString(cursor.getColumnIndex("descricao"));
                @SuppressLint("Range") String data = cursor.getString(cursor.getColumnIndex("data"));
                @SuppressLint("Range") String hora = cursor.getString(cursor.getColumnIndex("hora"));

                Compromisso compromisso = new Compromisso(descricao, data, hora);
                compromissos.add(compromisso);
            } while (cursor.moveToNext());

            cursor.close();

            compromissoAdapter.notifyDataSetChanged();
        }
    }
}
