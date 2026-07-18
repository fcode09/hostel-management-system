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
 * Se lanza al intentar hacer check-out de una habitacion que no tiene
 * ninguna reserva activa registrada.
 */
public class ReservaNoEncontradaException extends Exception {

    public ReservaNoEncontradaException(String mensaje) {
        super(mensaje);
    }
}
