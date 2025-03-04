package corso.java.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class GameStatus {
	private int hunterLife;
	private int monstersCount;
	private int vitaCavaliere;
	private int movimenti;

	@Override
	public String toString() {
		return String.format("Life: %d - Monsters: %d -\nVita Cavaliere: %d\n"
				+ "Movimenti Rimanenti: %d", 
				hunterLife, monstersCount, vitaCavaliere, movimenti);
	}
}
