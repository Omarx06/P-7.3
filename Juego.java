import java.util.LinkedList;
import java.util.Scanner;


public class Juego {
   private Pozo pozo;
   private Tablero tablero;
   private Jugador jugador1;
   private Jugador jugador2;
   private Jugador jugadorActual;


   public Juego() {
       pozo = new Pozo();
       tablero = new Tablero();
   }


   public void iniciarJuego() {
       Scanner scanner = new Scanner(System.in);


       // Pedir los nombres de los jugadores
       System.out.print("Introduce el nombre del Jugador 1: ");
       jugador1 = new Jugador(scanner.nextLine());


       System.out.print("Introduce el nombre del Jugador 2: ");
       jugador2 = new Jugador(scanner.nextLine());


       // Inicializar el pozo y repartir fichas
       jugador1.repartirFichasIniciales(pozo);
       jugador2.repartirFichasIniciales(pozo);


       // Mostrar las fichas iniciales
       mostrarFichas(jugador1);
       mostrarFichas(jugador2);


       // Selección de fichas iniciales y determinar el jugador inicial
       determinarPrimerJugador(scanner);


       // Bucle principal del juego
       while (!pozo.estaVacio() && !jugador1.getFichas().isEmpty() && !jugador2.getFichas().isEmpty()) {
           realizarTurno(scanner);
           cambiarTurno();
       }


       // Determinar el ganador al final
       determinarGanador();
       scanner.close();
   }


   private void mostrarFichas(Jugador jugador) {
       System.out.println("\nFichas de " + jugador.getNombre() + ":");
       for (int i = 0; i < jugador.getFichas().size(); i++) {
           System.out.println((i + 1) + ". " + jugador.getFichas().get(i));
       }
   }


   private void mostrarTablero() {
       System.out.println("\nEstado del tablero:");
       tablero.mostrarFichas();
   }


   private void determinarPrimerJugador(Scanner scanner) {
       // Determinar las dos primeras fichas de ambos jugadores
       System.out.println("\n" + jugador1.getNombre() + ", selecciona una ficha para jugar:");
       mostrarFichas(jugador1);
       int seleccion1 = scanner.nextInt() - 1;
       Ficha ficha1 = jugador1.getFichas().remove(seleccion1);


       System.out.println("\n" + jugador2.getNombre() + ", selecciona una ficha para jugar:");
       mostrarFichas(jugador2);
       int seleccion2 = scanner.nextInt() - 1;
       Ficha ficha2 = jugador2.getFichas().remove(seleccion2);


       // Determinar el primer jugador según la suma de puntos
       int puntosJugador1 = sumarPuntos(ficha1);
       int puntosJugador2 = sumarPuntos(ficha2);


       // Determinar quién comienza según la suma de puntos
       if (puntosJugador1 > puntosJugador2) {
           // Jugador 1 comienza
           System.out.println(jugador1.getNombre() + " comienza con la ficha: " + ficha1);


           // Determinar cómo colocar la ficha del jugador 1
           if (ficha1 instanceof FichaTridomino) {
               // Si es tridomino, colocar normal
               tablero.colocarFicha(ficha1, "normal", -1);
           } else {
               // Si es domino, colocar vertical
               tablero.colocarFicha(ficha1, "vertical", -1);
           }


           jugadorActual = jugador1;
           // El jugador 2 mantiene su ficha en la mano
           jugador2.getFichas().add(ficha2);
       } else {
           // Jugador 2 comienza
           System.out.println(jugador2.getNombre() + " comienza con la ficha: " + ficha2);


           // Determinar cómo colocar la ficha del jugador 2
           if (ficha2 instanceof FichaTridomino) {
               // Si es tridomino, colocar normal
               tablero.colocarFicha(ficha2, "normal", -1);
           } else {
               // Si es domino, colocar vertical
               tablero.colocarFicha(ficha2, "vertical", -1);
           }


           jugadorActual = jugador2;
           // El jugador 1 mantiene su ficha en la mano
           jugador1.getFichas().add(ficha1);
       }
   }


   private void realizarTurno(Scanner scanner) {
       System.out.println("\nTurno de " + jugadorActual.getNombre());
       mostrarTablero();
       mostrarFichas(jugadorActual);


       // Obtener el valor de la última ficha colocada en el tablero
       Ficha ultimaFicha = tablero.obtenerUltimaFicha();
       int valorConectar = (ultimaFicha != null) ? ultimaFicha.getLado2() : -1;


       // Obtener las fichas jugables del jugador
       LinkedList<Ficha> fichasJugables = obtenerFichasJugables(jugadorActual, valorConectar);


       // Si no tiene fichas jugables, le damos la opción de tomar 2 fichas del pozo
       if (fichasJugables.isEmpty()) {
           System.out.println("No tienes fichas jugables. ¿Quieres tomar 2 fichas del pozo? (p para tomar)");
           String opcion = scanner.next();
           if (opcion.equalsIgnoreCase("p")) {
               jugadorActual.tomarFichas(pozo, 2);  // Tomamos 2 fichas del pozo
               fichasJugables = obtenerFichasJugables(jugadorActual, valorConectar);  // Recalculamos las fichas jugables después de tomar las fichas


               // Si después de tomar las fichas sigue sin poder jugar, pierde el turno
               if (fichasJugables.isEmpty()) {
                   System.out.println("Aún no puedes jugar. Pierdes el turno.");
                   return;
               }
           }
       }


       // Mostrar las fichas jugables
       System.out.println("\nFichas jugables:");
       for (int i = 0; i < fichasJugables.size(); i++) {
           System.out.println((i + 1) + ". " + fichasJugables.get(i));
       }


       // Pedir al jugador que seleccione una ficha
       System.out.print("Selecciona la ficha que deseas jugar (1-" + fichasJugables.size() + "): ");
       int seleccion = scanner.nextInt() - 1;
       Ficha fichaSeleccionada = fichasJugables.get(seleccion);


       // Hacer una copia de la ficha seleccionada para evitar modificar la original
       Ficha fichaParaColocar = copiarFicha(fichaSeleccionada);


       // Remover la ficha seleccionada de la mano del jugador
       jugadorActual.getFichas().remove(fichaSeleccionada);


       // Realizamos la rotación en la copia de la ficha según la orientación
       fichaParaColocar = ajustarFichaParaConexion(fichaParaColocar, valorConectar);


       // Colocar la ficha en el tablero con la orientación correcta
       tablero.colocarFicha(fichaParaColocar, "normal", valorConectar);


       // Sumar los puntos por la ficha colocada
       jugadorActual.sumarPuntos(sumarPuntos(fichaParaColocar));
       System.out.println("Colocaste la ficha: " + fichaParaColocar);
       System.out.println("Puntos actuales: " + jugadorActual.getPuntos());
   }


   // Método para copiar una ficha
   private Ficha copiarFicha(Ficha ficha) {
       if (ficha instanceof FichaTridomino) {
           FichaTridomino tridomino = (FichaTridomino) ficha;
           return new FichaTridomino(tridomino.getLado1(), tridomino.getLado2(), tridomino.getLado3());
       } else if (ficha instanceof Ficha) {
           Ficha domino = (Ficha) ficha;
           return new Ficha(domino.getLado1(), domino.getLado2());
       }
       return null; // Si es un tipo de ficha no esperado, no hacemos nada
   }


   // Método para ajustar la ficha y conectar con el tablero (rotaciones si es necesario)
   private Ficha ajustarFichaParaConexion(Ficha ficha, int valorConectar) {
       // Verificamos si es una ficha de dominó o tridomino
       if (ficha instanceof Ficha) {
           Ficha domino = (Ficha) ficha;


           // Intentamos conectar con el lado1 del dominó
           if (domino.getLado1() == valorConectar) {
               return domino;  // Si el lado1 conecta, no necesitamos rotar
           }


           // Intentamos conectar con el lado2 del dominó
           if (domino.getLado2() == valorConectar) {
               domino.rotateRight();  // Rotamos para que el lado2 pase a ser el lado1
               return domino;
           }


           // Si ningún lado conecta, rotamos para intercambiar los lados
           domino.rotateRight();  // Rotación para intercambiar los valores
           return domino;  // Retornamos la ficha ajustada
       }


       // Si es una ficha de tridomino
       if (ficha instanceof FichaTridomino) {
           FichaTridomino tridomino = (FichaTridomino) ficha;


           // Intentamos conectar con el lado1
           if (tridomino.getLado1() == valorConectar) {
               return tridomino;  // No rotamos si ya conecta
           }


           // Intentamos conectar con el lado2
           if (tridomino.getLado2() == valorConectar) {
               tridomino.rotateRight();  // Rotamos para colocar lado2 en la posición correcta
               return tridomino;
           }


           // Intentamos conectar con el lado3
           if (tridomino.getLado3() == valorConectar) {
               tridomino.rotateRight();  // Rotamos una vez
               tridomino.rotateRight();  // Rotamos nuevamente para poner lado3 en la posición correcta
               return tridomino;
           }


           // Si ninguno de los tres lados conecta, rotamos varias veces para conectar
           tridomino.rotateRight();  // Primera rotación
           tridomino.rotateRight();  // Segunda rotación
           return tridomino;  // Devolvemos la ficha ajustada
       }


       return ficha;  // Si no es una ficha de dominó ni de tridomino, devolvemos la ficha original
   }


   private int sumarPuntos(Ficha ficha) {
       // Si la ficha es un tridomino, sumamos los tres lados
       if (ficha instanceof FichaTridomino) {
           FichaTridomino tridomino = (FichaTridomino) ficha;
           return tridomino.getLado1() + tridomino.getLado2() + tridomino.getLado3();
       }
       // Si es un domino, sumamos los dos lados
       return ficha.getLado1() + ficha.getLado2();
   }


   private LinkedList<Ficha> obtenerFichasJugables(Jugador jugador, int valorRequerido) {
       LinkedList<Ficha> jugables = new LinkedList<>();


       // Verificamos si hay un solo valor en el tablero (valorRequerido)
       if (valorRequerido != -1) {
           for (Ficha ficha : jugador.getFichas()) {
               if (esFichaValida(ficha, valorRequerido)) {
                   jugables.add(ficha);
               }
           }
       } else {
           // Si no hay un valor requerido (el tablero está vacío), podemos agregar todas las fichas
           for (Ficha ficha : jugador.getFichas()) {
               jugables.add(ficha);
           }
       }
       return jugables;
   }


   private boolean esFichaValida(Ficha ficha, int valorConectar) {
       if (ficha instanceof FichaTridomino) {
           FichaTridomino tridomino = (FichaTridomino) ficha;
           // Verificamos si alguno de los lados del tridomino se conecta con el valor requerido
           return tridomino.getLado1() == valorConectar ||
                   tridomino.getLado2() == valorConectar ||
                   tridomino.getLado3() == valorConectar;
       }


       // Para las fichas de domino, verificamos ambos lados
       return ficha.getLado1() == valorConectar || ficha.getLado2() == valorConectar;
   }


   private void cambiarTurno() {
       jugadorActual = (jugadorActual == jugador1) ? jugador2 : jugador1;
   }


   private void determinarGanador() {
       int puntosJ1 = jugador1.getPuntos();
       int puntosJ2 = jugador2.getPuntos();


       if (puntosJ1 > puntosJ2) {
           System.out.println("¡Ganador: " + jugador1.getNombre() + " con " + puntosJ1 + " puntos!");
       } else if (puntosJ2 > puntosJ1) {
           System.out.println("¡Ganador: " + jugador2.getNombre() + " con " + puntosJ2 + " puntos!");
       } else {
           System.out.println("¡Es un empate con " + puntosJ1 + " puntos cada uno!");
       }
   }
}
