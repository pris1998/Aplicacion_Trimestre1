package com.example.repasobd.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

import androidx.preference.PreferenceManager;


//Esta clase coloca el tipo de preferencias que va haber, en este caso
//la pantalla va a cambiar de color cada vez que pulses un color diferente.
public class Preferences {
    public static int loadPreferences(Context context){
        int itemSeleccionado = Color.WHITE;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String color = sharedPreferences.getString("color", "rojo");
        Log.i("Color", color);
        switch (color){
            case "violeta":
                itemSeleccionado = Color.MAGENTA;
                Toast.makeText(context,"Color magenta",Toast.LENGTH_LONG).show();
                break;
            case "rojo":
                itemSeleccionado = Color.RED;
                Toast.makeText(context,"Color rojo",Toast.LENGTH_LONG).show();
                break;
            case "verde":
                itemSeleccionado = Color.GREEN;
                Toast.makeText(context,"Color verde",Toast.LENGTH_LONG).show();
                break;
        }
        return itemSeleccionado;
    }

}
