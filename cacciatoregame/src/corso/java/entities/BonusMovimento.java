package corso.java.entities;

import java.util.Random;

public class BonusMovimento extends Bonus {

	private final Random rnd = new Random();

	private int movimenti = rnd.nextInt(6, 9);
	private int row;
	private int column;

	public BonusMovimento() {
		super(0, 0, 'M');
	}

	public int getMovimenti() {
		return movimenti;
	}

	@Override
	public int getRow() {
		return row;
	}

	@Override
	public int getColumn() {
		return column;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public void setMovimenti(int movimenti) {
		this.movimenti = movimenti;
	}
	
	
}

