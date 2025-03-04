package corso.java.entities;

import corso.java.game.Pawn;
import lombok.Data;

@Data

public abstract class Bonus extends GameActor implements Pawn{

	 public Bonus(int lifeLevel, int attack, char symbol) {
		super(lifeLevel, attack, symbol);
	 }
}
