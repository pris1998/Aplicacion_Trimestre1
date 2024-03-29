package com.example.repasobd.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.repasobd.R;
import com.example.repasobd.model.DatesUser;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    private CircularProgressDrawable circularprogressdrawable;
    ImageView imgPersona;
    Button btnEntrar , btnRegistrar, btnLogin;
    EditText textUser , textPass;
    DatesUser datoUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgPersona = findViewById(R.id.imageView);
        btnEntrar = findViewById(R.id.btnEntrar);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        textUser = findViewById(R.id.textUser);

        textPass = findViewById(R.id.txtPassword);
        datoUser = new DatesUser(this);

        circularprogressdrawable = new CircularProgressDrawable(this);
        circularprogressdrawable.setStrokeWidth(10f);
        circularprogressdrawable.setStyle(CircularProgressDrawable.LARGE);
        circularprogressdrawable.setCenterRadius(30f);
        circularprogressdrawable.start();

        //Genera la imagen con Glide
        Glide.with(MainActivity.this)
                .load("https://www.pinterest.es/pin/784681935064916793/")
                .placeholder(circularprogressdrawable)
                .error(R.drawable.icono)
                .into(imgPersona);

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String recibeUser = textUser.getText().toString().trim();
                String recibeContrasenia = textPass.getText().toString().trim();

                //Comporbacion si los campos estan vacios o no , con sus respectivos mensajes
                if (!recibeUser.equals("") || !recibeContrasenia.equals("")) {
                    if (datoUser.checkPassword(recibeUser,recibeContrasenia)) {
                        Toast.makeText(MainActivity.this, "Registro existoso,usuario y contraseña registrado", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(MainActivity.this, InicioActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(MainActivity.this, "Registro fallido,usuario no existe", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Por favor,complete los campos", Toast.LENGTH_LONG).show();
                }
            }
            });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Al no estar resgistrado te lleva a la ActividadRegister
                Intent intent = new Intent (MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}