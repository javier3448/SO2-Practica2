
import javax.swing.JFrame;

/**
 *
 * @author raulx
 */
public class Ventana extends JFrame{
    
    GraphicsR g = new GraphicsR();
    
    public Ventana() {
        setTitle("Problema 1");
        setSize(600,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fill();
        add(g);
        setLocationRelativeTo(null);
        setVisible(true);
        mover();
    }
   
    public void fill(){
        for (int i = 0; i < 20; i++) {
            Persona p = new Persona(1);
            g.addPerson(p);
        }
        for (int i = 0; i < 10; i++) {
            Persona p = new Persona(0);
            g.addPerson(p);
        }
    }
    
    public void mover() {
        Runnable r = new PersonaHilos(this.g.getLista(),this.g);
        Thread t = new Thread(r);
        t.start();
    }
   
}
