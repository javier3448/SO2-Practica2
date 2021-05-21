
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author raulx
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Ventana v = new Ventana();
        //v.setVisible(true);

        //Thread Pool
        ExecutorService executor = Executors.newCachedThreadPool();
        Estanteria estanteria = new Estanteria();
        

        //Creando 50 hilos... 50 personas 
        for (int i = 0; i < 100; i++){
            executor.execute(new Operacion(0,estanteria));
        }
        
        //Creando 50 hilos... 50 personas para sacar
        for (int i = 0; i < 85; i++){
            executor.execute(new Operacion(1,estanteria));
            
            
        }
        
        try {
            executor.shutdown();
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            estanteria.printList();
        }catch(Exception e) {
            System.out.println(e.getMessage());
        }
        
    }
    
}
