import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

// @Mejora: seria mas facil si cada jugador tubiera su propia clase, nos ahorariamos
// branching raro para los controles y para la deteccion de coliciones. No necesitamos
// guardar un monton de cosas en esta instancia; como el los int `key` ya que esos
// son valores que se saben estaticamente.
public class Jugador{

	public int x;
	public static final int y = Game.HEIGHT - 50;
	public int xVelocity = 0;
	
	// Para evitar cosas raras con el thread que consigue el input. Vamos hacer
	// la logica de disparo mas o menos parecida a como hacemos el xVelocity
	public boolean shooting = false;

	private int hp = 0;
	
	public Game game;
	
	// Relativa a la posicion x, y
	public int hitboxWidth;
	public int hitboxHeight;
	public Image sprite;
	public String laserSprite;

	public ReentrantReadWriteLock lasersLock = new ReentrantReadWriteLock();
	public ArrayList<Laser> lasers = new ArrayList<Laser>();
	
	public int leftKey;
	public int rightKey;
	public int shootKey;
	
	private boolean remove;
	
	public boolean isRunning;
	
	public Thread thread;

	public Jugador(Game game, int x, String pathSprite, String laserSprite, int leftKey, int rightKey, int shootKey) {
		this.game = game;
		
		setRemove(false);

		this.leftKey = leftKey;
		this.rightKey = rightKey;
		this.shootKey = shootKey;
		this.laserSprite = laserSprite;

		this.x = x;
		this.hp = 3;
		
		var iconJugador = new ImageIcon(pathSprite);
		sprite = iconJugador.getImage();
		
		hitboxWidth = sprite.getWidth(null);
		hitboxHeight = sprite.getHeight(null);
		
		thread = new Thread(new RunnableJugador());
	}
	
	public synchronized void setRemove(boolean b) {
		this.remove = b;
	}

	public synchronized boolean getRemove() {
		return this.remove;
	}
	
	public void move() {

		int siguienteX = this.x + this.xVelocity;
		if(siguienteX < Game.WIDTH - sprite.getWidth(null) && siguienteX > 0)
		{
			for (Jugador otherJugador : game.jugadores) {
				if(this != otherJugador && otherJugador != null && otherJugador.collides(siguienteX, y, hitboxWidth, hitboxHeight)) {
					//dont move
					return;
				}
			}
			// move
			x = siguienteX;
		}
		else {
			// dont move
			return;
		}
	}
	
	// @TODO: que no sea parte de esta clase, que simplemente sea un metodo estatico
	// al que hay que pasarle un par de rectangulos 
	public boolean collides(int otherX, int otherY, int otherHitboxWidth, int otherHitboxHeight) {
		// @TODO: hacerlo a mano, no me gusta instanciar un objeto solo para eso
		// ademas Rectangle usa doubles y eso tampoco me gusta
		Rectangle thisHitbox = new Rectangle(x, y, hitboxWidth, hitboxHeight);
		Rectangle otherHitbox = new Rectangle(otherX, otherY, otherHitboxWidth, otherHitboxHeight);
		return thisHitbox.intersects(otherHitbox);
	}
	
	private void cycle() {
		move();
		
		if(shooting) {
			shoot();
		}
		
		lasersCycle();
	}
	
	public void takeHit() {
		hp--;
		if(hp <= 0) {
			setRemove(true);
		}
	}
	
	public int getHp() {
		return hp;
	}

	private void lasersCycle() {
		
		lasersLock.writeLock().lock();
		{
			// @Volatile: segunda pasada para remover 'gameObjects' que murieron
			// @TODO: lo mas facil es que el ciclo se entere cuando el .cycle causo 
			// que removieran un 'gameObject' para que pueda ajustar los valores de i
			// array.size
			// @Mejora?!: no se que tan buena sea esta forma de eliminar 'gameObjects'
			// del juego (usando un boolean `remove` y haciendo una segunda pasada al arrelgo):
			for (Laser laser : lasers) {
				laser.cycle();
			}
			// eliminar los lasers que hayan salido de la pantalla o colisionado con 
			// algun enemigo
			for (int i = 0; i < lasers.size(); i++) {
				if(lasers.get(i).remove) {
					lasers.remove(i);
					i--;
				}
			}
		}
		lasersLock.writeLock().unlock();
	}

	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		
		if(keyCode == leftKey) {
			xVelocity = -4;
		}
		if(keyCode == rightKey) {
			xVelocity = 4;
		}
		if(keyCode == shootKey) {
			shooting = true;
		}
	}
	
	private long lastTimeShoot = 0;
	private final long timeToShoot = 200;
	public void shoot() {
		long now = System.currentTimeMillis();
		long deltaTime = now - lastTimeShoot;
		if(deltaTime >= timeToShoot) {
			Laser newLaser = new Laser(game, laserSprite);
			newLaser.x = x + (this.hitboxWidth/2) - (newLaser.hitboxWidth/2);
			newLaser.y = y;

			lasersLock.writeLock().lock();
			{
				lasers.add(newLaser);
			}
			lasersLock.writeLock().unlock();

			lastTimeShoot = now;
		}
	}

	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		
		if(keyCode == leftKey) {
			xVelocity = 0;
		}
		if(keyCode == rightKey) {
			xVelocity = 0;
		}
		if(keyCode == shootKey) {
			shooting = false;
		}
	}

	public void render(Graphics g) {
		g.drawImage(sprite, x, y, null);
		
		lasersLock.readLock().lock();
		{
			for (Laser laser : lasers) {
				laser.render(g);
			}
		}
		lasersLock.readLock().unlock();
	}
	
	public class RunnableJugador implements Runnable{
		@Override
		public void run() {
			// correr cycle
			long lastTime = System.currentTimeMillis();
			long timeToWait;
			
			while(!getRemove() && game.getRunning()) {

				cycle();

				long deltaTime = System.currentTimeMillis() - lastTime;
				timeToWait = game.DELAY - deltaTime;
				
				if(timeToWait < 0) {
					timeToWait = 2;
					System.out.println("WARNING: timeToWait menor que 0!!!");
				}

				try {
					Thread.sleep(timeToWait);
				} catch (InterruptedException e) {
					String msg = String.format("Thread interrupted: %s", e.getMessage());

					JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);
				}

				lastTime = System.currentTimeMillis();
			}
		}
	}

}
