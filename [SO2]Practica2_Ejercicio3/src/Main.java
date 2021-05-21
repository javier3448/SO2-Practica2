import java.awt.EventQueue;

// @TODO: Pareciera que los colores de las naves son algo diferentes, verificar
// que no sea asi

public class Main {
	
	public static void main(String[] args) {
		// https://stackoverflow.com/questions/22534356/java-awt-eventqueue-invokelater-explained
		EventQueue.invokeLater(() -> {
			GameContainer gameContainer = new GameContainer();
			gameContainer.setVisible(true);
		});
	}
}
