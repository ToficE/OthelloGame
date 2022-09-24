import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import backEnd.Board;

public class VisualBoard extends JPanel{
	private int board_xstart = 100;
	private int board_ystart = 50;
	private int board_xend = 700;
	private int board_yend = 650;
	private int height = board_yend - board_ystart;
	private int width = board_xend - board_xstart;
	
	VisualBoard() {
		this.setPreferredSize(new Dimension(800,900));
		this.setBackground(Color.gray);
	}
	
	public void paint(Graphics g) {
		Graphics2D rect = (Graphics2D) g;
		rect.setPaint(new Color(0x485d3f));
		rect.fillRect(board_xstart, board_ystart, width, height);
		
		Graphics2D border = (Graphics2D) g;
		border.setPaint(Color.black);
		border.setStroke(new BasicStroke(20));
		border.drawLine(board_xstart-10, board_ystart, board_xstart-10, board_yend);
		border.drawLine(board_xend+10, board_ystart, board_xend+10, board_yend);
		border.drawLine(board_xstart-10, board_ystart-10, board_xend+10, board_ystart-10);
		border.drawLine(board_xstart-10, board_yend+10, board_xend+10, board_yend+10);
		
		Graphics2D grid = (Graphics2D) g;
		grid.setPaint(Color.black);
		grid.setStroke(new BasicStroke(3));
		
		int offset = (board_xend-board_xstart)/8;
		for(int i = 0; i < 8; i++) {
			grid.drawLine(board_xstart+offset*i, board_ystart, board_xstart+offset*i, board_yend);
			grid.drawLine(board_xstart, board_ystart+offset*i, board_xend, board_ystart+offset*i);
		}
	}
}
