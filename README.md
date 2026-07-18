# TF_Grupo2 — Sistema de Gestión de Hospedaje

Aplicación de consola en Java que simula la recepción de un hostal: permite
registrar el check-in de un huésped, ver qué habitaciones están libres y
hacer el check-out cuando el huésped se va.

Este README está pensado para alguien que recién abre el proyecto y no
sabe por dónde arrancar a leerlo. Vas a encontrar: cómo correrlo, cómo está
organizado, y un recorrido paso a paso de qué hace el código cuando lo usás.

---

## 1. ¿Qué hace el programa?

Es un menú de consola con 4 opciones:

```
=== RECEPCION - HOSTAL ===
1. Registrar nueva reserva (Check-in)
2. Ver habitaciones disponibles
3. Finalizar estadia (Check-out)
4. Cerrar sistema
```

- **Opción 1**: pide nombre, DNI, número de habitación y días de estadía,
  y si la habitación existe y está libre, registra la reserva y la ocupa.
- **Opción 2**: lista las habitaciones que están libres en ese momento.
- **Opción 3**: libera una habitación y muestra el "ticket" con el total a
  pagar (días × precio por noche).
- **Opción 4**: cierra el programa.

El hostal arranca siempre con 4 habitaciones precargadas: 101 (Simple),
102 (Doble), 103 (Matrimonial) y 104 (Simple).

---

## 2. Cómo correrlo

### Desde NetBeans
1. `File → Open Project` y seleccionar la carpeta `TF_Grupo2` (la que tiene
   el `pom.xml`). NetBeans la reconoce sola como proyecto Maven.
2. Click derecho sobre el proyecto → **Run**.

### Desde la terminal (si tenés Maven instalado)
```bash
mvn compile exec:java
```

### Generar un .jar ejecutable
```bash
mvn package
java -jar target/TF_Grupo2.jar
```

Requiere **Java 21** (está fijado en el `pom.xml` con `maven.compiler.release`).

---

## 3. Mapa del proyecto (dónde está cada cosa)

```
src/main/java/com/omarquea/hostal/
│
├── App.java                          → arranca el programa (main)
│
├── modelo/                           → los "objetos" del negocio (datos + reglas propias)
│   ├── TipoHabitacion.java           → enum: Simple / Doble / Matrimonial + precio base
│   ├── EstadoHabitacion.java         → enum: Disponible / Ocupada
│   ├── Habitacion.java               → una habitacion del hostal
│   └── Reserva.java                  → una reserva hecha por un huesped
│
├── excepcion/                        → errores propios del negocio (no genéricos de Java)
│   ├── HabitacionNoDisponibleException.java
│   ├── ReservaNoEncontradaException.java
│   └── EntradaInvalidaException.java
│
├── servicio/                         → la "lógica" del hostal
│   └── GestorHospedaje.java          → registra reservas, hace checkout, busca habitaciones
│
└── ui/                                → todo lo que habla con la persona que usa la consola
    └── MenuConsola.java              → imprime el menú, lee el teclado, muestra resultados
```

**Por qué está separado así:** cada carpeta (paquete) tiene una única
responsabilidad. `modelo` no sabe que existe una consola. `servicio` no
sabe que existe un `Scanner` ni un `System.out`. `ui` es la única que
habla con la persona. Esto se llama **separación de responsabilidades**:
si mañana quisiéramos una versión con ventanas (Swing) en vez de consola,
solo habría que reescribir `ui/`, todo lo demás queda igual.

---

## 4. El recorrido completo: qué pasa cuando ejecutás el programa

```
App.main()
   │
   ▼
new MenuConsola().iniciar()
   │
   ▼
while (!salir) {
   mostrarMenu()              → imprime las 4 opciones
   leerEntero(...)             → lee la opción, reintenta si no es un número
   switch (opcion) { ... }     → deriva a uno de los 4 métodos privados
}
```

Cada opción del switch llama a un método privado de `MenuConsola`, y ese
método le pide el trabajo pesado a `GestorHospedaje` (que vive en
`servicio/`). `MenuConsola` solo junta los datos y muestra el resultado;
**no valida nada de negocio por su cuenta** — esa responsabilidad es
100% de `GestorHospedaje`.

### Ejemplo real: registrar una reserva (Opción 1)

```
Usuario elige "1"
   │
   ▼
MenuConsola.registrarReserva()
   │  pide: nombre, DNI, numeroHabitacion, dias (con leerTexto/leerEntero)
   ▼
gestor.registrarReserva(nombre, dni, numeroHabitacion, dias)
   │
   ├─ ¿dias <= 0? ────────────────► throw EntradaInvalidaException
   │
   ├─ buscarHabitacion(numeroHabitacion)
   │     recorre la lista de habitaciones (for-each) buscando el número
   │     si no la encuentra ───────► throw HabitacionNoDisponibleException
   │
   ├─ ¿la habitacion ya esta OCUPADA? ► throw HabitacionNoDisponibleException
   │
   └─ todo OK:
        habitacion.setEstado(OCUPADA)
        crea un new Reserva(...)
        la guarda en la lista de reservas
        devuelve la Reserva a MenuConsola
   │
   ▼
MenuConsola imprime la Reserva (usa su toString(), formateado con String.format)
```

Si en cualquier paso se lanza una excepción, `MenuConsola` la atrapa en su
`try/catch` y muestra `Error: <mensaje>` sin cortar el programa — el menú
vuelve a aparecer y se puede seguir usando.

### Los otros dos flujos son más cortos

- **Ver disponibles (opción 2):** `MenuConsola` le pide a `GestorHospedaje`
  la lista de habitaciones libres (`getHabitacionesDisponibles()`, que
  filtra con un for-each) y las imprime una por una.
- **Check-out (opción 3):** `GestorHospedaje.realizarCheckOut(numero)`
  busca la reserva activa de esa habitación, si no existe lanza
  `ReservaNoEncontradaException`; si existe, libera la habitación
  (`estado = DISPONIBLE`), quita la reserva de la lista y la devuelve para
  que `MenuConsola` imprima el ticket.

---

## 5. Clase por clase

### `modelo/TipoHabitacion.java` (enum)
Reemplaza lo que antes hubiera sido un `String tipo = "Simple"`. Cada
constante (`SIMPLE`, `DOBLE`, `MATRIMONIAL`) trae ya su `precioBase`
adentro. Ventaja sobre usar Strings sueltos: es imposible escribir mal
un tipo (el compilador no te deja poner `TipoHabitacion.SIMPLE0`), y el
precio por defecto vive en un solo lugar.

### `modelo/EstadoHabitacion.java` (enum)
Lo mismo pero para el estado: `DISPONIBLE` / `OCUPADA`. Antes se comparaba
con `.equals("Disponible")`, que se rompe si alguien tipea mal el string;
con enum, el compilador directamente no compila si escribís mal el nombre.

### `modelo/Habitacion.java`
Representa una habitación física. Datos: número, tipo, precio por noche y
estado actual. Tiene **dos constructores sobrecargados**:
- Uno completo, donde vos decidís el precio (por si querés poner una
  promoción distinta a la tarifa base).
- Uno corto, que usa automáticamente el precio base del `TipoHabitacion`.

También sobreescribe `toString()` para que al hacer `System.out.println(habitacion)`
se imprima ya formateado con `String.format` (columnas alineadas), en vez
de concatenar strings a mano.

### `modelo/Reserva.java`
Representa la reserva de un huésped sobre una `Habitacion` durante N días.
Calcula el total con `calcularTotal()` (`dias * precioPorNoche`) y también
sobreescribe `toString()` para imprimir el ticket formateado.

### `excepcion/*.java`
Tres excepciones "checked" (heredan de `Exception`, no de `RuntimeException`),
a propósito: eso obliga a quien llama a esos métodos a manejarlas con un
`try/catch` explícito, en vez de que el error pase desapercibido.

| Excepción | Cuándo se lanza |
|---|---|
| `HabitacionNoDisponibleException` | La habitación no existe o ya está ocupada |
| `ReservaNoEncontradaException` | Se quiere hacer check-out de una habitación sin reserva activa |
| `EntradaInvalidaException` | Un dato de negocio es inválido (ej. días ≤ 0) |

### `servicio/GestorHospedaje.java`
El "cerebro" del sistema. Guarda dos listas (`habitaciones`, `reservas`)
y expone métodos para:
- `getHabitacionesDisponibles()` — filtra las libres.
- `buscarHabitacion(numero)` — devuelve un `Optional<Habitacion>` (en vez
  de `null`, para no arriesgarse a un `NullPointerException` si alguien
  se olvida de chequear).
- `registrarReserva(...)` — valida y crea la reserva, o lanza excepción.
- `realizarCheckOut(...)` — libera la habitación, o lanza excepción.

No tiene ni un solo `System.out` ni `Scanner`: se podría probar con tests
unitarios sin tocar la consola para nada.

### `ui/MenuConsola.java`
La única clase que lee el teclado (`Scanner`) e imprime en pantalla. Tiene
dos métodos "helper" importantes:
- `leerEntero(mensaje)`: pide un número y **reintenta en un loop** si el
  usuario tipea algo que no es un entero, sin perder los otros datos que
  ya se habían cargado en la misma operación.
- `leerTexto(mensaje)`: pide una línea de texto simple (nombre, DNI).

### `App.java`
Dos líneas: crea un `MenuConsola` y lo arranca. Es el único punto de
entrada (`main`), y no tiene ninguna lógica propia — a propósito, para
que sea trivial de ubicar.

---

## 6. Conceptos de POO que vas a poder señalar en la exposición

| Concepto | Dónde se ve |
|---|---|
| Encapsulamiento | Atributos `private` en `Habitacion` y `Reserva`, accesibles solo por getters/setters |
| Sobrecarga (overloading) | Los dos constructores de `Habitacion` |
| Sobreescritura (overriding) | `toString()` redefinido en `Habitacion` y `Reserva` |
| Enumeraciones | `TipoHabitacion` y `EstadoHabitacion`, en vez de Strings sueltos |
| Manejo de excepciones | Las 3 clases de `excepcion/` + los `try/catch` en `MenuConsola` |
| Estructuras repetitivas | `while` (loop del menú y de `leerEntero`), `for-each` (recorrer habitaciones/reservas) |
| Estructuras condicionales | `switch` del menú, `if` de validaciones en `GestorHospedaje` |
| Colecciones | `ArrayList<Habitacion>` y `ArrayList<Reserva>` dentro de `GestorHospedaje` |

---

## 7. Estado del proyecto / pendientes

- El código funcional está completo y probado manualmente (check-in,
  listado, check-out, errores de formato y de negocio).
- **Pendiente**: tests unitarios (opcional) e informe técnico del curso.
- El proyecto Ant/NetBeans viejo (previo a la migración a Maven) quedó
  guardado como referencia en otra carpeta, sin usarse.
