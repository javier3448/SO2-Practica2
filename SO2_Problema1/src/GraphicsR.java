
import javax.swing.JPanel;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * @author raulx
 */
public class GraphicsR extends JPanel {
    
    private ArrayList<Persona> personas = new ArrayList<Persona>();
    
    public void addPerson(Persona p){
        personas.add(p);
    }
    
   
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.BLACK);
        
        Graphics2D g2D = (Graphics2D) g;
        
        g2D.setColor(Color.WHITE);
        
        //Primera puerta
        g2D.fillRect(100, 40, 80, 40);
        //Segunda Puerta
        g2D.fillRect(100,350,80,40);
        
        //Estanteria
        g2D.fillRect(500, 150,60,120);
        
        g2D.setColor(Color.YELLOW);
        for (Persona p: personas){
            g2D.fill(p.getShape());
        }

    }

    public ArrayList<Persona> getLista() {
        return this.personas;
    }
    
}
