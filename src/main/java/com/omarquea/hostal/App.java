/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.omarquea.hostal;

import com.omarquea.hostal.ui.MenuConsola;

/**
 *
 * @author omarquea
 */
/**
 * Punto de entrada del sistema de gestion de hospedaje. Solo arranca el menu;
 * toda la logica real vive en las capas modelo, servicio y ui.
 */
public class App {

    public static void main(String[] args) {
        new MenuConsola().iniciar();
    }
}
