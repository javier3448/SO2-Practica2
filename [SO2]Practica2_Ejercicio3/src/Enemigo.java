import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class Enemigo {
	
	public Game game;
	
	public int x;
	public int y;
	private int hp;
	
	public void takeHit() {
		hp--;
		if(hp <= 0) {
			this.remove = true;
		}
	}
	
	// Es modificada por Spawner cuando sube el spawn rate
	public static double yVelocity = 1;
	
	// @Mejora: podria ser static final porque es la misma imagen para todos los
	// enemigos
	public Image sprite;
	
	public int hitboxWidth;
	public int hitboxHeight;
	
	// para que despues de recorrer todos los enemigos lo podamos quitar del arreglo
	// en una segunda pasada. Creo que hay una mejor manera de solucionar este problema, pero
	// por ahora esto esta bien
	public boolean remove;
	
	public Enemigo(Game game) {
		this.game = game;
		this.remove = false;

		this.x = 0;
		this.y = 0;
		this.hp = 2;

		var iconEnemigo = new ImageIcon("resources/enemigo.png");
		sprite = iconEnemigo.getImage();
		
		hitboxWidth = sprite.getWidth(null);
		hitboxHeight = sprite.getHeight(null);
	}
	
	public void cycle() {
		// despawn cuando se sale de la pantalla
		if(y > Game.HEIGHT) {
			this.remove = true;
			for(Jugador jugador : game.jugadores) {
				if(jugador != null) {
					jugador.takeHit();
				}
			}
			return;
		}

		// colisionar con jugadores
		for (Jugador jugador : game.jugadores) {
			if(jugador != null && jugador.collides(this.x, this.y, hitboxWidth, hitboxHeight)) {
				this.hp = 0;
				jugador.takeHit();

				this.remove = true;
			}
		}


		y += (int) yVelocity;
	}
	
	public void render(Graphics g) {
		g.drawImage(sprite, x, y, null);
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
}
