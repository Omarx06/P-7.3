import java.util.ArrayList;


public class Jugador {
   private String nombre;
   private ArrayList<Ficha> fichas;
   private int puntos;


   public Jugador(String nombre) {
       this.nombre = nombre;
       this.fichas = new ArrayList<>();
       this.puntos = 0;
   }


   public void repartirFichasIniciales(Pozo pozo) {
       tomarFichas(pozo, 10);
   }


   public void tomarFichas(Pozo pozo, int cantidad) {
       for (int i = 0; i < cantidad; i++) {
           Ficha ficha = pozo.obtenerFicha();
           if (ficha != null) {
               fichas.add(ficha);
           } else {
               System.out.println("El pozo está vacío. No se pueden tomar más fichas.");
               break;
           }
       }
   }


   public ArrayList<Ficha> getFichas() {
       return fichas;
   }


   public int getPuntos() {
       return puntos;
   }


   public void sumarPuntos(int puntos) {
       this.puntos += puntos;
   }


   public String getNombre() {
       return nombre;
   }


   public Ficha elegirFicha(int valor) {
       for (Ficha ficha : fichas) {
           if (esFichaJugable(ficha, valor)) {
               fichas.remove(ficha);
               return ficha;
           }
       }
       System.out.println("No se encontró una ficha jugable para el valor: " + valor);
       return null;
   }


   private boolean esFichaJugable(Ficha ficha, int valor) {
       if (ficha instanceof FichaTridomino) {
           FichaTridomino tridomino = (FichaTridomino) ficha;
           return tridomino.getLado1() == valor || tridomino.getLado2() == valor || tridomino.getLado3() == valor;
       }
       return ficha.getLado1() == valor || ficha.getLado2() == valor;
   }
}
