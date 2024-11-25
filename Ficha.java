public class Ficha implements Movible {
   private int lado1;
   private int lado2;




   public Ficha(int lado1, int lado2) {
       this.lado1 = lado1;
       this.lado2 = lado2;
   }


   public int getLado1() {
       return lado1;
   }


   public int getLado2() {
       return lado2;
   }


   public void setLado1(int lado1) {
       this.lado1 = lado1;
   }


   public void setLado2(int lado2) {
       this.lado2 = lado2;
   }


   @Override
   public void rotateRight() {
       int temp = lado1;
       lado1 = lado2;
       lado2 = temp;
   }


   @Override
   public void rotateLeft() {
       rotateRight(); // En una ficha de dominó estándar, ambas rotaciones son equivalentes
   }


   public String dibujarFicha(String orientacion) {
       if ("vertical".equals(orientacion)) {
           return "[" + lado1 + "]\n[" + lado2 + "]";
       } else { // "horizontal"
           return "[" + lado1 + "|" + lado2 + "]";
       }
   }


   @Override
   public String toString() {
       return "[" + lado1 + "|" + lado2 + "]";
   }
}
