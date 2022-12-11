package com.example.repasobd.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.repasobd.R;
import com.example.repasobd.model.ClassUser;
import com.example.repasobd.model.DatesUser;

public class RegisterActivity extends AppCompatActivity {
    EditText userReg , passReg , emailReg;
    Button btnCrearCuenta, btnCancelar;
    DatesUser datoUser;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userReg=(EditText)findViewById(R.id.UserReg);
        passReg= (EditText)findViewById(R.id.passReg);
        emailReg=(EditText)findViewById(R.id.emailReg);
        btnCrearCuenta= (Button) findViewById(R.id.btnCrearCuenta);
        btnCancelar = (Button) findViewById(R.id.btnCancelar);

        datoUser=new DatesUser(this);
        btnCrearCuenta.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ClassUser userRegistro = new ClassUser();
                userRegistro.setUsuario(userReg.getText().toString());
                userRegistro.setContraseña(passReg.getText().toString());
                userRegistro.setEmailUser(emailReg.getText().toString());
                if (!userRegistro.isNull()){
                    myToast("Error: campos vacíos");
                }else if(datoUser.insertarUsuario(userRegistro) == 1){
                    //myToast("Usuario registrado");
                    Intent intent2 = new Intent(RegisterActivity.this , MainActivity.class);
                    startActivity(intent2);

                }else{
                    myToast("El usuario ya existe");
                }
            }
        });
        btnCancelar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this , MainActivity.class);
                startActivity(intent);

            }
        });



    }
    public void myToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
    }
}