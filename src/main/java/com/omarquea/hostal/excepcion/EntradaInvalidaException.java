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
 * Se lanza cuando un dato de negocio no cumple una regla minima
 * (por ejemplo, dias de estadia menores o iguales a cero).
 */
public class EntradaInvalidaException extends Exception {

    public EntradaInvalidaException(String mensaje) {
        super(mensaje);
    }
}
