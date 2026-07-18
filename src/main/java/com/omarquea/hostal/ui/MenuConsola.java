/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.omarquea.hostal.ui;

import com.omarquea.hostal.excepcion.EntradaInvalidaException;
import com.omarquea.hostal.excepcion.HabitacionNoDisponibleException;
import com.omarquea.hostal.excepcion.ReservaNoEncontradaException;
import com.omarquea.hostal.modelo.Habitacion;
import com.omarquea.hostal.modelo.Reserva;
import com.omarquea.hostal.servicio.GestorHospedaje;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author omarquea
 */
/**
 * Toda la interaccion por consola vive aca: el GestorHospedaje ni se entera
 * de que existe un Scanner. Cada dato se lee por separado con su propio
 * reintento, asi un error de formato en un campo no descarta los anteriores.
 */
public class MenuConsola {

    private final Scanner scanner = new Scanner(System.in);
    private final GestorHospedaje gestor = new GestorHospedaje();

    public void iniciar() {
        boolean salir = false;

        // Estructura repetitiva (while) para mantener el programa activo
        while (!salir) {
            mostrarMenu();
            int opcion = leerEntero("Seleccione una opcion: ");

            // Estructura condicional (switch)
            switch (opcion) {
                case 1 -> registrarReserva();
                case 2 -> mostrarHabitacionesDisponibles();
                case 3 -> realizarCheckOut();
                case 4 -> salir = true;
                default -> System.out.println("Atencion: seleccione una opcion del 1 al 4.");
            }
        }

        System.out.println("Cerrando turno. Buen trabajo!");
        scanner.close();
    }

    private void mostrarMenu() {
        System.out.println("\n=== RECEPCION - HOSTAL ===");
        System.out.println("1. Registrar nueva reserva (Check-in)");
        System.out.println("2. Ver habitaciones disponibles");
        System.out.println("3. Finalizar estadia (Check-out)");
        System.out.println("4. Cerrar sistema");
    }

    private void registrarReserva() {
        System.out.println("\n--- NUEVA RESERVA ---");
        String nombre = leerTexto("Ingrese nombre del huesped: ");
        String dni = leerTexto("Ingrese DNI: ");
        int numeroHabitacion = leerEntero("Ingrese el numero de habitacion a reservar: ");
        int dias = leerEntero("Ingrese cantidad de dias de estadia: ");

        // Manejo de excepciones: el gestor valida la reserva y avisa que salio mal
        try {
            Reserva reserva = gestor.registrarReserva(nombre, dni, numeroHabitacion, dias);
            System.out.println("\n*** Reserva registrada con exito ***");
            System.out.println(reserva);
        } catch (HabitacionNoDisponibleException | EntradaInvalidaException e) {
            System.out.printf("%nError: %s%n", e.getMessage());
        }
    }

    private void mostrarHabitacionesDisponibles() {
        System.out.println("\n--- HABITACIONES DISPONIBLES ---");
        List<Habitacion> disponibles = gestor.getHabitacionesDisponibles();

        if (disponibles.isEmpty()) {
            System.out.println("Lo sentimos, el hostal esta completamente lleno.");
            return;
        }

        for (Habitacion habitacion : disponibles) {
            System.out.println(habitacion);
        }
    }

    private void realizarCheckOut() {
        System.out.println("\n--- CHECK-OUT ---");
        int numeroHabitacion = leerEntero("Ingrese el numero de habitacion a liberar: ");

        // Manejo de excepciones
        try {
            Reserva reserva = gestor.realizarCheckOut(numeroHabitacion);
            System.out.println("\n--- TICKET DE CHECK-OUT ---");
            System.out.println(reserva);
            System.out.printf("La habitacion %d ha sido liberada.%n", numeroHabitacion);
        } catch (ReservaNoEncontradaException e) {
            System.out.printf("%nError: %s%n", e.getMessage());
        }
    }

    // Estructura repetitiva (while) + manejo de excepciones: reintenta hasta
    // recibir un numero valido, sin perder los datos ya cargados en los otros campos
    private int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                int valor = scanner.nextInt();
                scanner.nextLine();
                return valor;
            } catch (InputMismatchException e) {
                System.out.println("Error de formato: ingrese unicamente numeros enteros.");
                scanner.nextLine();
            }
        }
    }

    private String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine();
    }
}
