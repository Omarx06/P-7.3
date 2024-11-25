import java.util.LinkedList;


public class Tablero {
   private LinkedList<Ficha> fichas;
   private LinkedList<String> orientaciones;


   public Tablero() {
       fichas = new LinkedList<>();
       orientaciones = new LinkedList<>();
   }


   public void colocarFicha(Ficha ficha, String orientacion, int valorConectar) {
       // Ajustar la orientación de la ficha sin modificar la original
       Ficha fichaAjustada = ajustarFichaParaConexion(ficha, valorConectar);


       if ("vertical".equals(orientacion)) {
           fichas.addFirst(fichaAjustada);  // Colocar el domino verticalmente
           orientaciones.addFirst("vertical");
       } else if ("normal".equals(orientacion)) {
           fichas.addLast(fichaAjustada);   // Colocar el tridomino normalmente
           orientaciones.addLast("normal");
       }
   }


   private Ficha ajustarFichaParaConexion(Ficha ficha, int valorConectar) {
       if (ficha instanceof Ficha) {
           Ficha domino = (Ficha) ficha;
           if (domino.getLado1() != valorConectar) {
               domino.rotateRight(); // Rotar para conectar
           }
           return domino;
       }


       if (ficha instanceof FichaTridomino) {
           FichaTridomino tridomino = (FichaTridomino) ficha;
           if (tridomino.getLado1() != valorConectar) {
               tridomino.rotateRight(); // Rotar para conectar
           }
           return tridomino;
       }


       return ficha; // Retornar la ficha sin cambios si no se necesita ajuste
   }


   public void mostrarFichas() {
       System.out.println("\nTablero actual:");
       for (int i = 0; i < fichas.size(); i++) {
           Ficha ficha = fichas.get(i);
           String orientacion = orientaciones.get(i);


           if (ficha instanceof FichaTridomino) {
               System.out.println(((FichaTridomino) ficha).dibujarFicha(orientacion));
           } else {
               System.out.println(ficha.dibujarFicha(orientacion));
           }
       }
   }


   public Ficha obtenerUltimaFicha() {
       return fichas.isEmpty() ? null : fichas.getLast();
   }
}
