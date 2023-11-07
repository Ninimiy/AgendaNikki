package com.example.agendanikki;

import android.app.Activity;
import android.app.AlertDialog;
//mensagem de alerta
public class Msg {
    public static void mostrar(String txt, Activity act){
        AlertDialog.Builder adb=new AlertDialog.Builder(act);
        adb.setMessage(txt);
        adb.setNeutralButton("OK",null);
        adb.show();
    }

}