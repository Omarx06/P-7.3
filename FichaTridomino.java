public class FichaTridomino extends Ficha implements Movible {
   private int lado3;


   public FichaTridomino(int lado1, int lado2, int lado3) {
       super(lado1, lado2);
       this.lado3 = lado3;
   }


   public int getLado3() {
       return lado3;
   }


   public void setLado3(int lado3) {
       this.lado3 = lado3;
   }


   @Override
   public void rotateRight() {
       int temp = getLado1();
       setLado1(getLado2());
       setLado2(lado3);
       lado3 = temp;
   }


   @Override
   public void rotateLeft() {
       int temp = lado3;
       lado3 = getLado2();
       setLado2(getLado1());
       setLado1(temp);
   }


   @Override
   public String dibujarFicha(String orientacion) {
       if ("normal".equals(orientacion)) {
           return "    " + getLado1() + "\n" +
                   "   / \\\n" +
                   "  " + getLado2() + "---" + lado3;
       } else { // "invertido"
           return "  " + getLado2() + "---" + lado3 + "\n" +
                   "   \\ /\n" +
                   "    " + getLado1();
       }
   }


   @Override
   public String toString() {
       return "[" + getLado1() + "|" + getLado2() + "|" + lado3 + "]";
   }
}
