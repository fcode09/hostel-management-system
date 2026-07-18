/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.omarquea.hostal.modelo;

/**
 *
 * @author omarquea
 */
/**
 * Tipos de habitacion que ofrece el hostal, cada uno con su tarifa base.
 * Reemplaza el viejo campo String "tipo" para evitar errores de tipeo
 * (ej. "simple" vs "Simple") y centralizar el precio por defecto de cada tipo.
 */
public enum TipoHabitacion {

    SIMPLE("Simple", 50.0),
    DOBLE("Doble", 80.0),
    MATRIMONIAL("Matrimonial", 120.0);

    private final String etiqueta;
    private final double precioBase;

    TipoHabitacion(String etiqueta, double precioBase) {
        this.etiqueta = etiqueta;
        this.precioBase = precioBase;
    }

    public double getPrecioBase() {
        return precioBase;
    }

    @Override
    public String toString() {
        return etiqueta;
    }
}
