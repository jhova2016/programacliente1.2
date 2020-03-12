package com.example.programacliente12;

public class elemento {
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    private String nombre;
    private String tipo;

    public elemento(String nombre, String tipo) {
        this.nombre = nombre;
        this.tipo = tipo;
    }
    public elemento() {

    }


}
