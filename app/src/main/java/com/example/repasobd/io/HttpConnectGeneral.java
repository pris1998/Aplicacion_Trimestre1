package com.example.repasobd.io;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpConnectGeneral {
    //URL
    private static final String URL_BASE ="https://www.amiiboapi.com/api/amiibo/";
    //metodo àra peticiones GET
    public static String getRequest(String enpoint){
        HttpURLConnection http = null;
        String content = null;
        try{
            URL url = new URL(URL_BASE + enpoint);
            http = (HttpURLConnection) url.openConnection();
            http.setRequestProperty("Content-Type", "application/json");
            http.setRequestProperty("Accept", "application/json");
            //si el servidor devuelve un codigo (HTTP_OK == 200) todo ha salido bien
            if( http.getResponseCode() == HttpURLConnection.HTTP_OK ) {
                StringBuilder sb = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(http.getInputStream()));
            String linea ;
                while( (linea = reader.readLine()) != null){
                    sb.append(linea);
                }
                content = sb.toString();
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        finally {
            //desconecto la conexión.
            if( http != null ) http.disconnect();
        }
        return content;
    }


}
