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

    public static void abrirBanco(Activity act){
        try{
            ContextWrapper cw=new ContextWrapper(act);
            db=cw.openOrCreateDatabase("bancoAgenda",MODE_PRIVATE,null);
        }catch (Exception ex){
            Msg.mostrar("Erro ao abrir ou criar Banco de Dados",act);
        }
    }
    public static void abrirTabelaCompromissos(Activity act){
        abrirBanco(act);
        try{
            db.execSQL("CREATE TABLE IF NOT EXISTS compromissos (id INTEGER PRIMARY KEY, descricao TEXT, data TEXT, hora TEXT);");
        }
        catch(Exception ex){
            Msg.mostrar(ex.getMessage(),act);
        }
    }

    public static void fecharDB(){
        db.close();
    }

    public static void inserirCompromisso(String descricao, String data, String hora, Activity act){
        abrirBanco(act);
        try {
            abrirTabelaCompromissos(act); // Abra a tabela de compromissos
            db.execSQL("INSERT INTO compromissos (descricao, data, hora) VALUES ('" + descricao + "','" + data + "','" + hora + "')");
        }
        catch (Exception ex) {
            Msg.mostrar(ex.getMessage(), act);
        }
        finally {
            Msg.mostrar("Compromisso inserido", act);
            fecharDB();
        }
    }


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