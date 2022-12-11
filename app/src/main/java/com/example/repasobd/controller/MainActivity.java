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
        // boton de la biblioteca externa
        //btnLogin = findViewById(R.id.btnLogin);
        /*
        buttonLoading.setOnButtonLoadingListener(new ButtonLoading.OnButtonLoadingListener() {
            @Override
            public void onClick() {
                //...
            }

            @Override
            public void onStart() {
                //...
            }

            @Override
            public void onFinish() {
                //...
            }
        });*/

        circularprogressdrawable = new CircularProgressDrawable(this);
        circularprogressdrawable.setStrokeWidth(10f);
        circularprogressdrawable.setStyle(CircularProgressDrawable.LARGE);
        circularprogressdrawable.setCenterRadius(30f);
        circularprogressdrawable.start();
        /**
         * Esta parte es la del ImfenView crea un Glide llamando desde un enlace a la foto
         */
        Glide.with(MainActivity.this)
                .load("https://img00.deviantart.net/8a9a/i/2010/057/8/5/totoro_by_autodach.jpg")
                .placeholder(circularprogressdrawable)
                .error(R.drawable.img_login)
                .into(imgPersona);
        /**
         * Funcionalidad del boton Entrar del Login desde un Listener
         */
        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String recibeUser = textUser.getText().toString().trim();
                String recibeContrasenia = textPass.getText().toString().trim();

                //vemos si esta vacio
                if (recibeUser.equals("") && recibeContrasenia.equals("")) {
                    //mensaje
                    Log.d("SELECT * FROM TABLE_NAME", "Obtencion de datos ");

                    Snackbar.make(view, "Error: los campos estan vacios , no los has rellenado", Snackbar.LENGTH_SHORT).show();
                } else {


                    /*if (datoUser.loginMetodo(recibeUser, recibeContrasenia) == 1) {

                        //darle acceso a la base de datos aunque los datos ya esten guardados
                        //datoUser.loginMetodo(recibeUser, recibeContrasenia);
                        Snackbar.make(view, "Datos correctos", Snackbar.LENGTH_SHORT).show();
                        Log.d("Datos ", "Exito de datos");
                        //comprobar que te lo haya cogido para pasar de hoja


                    }else{*/

                    Intent intent = new Intent(MainActivity.this, InicioActivity.class);
                    startActivity(intent);
                    Toast.makeText(MainActivity.this, "Usuario existe", Toast.LENGTH_LONG).show();

                }
            }
            });
        /**
         * Funcionalidad del boton Registrar del Login desde un Listener
         */
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}