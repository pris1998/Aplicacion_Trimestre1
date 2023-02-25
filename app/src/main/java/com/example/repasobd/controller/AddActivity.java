package com.example.repasobd.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.repasobd.R;
import com.example.repasobd.model.Amiibo;

public class AddActivity extends AppCompatActivity {
    TextView textoCharacter ;
    TextView textogameSeries;
    ImageView imagenPersonaje;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        textoCharacter = findViewById(R.id.textoCharacter);
        textogameSeries = findViewById(R.id.textogameSeries);
        imagenPersonaje = findViewById(R.id.imagenPersonaje);
        //Activacion de la flecha para volver hacia atrás
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true); //Todo 3.1 si existe (no es nulo) mostramos el botón hacia atrás.
        }

        Amiibo amiibo = (Amiibo) getIntent().getExtras().getSerializable("AMIIBO");
        textoCharacter.setText(amiibo.getCharacter());
        textogameSeries.setText(amiibo.getGameSeries());
        Glide.with(imagenPersonaje)
                .load(amiibo.getImagen())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(imagenPersonaje);


    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:  //  acceso al recurso del botón volver
                onBackPressed();     // vuelve hacia la ventana anterior.
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}