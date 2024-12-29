package com.julieht.desafio.servicio;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest;

public class ConsumoApi {
    public String obtenerDatos(String url) {
        HttpClient cliente = HttpClient.newHttpClient();
        HttpRequest pedido = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        HttpResponse<String> respuesta = null;

        try {
            respuesta = cliente
                    .send(pedido, HttpResponse.BodyHandlers.ofString());

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        String json = respuesta.body();
        return json;
    }
}
