import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

// Tiene toda la logica de juego como tal, solo necesita que le pasen un canvas
// del tamanno adecuado. 

public class Game extends JPanel implements Runnable{
	
	private static final long serialVersionUID = 1L;
	
	public static final int WIDTH = 448;
	public static final int HEIGHT = 512;

	public final int INITIAL_DELAY = 100;
	public final int DELAY = 15;
	
	public Thread gameThread;
	
	public ReentrantReadWriteLock runningLock = new ReentrantReadWriteLock();
	private boolean running;
	
	// si el jugador esta muerto lo ponemos en null
	// @mejora: usar optional o algo asi para expresar eso
	public Jugador[] jugadores = new Jugador[2];
	public Spawner spawner;

	public Game() {
		
		// [??]
		addKeyListener(new TAdapter());
		setBackground(Color.BLACK);
		// [!!!] Si no ponemos esto no funciona el keyListener
		setFocusable(true);
		setMinimumSize(new Dimension(WIDTH, HEIGHT));
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setMaximumSize(new Dimension(WIDTH, HEIGHT));
		
		running = true;

		spawner = new Spawner(this);

		//jugador 1
		jugadores[0] = new Jugador(this, 0, "resources/jugador1.png", "resources/laser1.png", KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_S);

		//jugador 2
		jugadores[1] = new Jugador(this, WIDTH / 2, "resources/jugador2.png", "resources/laser2.png", KeyEvent.VK_J, KeyEvent.VK_L, KeyEvent.VK_K);
	}

	// [?] No se si este tipo de metodo tiene sentido
	public boolean getRunning() {
		runningLock.readLock().lock();
		boolean result = this.running;
		runningLock.readLock().unlock();

		return result;
	}
	
	public void setRunning(boolean b) {
		runningLock.writeLock().lock();
		this.running = b;
		runningLock.writeLock().unlock();
	}
	
	private class TAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			for (Jugador jugador : jugadores) {
				if(jugador != null) {
					jugador.keyPressed(e);
				}
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			for (Jugador jugador : jugadores) {
				if(jugador != null) {
					jugador.keyReleased(e);
				}
			}
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		render(g);
		
		Toolkit.getDefaultToolkit().sync();
	}
	
	public void render(Graphics g) {
		for (Jugador jugador : jugadores) {
			if(jugador != null) {
				jugador.render(g);
			}
		}
		
		spawner.render(g);
		
		// HUB:
		{
			g.setColor(Color.WHITE);
			g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));

			g.drawString(
				"Jugador 1: " + (jugadores[0] != null ? jugadores[0].getHp() : 0) , 
				8, g.getFontMetrics().getHeight()
			);

			String stringJugador2 = "Jugador 2: " + (jugadores[1] != null ? jugadores[1].getHp() : 0);
			char[] charArrayJugador2 = stringJugador2.toCharArray();
			g.drawString(
				stringJugador2, 
				WIDTH - (8 + g.getFontMetrics().charsWidth(charArrayJugador2, 0, charArrayJugador2.length)), g.getFontMetrics().getHeight() 
			);
			// TODO: ALGO QUE INDIQUE EL 'NIVEL' (i.e. spawn rate del spawner)
		}
	}

	public void cycle() {
		// .cycle de todos los componentes
//		for (Jugador jugador : jugadores) {
//			if(jugador != null) {
//				jugador.cycle();
//			}
//		}
		
		// chapuz:
		boolean allNull = true;
		for (int i = 0; i < jugadores.length; i++) {
			if(jugadores[i] != null) {
				if(jugadores[i].getRemove()) {
					jugadores[i] = null;
					continue;
				}
				allNull = false;
			}
		}
		if(allNull) {
			setRunning(false);
		}
	}

	// El tutorial dice:
	// 'The addNotify() method is called after our JPanel has been added to the 
	// JFrame component. This method is often used for various initialisation tasks.
	// No entiendo bien como funciona :(
	@Override
	public void addNotify() {
		super.addNotify();
	
		gameThread = new Thread(this);
		gameThread.start();
	}

	@Override
	public void run() {
		
		long lastTime = System.currentTimeMillis();
		long timeToWait;
		
		long lastTimeFps = System.currentTimeMillis();
		int fpsCount = 0;
		
		for(Jugador jugador : jugadores) {
			jugador.thread.start();
		}
		spawner.thread.start();
		
		while(true) {

			repaint();
			fpsCount++;
			
			if(getRunning()) {
				cycle();
			}

			long deltaTime = System.currentTimeMillis() - lastTime;
			timeToWait = DELAY - deltaTime;
			
			if(timeToWait < 0) {
				timeToWait = 2;
				System.out.println("WARNING: timeToWait menor que 0!!!");
			}

			try {
				Thread.sleep(timeToWait);
			} catch (InterruptedException e) {
				String msg = String.format("Thread interrupted: %s", e.getMessage());

                JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
			}

			// Este deltaTime es bastante impresiso pero no importa porque los 
			// fps son solo para debugging
			long timeDiffFps = System.currentTimeMillis() - lastTimeFps;
			if(timeDiffFps >= 1000) {
				System.out.println("fps: " + fpsCount);
				lastTimeFps = System.currentTimeMillis();
				fpsCount = 0;
			}

			lastTime = System.currentTimeMillis();
		}
	}
}
