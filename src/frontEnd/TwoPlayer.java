package frontEnd;

import java.awt.BorderLayout;
import java.awt.Button;
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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;
import javax.swing.border.Border;

import backEnd.Board;
import backEnd.Position;

public class TwoPlayer {
	
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
	
	boolean inGameModeEnabled = true; // set to false to disable some buttons and user clicking on board
	
	private boolean isGameOver = false;
	private String winnerColor;
	
	public TwoPlayer() {
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
		othelloBoard.generatePlaceability(current_color);
		othelloBoard.setCurrentColor("black");
		saveBoardState();
		
		// add undo button
		/*
		JButton undoButton = createUndoButton();
		JButton quitButton = createQuitButton();
		this.gameFrame.add(undoButton, BorderLayout.SOUTH);
		this.gameFrame.add(quitButton, BorderLayout.SOUTH);
		*/
		
		buttonMenu = createButtonMenu();
		this.gameFrame.add(buttonMenu, BorderLayout.SOUTH);
		
		// add count panel
		pieceCountPanel = new PieceCountPanel();
		pieceCountPanel.updateDisplay(boardMemory.getLast(), current_color);
		
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
		buttonMenu = new JPanel();
		buttonMenu.setLayout(new GridLayout());
		
		if (inGameModeEnabled) {
			JButton undoButton = createUndoButton();
			JButton quitButton = createQuitButton();
			JButton menuButton = createMenuButton();
			JButton forfeitButton = createForfeitButton();
			buttonMenu.add(quitButton);
			buttonMenu.add(menuButton);
			buttonMenu.add(undoButton);
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
	
	private JButton createForfeitButton() {
		JButton forfeitButton = new JButton("Forfeit Game");
		forfeitButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				isGameOver = true;
				inGameModeEnabled = false;
				
				if (next_color == "white") {
					winnerColor = "White wins!";
				} else {
					winnerColor = "Black wins!";
				}
				
				JPanel gameOverMenu = createGameOverMenu(winnerColor);
				//layeredPane.setBounds(200, 200, 300, 300);
				boardPanel.setVisible(false);
				gameFrame.remove(boardPanel);
				gameFrame.remove(buttonMenu);
				gameFrame.add(gameOverMenu);
			}
		});
		
		return forfeitButton;
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
	
	private JButton createUndoButton() {
		JButton undoButton = new JButton("Undo Move");
        undoButton.addActionListener(new ActionListener() {
        	// tmp = temporary variable to store the color when maintaining turns after undo move.
            String tmp;
            
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
	                othelloBoard = boardMemory.removeLast();
					
					
	                // maintain turn
					String currentColor = othelloBoard.getCurrentColor();
					
					if (currentColor == "white") {
						current_color = "white";
						next_color = "black";
					} else {
						current_color = "black";
						next_color = "white";
					}
					
					
				    // re-generate placeability for this state
					othelloBoard.generatePlaceability(currentColor);
					
					// update GUI
	                boardPanel.drawBoard(othelloBoard);
					pieceCountPanel.updateDisplay(othelloBoard, currentColor);
					saveBoardState();
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
					
					// place a new piece if the new piece is placeable.
					if (othelloBoard.isPlaceablePosition(row, col, current_color)) {
						// update the board
						othelloBoard.addPiece(row, col, current_color);
						othelloBoard.updateBoard(row, col, current_color);
						
						// update turns
						// if there ARE placeable positions for the next color, change turns, OTHERWISE maintain turns.
						// ASLO only switch turns if the game is not over yet
						if (!othelloBoard.getPlaceablePositions(next_color).isEmpty() && !isGameOver) {
							tmp = next_color;
							setNext(current_color);
							setCurrent(tmp);
						}
						othelloBoard.setCurrentColor(current_color);
						
						// add the new board state to the memory
						saveBoardState();
						// and point the current board to the last board in memory.
						othelloBoard.generatePlaceability(current_color);
						
						// update the GUI
						boardPanel.drawBoard(othelloBoard);
						pieceCountPanel.updateDisplay(othelloBoard, current_color);
						System.out.println("Board Score: " + othelloBoard.evaluateBoardScore("white"));
						
						// [win/loss] cases
						if (othelloBoard.getBlackCount() == 0 || othelloBoard.getWhiteCount() == 0 || othelloBoard.getPieceCount() == 64
								|| (othelloBoard.getPlaceablePositions("black").isEmpty() && othelloBoard.getPlaceablePositions("white").isEmpty())) {
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
						// adding the game over display
					}
				}

				@Override
				public void mousePressed(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub
					
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
	public void setCurrent(String color) {
		this.current_color = color;
	}
	public void setNext(String color) {
		this.next_color = color;
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
}
