package backEnd;

import java.util.LinkedList;

import backEnd.GameEngine.GameTree.GameState;


// this game engine needs a complete make-over!
public class GameEngine {
	private Board startState;
	private int depth;
	private String initialColor;
	private GameTree gameTree;
	
	// game engine constructor
	public GameEngine(Board startState, int depth, String initialColor) {
		this.startState = startState;
		this.depth = depth;
		this.initialColor = initialColor;
		
		// create decision tree
		gameTree = new GameTree(this.startState, this.depth, this.initialColor);
	}
	
	/*
	 * This function is the minimax algorithm:
	 * Used to find the best possible path.
	 * 
	 * 
	 */
	public Position bestPlaceablePosition(backEnd.GameEngine.GameTree.GameState gameState, int depth, String color) {
		Position bestMove = null;
		double score = minimax(gameState, depth, /*-1000000000, 100000000,*/ color);
		System.out.println("Score: " + score);
		for (GameState gState : gameState.nextStates) {
			if (score == gState.score) {
				bestMove = gState.lastMove;
			}
		}
		return bestMove; // if null is returned, there is an error!
	}
	
	public GameTree getTree() {
		return this.gameTree;
	}
	
	public double minimax(backEnd.GameEngine.GameTree.GameState gameState, int depth, /*double alpha, double beta,*/ String color) {
		if (depth == 0 || gameState.aBoard.isGameOver()) {
			gameState.setScore(gameState.aBoard.evaluateBoardScore(color));
			return gameState.getScore();
		} 
		
		if (color == "white") { // maximizing player
			
			double maxEval = -10000000;
			double eval;
			for (GameState childState : gameState.nextStates) {
				eval = minimax(childState, depth-1,/* alpha, beta, */childState.currentColor);
				maxEval = Math.max(maxEval, eval);
				/*alpha = Math.max(alpha, eval);
				
				if  (beta <= alpha) {
					break;
				}*/
			}
			gameState.score = maxEval;
			return gameState.score;
		} else if (color == "black") { // minimizing player
			
			double minEval = 100000000;
			double eval;
			for (GameState childState : gameState.nextStates) {
				eval = minimax(childState, depth-1, /*alpha, beta,*/ childState.currentColor);
				minEval = Math.min(minEval, eval);
				/*beta = Math.min(beta, eval);
				
				if (beta <= alpha) {
					break;
				}*/
			}
			gameState.score = minEval;
			return gameState.score;
			
		} return 123456; // if this number is returned, there is an error in the evaluation!
	}
	
	// inner class
	public class GameTree {
		
		private Board startState;
		private int depth;
		private String initialColor;
		private GameState rootNode;

		public GameTree(Board startState, int depth, String initialColor) {
			this.startState = startState;
			this.depth = depth;
			this.initialColor = initialColor;
			
			GameState rootNode = new GameState(startState, initialColor);
			this.rootNode = rootNode;
			
			recursiveTreeBuild(this.rootNode, depth, initialColor);
		}
		
		public GameState getRootNode() {
			return this.rootNode;
		}
		
		// problem in this function obviously!
		public void recursiveTreeBuild(GameState initialState, int depth, String initialColor) {
			if (depth > 0) {
				initialState.newBoardStates(initialState.aBoard);
				depth--;
				for (GameState gState : initialState.nextStates) {
					recursiveTreeBuild(gState, depth, gState.currentColor);
				}
			}
		}
		
		/*
		 * AKA Tree Node
		 * This class is responsible for storing a game state and evaluating the score of such a state
		 */
		// inner-inner class
		public class GameState {
			private Board aBoard;
			private LinkedList<Board> nextBoards;
			private LinkedList<GameState> nextStates;
			private String currentColor;
			private String nextColor;
			double score;
			Position lastMove;
			
			// constructor
			public GameState(Board aBoard, String currentColor) {
				this.aBoard = aBoard;
				LinkedList<Board> nextBoards = new LinkedList<Board>();
				this.nextBoards = nextBoards;
				newBoardStates(aBoard);
				this.currentColor = currentColor;
				this.score = 0; // initialize score
			}
			
			// SETTERS
			
			// this function creates a deep copy
			private void saveBoardState(Board aBoard) {
				try {
					Board boardClone = aBoard.clone();
					this.nextBoards.addLast(boardClone);
				} catch (CloneNotSupportedException ex) {
				    ex.printStackTrace();
				}
			}
			
			/*
			 * This function creates subsequent board states of all the placeable positions.
			 */
			public void newBoardStates(Board gState) {
				
				LinkedList<GameState> nextStates = new LinkedList<GameState>();
				// positions to iterate through
				LinkedList<Position> availablePositions = gState.getPlaceablePositions(currentColor);
				int stateSize = availablePositions.size();
				
				// create duplicate states
				for (int i = 0; i < stateSize; i++) {
					saveBoardState(gState);
				}
				
				int k = 0;
				int aRow;
				int aCol;
		
				for (Board aBoard : this.nextBoards) {
					// get available position
					aRow = availablePositions.get(k).getRow();
					aCol = availablePositions.get(k).getCol();
					
					// update board
					Position lastMove = new Position(aRow, aCol);
					aBoard.addPiece(aRow, aCol, currentColor);
					aBoard.updateBoard(aRow, aCol, currentColor);
					
					// ... and update placeability
					// swap if placeable for next color
					nextColor();
					if (!aBoard.getPlaceablePositions(currentColor).isEmpty()) {
						aBoard.generatePlaceability(currentColor);
					} else {
						nextColor(); // revert back to next color
						aBoard.generatePlaceability(currentColor);
					}
					
					// create a new game state and add the state to the board.
					GameState aState = new GameState(aBoard, currentColor);
					aState.setLastMove(lastMove);
					nextStates.add(aState);
					
				}
				setNextStates(nextStates);
			}
			
			public void setNextStates(LinkedList<GameState> nextStates) {
				this.nextStates = nextStates;
			}
			
			public void setLastMove(Position lastMove) {
				this.lastMove = lastMove;
			}
			
			// change color
			public void nextColor() {
				if (currentColor == "white") {
					nextColor = "black";
				} else {
					nextColor = "white";
				}
				String tmp = currentColor;
				currentColor = nextColor;
				nextColor = tmp;
			}
			
			// GETTERS
			
			public double getScore() {
				return this.score;
			}
			
			
			public LinkedList<GameState> getGameStates() {
				return this.nextStates;
			}
			
			
			public Position getLastMove() {
				return this.lastMove;
			}
			
			public void setScore(double score) {
				this.score = score;
			}
			
		} // end of inner-inner class
		
	}// end of inner class
	
}
