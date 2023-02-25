package com.example.repasobd.model;

import java.io.Serializable;

public class Amiibo implements Serializable {
    String character;
    String amiiboSeries;
    String gameSeries;
    String imagen;

    public Amiibo(String character, String amiiboSeries, String gameSeries, String imagen) {
        this.character = character;
        this.amiiboSeries = amiiboSeries;
        this.gameSeries = gameSeries;
        this.imagen = imagen;
    }

    public String getCharacter() {
        return character;
    }


    public String getAmiiboSeries() {
        return amiiboSeries;
    }


    public String getGameSeries() {
        return gameSeries;
    }


    public String getImagen() {return imagen;}

}
