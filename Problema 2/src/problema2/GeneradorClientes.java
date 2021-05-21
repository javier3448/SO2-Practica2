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
public class GeneradorClientes extends Thread {

    LinkedList<Cliente> sillas;
    JPanel pane;
    LinkedList<Dibujo> dibujos;
    int id = 1;
    int generacionCliente;
    JTextArea jt;

    public GeneradorClientes(LinkedList<Cliente> sillas, int generacionCliente, JPanel pane, LinkedList<Dibujo> dibujos, JTextArea jt) {
        this.sillas = sillas;
        this.generacionCliente = generacionCliente;
        this.id = 1;
        this.pane = pane;
        this.dibujos = dibujos;
        this.jt = jt;
    }

    public void run() {
        while (true) {
            try {

                sillas.addLast(new Cliente(id++));
                if (sillas.size() > 20) {
                    System.out.println("Adios Cliente: " + sillas.getLast().getId());
                    jt.append("\nAdios Cliente: " + sillas.getLast().getId());
                    dibujos.addLast(new Dibujo(((sillas.size() - 2) * 40), 400, "imagenes/angry.png"));
                    for (int i = 2; i < dibujos.size(); i++) dibujos.get(i).x = ((i - 2) * 40);
                    pane.repaint();
                    Thread.sleep(1500);
                    dibujos.removeLast();
                    sillas.removeLast();
                    pane.repaint();
                    
                    continue;
                }
                dibujos.addLast(new Dibujo(((sillas.size() - 2) * 40), 400, "imagenes/client.png"));

                for (int i = 2; i < dibujos.size(); i++)dibujos.get(i).x = ((i - 2) * 40);

                pane.repaint();
                jt.append("\nCliente Generado: " + (id - 1));
                System.out.println("Cantidad clientes: " + sillas.size());
                
                Thread.sleep(generacionCliente);

            } catch (InterruptedException ex) {
                System.out.println("Generacion Clientes:" + ex.getMessage());
            }
        }
    }

}
