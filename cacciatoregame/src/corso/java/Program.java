package corso.java;

import java.io.IOException;

import corso.java.entities.GameActor;
import corso.java.entities.GameGrid;
import corso.java.game.GameMasterV1;

public class Program {
	//	AGGIORNAMENTI
	// Numero di Movimenti Limitati OK
	// Numero Casuale di Mostri OK
	// Mostri Intelligenti OK
	// Vita Dark Visibile OK
	// Bonus Movimento OK 
	
	static void printGrid(GameGrid grid) {
		var cells = grid.getCells();
		System.out.print(' ');
		for (int col = 0; col < grid.getWidth(); ++col) {
			System.out.print(col % 10);
		}
		System.out.println();
		for (int row = 0; row < grid.getHeight(); ++row) {
			System.out.print(row % 10);
			for (int col = 0; col < grid.getWidth(); ++col) {
				GameActor a = cells[row][col];
				if (a == null) {
					System.out.print(' ');
				} else {
					System.out.print(a.getSymbol());
				}
			}
			System.out.println();
		}
	}

	static void gameLoop() {
		// Crea il gioco
		var master = new GameMasterV1();
		while (!master.hunterLose() && !master.hunterWon() && master.getMovimenti() > 0) {
			// Stampa lo Stato del Cacciatore
			System.out.println(master.getStatus());
			// Stampa la Griglia aggiornata
			printGrid(master.grid());
			char d = inputHunter();
			master.hunterMove(d);
			master.monstersMove();
			master.evaluateStatus();
			if (master.hunterLose()) {
				System.out.println(master.getStatus());
				System.out.println("HAI PERSO");
			}
			if (master.hunterWon()) {
				System.out.println(master.getStatus());
				System.out.println("HAI VINTO");
			}
			if(master.getMovimenti() <= 0) {
				System.out.println(master.getStatus());
				System.out.println("MOVIMENTI ESAURITI");				
			}
		}
	}

	private static char inputHunter() {
		System.out.print("Move (wasd): ");
		try {
			int d = System.in.read();
//			System.out.println();
			System.in.skip(System.in.available());
			return (char) d;
		} catch (IOException e) {
			return 0;
		}
	}

	public static void main(String[] args) {
//		var rnd = new Random();
//		var grid = new GameGrid(20, 15);
//		for (var i = 0; i < 10; ++i) {
//			int row = rnd.nextInt(grid.getHeight());
//			int col = rnd.nextInt(grid.getWidth());
//			grid.getCells()[row][col] = new DirtySlime();
//		}
//		grid.getCells()[13][3] = new DarkKnight(15, 15);
//		var h = new Hunter(10, 10);
//		grid.getCells()[7][10] = h;
//		printGrid(grid);

		System.out.println("Giochiamo!!!");
		gameLoop();
		System.out.println("Game Over");
	}

}
