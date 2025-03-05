package corso.java.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import corso.java.entities.Bonus;
import corso.java.entities.BonusMovimento;
import corso.java.entities.DarkKnight;
import corso.java.entities.DirtySlime;
import corso.java.entities.GameGrid;

public class GameMasterV1 implements GameMaster {
	private int movimenti = 30;
	private boolean lose;
	private boolean won;
	private final GameGrid gameGrid;
	private final HunterPawn hunter = new HunterPawn(20, 5);
	private final MonsterDecorator darkKnight = new MonsterDecorator(new DarkKnight(20, 2));
	private final List<MonsterDecorator> monsters = new ArrayList<MonsterDecorator>();
	private List<Bonus> bonusLista = new ArrayList<Bonus>();

	private final Random rnd = new Random();

	public GameMasterV1() {
		// Creazione della Griglia
		gameGrid = new GameGrid(20, 15);
		// Disposizione di 5 Mostri Slyme e 1 Dark
		for (int i = 0; i < rnd.nextInt(2, 8); ++i) {
			monsters.add(new MonsterDecorator(new DirtySlime()));
		}
		monsters.add(darkKnight);
		spawnMonsters();
		// Disposizione del Cacciatore
		int row = gameGrid.getHeight() / 2;
		int column = gameGrid.getWidth() / 2;
		placeHunter(row, column);
		// Disposizione Bonus Movimento
		for (int i = 0; i < rnd.nextInt(2, 4); ++i) {
			this.bonusLista.add(new BonusMovimento());
		}
		spawnBonus();
		BonusMovimento bonusMov = new BonusMovimento();
		gameGrid.getCells()[2][2] = bonusMov;
		bonusMov.setRow(2);
		bonusMov.setColumn(2);
		bonusLista.add(bonusMov);
	}

	private void spawnMonsters() {
		for (var monster : monsters) {
			var x = rnd.nextInt(gameGrid.getWidth());
			var y = rnd.nextInt(gameGrid.getHeight());
			placeMonster(monster, y, x);
		}
	}

	private void placeMonster(MonsterDecorator monster, int row, int col) {
		gameGrid.getCells()[monster.getRow()][monster.getColumn()] = null;
		monster.setRow(row);
		monster.setColumn(col);
		gameGrid.getCells()[row][col] = monster;
	}

	private void spawnBonus() {
		for (Bonus bonus1 : bonusLista) {
			var x = rnd.nextInt(gameGrid.getWidth());
			var y = rnd.nextInt(gameGrid.getHeight());
			placeBonus(bonus1, y, x);
		}
	}

	private void placeBonus(Bonus bonus, int row, int col) {
		gameGrid.getCells()[bonus.getRow()][bonus.getColumn()] = null;
		bonus.setRow(row);
		bonus.setColumn(col);
		gameGrid.getCells()[row][col] = bonus;
	}

	private void placeHunter(int row, int col) {
		// Nella posizione in cui stava il cacciatore metti null e inseriscilo
		// in una nuova posizione
		gameGrid.getCells()[hunter.getRow()][hunter.getColumn()] = null;
		hunter.setRow(row);
		hunter.setColumn(col);
		gameGrid.getCells()[row][col] = hunter;
	}

	@Override
	public void hunterMove(char direction) {
		// sulla base della direzione
		// dobbiamo prendere la posizione attuale del cacciatore
		// e cambiarla opportunamente
		var dx = 0;
		var dy = 0;
		if (direction == 'a')
			dx = -1;
		if (direction == 'd')
			dx = 1;
		if (direction == 'w')
			dy = -1;
		if (direction == 's')
			dy = 1;

		var px = hunter.getColumn();
		var py = hunter.getRow();

		var x = px + dx;
		var y = py + dy;

		if (x < 0)
			x = 0;
		if (x >= gameGrid.getWidth())
			x = gameGrid.getWidth() - 1;
		if (y < 0)
			y = 0;
		if (y >= gameGrid.getHeight())
			y = gameGrid.getHeight() - 1;
		placeHunter(y, x);
		movimenti--;
	}

	@Override
	public void monstersMove() {
		var rnd = new Random();
		// dobbiamo prendere la posizione attuale di ogni mostro
		// e cambiarla opportunamente
		for (var monster : monsters) {
//			var dx = rnd.nextInt(3) - 1; // 0 1 2 -1 => -1 0 1
			var dy = rnd.nextInt(3) - 1;
			// I Mostri piu'intelligenti
			int dx;
			if (hunter.getColumn() > monster.getColumn()) {
				dx = rnd.nextInt(2); // 0 1
			} else {
				dx = rnd.nextInt(2) - 1; // -1 0
			}
			if (hunter.getColumn() == monster.getColumn()) {
				dx = rnd.nextInt(3) - 1;
			}
			var px = monster.getColumn();
			var py = monster.getRow();

			var x = px + dx;
			var y = py + dy;

			if (x < 0)
				x = 0;
			if (x >= gameGrid.getWidth())
				x = gameGrid.getWidth() - 1;
			if (y < 0)
				y = 0;
			if (y >= gameGrid.getHeight())
				y = gameGrid.getHeight() - 1;
			placeMonster(monster, y, x);
		}
	}

	@Override
	public boolean hunterWon() {
		return won;
	}

	@Override
	public boolean hunterLose() {
		return lose;
	}

	@Override
	public GameGrid grid() {
		return this.gameGrid;
	}

	@Override
	public void evaluateStatus() {
		// dobbiamo vedere se c'è collisione tra cacciatore e mostri
		// troviamo tutti i mostri che stanno nella stessa posizione del cacciatore
		var x = hunter.getColumn();
		var y = hunter.getRow();
		var a = hunter.getAttack();
		var l = hunter.getLifeLevel();
		// Stream di Collisioni Mostri con Cacciatore
		var collisions = monsters.stream().filter(m -> m.getRow() == y && m.getColumn() == x);
		// Somma degli attacchi dei Mostri in collisione da scalare alla vita del
		// Cacciatore
		var monstersAttack = collisions.mapToInt(m -> m.getAttack()).sum();
		l -= monstersAttack; // Decremento la vita del Cacciatore
		hunter.setLifeLevel(l);
		if (l <= 0) {
			// Vita <= 0 Perdi
			lose = true;
		}
		// Lista di Collisioni Mostri con Cacciatore
		List<MonsterDecorator> collisioni = monsters.stream().filter(m -> m.getRow() == y && m.getColumn() == x)
				.toList();
		for (var monster : collisioni) {
			monster.setLifeLevel(monster.getLifeLevel() - a);
			if (monster.getLifeLevel() <= 0) {
				// devo eliminare il mostro dal gioco
				gameGrid.getCells()[monster.getRow()][monster.getColumn()] = null;
				monsters.remove(monster);
			}
		}
//		for (BonusMovimento bonus : bonusMovimenti) {
//			if((hunter.getRow() == bonus.getRow()) && (hunter.getColumn() == bonus.getColumn())) {
//				gameGrid.getCells()[bonus.getRow()][bonus.getColumn()] = null;
//				this.movimenti += bonus.getMovimenti();
//			}
//		}
		if (monsters.size() == 0) { // se non ci sono mostri
			won = true;
		}
		placeHunter(hunter.getRow(), hunter.getColumn());
	}

	public int getMovimenti() {
		return movimenti;
	}

	public void setMovimenti(int movimenti) {
		this.movimenti = movimenti;
	}

	@Override
	public GameStatus getStatus() {
		return new GameStatus(hunter.getLifeLevel(), monsters.size(), darkKnight.getLifeLevel(), movimenti);
	}
}
