package frontEnd;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MainMenu {
	  //Instance variables
    JFrame mainMenuFrame;
    TwoPlayer pVpGame;
    PlayerVsComputer pVcGame;
    final Dimension MAIN_MENU_SIZE = new Dimension(800, 200);

    public MainMenu(){

        //Creating the JFrame
        mainMenuFrame = new JFrame("Main menu - Choose your mode");
        mainMenuFrame.setSize(MAIN_MENU_SIZE);

        //Two player mode button
        JButton TwoPlayers = new JButton("Player vs Player");
        TwoPlayers.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pVpGame = new TwoPlayer();
                mainMenuFrame.dispatchEvent(new WindowEvent(mainMenuFrame, WindowEvent.WINDOW_CLOSING)); // close main menu
            }
        });
        
        JButton PlayerVsComputerButton = new JButton("Player vs Computer");
        PlayerVsComputerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	
            	int result = JOptionPane.CANCEL_OPTION;
                boolean playerSide = true; //Just initializing for the compiler
                String playerColor;

                while (result == JOptionPane.CANCEL_OPTION) {
                    JPanel panel = new JPanel();
                    panel.add(new JLabel("Which side do you want to play as?"));
                    DefaultComboBoxModel model = new DefaultComboBoxModel();
                    model.addElement("White");
                    model.addElement("Black");
                    JComboBox comboBox = new JComboBox(model);
                    panel.add(comboBox);
                    result = JOptionPane.showConfirmDialog(null, panel, "Choose side", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(result == JOptionPane.OK_OPTION) playerSide = (comboBox.getSelectedItem().toString().equals("White")? true : false);
                }
                
                if (playerSide == true) {
                	playerColor = "white";
                } else {
                	playerColor = "black";
                }

                pVcGame = new PlayerVsComputer(playerColor);
                mainMenuFrame.dispatchEvent(new WindowEvent(mainMenuFrame, WindowEvent.WINDOW_CLOSING)); // close main menu
            }
        });
        
      //Creating a JPanel with a gridLayout that contains the buttons to add to the JFrame
        JPanel p = new JPanel(new GridLayout(2, 1));
        p.add(TwoPlayers);
        p.add(PlayerVsComputerButton);
        mainMenuFrame.setContentPane(p);

        //Validating the frame
        mainMenuFrame.setVisible(true);
        mainMenuFrame.validate();
        mainMenuFrame.repaint();
    }
}
