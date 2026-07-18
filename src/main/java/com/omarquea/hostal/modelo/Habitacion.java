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
 * Una habitacion del hostal. Nace siempre en estado DISPONIBLE y solo
 * el GestorHospedaje puede cambiar su estado al reservarla o liberarla.
 */
public class Habitacion {

    // 1. ENCAPSULAMIENTO: atributos privados, solo accesibles via getters/setters
    private final int numero;
    private final TipoHabitacion tipo;
    private final double precioPorNoche;
    private EstadoHabitacion estado;

    // 2. SOBRECARGA (Overloading): constructor completo.
    // Permite fijar un precio distinto a la tarifa base del tipo
    // (por ejemplo, una promocion o un recargo puntual).
    public Habitacion(int numero, TipoHabitacion tipo, double precioPorNoche) {
        this.numero = numero;
        this.tipo = tipo;
        this.precioPorNoche = precioPorNoche;
        this.estado = EstadoHabitacion.DISPONIBLE;
    }

    // 2. SOBRECARGA (Overloading): constructor simplificado.
    // Se usa cuando alcanza con la tarifa base definida en el propio enum del tipo.
    public Habitacion(int numero, TipoHabitacion tipo) {
        this(numero, tipo, tipo.getPrecioBase());
    }

    // 1. ENCAPSULAMIENTO: getters y setters publicos
    public int getNumero() {
        return numero;
    }

    public TipoHabitacion getTipo() {
        return tipo;
    }

    public double getPrecioPorNoche() {
        return precioPorNoche;
    }

    public EstadoHabitacion getEstado() {
        return estado;
    }

    public void setEstado(EstadoHabitacion estado) {
        this.estado = estado;
    }

    public boolean estaDisponible() {
        return estado == EstadoHabitacion.DISPONIBLE;
    }

    // 3. SOBREESCRITURA (Overriding): redefinimos el toString heredado de Object
    @Override
    public String toString() {
        return String.format("Habitacion %-3d | Tipo: %-11s | Precio: S/ %6.2f | Estado: %s",
                numero, tipo, precioPorNoche, estado);
    }
}
