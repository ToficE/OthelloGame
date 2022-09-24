import java.awt.Color;

import javax.swing.JFrame;

public class UserInterface extends JFrame {
	
	VisualBoard othello = new VisualBoard();
	
	UserInterface() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//this.setSize(800, 800);
		this.add(othello);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setBackground(new Color(0xbebebe));
	}
}
