/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.omarquea.hostal.servicio;

import com.omarquea.hostal.excepcion.EntradaInvalidaException;
import com.omarquea.hostal.excepcion.HabitacionNoDisponibleException;
import com.omarquea.hostal.excepcion.ReservaNoEncontradaException;
import com.omarquea.hostal.modelo.EstadoHabitacion;
import com.omarquea.hostal.modelo.Habitacion;
import com.omarquea.hostal.modelo.Reserva;
import com.omarquea.hostal.modelo.TipoHabitacion;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author omarquea
 */
/**
 * Reglas de negocio del hostal: alta de habitaciones, registro de reservas
 * y check-out. A proposito no conoce Scanner ni System.out: la entrada/salida
 * por consola es responsabilidad exclusiva de la capa ui.MenuConsola.
 */
public class GestorHospedaje {

    // Uso de listas dinamicas encapsuladas: solo esta clase puede modificarlas
    private final List<Habitacion> habitaciones = new ArrayList<>();
    private final List<Reserva> reservas = new ArrayList<>();

    public GestorHospedaje() {
        inicializarHabitaciones();
    }

    private void inicializarHabitaciones() {
        habitaciones.add(new Habitacion(101, TipoHabitacion.SIMPLE));
        habitaciones.add(new Habitacion(102, TipoHabitacion.DOBLE));
        habitaciones.add(new Habitacion(103, TipoHabitacion.MATRIMONIAL));
        habitaciones.add(new Habitacion(104, TipoHabitacion.SIMPLE));
    }

    // Estructura repetitiva (for-each) para filtrar solo las habitaciones libres
    public List<Habitacion> getHabitacionesDisponibles() {
        List<Habitacion> disponibles = new ArrayList<>();
        for (Habitacion habitacion : habitaciones) {
            if (habitacion.estaDisponible()) {
                disponibles.add(habitacion);
            }
        }
        return disponibles;
    }

    public Optional<Habitacion> buscarHabitacion(int numero) {
        for (Habitacion habitacion : habitaciones) {
            if (habitacion.getNumero() == numero) {
                return Optional.of(habitacion);
            }
        }
        return Optional.empty();
    }

    public Reserva registrarReserva(String nombre, String dni, int numeroHabitacion, int dias)
            throws HabitacionNoDisponibleException, EntradaInvalidaException {

        // Manejo de excepciones: validamos la regla de negocio antes de tocar el estado
        if (dias <= 0) {
            throw new EntradaInvalidaException("La cantidad de dias de estadia debe ser mayor a 0.");
        }

        Habitacion habitacion = buscarHabitacion(numeroHabitacion)
                .orElseThrow(() -> new HabitacionNoDisponibleException(
                        String.format("No existe una habitacion con el numero %d.", numeroHabitacion)));

        // Estructura condicional: solo se reserva si la habitacion sigue disponible
        if (!habitacion.estaDisponible()) {
            throw new HabitacionNoDisponibleException(
                    String.format("La habitacion %d ya esta ocupada.", numeroHabitacion));
        }

        habitacion.setEstado(EstadoHabitacion.OCUPADA);
        Reserva reserva = new Reserva(nombre, dni, habitacion, dias);
        reservas.add(reserva);
        return reserva;
    }

    // Estructura repetitiva (for-each) con corte anticipado (break) al encontrar la reserva
    public Reserva realizarCheckOut(int numeroHabitacion) throws ReservaNoEncontradaException {
        Reserva reservaEncontrada = null;
        for (Reserva reserva : reservas) {
            if (reserva.getHabitacion().getNumero() == numeroHabitacion) {
                reservaEncontrada = reserva;
                break;
            }
        }

        if (reservaEncontrada == null) {
            throw new ReservaNoEncontradaException(
                    String.format("No se encontro una reserva activa en la habitacion %d.", numeroHabitacion));
        }

        reservaEncontrada.getHabitacion().setEstado(EstadoHabitacion.DISPONIBLE);
        reservas.remove(reservaEncontrada);
        return reservaEncontrada;
    }
}
