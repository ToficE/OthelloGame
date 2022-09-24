package backEnd;

public class Position implements Cloneable {
	private boolean hasPiece;
	private String color; // "black" / "white"
	private boolean isBlackPlaceable;
	private boolean isWhitePlaceable;
	private boolean isNotPlaceable;
	private int row;
	private int col;
	private double score;
	
	// constructor
	public Position(int row, int col) {
		this.row = row;
		this.col = col;
		this.hasPiece = false;
		this.color = "none";
		this.isBlackPlaceable = false;
		this.isWhitePlaceable = false;
		this.isNotPlaceable = false;
	}
	
	// deep clone the position object
	protected Position clone() throws CloneNotSupportedException {
		return (Position) super.clone();
	}
	
	// getters
	public int getRow() {
		return this.row;
	}
	
	public int getCol() {
		return this.col;
	}
	
	public boolean hasPiece() {
		return this.hasPiece;
	}
	
	public String getColor() {
		return this.color;
	}
	
	public boolean getPlaceable() {
		return this.isNotPlaceable;
	}
	
	public boolean isWhitePlaceable() {
		return this.isWhitePlaceable;
	}
	
	public boolean isBlackPlaceable() {
		return this.isBlackPlaceable;
	}
	
	public String displayColorW() {
		if (hasPiece == false) {
			if (isBlackPlaceable) {
				return "'";
			} else
			if (isWhitePlaceable) {
				return ",";
			} else {
				return " ";
			}
		} else {
			if (this.color == "white") {
				return "o";
			} else {
				return "*";
			}
		}
	}
	
	public String displayColor() {
		if (hasPiece == false) {
			return " ";
		} else {
			if (this.color == "white") {
				return "o";
			} else {
				return "*";
			}
		}
	}
	
	public double getScore() {
		return this.score;
	}
	
	// setters
	public void setPiece(String color) {
		this.hasPiece = true;
		this.color = color;
		this.isWhitePlaceable = false;
		this.isBlackPlaceable = false;
		this.isNotPlaceable = true;
	}
	
	public void flipPiece() {
		if (this.color == "white") {
			this.color = "black";
		} else {
			this.color = "white";
		}
	}
	
	public void setWhitePlaceable() {
		this.isWhitePlaceable = true;
		this.isBlackPlaceable = false;
		this.isNotPlaceable = false;
	}
	
	public void setBlackPlaceable() {
		this.isBlackPlaceable = true;
		this.isWhitePlaceable = false;
		this.isNotPlaceable = false;
	}
	
	public void setNotPlaceable() {
		this.isNotPlaceable = true;
		this.isWhitePlaceable = false;
		this.isBlackPlaceable = false;
	}
	
	public void setScore(double score) {
		this.score = score;
	}
}