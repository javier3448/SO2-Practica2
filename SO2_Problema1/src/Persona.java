
import java.awt.geom.Ellipse2D;

/**
 * @author raulx
 */
public class Persona {
    
    public int tipo;
    private double x=50;
    private double y=0;
    
    public Persona(int tipo) {
        this.tipo = tipo;
        this.y = (this.tipo == 1) ? 46 : 356;
    }

    public void moverPersona(){
        x++;
    }

    public Ellipse2D getShape(){
        return new Ellipse2D.Double(x,y,15,15);
    }
    
}
