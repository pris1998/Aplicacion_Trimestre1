package com.example.repasobd.model;
/**
 * Me creo una clase Usuario que recoge la informacion del usuario:
 * IdUsuario , usuerio(nombreUsuario) , contraseña,
 * */

public class ClassUser {

    int idUsuario;
    String usuario;
    String contrasenia;
    String emailUser;

    public ClassUser() {
    }
    /**Metodo que no permita campos nulos */
    public boolean isNull(){
        boolean esValido  = true;
        if (emailUser.equals("") && usuario.equals("")&& contrasenia.equals("")){
            esValido = false;
        }else{
            esValido = true;
        }
        return esValido;
    }
    /**
     * El contructor
     * */
    public ClassUser(String contrasenia, String usuario , String emailUser) {
        this.emailUser = emailUser;
        this.contrasenia = contrasenia;
        this.usuario = usuario;
    }
    /**
     * Getters y setter de cada variable
     * */
    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContraseña(String contraseña) {
        this.contrasenia =contrasenia;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    /**
     * To String para que me lo muestre de esta forma
     * */
    @Override
    public String toString() {
        return "UsuarioClass{" +
                "idUsuario=" + idUsuario +
                ", nombreU='" + emailUser + '\'' +
                ", contraseña='" + contrasenia + '\'' +
                ", usuario='" + usuario + '\'' +
                '}';
    }
}
