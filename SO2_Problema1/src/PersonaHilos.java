
import java.awt.Component;
import java.util.ArrayList;

/**
 * @author raulx
 */
public class PersonaHilos implements Runnable{

    private ArrayList<Persona> personas;
    private Component component;
    
    public PersonaHilos(ArrayList<Persona> personas, Component component) {
        this.personas = personas;
        this.component = component;
    }

    @Override
    public void run() {
        for(Persona p: this.personas) {
            for (int i = 0; i < 430; i++) {
                p.moverPersona();
                component.paint(component.getGraphics());
                try {
                    Thread.sleep(15);
                } catch (Exception e) {
                }
                
            }
        }
    }
    
    
    
}
