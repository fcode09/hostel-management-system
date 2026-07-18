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
 * Estado de ocupacion de una habitacion. Reemplaza los Strings sueltos
 * "Disponible"/"Ocupada" que se comparaban con equals() en la version anterior.
 */
public enum EstadoHabitacion {

    DISPONIBLE("Disponible"),
    OCUPADA("Ocupada");

    private final String etiqueta;

    EstadoHabitacion(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    @Override
    public String toString() {
        return etiqueta;
    }
}
