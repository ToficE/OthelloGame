package frontEnd;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import backEnd.Board;

public class PieceCountPanel extends JPanel {
	
	private final JPanel whitePanel;
	private final JPanel blackPanel;
	
	int whiteCount;
	int blackCount;
	
	private final Color PANEL_COLOR = Color.decode("0x485d3f");
	private final static Dimension PANEL_BORDER = new Dimension(100, 100);
	private final static Dimension PIECES_DIMENSION = new Dimension(100, 87);
	
	public PieceCountPanel() {
		super(new  BorderLayout());
		this.setBackground(Color.LIGHT_GRAY);
		this.setSize(PANEL_BORDER);
		this.whitePanel = new JPanel();
		this.blackPanel = new JPanel();
		this.whitePanel.setBackground(PANEL_COLOR);
		this.blackPanel.setBackground(PANEL_COLOR);
		Border blackline =  BorderFactory.createLineBorder(Color.black);
		this.whitePanel.setBorder(blackline);
		this.blackPanel.setBorder(blackline);
		this.whitePanel.setLayout(new FlowLayout());
		this.blackPanel.setLayout(new FlowLayout());
		this.setBorder(blackline);
		try {
			final BufferedImage white_image = ImageIO.read(new File("images/white_piece.png"));
		    Image w_image = white_image.getScaledInstance(30, 30, white_image.SCALE_SMOOTH);
			final BufferedImage black_image = ImageIO.read(new File("images/black_piece.png"));
		    Image b_image = black_image.getScaledInstance(31, 31, white_image.SCALE_SMOOTH);
		    this.whitePanel.add(new JLabel(new ImageIcon(w_image), JLabel.CENTER));
		    this.blackPanel.add(new JLabel(new ImageIcon(b_image), JLabel.CENTER));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		JLabel whiteNumber = new JLabel(String.valueOf(whiteCount));
		JLabel blackNumber = new JLabel(String.valueOf(blackCount));
		whiteNumber.setFont(new Font("Serif", Font.BOLD, 20));
		blackNumber.setFont(new Font("Serif", Font.BOLD, 20));
		
		this.whitePanel.add(whiteNumber);
		this.blackPanel.add(blackNumber);

		
		
		add(this.whitePanel, BorderLayout.NORTH);
		add(this.blackPanel, BorderLayout.SOUTH);
		setPreferredSize(PIECES_DIMENSION);
	}
	
	public void updateDisplay(final Board board, String color) {
		this.blackPanel.removeAll();
		this.whitePanel.removeAll();
		
		try {
			Border blackline =  BorderFactory.createLineBorder(Color.black);
			Border redline =  BorderFactory.createLineBorder(Color.red);
			if (color == "white") {
				this.whitePanel.setBorder(redline);
				this.blackPanel.setBorder(blackline);
			} else {
				this.whitePanel.setBorder(blackline);
				this.blackPanel.setBorder(redline);
			}
			final BufferedImage white_image = ImageIO.read(new File("images/white_piece.png"));
		    Image w_image = white_image.getScaledInstance(30, 30, white_image.SCALE_SMOOTH);
			final BufferedImage black_image = ImageIO.read(new File("images/black_piece.png"));
		    Image b_image = black_image.getScaledInstance(31, 31, white_image.SCALE_SMOOTH);
		    this.whitePanel.add(new JLabel(new ImageIcon(w_image), JLabel.CENTER));
		    this.blackPanel.add(new JLabel(new ImageIcon(b_image), JLabel.CENTER));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.whiteCount = board.getWhiteCount();
		this.blackCount = board.getBlackCount();
		
		JLabel whiteNumber = new JLabel(String.valueOf(whiteCount));
		JLabel blackNumber = new JLabel(String.valueOf(blackCount));
		whiteNumber.setFont(new Font("Serif", Font.BOLD, 20));
		blackNumber.setFont(new Font("Serif", Font.BOLD, 20));
		
		this.whitePanel.add("Center", whiteNumber);
		this.blackPanel.add("Center", blackNumber);
		
		validate();
	}
}
