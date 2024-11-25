import java.util.ArrayList;
import java.util.Collections;


public class Pozo {
   private ArrayList<Ficha> fichas;


   public Pozo() {
       fichas = new ArrayList<>();
       inicializarFichas();
   }


   public void inicializarFichas() {
       // Agregar fichas de dominó
       for (int i = 0; i <= 6; i++) {
           for (int j = i; j <= 6; j++) {
               fichas.add(new Ficha(i, j));
           }
       }


       // Agregar fichas de tridominó
       for (int i = 0; i <= 6; i++) {
           for (int j = i; j <= 6; j++) {
               for (int k = j; k <= 6; k++) {
                   fichas.add(new FichaTridomino(i, j, k));
               }
           }
       }


       Collections.shuffle(fichas);  // Mezclar las fichas
   }


   public Ficha obtenerFicha() {
       return !fichas.isEmpty() ? fichas.remove(0) : null;
   }


   public boolean estaVacio() {
       return fichas.isEmpty();
   }
}
