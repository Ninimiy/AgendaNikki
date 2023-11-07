package com.example.agendanikki;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor; //Navegar entre os registros
import android.content.ContextWrapper;
import android.view.View;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;
import static android.content.Context.MODE_PRIVATE;

public class BancoDados {
    static SQLiteDatabase db=null;
    static Cursor cursor;

    //abre o banco de dados ou cria se nao existir
    public static void abrirBanco(Activity act){
        try{
            ContextWrapper cw=new ContextWrapper(act);
            db=cw.openOrCreateDatabase("bancoAgenda",MODE_PRIVATE,null);
        }catch (Exception ex){
            Msg.mostrar("Erro ao abrir ou criar Banco de Dados",act);
        }
    }
    //abre a tabela compromissos chamando abrirBanco primeiro para garantiur que o db esteja aberto
    //e executa uma consulta SQL para criar a tabela se ela nao existir
    public static void abrirTabelaCompromissos(Activity act){
        abrirBanco(act);
        try{
            db.execSQL("CREATE TABLE IF NOT EXISTS compromissos (id INTEGER PRIMARY KEY, descricao TEXT, data TEXT, hora TEXT);");
        }
        catch(Exception ex){
            Msg.mostrar(ex.getMessage(),act);
        }
    }

    //fecha o banco de dados quando nao for mais necessario
    public static void fecharDB(){
        db.close();
    }
    //serve para inserir um novo compromisso na tabela "compromissos"
    //abre o banco primeiro e executa a consulta SQL para inserir os dados na tabela
    //mostra uma alerta e depois fecha o banco de dados
    public static void inserirCompromisso(String descricao, String data, String hora, Activity act){
        abrirBanco(act);
        try {
            abrirTabelaCompromissos(act); // Abra a tabela de compromissos
            db.execSQL("INSERT INTO compromissos (descricao, data, hora) VALUES ('" + descricao + "','" + data + "','" + hora + "')");
        }
        catch (Exception ex) {
            Msg.mostrar("Erro ao inserir compromisso:" + ex.getMessage(), act);
        }
        finally {
            Msg.mostrar("Compromisso inserido", act);
            fecharDB();
        }
    }
    //fornece dados para que CompromissoAdapter possa preencher a lista
    //de compromissos na interface
    public static Cursor buscarDados(Activity act) {
        fecharDB();
        abrirBanco(act);

        cursor = db.query("compromissos",
                new String[]{"descricao", "data", "hora"},
                null,
                null,
                null,
                null,
                "data ASC",
                null
        );
        cursor.moveToFirst();
        return cursor;
    }
}