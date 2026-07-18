/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.omarquea.hostal.modelo;

/**
 *
 * @author omarquea
 */
/**
 * Reserva de un huesped sobre una habitacion puntual, por una cantidad de dias.
 */
public class Reserva {

    // 1. ENCAPSULAMIENTO: atributos privados
    private final String clienteNombre;
    private final String clienteDni;
    private final Habitacion habitacion;
    private final int diasEstadia;

    public Reserva(String clienteNombre, String clienteDni, Habitacion habitacion, int diasEstadia) {
        this.clienteNombre = clienteNombre;
        this.clienteDni = clienteDni;
        this.habitacion = habitacion;
        this.diasEstadia = diasEstadia;
    }

    public Habitacion getHabitacion() {
        return habitacion;
    }

    public double calcularTotal() {
        return diasEstadia * habitacion.getPrecioPorNoche();
    }

    // 3. SOBREESCRITURA (Overriding): para imprimir el ticket ya formateado
    @Override
    public String toString() {
        return String.format(
                "Cliente: %s | DNI: %s | Habitacion: %d | Dias: %d | Total a pagar: S/ %.2f",
                clienteNombre, clienteDni, habitacion.getNumero(), diasEstadia, calcularTotal());
    }
}
