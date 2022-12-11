package com.example.repasobd.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.repasobd.R;

public class AddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        /**
         * Flecha para volver atras
         */
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true); //Todo 3.1 si existe (no es nulo) mostramos el botón hacia atrás.
        }
       
    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:  //  acceso al recurso del botón volver
                onBackPressed();     // método que tiene Android para volver hacia la ventana anterior.
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}