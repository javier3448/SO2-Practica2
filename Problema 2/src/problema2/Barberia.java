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
public class Barberia extends Thread {

    LinkedList<Cliente> sillas;
    LinkedList<Dibujo> dibujos;
    Barbero barbero;
    JPanel pane;
    JTextArea jt;

    public Barberia(LinkedList<Cliente> sillas, Barbero barbero, LinkedList<Dibujo> dibujos, JPanel pane, JTextArea jt) {
        this.dibujos = dibujos;
        this.sillas = sillas;
        this.barbero = barbero;
        this.pane = pane;
        this.jt = jt;

    }

    public void run() {
        while (true) {
            try {

                int estado = barbero.getEstado();

                //Dormido
                if (estado == 0) {
                    if (sillas.size() > 0) {
                        sillas.removeFirst();
                        moveCliente();
                        pane.repaint();
                        barbero.setEstado(1); //Despertando
                    }
                } // en Busca de otro cliente
                else if (estado == 2) {
                    if (sillas.size() > 0) {
                        System.out.println("Buscar Nuevo Cliente");
                        jt.append("\nBuscar Nuevo Cliente");
                        dibujos.set(0, new Dibujo(30, 340, "imagenes/barbero.png"));
                        pane.repaint();
                        Thread.sleep(1500);
                        dibujos.set(0, new Dibujo(30, 30, "imagenes/barbero.png"));
                        pane.repaint();
                        sillas.removeFirst();
                        moveCliente();
                        pane.repaint();
                        barbero.setEstado(3); //Cortar el Pelo
                    } else {
                        dibujos.set(0, new Dibujo(30, 30, "imagenes/dormido.png"));
                        pane.repaint();
                        System.out.println("A mimir");
                        jt.append("\nNo hay clientes a mimir");
                        barbero.setEstado(0); //A mimir
                    }
                }

                Thread.sleep(200);
            } catch (InterruptedException ex) {
                System.out.println("Barberia:" + ex.getMessage());
            }
        }
    }

    private void moveCliente() {
        dibujos.set(1, new Dibujo(100, 30, "imagenes/client.png"));
        dibujos.remove(2);

    }
}
