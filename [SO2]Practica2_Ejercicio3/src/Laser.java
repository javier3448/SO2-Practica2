import java.awt.Graphics;
import java.awt.Image;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.ImageIcon;

public class Laser {
	Game game;
	
	// BUG:
	// TODO: RACE CONDITION:
	public int x;
	public int y;

	public static final Image sprite = (new ImageIcon("resources/laser1.png")).getImage();
	public static final int hitboxWidth = sprite.getWidth(null);
	public static final int hitboxHeight = sprite.getHeight(null);
	
	public static final int yVelocity = -10;

	// @Mejora!: podria ser static final porque es la misma imagen para todos los
	// enemigos
	
	// para que despues de recorrer todos los enemigos lo podamos quitar del arreglo
	// en una segunda pasada. Creo que hay una mejor manera de solucionar este problema, pero
	// por ahora esto esta bien.
	public boolean remove;
	
	public Laser(Game game, String spritePath) {
		this.game = game;
		
		this.x = 0;
		this.y = 0;
		this.remove = false;
		
	}
	
	// @Mejora: las colisiones no deberian de ir metodos, porque es algo confuso
	// ya que usamos al menos dos objetos, entonces no es obvio en que objeto
	// deberia estar la logica, por ejemplo no se si poner la logica de perder vida
	// y desaparecer en Laser o en Enemigo
	
	public void cycle() {
		// despawn
		if(y < 0 - hitboxHeight) {
			this.remove = true;
		}

		// colisionar con enemigos
		game.spawner.enemigosLock.writeLock().lock();
		{
			for (Enemigo enemigo : game.spawner.enemigos) {
				if(enemigo.collides(this.x, this.y, hitboxWidth, hitboxHeight)) {
					enemigo.takeHit();

					this.remove = true;
				}
			}
		}
		game.spawner.enemigosLock.writeLock().unlock();


		y += (int) yVelocity;
	}

	public void render(Graphics g) {
		g.drawImage(sprite, x, y, null);
	}
}
