# Explicación: `GestorHospedaje.java` (versión original)

Este documento explica qué hace, método por método, la clase
`Negocio.GestorHospedaje` tal como estaba en el proyecto original (antes de
la reorganización a `modelo/excepcion/servicio/ui`). Sirve como referencia
para el informe técnico, para poder comparar "cómo era antes" contra la
versión actual del proyecto.

Código completo de referencia: `legacy-netbeans-ant/src/main/java/Negocio/GestorHospedaje.java`
(en el proyecto viejo).

---

## Qué es esta clase

Es el "cerebro" del sistema: la única clase que sabe manejar la lista de
habitaciones y la lista de reservas del hostal. Todo lo que el usuario
puede hacer desde el menú (ver habitaciones, reservar, hacer check-out)
termina llamando a un método de esta clase.

```java
public class GestorHospedaje {
    private ArrayList<Habitacion> listaHabitaciones;
    private ArrayList<Reserva> listaReservas;
```

Guarda dos listas dinámicas (`ArrayList`) como atributos **privados**: nadie
fuera de la clase puede tocarlas directamente, solo a través de los métodos
públicos que ella misma expone. Eso es encapsulamiento.

---

## Constructor + `inicializarHabitaciones()`

```java
public GestorHospedaje() {
    listaHabitaciones = new ArrayList<>();
    listaReservas = new ArrayList<>();
    inicializarHabitaciones();
}

private void inicializarHabitaciones() {
    listaHabitaciones.add(new Habitacion(101, "Simple", 50.0));
    listaHabitaciones.add(new Habitacion(102, "Doble", 80.0));
    listaHabitaciones.add(new Habitacion(103, "Matrimonial", 120.0));
    listaHabitaciones.add(new Habitacion(104));
}
```

Apenas se crea un `GestorHospedaje`, arranca con 4 habitaciones ya
cargadas "de fábrica" (nadie las tipea, están fijas en el código).

Las primeras 3 usan el **constructor completo** de `Habitacion`
(número, tipo, precio). La habitación 104 usa el **constructor
sobrecargado** que solo pide el número — internamente `Habitacion` le
pone un tipo y precio por defecto ("Simple", 50.0). Este es el ejemplo de
**sobrecarga de constructores (overloading)** del proyecto.

---

## `mostrarHabitacionesDisponibles()`

```java
public void mostrarHabitacionesDisponibles() {
    System.out.println("\n--- HABITACIONES DISPONIBLES ---");
    boolean hayDisponibles = false;

    for (Habitacion hab : listaHabitaciones) {
        if (hab.getEstado().equals("Disponible")) {
            System.out.println(hab.toString());
            hayDisponibles = true;
        }
    }

    if (!hayDisponibles) {
        System.out.println("Lo sentimos, el hostal esta completamente lleno.");
    }
}
```

Recorre **todas** las habitaciones con un `for-each` (estructura
repetitiva) y va imprimiendo solo las que tienen el estado `"Disponible"`
(comparado como texto, con `.equals()`). La bandera `hayDisponibles` sirve
para saber, al terminar el recorrido, si no se imprimió ninguna — en ese
caso avisa que el hostal está lleno.

No devuelve nada (`void`): imprime directamente por consola. La lógica de
"cuáles están libres" y la impresión están mezcladas en el mismo método.

---

## `buscarHabitacion(int numero)`

```java
public Habitacion buscarHabitacion(int numero) {
    for (Habitacion hab : listaHabitaciones) {
        if (hab.getNumero() == numero) {
            return hab;
        }
    }
    return null;
}
```

Recorre la lista buscando la habitación cuyo número coincida. Si la
encuentra, corta el recorrido devolviéndola (`return` dentro del `for`
termina el método al toque). Si llega al final sin encontrarla, devuelve
`null`. Este método lo usan internamente `registrarReserva()` y (de forma
indirecta) el resto de la clase para ubicar una habitación por número.

---

## `registrarReserva(...)`

```java
public void registrarReserva(String nombre, String dni, int numeroHab, int dias) {
    Habitacion hab = buscarHabitacion(numeroHab);

    if (hab != null && hab.getEstado().equals("Disponible")) {
        hab.setEstado("Ocupada");
        Reserva nuevaReserva = new Reserva(nombre, dni, hab, dias);
        listaReservas.add(nuevaReserva);

        System.out.println("\n*** Reserva registrada con exito ***");
        System.out.println(nuevaReserva.toString());
    } else {
        System.out.println("\nError: La habitacion no existe o ya esta ocupada.");
    }
}
```

Paso a paso:
1. Busca la habitación por número con `buscarHabitacion`.
2. **Condición doble** (`if` con `&&`): solo sigue si la habitación existe
   (`hab != null`) **y** está disponible. Si `hab` fuera `null`, Java ni
   siquiera evalúa `hab.getEstado()` gracias al cortocircuito de `&&` (si
   evaluara igual, tiraría `NullPointerException`).
3. Si pasa la condición: marca la habitación como `"Ocupada"`, crea una
   `Reserva` nueva, la agrega a `listaReservas` y muestra un mensaje de
   éxito con el detalle de la reserva (usando el `toString()` de `Reserva`).
4. Si no pasa la condición (no existe o ya está ocupada): imprime un único
   mensaje de error genérico — no distingue si el problema fue "no existe"
   o "ya está ocupada", ambos casos caen en el mismo `else`.

No hay validación de `dias` (podría reservarse con `dias = -5` sin que nada
lo impida) ni manejo de excepciones: los errores se resuelven imprimiendo
un mensaje y devolviendo `void`, no lanzando una excepción.

---

## `realizarCheckOut(int numeroHab)`

```java
public void realizarCheckOut(int numeroHab) {
    Reserva reservaEncontrada = null;

    for (Reserva res : listaReservas) {
        if (res.getHabitacion().getNumero() == numeroHab) {
            reservaEncontrada = res;
            break;
        }
    }

    if (reservaEncontrada != null) {
        reservaEncontrada.getHabitacion().setEstado("Disponible");

        System.out.println("\n--- TICKET DE CHECK-OUT ---");
        System.out.println(reservaEncontrada.toString());
        System.out.println("La habitacion " + numeroHab + " ha sido liberada.");

        listaReservas.remove(reservaEncontrada);
    } else {
        System.out.println("\nError: No se encontro una reserva activa en la habitacion " + numeroHab + ".");
    }
}
```

Paso a paso:
1. Recorre `listaReservas` buscando la reserva cuya habitación tenga el
   número pedido. En cuanto la encuentra, guarda la referencia y corta el
   loop con `break` (no hace falta seguir recorriendo el resto).
2. Si encontró una reserva: libera la habitación (`setEstado("Disponible")`),
   imprime el ticket de check-out (usando `Reserva.toString()`, que ya
   incluye el cálculo del total a pagar) y saca esa reserva de la lista
   con `listaReservas.remove(...)`.
3. Si no encontró ninguna reserva activa en esa habitación, imprime un
   mensaje de error armado con concatenación de Strings (`"..." + numeroHab + "..."`).

---

## Resumen de lo que hace la clase

| Método | Qué hace | Devuelve |
|---|---|---|
| `GestorHospedaje()` | Crea las listas vacías y precarga 4 habitaciones | — |
| `mostrarHabitacionesDisponibles()` | Imprime las habitaciones con estado "Disponible" | `void` (imprime) |
| `buscarHabitacion(numero)` | Busca una habitación por número | `Habitacion` o `null` |
| `registrarReserva(...)` | Valida y registra una reserva, ocupa la habitación | `void` (imprime) |
| `realizarCheckOut(numeroHab)` | Libera una habitación y cierra su reserva activa | `void` (imprime) |

Los cuatro métodos públicos combinan **lógica de negocio** (buscar,
validar, calcular) con **presentación** (`System.out.println` directo
adentro de la misma clase), y el estado de una habitación se maneja como
texto libre (`"Disponible"` / `"Ocupada"`) en vez de un tipo controlado
por el compilador — son justamente los dos puntos que la versión actual
del proyecto (paquetes `modelo/servicio/ui`, enums `EstadoHabitacion`) vino
a resolver.
