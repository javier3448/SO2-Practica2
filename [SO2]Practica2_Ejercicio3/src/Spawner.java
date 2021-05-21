import java.awt.Graphics;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.swing.JOptionPane;

public class Spawner {
	
	public final long timeToIncreaseSpawnRate = 25000;
	
	public Game game;
	
	private long timeToSpawn;

	private long lastTimeSpawn;
	private long lastTimeSpawnRateIncreased;

	public ReentrantReadWriteLock enemigosLock = new ReentrantReadWriteLock();
	public ArrayList<Enemigo> enemigos = new ArrayList<Enemigo>();
	
	public Thread thread;
	
	public Spawner(Game game){
		this.game = game;
		
		// timeToSpawn inicial de 2000 milis
		this.timeToSpawn = 3000;
		
		lastTimeSpawn = System.currentTimeMillis();
		lastTimeSpawnRateIncreased = System.currentTimeMillis();
		
		thread = new Thread(new RunnableSpawner());
	}
	
	private void cycleEnemigos() {
		enemigosLock.writeLock().lock();
		{
			// @Volatile: segunda pasada para remover 'gameObjects' que murieron
			// @TODO: lo mas facil es que el ciclo se entere cuando el .cycle causo 
			// que removieran un 'gameObject' para que pueda ajustar los valores de i
			// array.size
			// @Mejora?!: no se que tan buena sea esta forma de eliminar 'gameObjects'
			// del juego (usando un boolean `remove` y haciendo una segunda pasada al arrelgo):
			for (Enemigo enemigo : enemigos) {
				enemigo.cycle();
			}
			// eliminar los enemigos que hayan salido de la pantalla o colisionado con 
			// algun jugador
			for (int i = 0; i < enemigos.size(); i++) {
				if(enemigos.get(i).remove) {
					enemigos.remove(i);
					i--;
				}
			}
		}
		enemigosLock.writeLock().unlock();
	}
	
	private void cycle() {
		
		cycleEnemigos();

		// Spawn cada `timeToSpawn` milisegundos
		{
			long currTime = System.currentTimeMillis();
			long deltaTime = currTime - lastTimeSpawn;
			if(deltaTime >= timeToSpawn) {
				

				Enemigo nuevoEnemigo = new Enemigo(game);
				nuevoEnemigo.x = ThreadLocalRandom.current().nextInt(0, game.WIDTH - nuevoEnemigo.hitboxWidth);
				nuevoEnemigo.y = -50;

				enemigosLock.writeLock().lock();
				{
					enemigos.add(nuevoEnemigo);
				}
				enemigosLock.writeLock().unlock();

				lastTimeSpawn = currTime;
			}
		}
		
		// Subir `timeToSpawn` cada `timeToIncreaseSpawnRate` milisegundos
		{
			long currTime = System.currentTimeMillis();
			long deltaTime = currTime - lastTimeSpawnRateIncreased;
			if(deltaTime >= timeToIncreaseSpawnRate) {
				// se subimos de velocidad a los enemigos
				Enemigo.yVelocity = Enemigo.yVelocity * 1.25;
				// @Mejora: subir velocidad de los jugadores y cuando disparan por segundo

				timeToSpawn = (long)(0.75 * timeToSpawn);
				lastTimeSpawnRateIncreased = currTime;
			}
		}
	}
	
	public class RunnableSpawner implements Runnable{
		//@Override:
		public void run() {
			// correr cycle
			long lastTime = System.currentTimeMillis();
			long timeToWait;
			
			while(game.getRunning()) {

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
	
	public void render(Graphics g) {
		enemigosLock.readLock().lock();
		{
			for (Enemigo enemigo : enemigos) {
				enemigo.render(g);
			}
		}
		enemigosLock.readLock().unlock();
	}
}
