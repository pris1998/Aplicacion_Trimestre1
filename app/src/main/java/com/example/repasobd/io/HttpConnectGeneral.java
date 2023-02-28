package com.example.repasobd.io;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpConnectGeneral {
    //Variable estatica para obtener la url de la API
    private static final String URL_BASE ="https://www.amiiboapi.com/api/amiibo/";
    //Método encarga realizar la consulta de la API
    public static String getRequest(String enpoint){
        HttpURLConnection http = null;
        String content = null;
        try{
            //Termina crear el enlace,URL base necesita un valor para saber donde colocarse
            URL url = new URL(URL_BASE + enpoint);
            http = (HttpURLConnection) url.openConnection();
            http.setRequestProperty("Content-Type", "application/json");
            http.setRequestProperty("Accept", "application/json");
            //Si el servidor devuelve un codigo (HTTP_OK == 200) todo ha salido bien
            if( http.getResponseCode() == HttpURLConnection.HTTP_OK ) {
                //Comprueba que ha realizado correctamente
                StringBuilder sb = new StringBuilder();
                //Creacion lector de información
                BufferedReader reader = new BufferedReader(new InputStreamReader(http.getInputStream()));
                //Cuanto hay que leer
                String linea ;
                //Recorre JSON hasta no haber ninguna línea que recorrerla
                while( (linea = reader.readLine()) != null){
                    //Añade la linea que se ha eído
                    sb.append(linea);
                }
                //Se añade al contenido todo lo del constructor
                content = sb.toString();
                reader.close();
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        finally {
            //Desconecto la conexión.
            if( http != null ) http.disconnect();
        }
        return content;
    }


}
