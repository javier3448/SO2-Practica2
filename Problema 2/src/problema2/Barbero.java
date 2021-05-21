/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package problema2;

import java.util.LinkedList;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 *
 * @author Carlos
 */
public class Barbero extends Thread {

    int estado;
    int duracionCorte;
    LinkedList<Dibujo> dibujos;
    JPanel pane;
    JTextArea jt;

    public Barbero(int duracionCorte, int estado, LinkedList<Dibujo> dibujos, JPanel pane, JTextArea jt) {
        this.dibujos = dibujos;
        this.estado = estado;
        this.duracionCorte = duracionCorte;
        this.pane = pane;
        this.jt = jt;
    }

    public void run() {
        while (true) {
            try {
                
                if (estado == 1 || estado == 3) {
                    //Alguien desperto al barbero
                    if(estado == 1) {
                        Thread.sleep(1000);
                        System.out.println("Barbero Despierto");
                        dibujos.set(0, new Dibujo(30,30,"imagenes/barbero.png"));
                        jt.append("\nBarbero Despierto");
                        pane.repaint();
                        
                    }
                    System.out.println("Barbero Cortara el Pelo");
                    jt.append("\nCortando Pelo");
                    Thread.sleep(duracionCorte);
                    System.out.println("Termino de Cortar el pelo");
                    jt.append("\nFin de Cortar pelo");
                    dibujos.set(1, null);
                    this.estado = 2;
                } 

                Thread.sleep(200);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public int getEstado() {
        return this.estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

}
