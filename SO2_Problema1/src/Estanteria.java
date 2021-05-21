
import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *
 * @author raulx
 */
public class Estanteria {
    
    private final LinkedList lista;
    private int maxSize = 20;
    //private final Lock lock = new ReentrantLock(); 
    ReentrantLock lock;
    ReentrantReadWriteLock lockReadWrite;

    public Estanteria() {
        this.lista = new LinkedList();
        this.lock = new ReentrantLock();
        this.lockReadWrite = new ReentrantReadWriteLock(true);
    }
    
    public synchronized void addBox() {
        try {
            while(lista.size() == maxSize){
                wait();
            }
            if (lista.size() == 0) {
                notifyAll();
            }
            this.lista.add("1");
        } catch (Exception e) {
            System.out.println("Estanteria addBox:" + e.getMessage());
        }
    }
    
    public synchronized void getBox() {
        try{

            while(lista.size() == 0){
                wait();
            }
            if(lista.size() == maxSize){
                notifyAll();
            }
            this.lista.remove(0);
        }catch(Exception e){
            System.out.println("Estanteria getBox:" + e.getMessage());
        } 
    }
    
    public void printList() {
        for(int i = 0; i < lista.size(); i++){
            System.out.println("|"+i+"|"+lista.get(i)+"|");
        }
    }
    
}
