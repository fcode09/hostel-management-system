/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package com.omarquea.hostal.excepcion;

/**
 *
 * @author omarquea
 */
/**
 * Se lanza cuando se intenta reservar una habitacion que no existe
 * o que ya esta ocupada por otro huesped.
 */
public class HabitacionNoDisponibleException extends Exception {

    public HabitacionNoDisponibleException(String mensaje) {
        super(mensaje);
    }
}
