package com.example.repasobd.model;

public class Anime {

    private String anime_name;
    private String url;


    public Anime(String anime_name, String url ) {
        this.anime_name = anime_name;
        this.url = url;

    }

    public String getAnime_name() {return anime_name;}

    public void setAnime_name(String anime_name) {this.anime_name = anime_name;}

    public String getUrl() {return url;}

    public void setUrl(String url) {this.url = url;}


}
