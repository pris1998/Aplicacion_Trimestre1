package com.example.repasobd.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

import androidx.preference.PreferenceManager;



public class Preferences {

    public static int loadPreferences(Context context){
        int itemSeleccionado = Color.WHITE;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String color = sharedPreferences.getString("color", "rojo");
        Log.i("Color", color);
        switch (color){
            case "violeta":
                //el color violeta
                itemSeleccionado = Color.MAGENTA;
                Toast.makeText(context,"Color magenta",Toast.LENGTH_LONG).show();
                break;
            case "rojo":
                //color azul
                itemSeleccionado = Color.RED;
                Toast.makeText(context,"Color rojo",Toast.LENGTH_LONG).show();
                break;
            case "verde":
                //color verde
                itemSeleccionado = Color.GREEN;
                Toast.makeText(context,"Color verde",Toast.LENGTH_LONG).show();
                break;
        }
        return itemSeleccionado;


    }

}
