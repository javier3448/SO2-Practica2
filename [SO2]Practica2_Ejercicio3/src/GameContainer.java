import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

// tiene toda la logica del JFrame/Canvas y toda las cosas java que necesitamos 
// para conseguir un lugar donde dibujar nuestro juego. Esta hecho para que construirlo
// cree una 'nueva instancia' de nuestro juego.
// Hereda de Canvas porque necesitamos acceso a los metodos de Canvas desde la 
// clase Game (que contiene toda la logica del juego como tal), y no quiero pasar 
// una instancia Canvas cualquiera, quiero expresar en el sistema de tipos que el canvas
// de juego tiene cierto tamano y ciertas caracteristicas... :/

public class GameContainer extends JFrame{
	private static final long serialVersionUID = 1L;
	
	public GameContainer() {
		add(new Game());
		
		pack();

		setTitle("Ejercicio 3");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}
}
