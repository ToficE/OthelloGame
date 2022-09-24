package frontEnd;
import java.util.Scanner;

import javax.swing.JFrame;

import backEnd.Board;

public class Main {

	public static void main(String[] args) {
		//consoleGame(); // back-end validation

		MainMenu mainMenu = new MainMenu(); // full game
	}
	
	// this function is to test and validate the functioning of the game
	public static void consoleGame() {
 		Board board = new Board();
		board.setInitialPieces();
		// game UI
		System.out.println("WELCOME TO OTHELLO");
		
		while (board.getPieceCount() < 64) {
			int row = 0;
			int col = 0;
			String letter = "";
			Scanner userInput = new Scanner(System.in);
			
			// white turn
			if (board.getPieceCount() % 2 == 0 && board.generatePlaceability("white") == true) {
				System.out.println("White plays!");
				board.displayBoard(true);
				// row input
				System.out.println("Enter a row letter: ");
				letter = userInput.next();
				while (LetterToRow(letter) == -1) { // invalid letter input
					System.out.println("Invalid row.");
					System.out.println("Enter a row: ");
					letter = userInput.nextLine();
				}
				row = LetterToRow(letter); 
				
				// column input
				System.out.print("Enter a column number: ");
				col = userInput.nextInt();
				while (col < 1 || col > 8) { // invalid column input
					System.out.println("Invalid column number.");
					System.out.println("Enter a column number: ");
					col = userInput.nextInt();
				}
				col--;
				
				// invalid free position
				while (board.isPlaceablePosition(row, col, "white") == false) {
					System.out.println("Enter a legal position.");
					System.out.print("Enter a row letter: ");
					letter = userInput.next();
					while (LetterToRow(letter) == -1) {
						System.out.println("Invalid row.");
						System.out.println("Enter a row: ");
						letter = userInput.nextLine();
					}
					
					row = LetterToRow(letter);
					System.out.print("Enter a column number: ");
					col = userInput.nextInt();
					while (col < 1 || col > 8) {
						System.out.println("Invalid column number.");
						System.out.println("Enter a column number: ");
						col = userInput.nextInt();
						col--;
					}
				}
				
				// add new, update board, display board
				board.addPiece(row, col, "white");
				board.updateBoard(row, col, "white");
			}
			
			// black turn
			if (board.getPieceCount() % 2 != 0 && board.generatePlaceability("black") == true) {
				System.out.println("Black plays!");
				board.generatePlaceability("black");
				board.displayBoard(true);
				// row input
				System.out.println("Enter a row letter: ");
				letter = userInput.nextLine();
				while (LetterToRow(letter) == -1) { // invalid letter input
					System.out.println("Invalid row.");
					System.out.println("Enter a row: ");
					letter = userInput.nextLine();
				}
				row = LetterToRow(letter); 
				
				// column input
				System.out.print("Enter a column number: ");
				col = userInput.nextInt();
				while (col < 1 || col > 8) { // invalid column input
					System.out.println("Invalid column number.");
					System.out.println("Enter a column number: ");
					col = userInput.nextInt();
				}
				col--;
				
				// invalid free position
				while (board.isPlaceablePosition(row, col, "black") == false) {
					System.out.println("Enter a legal position.");
					System.out.print("Enter a row letter: ");
					letter = userInput.nextLine();
					while (LetterToRow(letter) == -1) {
						System.out.println("Invalid row.");
						System.out.println("Enter a row: ");
						letter = userInput.nextLine();
					}
					
					row = LetterToRow(letter);
					System.out.print("Enter a column number: ");
					col = userInput.nextInt();
					while (col < 1 || col > 8) {
						System.out.println("Invalid column number.");
						System.out.println("Enter a column number: ");
						col = userInput.nextInt();
						col--;
					}
				}
				
				// add new, update board, display board
				board.addPiece(row, col, "black");
				board.updateBoard(row, col, "black");
			}			
		}
		
		System.out.println("Thank you for playing!");
		System.out.println("The winner is");
		if (board.getBlackCount() > board.getWhiteCount()) {
			System.out.print(" BLACK!");
		} else if (board.getBlackCount() < board.getWhiteCount()) {
			System.out.print(" WHITE!");
		} else {
			System.out.print("... No one! It's a tie!");
		}
		
		System.out.println("BLACK POINTS: " + board.getBlackCount() +"   WHITE POINTS: " + board.getWhiteCount());
	}
	
	public static int LetterToRow(String letter) {
		switch(letter) {
		case "A": return 0;
		case "B": return 1;
		case "C": return 2;
		case "D": return 3;
		case "E": return 4;
		case "F": return 5;
		case "G": return 6;
		case "H": return 7;
		default: return -1;
		}
	}
}