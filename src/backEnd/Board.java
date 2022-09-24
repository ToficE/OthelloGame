package backEnd;
import java.util.LinkedList;

public class Board implements Cloneable {
	private Position[][] board;
	private static int pieceCount;
	private String currentColor;
	
	public Board() {
		Position[][] board = new Position[8][8];
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				board[i][j] = new Position(i, j);
			}
		}
		this.board = board;
		pieceCount = 0;
		this.currentColor = "white";
	}
	
	public Board clone() throws CloneNotSupportedException {
		Board clone = (Board) super.clone();
		clone.setBoard(clone.duplicateBoard().clone());
		return clone;
	}
	
	public void setBoard(Position[][] board) {
		this.board = board;
	}
	
	public Position[][] duplicateBoard() {
		Position[][] boardCopy = new Position[8][8];
		int i;
		int j = 0;
		for (Position[] row : this.board) {
			i = 0;
			for (Position pos : row) {
				try {
					boardCopy[j][i] = (Position) pos.clone();
				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				i++;
			}
			j++;
		}
		return boardCopy;
	}
	
	public Position[][] getBoard() {
		return this.board;
	}
	
	
	public void setInitialPieces() {
		this.board[3][3].setPiece("black");
		this.board[3][4].setPiece("white");
		this.board[4][3].setPiece("white");
		this.board[4][4].setPiece("black");
		pieceCount = 4;
	}
	
	public void displayGboard() {
		
	}
	
	public Position getPosition(int row, int col) {
		return this.board[row][col];
	}
	
	// console UI
	public void displayBoard() {
		int row = 0;
		System.out.println("     1   2   3   4   5   6   7   8");
		for (Position[] pieces : this.board) {
			System.out.println("   ---------------------------------");
			switch (row) {
				case 0: System.out.print("A "); break;
				case 1: System.out.print("B "); break;
				case 2: System.out.print("C "); break;
				case 3: System.out.print("D "); break;
				case 4: System.out.print("E "); break;
				case 5: System.out.print("F "); break;
				case 6: System.out.print("G "); break;
				case 7: System.out.print("H "); break;
			}
			for(Position pieceX : pieces) {
				System.out.print(" | " + pieceX.displayColor());
			}
			System.out.println(" |");
			row++;
		}
		System.out.println("   ---------------------------------");
	}
	
	public void displayBoard(boolean withHelp) {
		int row = 0;
		System.out.println("     1   2   3   4   5   6   7   8");
		for (Position[] pieces : this.board) {
			System.out.println("   ---------------------------------");
			switch (row) {
				case 0: System.out.print("A "); break;
				case 1: System.out.print("B "); break;
				case 2: System.out.print("C "); break;
				case 3: System.out.print("D "); break;
				case 4: System.out.print("E "); break;
				case 5: System.out.print("F "); break;
				case 6: System.out.print("G "); break;
				case 7: System.out.print("H "); break;
			}
			for(Position pieceX : pieces) {
				System.out.print(" | " + pieceX.displayColorW());
			}
			System.out.println(" |");
			row++;
		}
		System.out.println("   ---------------------------------");
	}
	
	public void addPiece(int row, int col, String color) {
		board[row][col].setPiece(color);
		
		pieceCount++;
	}
	
	public int getWhiteCount() {
		int white_count = 0;
		for (Position[] row : this.board) {
			for (Position pos : row) {
				if (pos.getColor() == "white") {
					white_count++;
				}
			}
		}
		return white_count;	
	}
	
	public int getBlackCount() {
		int black_count = 0;
		for (Position[] row : this.board) {
			for (Position pos : row) {
				if (pos.getColor() == "black") {
					black_count++;
				}
			}
		}
		return black_count;	
	}
	
	public int getPieceCount() {
		return pieceCount;
	}
	
	public boolean hasU(int j, int i) { // has up neighbor
		if (j == 0) {
			return false;
		} else if (!this.board[j-1][i].hasPiece()) {
			return false;
		} else {
			return true;
		}
	}
	
	public boolean hasD(int j, int i) { // has down neighbor
		if (j == 7) {
			return false;
		} else if (!this.board[j+1][i].hasPiece()) {
			return false;
		} else {
			return true;
		}
	}
	
	public boolean hasR(int j, int i) { // has right neighbor
		if (i == 7) {
			return false;
		} else if (!this.board[j][i+1].hasPiece()) {
			return false;
		} else {
			return true;
		}
	}
	
	public boolean hasL(int j, int i) { // has left neighbor
		if (i == 0) {
			return false;
		} else if (!this.board[j][i-1].hasPiece()) {
			return false;
		} else {
			return true;
		}
	}
	
	public boolean hasUR(int j, int i) { // has up-right neighbor
		if (i == 7 || j == 0) {
			return false;
		} else if (!this.board[j-1][i+1].hasPiece()) {
			return false;
		} else {
			return true;
		}
	}
	
	public boolean hasUL(int j, int i) { // has up-left neighbor
		if (i == 0 || j == 0) {
			return false;
		} else if (!this.board[j-1][i-1].hasPiece()) {
			return false;
		} else {
			return true;
		}
	}
	
	public boolean hasDR(int j, int i) { // has down-right neighbor
		if (i == 7 || j == 7) {
			return false;
		} else if (!this.board[j+1][i+1].hasPiece()) {
			return false;
		} else {
			return true;
		}
	}
	
	public boolean hasDL(int j, int i) { // has down-left neighbor
		if (i == 0 || j == 7) {
			return false;
		} else if (!this.board[j+1][i-1].hasPiece()) {
			return false;
		} else {
			return true;
		}
	}
	
//
	
	public boolean isPlaceablePosition(int j, int i, String color) {
		int initial_i = i;
		int initial_j = j;
		boolean encounteredOppositeColor;
		
		if (this.board[j][i].hasPiece() == true) {
			return false; // position is already taken by another piece
		}
		
		String opposite_color;
		// determine color
		if (color == "white") {
			opposite_color = "black";
		} else {
			opposite_color = "white";
		}
		
		boolean isPlaceable = false;
		
		//*********************************************************//
		// check all directions
		// ^ up
		encounteredOppositeColor = false;
		while (hasU(j, i) && !isPlaceable) {
			j--;
			if (this.board[j][i].getColor() == opposite_color) {
				encounteredOppositeColor = true;
			} else
			
			if (this.board[j][i].getColor() == color && encounteredOppositeColor == true) {
				isPlaceable = true;
				break;
			} else {
				break;
			}
			
		}
		
		// v down
		i = initial_i;
		j = initial_j;
		encounteredOppositeColor = false;
		while (hasD(j, i) && !isPlaceable) {
			j++;
			if (this.board[j][i].getColor() == opposite_color) {
				encounteredOppositeColor = true;
			} else			
			if (this.board[j][i].getColor() == color && encounteredOppositeColor == true) {
				isPlaceable = true;
				break;
			} else {
				break;
			}
		}
		
		// > right
		i = initial_i;
		j = initial_j;
		encounteredOppositeColor = false;
		while (hasR(j, i) && !isPlaceable) {
			i++;
			if (this.board[j][i].getColor() == opposite_color) {
				encounteredOppositeColor = true;
			} else	
			if (this.board[j][i].getColor() == color && encounteredOppositeColor == true) {
				isPlaceable = true;
				break;
			} else {
				break;
			}
		}
		
		// < left
		i = initial_i;
		j = initial_j;
		encounteredOppositeColor = false;
		while (hasL(j, i) && !isPlaceable) {
			i--;
			if (this.board[j][i].getColor() == opposite_color) {
				encounteredOppositeColor = true;
			} else			
			if (this.board[j][i].getColor() == color && encounteredOppositeColor == true) {
				isPlaceable = true;
				break;
			} else {
				break;
			}
		}
		
		// ^> Up-right
		i = initial_i;
		j = initial_j;
		encounteredOppositeColor = false;
		while (hasUR(j, i) && !isPlaceable) {
			j--;
			i++;
			if (this.board[j][i].getColor() == opposite_color) {
				encounteredOppositeColor = true;
			} else			
			if (this.board[j][i].getColor() == color && encounteredOppositeColor == true) {
				isPlaceable = true;
				break;
			} else {
				break;
			}
		}
		
		// v> down-right
		i = initial_i;
		j = initial_j;
		encounteredOppositeColor = false;
		while (hasDR(j, i) && !isPlaceable) {
			j++;
			i++;
			if (this.board[j][i].getColor() == opposite_color) {
				encounteredOppositeColor = true;
			} else			
			if (this.board[j][i].getColor() == color && encounteredOppositeColor == true) {
				isPlaceable = true;
				break;
			} else {
				break;
			}
		}
		
		// ^> up-left
		i = initial_i;
		j = initial_j;
		encounteredOppositeColor = false;
		while (hasUL(j, i) && !isPlaceable) {
			j--;
			i--;
			if (this.board[j][i].getColor() == opposite_color) {
				encounteredOppositeColor = true;
			} else			
			if (this.board[j][i].getColor() == color && encounteredOppositeColor == true) {
				isPlaceable = true;
				break;
			} else {
				break;
			}
		}
		
		// v> down-left
		i = initial_i;
		j = initial_j;
		encounteredOppositeColor = false;
		while (hasDL(j, i) && !isPlaceable) {
			j++;
			i--;
			if (this.board[j][i].getColor() == opposite_color) {
				encounteredOppositeColor = true;
			} else			
			if (this.board[j][i].getColor() == color && encounteredOppositeColor == true) {
				isPlaceable = true;
				break;
			} else {
				break;
			}
		}
		
		//*********************************************************//
		return isPlaceable;
		
	}
	

	public void updateBoard(int j, int i, String color) {
		
		// new board for the memory of moves
		int initial_i = i;
		int initial_j = j;
		boolean encounteredOppositeColor;
		
		String opposite_color;
		// determine color
		if (color == "white") {
			opposite_color = "black";
		} else {
			opposite_color = "white";
		}
		
		//*********************************************************//
		LinkedList<Position> flippable_pieces = new LinkedList<Position>();
		// check all directions
		// ^ up
		encounteredOppositeColor = false;
		int encounter = 0;
		boolean success = false;
		while (hasU(j, i)) {
			j--;
			if (this.board[j][i].getColor() == opposite_color) {
				encounteredOppositeColor = true;
				encounter++;
				flippable_pieces.add(this.board[j][i]);
			}
			else if (this.board[j][i].getColor() == color && encounteredOppositeColor == true) {
				success = true;
				break;
			} else {
				break;
			}
		}
		if (success == false) {
			for (int k = 0; k < encounter; k++) {
				flippable_pieces.removeLast();
			}
		}
		
		// v down
		i = initial_i;
		j = initial_j;
		encounteredOppositeColor = false;
		encounter = 0;
		success = false;
		while (hasD(j, i)) {
			j++;
			if (this.board[j][i].getColor() == opposite_color) {
				encounteredOppositeColor = true;
				encounter++;
				flippable_pieces.add(this.board[j][i]);
			}
			else if (this.board[j][i].getColor() == color && encounteredOppositeColor == true) {
				success = true;
				break;
			} else {
				break;
			}
		}
		if (success == false) {
			for (int k = 0; k < encounter; k++) {
				flippable_pieces.removeLast();
			}
		}
		
		// > right
		i = initial_i;
		j = initial_j;
		encounteredOppositeColor = false;
		encounter = 0;
		success = false;
		while (hasR(j, i)) {
			i++;
			if (this.board[j][i].getColor() == opposite_color) {
				encounteredOppositeColor = true;
				encounter++;
				flippable_pieces.add(this.board[j][i]);
			}
			else if (this.board[j][i].getColor() == color && encounteredOppositeColor == true) {
				success = true;
				break;
			} else {
				break;
			}
		}
		if (success == false) {
			for (int k = 0; k < encounter; k++) {
				flippable_pieces.removeLast();
			}
		}
		
		// < left
		i = initial_i;
		j = initial_j;
		encounteredOppositeColor = false;
		encounter = 0;
		success = false;
		while (hasL(j, i)) {
			i--;
			if (this.board[j][i].getColor() == opposite_color) {
				encounteredOppositeColor = true;
				encounter++;
				flippable_pieces.add(this.board[j][i]);
			}
			else if (this.board[j][i].getColor() == color && encounteredOppositeColor == true) {
				success = true;
				break;
			} else {
				break;
			}
		}
		if (success == false) {
			for (int k = 0; k < encounter; k++) {
				flippable_pieces.removeLast();
			}
		}
		
		// ^> Up-right
		i = initial_i;
		j = initial_j;
		encounteredOppositeColor = false;
		encounter = 0;
		success = false;
		while (hasUR(j, i)) {
			j--;
			i++;
			if (this.board[j][i].getColor() == opposite_color) {
				encounteredOppositeColor = true;
				encounter++;
				flippable_pieces.add(this.board[j][i]);
			}
			else if (this.board[j][i].getColor() == color && encounteredOppositeColor == true) {
				success = true;
				break;
			} else {
				break;
			}
		}
		if (success == false) {
			for (int k = 0; k < encounter; k++) {
				flippable_pieces.removeLast();
			}
		}
		
		// v> down-right
		i = initial_i;
		j = initial_j;
		encounteredOppositeColor = false;
		encounter = 0;
		success = false;
		while (hasDR(j, i)) {
			j++;
			i++;
			if (this.board[j][i].getColor() == opposite_color) {
				encounteredOppositeColor = true;
				encounter++;
				flippable_pieces.add(this.board[j][i]);
			}
			else if (this.board[j][i].getColor() == color && encounteredOppositeColor == true) {
				success = true;
				break;
			} else {
				break;
			}
		}
		if (success == false) {
			for (int k = 0; k < encounter; k++) {
				flippable_pieces.removeLast();
			}
		}
		
		// v> up-left
		i = initial_i;
		j = initial_j;
		encounteredOppositeColor = false;
		encounter = 0;
		success = false;
		while (hasUL(j, i)) {
			j--;
			i--;
			if (this.board[j][i].getColor() == opposite_color) {
				encounteredOppositeColor = true;
				encounter++;
				flippable_pieces.add(this.board[j][i]);
			}
			else if (this.board[j][i].getColor() == color && encounteredOppositeColor == true) {
				success = true;
				break;
			} else {
				break;
			}
		}
		if (success == false) {
			for (int k = 0; k < encounter; k++) {
				flippable_pieces.removeLast();
			}
		}
		
		// v> down-left
		i = initial_i;
		j = initial_j;
		encounteredOppositeColor = false;
		encounter = 0;
		success = false;
		while (hasDL(j, i)) {
			j++;
			i--;
			if (this.board[j][i].getColor() == opposite_color) {
				encounteredOppositeColor = true;
				encounter++;
				flippable_pieces.add(this.board[j][i]);
			}
			else if (this.board[j][i].getColor() == color && encounteredOppositeColor == true) {
				success = true;
				break;
			} else {
				break;
			}
		}
		if (success == false) {
			for (int k = 0; k < encounter; k++) {
				flippable_pieces.removeLast();
			}
		}
		
		for (Position piece : flippable_pieces) {
			piece.flipPiece();
		}
		
		
		//*********************************************************//
	}
	
//	
	// this function has dual function:
	// 1. It updates the placeable positions for the color
	// 2. It returns whether there is even a placeable position for that color
	public boolean generatePlaceability(String color) {
		boolean placeable_position = false;
		for (int j = 0; j < 8; j++) {
			for (int i = 0; i < 8; i++) {
				if (isPlaceablePosition(j, i, color) && color == "white") {
					this.board[j][i].setWhitePlaceable();
					placeable_position = true;
				} else
				if (isPlaceablePosition(j, i, color) && color == "black") {
					this.board[j][i].setBlackPlaceable();
					placeable_position = true;
				} else {
					this.board[j][i].setNotPlaceable();
				}
				
			}
		}
		return placeable_position;
	}
	
	
	// returns a list of the available positions. To be used by the graphical interface.
	public LinkedList<Position> getPlaceablePositions(String color) {
		LinkedList<Position> availablePositions = new LinkedList<Position>();
		for (int j = 0; j < 8; j++) {
			for (int i = 0; i < 8; i++) {
				if (isPlaceablePosition(j, i, color) && color == "white") {
					this.board[j][i].setWhitePlaceable();
					availablePositions.add(this.board[j][i]);
				} else
				if (isPlaceablePosition(j, i, color) && color == "black") {
					this.board[j][i].setBlackPlaceable();
					availablePositions.add(this.board[j][i]);
				} else {
					this.board[j][i].setNotPlaceable();
				}
			}
		}
		return availablePositions;
	}
	
	// returns the positions of the Discs that are touching open squares
	public LinkedList<Position> getFrontierDiscs(String color) {
		LinkedList<Position> frontierDiscs = new LinkedList<Position>();
		int y;
		int x;
		for (Position[] row : board) {
			for (Position pos : row) {
				if (pos.getColor() == color) {
					y = pos.getRow();
					x = pos.getCol();
					if (!hasU(y,x) || !hasD(y,x) || !hasR(y,x) || !hasL(x,y) ||
							!hasUR(y,x) || !hasUL(y,x) || !hasDR(y,x) || !hasDL(y,x)) {
						frontierDiscs.add(pos); // add frontier discs of the requested color
					}
				}
			}
		}
		return frontierDiscs;
	}
	
	// returns the positions of the Discs that are NOT touching open squares
	public LinkedList<Position> getInteriorDiscs(String color) {
		LinkedList<Position> interiorDiscs = new LinkedList<Position>();
		int y;
		int x;
		for (Position[] row : board) {
			for (Position pos : row) {
				if (pos.getColor() == color) {
					y = pos.getRow();
					x = pos.getCol();
					if (hasU(y,x) && hasD(y,x) && hasR(y,x) && hasL(x,y) &&
							hasUR(y,x) && hasUL(y,x) && hasDR(y,x) && hasDL(y,x)) {
						interiorDiscs.add(pos); // add frontier discs of the requested color
					}
				}
			}
		}
		return interiorDiscs;
	}
	
	public boolean isFrontierDisc(int row, int col) {
		int y = row;
		int x = col;
		if (!hasU(y,x) || !hasD(y,x) || !hasR(y,x) || !hasL(x,y) ||
				!hasUR(y,x) || !hasUL(y,x) || !hasDR(y,x) || !hasDL(y,x)) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isInteriorDisc(int row, int col) {
		int y = row;
		int x = col;
		if (hasU(y,x) && hasD(y,x) && hasR(y,x) && hasL(x,y) &&
				hasUR(y,x) && hasUL(y,x) && hasDR(y,x) && hasDL(y,x)) {
			return true;
		} else {
			return false;
		}
	}
	
	/* Evaluates the score of the board for a specific color.
	 * To be used by the GameEngine class.
	 * 
	 * We need to maximize the number of interior discs while minimizing frontier discs
	 * We also need to consider the tile positioning.
	 * 
	 * See this diagram of the upper left quadrant of the board.
	 * 
	 * The values of the tile pieces are determines by a Standford paper linked to this video:
	 * https://www.youtube.com/watch?v=y7AKtWGOPAE&t=991s&ab_channel=KeithGalli
	*/
	public double evaluateBoardScore(String color) {
		
		String oppositeColor;
		
		if (color == "white") {
			oppositeColor = "black";
		} else {
			oppositeColor = "white";
		}
		
		int y;
		int x;
		
		double score = 0; // the score is the reward // punishment for the computer
		String positionColor;
		
		double openingStage[][] = {{0, 0, 0, 0, 0, 0, 0, 0},
								   {0, -0.02231, 0.05583, 0.02004, 0.02004, 0.05583, -0.02231, 0},
								   {0, 0.05583, 0.10126, -0.10927, -0.10927, 0.10126, 0.05583, 0},
								   {0, 0.02004, -0.10927, -0.10155, -0.10155, -0.10927, 0.02004, 0},
								   {0, 0.02004, -0.10927, -0.10155, -0.10155, -0.10927, 0.02004, 0},
								   {0, 0.05583, 0.10126, -0.10927, -0.10927, 0.10126, 0.05583, 0},
								   {0, -0.02231, 0.05583, 0.02004, 0.02004, 0.05583, -0.02231, 0},
								   {0, 0, 0, 0, 0, 0, 0, 0}};
		
		double middleStage[][] = {{6.32711, -3.32813, 0.33907, -2.00512, -2.00512, 0.33907, -3.32813, 6.32711},
								  {-3.32813, -1.52928, -1.87550, -0.18176, -0.18176, -1.87550, -1.52928, -3.32813},
								  {0.33907, -1.87550, 1.06939, 0.62415, 0.62415, 1.06939, -1.87550, 0.33907},
								  {-2.00512, -0.18176, 0.62415, 0.10539, 0.10539, 0.62415, -0.18176, -2.00512},
								  {-2.00512, -0.18176, 0.62415, 0.10539, 0.10539, 0.62415, -0.18176, -2.00512},
								  {0.33907, -1.87550, 1.06939, 0.62415, 0.62415, 1.06939, -1.87550, 0.33907},
								  {-3.32813, -1.52928, -1.87550, -0.18176, -0.18176, -1.87550, -1.52928, -3.32813},
								  {6.32711, -3.32813, 0.33907, -2.00512, -2.00512, 0.33907, -3.32813, 6.32711}};
		
		double endStage[][] = {{5.50062, -0.17812, -2.58948, -0.59007, -0.59007, -2.58948, -0.17812, 5.50062},
						       {-0.17812, 0.96804, -2.16084, -2.01723, -2.01723, -2.16084, 0.96804, -0.17812},
						       {-2.58948, -2.16084, 0.49062, -1.07055, -1.07055, 0.49062, -2.16084, -2.58948},
						       {-0.59007, -2.01723, -1.07055, 0.73486, 0.73486, -1.07055, -2.01723, -0.59007},
						       {-0.59007, -2.01723, -1.07055, 0.73486, 0.73486, -1.07055, -2.01723, -0.59007},
						       {-2.58948, -2.16084, 0.49062, -1.07055, -1.07055, 0.49062, -2.16084, -2.58948},
						       {-0.17812, 0.96804, -2.16084, -2.01723, -2.01723, -2.16084, 0.96804, -0.17812},
						       {5.50062, -0.17812, -2.58948, -0.59007, -0.59007, -2.58948, -0.17812, 5.50062}};
		
		// loop through the positions on the board
		for (Position[] row : board) {
			for (Position pos : row) {
				y = pos.getRow();
				x = pos.getCol();
				positionColor = pos.getColor();
				
				double value;
				if (pieceCount <= 12) {
					value = openingStage[y][x];
					if (positionColor == color) {
						score += value;
					} else if (positionColor == oppositeColor) {
						if (value > 0.02)
							score -= value*1.5; // punish playing color for making the wrong move
						}
				} else if (pieceCount > 12 && pieceCount <= 42) {
					value = middleStage[y][x];
					if (positionColor == color) {
						score += value;
					} else if (positionColor == oppositeColor) {
						if (value > 5)
							score -= value*3; // punish playing color for making the wrong move
						}
				} else if (pieceCount > 42) {
					value = endStage[y][x];
					if (positionColor == color) {
						score += value;
					} else if (positionColor == oppositeColor) {
						score -= value; 
					}
				}
			}
		}
		
		if (color == "black") {
			score = score - 2*score; // minimizing player
		}
		// otherwise score remains score, and white is maximizing player
		
		return score;
		
	}
	
	public boolean isGameOver() {
		if (getBlackCount() == 0 || getWhiteCount() == 0 || getPieceCount() == 64 ||
				(getPlaceablePositions("white").size() == 0 && getPlaceablePositions("black").size() == 0)) {
			return true;
		} else {
			return false;
		}
	}
	
	// Used by score evaluation! Is next to corner of the same color!
	public boolean isNextToCorner(int row, int col, String color) {
		// 
		if ((row == 0 || row == 7) && col == 1) {
			if (board[row][0].getColor() == color) {
				return true;
			} else {
				return false;
			}
		} else if ((row == 0 || row == 7) && col == 6) {
			if (board[row][7].getColor() == color) {
				return true;
			} else {
				return false;
			}
		} else if (row == 1 && (col == 0 || col == 7)) {
			if (board[0][col].getColor() == color) {
				return true;
			} else {
				return false;
			}
		} else if (row == 6 && (col == 0 || col == 7)) {
			if (board[7][col].getColor() == color) {
				return true;
			} else {
				return false;
			}
		} else if (row == 1 && col == 1) {
			if (board[0][0].getColor() == color) {
				return true;
			} else {
				return false;
			}
		} else if (row == 1 && col == 6) {
			if (board[0][7].getColor() == color) {
				return true;
			} else {
				return false;
			}
		} else if (row == 6 && col == 1) {
			if (board[7][0].getColor() == color) {
				return true;
			} else {
				return false;
			}
		} else if (row == 6 && col == 6) {
			if (board[7][7].getColor() == color) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}
	
	
	// Used by score evaluation! Checks whether a line of the same color is being built.
	public boolean isBorderLine(int row, int col, String color) {
		
		boolean metOpposingColor = false;
		// top border & bottom border
		if ((row == 0 || row == 7) && (col >=2 && col <= 5)) {
			for (int i = col; i > 0; i --) { // check left
				if(board[row][i].getColor() == "none") {
					break;
				} else if(board[row][i].getColor() != color) {
					metOpposingColor = true;
				}
			}
			for (int i = col; i < (7 - col); i ++) { // check right
				if(board[row][i].getColor() == "none") {
					break;
				} else if(board[row][i].getColor() != color) {
					metOpposingColor = true;
				}
			}
		
			// left and right border
		} else if ((row >= 2 && row <= 5) && (col == 0 || col == 7)) {
			for (int i = row; i > 0 ; i --) { // check up
				if(board[i][col].getColor() == "none") {
					break;
				} else if(board[i][col].getColor() != color) {
					metOpposingColor = true;
				}
			}
			for (int i = row; i < (7 - row); i ++) { // check down
				if(board[i][col].getColor() == "none") {
					break;
				} else if(board[i][col].getColor() != color) {
					metOpposingColor = true;
				}
			}
		}
		
		return !metOpposingColor;
	}
	
	public void setCurrentColor(String currentColor) {
		this.currentColor = currentColor;
	}
	
	public String getCurrentColor() {
		return this.currentColor;
	}
// end
}
