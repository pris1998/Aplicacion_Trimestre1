package com.example.repasobd.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.repasobd.R;
import com.example.repasobd.fragments.SettingFragment;
import com.example.repasobd.utilities.Preferences;

public class SettingActivity extends AppCompatActivity {
    private ConstraintLayout constraintLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        constraintLayout = findViewById(R.id.setting_container);
        //Sirve para añadir el fragmente de la clase (SettingFragment) a la vista
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.setting_container, new SettingFragment())   //Todo 2.1 Es en esta línea donde se reemplaza el contenedor por una instancia de la clase SettingFragment
                .commit();
        constraintLayout.setBackgroundColor(Preferences.loadPreferences(this));

        //Activa la flecha para volver a la actividad anterior
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true); //Todo 3.1 si existe (no es nulo) mostramos el botón hacia atrás.
        }
    }
    //Añade el efecto de volver a la actividad anterior
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:  
                onBackPressed();     
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Carga de OnResume, hay que usarlo para que te lo guarde desde el ultimo momento
    @Override
    protected void onResume() {
        super.onResume();
        constraintLayout.setBackgroundColor(Preferences.loadPreferences(this));
    }
}