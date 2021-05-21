/**
 *
 * @author raulx
 */
public class Operacion implements Runnable{
    
    private int tipo;
    private Estanteria estanteria;

    public Operacion(int tipo, Estanteria estanteria) {
        this.tipo = tipo;
        this.estanteria = estanteria;
    }

    @Override
    public void run() {
        switch(tipo){
            case 0:
                this.estanteria.addBox();
                break;
            case 1:
                this.estanteria.getBox();
        }
    }
    
}
