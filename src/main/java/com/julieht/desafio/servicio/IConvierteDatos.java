package com.julieht.desafio.servicio;

public interface IConvierteDatos {
    <T> T obtenerDatos(String json, Class<T> clase);
}
