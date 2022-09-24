package frontEnd;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.Border;

import backEnd.Board;
import backEnd.GameEngine;
import backEnd.GameEngine.GameTree.GameState;
import backEnd.Position;

public class PlayerVsComputer{
	// instance variables
	
	private static final Dimension OUTER_FRAME_DIMENSION = new Dimension(800, 800);
	private static final Dimension BOARD_PANEL_DIMENSION = new Dimension(400, 400);
	private static final Dimension TILE_PANEL_DIMENSION = new Dimension(10, 10);
	private static final Dimension SIDE_PANEL_DIMENSION = new Dimension(52, 800);
	public Board othelloBoard;
	private LinkedList<Board> boardMemory = new LinkedList<Board>();
	
	private JPanel gameOverMenu;
	
	private final JFrame gameFrame;
	private final BoardPanel boardPanel;
	private PieceCountPanel pieceCountPanel;
	//private JLayeredPane gameOver;
	private JPanel buttonMenu;
	
	private String current_color = "black";
	private String next_color = "white";
	
	private int reviewIndex = 0;
	
	boolean isPlayerTurn;
	boolean isComputerTurn;
	boolean inGameModeEnabled = true; // set to false to disable some buttons and user clicking on board
	
	private boolean isGameOver = false;
	private String winnerColor;
	
	
	// game engine
	private GameEngine engine;
	int INITIAL_DEPTH = 6;
	private String playerColor;
	
	public PlayerVsComputer(String playerColor) {
		System.out.println("playerColor: "+ playerColor);
		// prepare game frame
		this.gameFrame = new JFrame("Othello");
		this.gameFrame.setLayout(new BorderLayout());
		final JMenuBar tableMenuBar = createTableMenuBar();
		this.gameFrame.setJMenuBar(tableMenuBar);
		this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
		this.gameFrame.setResizable(false);
		
		// generate new board
		othelloBoard = new Board();
		this.othelloBoard.setInitialPieces();
		othelloBoard.generatePlaceability("black");
		othelloBoard.setCurrentColor("black");
		
		buttonMenu = createButtonMenu();
		this.gameFrame.add(buttonMenu, BorderLayout.SOUTH);
		
		pieceCountPanel = new PieceCountPanel();
		
		JPanel RsidePanel = new JPanel();
		RsidePanel.setPreferredSize(SIDE_PANEL_DIMENSION);
		RsidePanel.setBackground(Color.LIGHT_GRAY);
		
		JPanel LsidePanel = new JPanel();
		LsidePanel.setPreferredSize(SIDE_PANEL_DIMENSION);
		LsidePanel.setBackground(Color.LIGHT_GRAY);
		
		// add board GUI
		this.boardPanel = new BoardPanel();
		this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
		this.gameFrame.add(this.pieceCountPanel, BorderLayout.NORTH);
		this.gameFrame.add(LsidePanel, BorderLayout.WEST);
		this.gameFrame.add(RsidePanel, BorderLayout.EAST);
		this.gameFrame.setVisible(true);
		
		this.playerColor = playerColor;
		if (this.playerColor == "white") {
			isPlayerTurn = false;
			isComputerTurn = true;
		} else {
			isPlayerTurn = true;
			isComputerTurn = false;
		}
		
		saveBoardState();
		// add count panel
		boardPanel.drawBoard(othelloBoard);
		pieceCountPanel.updateDisplay(othelloBoard, current_color);
		
		
		// add undo button
		/*
		JButton undoButton = createUndoButton();
		JButton quitButton = createQuitButton();
		this.gameFrame.add(undoButton, BorderLayout.SOUTH);
		this.gameFrame.add(quitButton, BorderLayout.SOUTH);
		
		
		buttonMenu = createButtonMenu();
		this.gameFrame.add(buttonMenu, BorderLayout.SOUTH);
		
		// add count panel
		pieceCountPanel.updateDisplay(boardMemory.getLast(), current_color);
		
		/*
		JPanel RsidePanel = new JPanel();
		RsidePanel.setPreferredSize(SIDE_PANEL_DIMENSION);
		RsidePanel.setBackground(Color.LIGHT_GRAY);
		
		JPanel LsidePanel = new JPanel();
		LsidePanel.setPreferredSize(SIDE_PANEL_DIMENSION);
		LsidePanel.setBackground(Color.LIGHT_GRAY);
		
		// add board GUI
		this.boardPanel = new BoardPanel();
		this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
		this.gameFrame.add(this.pieceCountPanel, BorderLayout.NORTH);
		this.gameFrame.add(LsidePanel, BorderLayout.WEST);
		this.gameFrame.add(RsidePanel, BorderLayout.EAST);
		this.gameFrame.setVisible(true);
		*/
		/*
		 * test labels for the gameOver display
		 */
	}
	
	private JMenuBar createTableMenuBar() {
		final JMenuBar tableMenuBar = new JMenuBar();
		tableMenuBar.add(createFileMenu());
		return tableMenuBar;
	}
	
	private JPanel createGameOverMenu(String winnerColor) {
		gameOverMenu = new JPanel();
		gameOverMenu.setLayout(new GridLayout(2, 1));
		
		JPanel gameOverText = new JPanel();
		gameOverText.setBackground(Color.gray);
		gameOverText.add(new JLabel("Game Over! " + winnerColor));
		
		gameOverMenu.add(gameOverText);
		
		JPanel gameOverOptions = new JPanel();
		gameOverOptions.setLayout(new GridLayout(1, 3));
		
		JButton quitButton = createQuitButton();
		JButton menuButton = createMenuButton();
		JButton reviewButton = createReviewGameButton();
		
		gameOverOptions.add(quitButton);
		gameOverOptions.add(menuButton);
		gameOverOptions.add(reviewButton);
		
		gameOverMenu.add(gameOverOptions);
		
		return gameOverMenu;
	}
	
	private JPanel createButtonMenu() {
		final JPanel buttonMenu = new JPanel();
		buttonMenu.setLayout(new GridLayout());
		
		if (inGameModeEnabled) {
			JButton undoButton = createUndoButton();
			JButton quitButton = createQuitButton();
			JButton menuButton = createMenuButton();
			JButton engineButton = createEngineMoveButton();
			JButton forfeitButton = createForfeitButton();
			buttonMenu.add(quitButton);
			buttonMenu.add(undoButton);
			buttonMenu.add(menuButton);
			buttonMenu.add(engineButton);
			buttonMenu.add(forfeitButton);
		} else {
			JButton quitButton = createQuitButton();
			JButton menuButton = createMenuButton();
			JButton backButton = createBackButton();
			JButton nextButton = createNextButton();
			buttonMenu.add(quitButton);
			buttonMenu.add(menuButton);
			buttonMenu.add(backButton);
			buttonMenu.add(nextButton);
		}
		
		return buttonMenu;
	}
	
	private JButton createEngineMoveButton() {
		JButton engineButton = new JButton("Computer Move");
		
		engineButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				// only if it is the computer's turn, then make the move.
				if (isComputerTurn) {
					computerPlays();
				}
			}
		});
		return engineButton;
	}
	
	private JButton createReviewGameButton() {
		JButton reviewButton = new JButton("Review Game");
		reviewButton.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {
            	/*
            	 * Add a delay to prevent button from being spam clicked.
            	 */

            	inGameModeEnabled = false; // prevent user from clicking on the boxes of the board
            	gameOverMenu.setVisible(false);; // remove the game over menu
            	buttonMenu.setVisible(false);; // remove original button menu
            	
            	// add back the game UI
            	boardPanel.setVisible(true);
            	gameFrame.add(boardPanel);
            	
            	othelloBoard = boardMemory.get(0);
            	current_color = othelloBoard.getCurrentColor();
            	boardPanel.drawBoard(othelloBoard);
				pieceCountPanel.updateDisplay(othelloBoard, current_color);
				
				buttonMenu = createButtonMenu();
				gameFrame.add(buttonMenu, BorderLayout.SOUTH);
            	
            	// create a new game frame with new buttons (Back, Forward Buttons), and standard (Quit and MainMenu buttons)
            	
            	
            	
            }
        });
        return reviewButton;
	}
	
	
	private JButton createQuitButton() {
		JButton quitButton = new JButton("Quit");
        quitButton.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {
            	/*
            	 * Add a delay to prevent button from being spam clicked.
            	 */
            	try {
					Thread.sleep(100);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            	
            	// quit program
            	System.exit(0);
            	
            }
        });
        return quitButton;
	}
	
	private JButton createMenuButton() {
		JButton menuButton = new JButton("Main Menu");
		
		 menuButton.addActionListener(new ActionListener() {
	            
	            public void actionPerformed(ActionEvent e) {
	            	/*
	            	 * Add a delay to prevent button from being spam clicked.
	            	 */
	            	try {
						Thread.sleep(100);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	            	
	            	// quit program and return to menu
	            	gameFrame.dispatchEvent(new WindowEvent(gameFrame, WindowEvent.WINDOW_CLOSING)); // close game
	            	MainMenu mainMenu = new MainMenu();
	            	
	            }
	     });
	     return menuButton;
	}
	
	private JButton createForfeitButton() {
		JButton forfeitButton = new JButton("Forfeit Game");
		forfeitButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				if (othelloBoard.getCurrentColor() == playerColor) {
					winnerColor = "You lose from forfeit!";
					isGameOver = true;
					inGameModeEnabled = false;
					
					JPanel gameOverMenu = createGameOverMenu(winnerColor);
					//layeredPane.setBounds(200, 200, 300, 300);
					boardPanel.setVisible(false);
					gameFrame.remove(boardPanel);
					gameFrame.remove(buttonMenu);
					gameFrame.add(gameOverMenu);
				}
			}
		});
		
		return forfeitButton;
	}
	
	//================================================================//
	//=====================REVIEW GAME MODE===========================//
	//================================================================//
	private JButton createNextButton() {
		JButton nextButton = new JButton("->");
		nextButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
            	try {
					Thread.sleep(100);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            	
            	// REVIEW MODE
            	if (reviewIndex < boardMemory.size() - 1) {
        			reviewIndex++;
            	} else {
            		reviewIndex = 0; // allow for cyclic looping (from start to end state with one click)
            	}
    			othelloBoard = boardMemory.get(reviewIndex);
    			current_color = othelloBoard.getCurrentColor();
                boardPanel.drawBoard(othelloBoard);
				pieceCountPanel.updateDisplay(othelloBoard, current_color);
			}
		});
		return nextButton;
	}
	
	private JButton createBackButton() {
		JButton backButton = new JButton("<-");
		backButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
            	try {
					Thread.sleep(100);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            	
            	if (reviewIndex > 0) {
            		// REVIEW MODE
        			reviewIndex--;
            	} else {
            		reviewIndex = boardMemory.size() - 1;
            	}
    			othelloBoard = boardMemory.get(reviewIndex);
    			current_color = othelloBoard.getCurrentColor();
                boardPanel.drawBoard(othelloBoard);
				pieceCountPanel.updateDisplay(othelloBoard, current_color);
			}
		});
		return backButton;
	}
	//================================================================//
	
	
	private JButton createUndoButton() {
		JButton undoButton = new JButton("Undo Move");
        undoButton.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {
            	/*
            	 * Add a delay to the button to avoid user from clicking multiple times too fast.
            	 * Clicking this button too fast will stack calls while the memory states
            	 * are being copies/erased.
            	 */
            	try {
					Thread.sleep(100);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            	
            	// Avoid first state of board being erased from memory.
            	if (boardMemory.size() > 1) {
	            		// remove the current state
		                boardMemory.removeLast();
		                
		                // obtain the previous state
		                othelloBoard = boardMemory.getLast();
						
						
		                // maintain turn
		                current_color = othelloBoard.getCurrentColor();
		                if (current_color == getComputerColor()) {
		                	isComputerTurn = true;
		                	isPlayerTurn = false;
		                } else {
		                	isComputerTurn = false;
		                	isPlayerTurn = true;
		                }
						
					    // re-generate placeability for this state
		                current_color = othelloBoard.getCurrentColor();
						
						// update GUI
		                boardPanel.drawBoard(othelloBoard);
						pieceCountPanel.updateDisplay(othelloBoard, current_color);
						
				
            	} else {
            		System.out.println("No states in memory. Click slower.");
            	}
            }
        });
        return undoButton;
	}
	
	private JMenu createFileMenu() {
		final JMenu fileMenu = new JMenu("File");
		
		final JMenuItem openPGN = new JMenuItem("Load PGN File");
		openPGN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("open up that pgn file!");
			}
		});
		fileMenu.add(openPGN);
		
		final JMenuItem exitMenuItem = new JMenuItem("Exit");
		exitMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		fileMenu.add(exitMenuItem);
		
		return fileMenu;
	}
	
	private class BoardPanel extends JPanel {
		final List<TilePanel> boardTiles;
		
		BoardPanel() {
			super(new GridLayout(8, 8));
			this.boardTiles = new ArrayList<>();
			for (int i = 0; i < 64; i++) {
				final TilePanel tilePanel = new TilePanel(this, i);
				this.boardTiles.add(tilePanel);
				add(tilePanel);
			}
			setPreferredSize(BOARD_PANEL_DIMENSION);
			setMinimumSize(BOARD_PANEL_DIMENSION);
			setMaximumSize(BOARD_PANEL_DIMENSION);
			validate();
		}
		
		public void drawBoard(final Board board) {
			removeAll();
			for (final TilePanel tilePanel : boardTiles) {
				tilePanel.drawTile(board);
				add(tilePanel);
			}
			validate();
			repaint();
		}
	}
	
	private class TilePanel extends JPanel {
		private final int tileId;
		
		TilePanel(final BoardPanel boardPanel, final int tileId) {
			super(new GridBagLayout());
			this.tileId = tileId;
			setPreferredSize(TILE_PANEL_DIMENSION);
			setMinimumSize(TILE_PANEL_DIMENSION);
			setMaximumSize(TILE_PANEL_DIMENSION);
			setBackground(new Color(0x485d3f));
			Border blackline =  BorderFactory.createLineBorder(Color.black);
			this.setBorder(blackline);
			assignTilePiece(othelloBoard);
			
			addMouseListener(new MouseListener() {

				@Override
				public void mouseClicked(MouseEvent e) {
					// tmp = temporary variable to store the color when switching turns.
					String tmp;
					
					// obtain row and col from tileId
					int row = tileId/8;
					int col = tileId%8;
					
					// start over with the logic
					
					// this section should only be for the playerSide in reality
					// what if the computer starts the game? How is it supposed to reach this code?
					// that's why it's important to have a process happening in the background.
					// TODO: create a function that allows the computer to play as long as there are valid moves.
					// TODO: parameters it should take? None? It will access and update the board from the instance variable
					
					// place a new piece if the new piece is placeable.
					if (othelloBoard.isPlaceablePosition(row, col, playerColor) && isPlayerTurn && inGameModeEnabled) {
						current_color = playerColor;
						othelloBoard.setCurrentColor(playerColor);
						// update the board
						othelloBoard.addPiece(row, col, playerColor);
						othelloBoard.updateBoard(row, col, playerColor);
				
						
						// update turns
						// if there ARE placeable positions for the next color, change turns, OTHERWISE maintain turns.
						// ASLO only switch turns if the game is not over yet
						othelloBoard.generatePlaceability(getComputerColor());
						if (!othelloBoard.getPlaceablePositions(getComputerColor()).isEmpty() && !isGameOver) {
							// here the computer must play!
							isComputerTurn = true;
							isPlayerTurn = false;
							current_color = getComputerColor();
							othelloBoard.setCurrentColor(current_color);
							boardPanel.drawBoard(othelloBoard);
							pieceCountPanel.updateDisplay(othelloBoard, current_color);
							saveBoardState();
							
						} else {
							othelloBoard.generatePlaceability(playerColor);
							current_color = playerColor;
							// update the GUI
							boardPanel.drawBoard(othelloBoard);
							pieceCountPanel.updateDisplay(othelloBoard, playerColor);
							saveBoardState();
							
						}
						
						checkGameOver(); // adding the game over display
						
					}
				}
				

				@Override
				public void mousePressed(MouseEvent e) {
					/*
					// TODO Auto-generated method stub
					// tmp = temporary variable to store the color when switching turns.
					String tmp;
					
					// obtain row and col from tileId
					int row = tileId/8;
					int col = tileId%8;
					
					// start over with the logic
					
					// this section should only be for the playerSide in reality
					// what if the computer starts the game? How is it supposed to reach this code?
					// that's why it's important to have a process happening in the background.
					// TODO: create a function that allows the computer to play as long as there are valid moves.
					// TODO: parameters it should take? None? It will access and update the board from the instance variable
					
					// place a new piece if the new piece is placeable.
					if (othelloBoard.isPlaceablePosition(row, col, playerColor)) {
						othelloBoard.setCurrentColor(playerColor);
						current_color = playerColor;
						System.out.println(current_color);
						// update the board
						othelloBoard.addPiece(row, col, playerColor);
						othelloBoard.updateBoard(row, col, playerColor);
						// update GUI
						boardPanel.drawBoard(othelloBoard);
						pieceCountPanel.updateDisplay(othelloBoard, playerColor);
						saveBoardState();
						
						// [win/loss] cases
						if (othelloBoard.getBlackCount() == 0 || othelloBoard.getWhiteCount() == 0 || othelloBoard.getPieceCount() == 64
								|| (othelloBoard.getPlaceablePositions(next_color).isEmpty() && othelloBoard.getPlaceablePositions(current_color).isEmpty())) {
							if (othelloBoard.getBlackCount() == 0 || othelloBoard.getWhiteCount() > othelloBoard.getBlackCount()) {
								winnerColor = "White wins!";
							} else
							if (othelloBoard.getWhiteCount() == 0 || othelloBoard.getWhiteCount() < othelloBoard.getBlackCount()) {
								winnerColor = "Black wins!";
							} else {
								winnerColor = "It's a tie!";
							}
							isGameOver = true;
						} 
						
						/*
						// update turns
						// if there ARE placeable positions for the next color, change turns, OTHERWISE maintain turns.
						// ASLO only switch turns if the game is not over yet
						othelloBoard.generatePlaceability(getComputerColor());
						if (!othelloBoard.getPlaceablePositions(getComputerColor()).isEmpty() && !isGameOver) {
							// here the computer must play!
							computerPlays();
							
						} else {
							othelloBoard.generatePlaceability(playerColor);
							
							// update the GUI
							boardPanel.drawBoard(othelloBoard);
							pieceCountPanel.updateDisplay(othelloBoard, playerColor);
						}
					
						/////////////////
						// GAME OVER PANE
				
						if (isGameOver) {
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
							JPanel gameOverMenu = createGameOverMenu(winnerColor);
							//layeredPane.setBounds(200, 200, 300, 300);
							boardPanel.setVisible(false);
							gameFrame.remove(boardPanel);
							gameFrame.remove(buttonMenu);
							gameFrame.add(gameOverMenu);
							
						}
						// adding the game over display
					}
					*/
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					/*
					// TODO Auto-generated method stub
					// update turns
					// if there ARE placeable positions for the next color, change turns, OTHERWISE maintain turns.
					// ASLO only switch turns if the game is not over yet
					othelloBoard.generatePlaceability(getComputerColor());
					if (!othelloBoard.getPlaceablePositions(getComputerColor()).isEmpty() && !isGameOver) {
						// here the computer must play!
						othelloBoard.setCurrentColor(getComputerColor());
						computerPlays();
						
					} else {
						othelloBoard.setCurrentColor(playerColor);
						othelloBoard.generatePlaceability(playerColor);
						
						// update the GUI
						boardPanel.drawBoard(othelloBoard);
						pieceCountPanel.updateDisplay(othelloBoard, playerColor);
					} */
				} 

				@Override
				public void mouseEntered(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
			});
			validate();
		}
		
		public void drawTile(final Board board) {
			assignTilePiece(board);
			validate();
			repaint();
		}
		
		
		private void assignTilePiece(final Board board) {
			this.removeAll();
			// obtain position on the board from the tileId
			int row = tileId/8;
			int col = tileId%8;
			String color = board.getPosition(row, col).getColor();
			
			if (color == "white") {
				try {
					final BufferedImage image = ImageIO.read(new File("images/white_piece.png"));
				    Image _image = image.getScaledInstance(68, 68, image.SCALE_SMOOTH);
				    add(new JLabel(new ImageIcon(_image), JLabel.CENTER));
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if (color == "black") {
				try {
					final BufferedImage image = ImageIO.read(new File("images/black_piece.png"));
				    Image _image = image.getScaledInstance(69, 69, image.SCALE_SMOOTH);
				    add(new JLabel(new ImageIcon(_image), JLabel.CENTER));
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if (board.getPosition(row, col).isBlackPlaceable() && current_color == "black") {
				try {
					final BufferedImage image = ImageIO.read(new File("images/place.png"));
				    Image _image = image.getScaledInstance(68, 68, image.SCALE_SMOOTH);
				    add(new JLabel(new ImageIcon(_image), JLabel.CENTER));
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if (board.getPosition(row, col).isWhitePlaceable() && current_color == "white") {
				try {
					final BufferedImage image = ImageIO.read(new File("images/place.png"));
				    Image _image = image.getScaledInstance(68, 68, image.SCALE_SMOOTH);
				    add(new JLabel(new ImageIcon(_image), JLabel.CENTER));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void saveBoardState() {
		try {
			Board othelloState = othelloBoard.clone();
			boardMemory.addLast(othelloState);
		} catch (CloneNotSupportedException ex) {
		    ex.printStackTrace();
		}
	}
	
	// used by review mode option
	public LinkedList<Board> getBoardMemory() {
		return this.boardMemory;
	}
	
	public String getComputerColor() {
		String computerColor;
		if (this.playerColor == "white") {
			computerColor = "black";
		} else {
			computerColor = "white";
		}
		return computerColor;
	}
	
	// perhaps a parameter it can take is the difficulty
	public void computerPlays() {
		// what do we need to do here?
		// generate a decision tree of the current board state
		// the tree should return all the possible next states
		// based on the difficuty, it can pick a range of the states. LEVELS: EASY | MEDIUM | HARD | IMPOSSIBLE
		// TO add later.
		String computerColor = getComputerColor();
		
		// while the player has no possible moves, the computer keeps playing!
		if (!isGameOver) {
			othelloBoard.setCurrentColor(computerColor);
			int pieceCount = othelloBoard.getPieceCount();
			othelloBoard.generatePlaceability(computerColor);
			current_color = computerColor;
			
			if (pieceCount <= 12) {
				INITIAL_DEPTH = 5;
			} else if (pieceCount > 12 && pieceCount <= 44) {
				INITIAL_DEPTH = 5;
			} else {
				INITIAL_DEPTH = 5;
			}
			
			engine = new GameEngine(othelloBoard, INITIAL_DEPTH, computerColor);
			
			// get the best move
			Position bestMove = engine.bestPlaceablePosition(engine.getTree().getRootNode(), INITIAL_DEPTH, computerColor);
			System.out.println("first children size: " + engine.getTree().getRootNode().getGameStates().size());
			
			LinkedList<GameState> possibleMoves = engine.getTree().getRootNode().getGameStates();
			
			// Insertion sort
			// generate the range of possibleMoves, then sort the moves
			
			for (int i = 1; i < possibleMoves.size(); i++) {
				GameState current = possibleMoves.get(i);
				int j = i - 1;
				while (j > 1 && possibleMoves.get(j).getScore() > current.getScore()) {
					possibleMoves.set(j + 1, possibleMoves.get(j));
					j--;
				}
				possibleMoves.set(j + 1, current);
			}
			
			//System.out.println("possible moves size: " + possibleMoves.size());
			Random rand = new Random();
			int movesSize = possibleMoves.size();
			int randomPos = rand.nextInt(movesSize-1);
			System.out.println("random position: " + randomPos);
			
			
			Position randomMove = possibleMoves.get(movesSize-1).getLastMove();
			// Let's just stick with the bestMove for now...
			// get the coordinates of the best move
			int y = bestMove.getRow();
			int x = bestMove.getCol();
			
			int randY = randomMove.getRow();
			int randX = randomMove.getCol();
			
			// add the piece to the board, update the board
			//othelloBoard.addPiece(y, x, computerColor);
			//othelloBoard.updateBoard(y, x, computerColor);
			
			othelloBoard.addPiece(randY, randX, computerColor);
			othelloBoard.updateBoard(randY, randX, computerColor);
			
			// update placeability for player!
			othelloBoard.generatePlaceability(playerColor);
			// break from the loop if there are placeable positions for the player
			if (!othelloBoard.getPlaceablePositions(playerColor).isEmpty() && !isGameOver) {
				// update turns
				current_color = playerColor;
				isPlayerTurn = true;
				isComputerTurn = false;
				// update the GUI
				othelloBoard.setCurrentColor(playerColor);
				boardPanel.drawBoard(othelloBoard);
				pieceCountPanel.updateDisplay(othelloBoard, playerColor);
				
				// save board for undo option
				saveBoardState();
			} else {
				// update the GUI
				current_color = computerColor;
				othelloBoard.generatePlaceability(computerColor);
				boardPanel.drawBoard(othelloBoard);
				pieceCountPanel.updateDisplay(othelloBoard, current_color);
				
				// save board for undo option
				saveBoardState();
			}
			
			checkGameOver();
			
		}
		
	} // end of function computerPlays()
	
	private void checkGameOver() {
		// [win/loss] cases
		if (othelloBoard.getBlackCount() == 0 || othelloBoard.getWhiteCount() == 0 || othelloBoard.getPieceCount() == 64
				|| (othelloBoard.getPlaceablePositions("white").isEmpty() && othelloBoard.getPlaceablePositions("black").isEmpty())) {
			if (othelloBoard.getBlackCount() == 0 || othelloBoard.getWhiteCount() > othelloBoard.getBlackCount()) {
				winnerColor = "White wins!";
			} else
			if (othelloBoard.getWhiteCount() == 0 || othelloBoard.getWhiteCount() < othelloBoard.getBlackCount()) {
				winnerColor = "Black wins!";
			} else {
				winnerColor = "It's a tie!";
			}
			isGameOver = true;
		} 
		
		/////////////////
		// GAME OVER PANE
		if (isGameOver) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			JPanel gameOverMenu = createGameOverMenu(winnerColor);
			//layeredPane.setBounds(200, 200, 300, 300);
			boardPanel.setVisible(false);
			gameFrame.remove(boardPanel);
			gameFrame.remove(buttonMenu);
			gameFrame.add(gameOverMenu);
		}
	}
	
	//
}

