package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import controller.OurObserver;
import model.ComputerPlayer;
import model.TicTacToeGame;

public class TextView extends JPanel implements OurObserver {

	private TicTacToeGame theGame;
	private JButton stateButton = new JButton("Click your move");
	private JButton buttons = null;
	private JButton button = new JButton("Change");
	private JTextField row = new JTextField("", SwingConstants.CENTER);
    private JTextField column = new JTextField("", SwingConstants.CENTER);
    private JLabel rowLabel = new JLabel("row");
    private JLabel colLabel = new JLabel("column");
    
    private JTextArea textState = new JTextArea("What the hell");
	private ComputerPlayer computerPlayer;
	private int height, width;

	public TextView(TicTacToeGame TicTacToeGame, int width, int height) {
	    theGame = TicTacToeGame;
	    this.height = height;
	    this.width = width;
	    computerPlayer = theGame.getComputerPlayer();
	    initializeTextPanel();
	  }

	// This method is called by OurObservable's notifyObservers()
	public void update() {
		if (theGame.maxMovesRemaining() == theGame.size() * theGame.size())
			resetButtons(true);

		if (!theGame.stillRunning())
			resetButtons(false);
		else {
			updateButtons();
			stateButton.setText("Click your move");
		}
	}

	private void initializeTextPanel() {
		//add(row);
		//add(column);
		JPanel textPanel = new JPanel();
		int size = theGame.size();
		//this.setBackground(Color.gray);
		//this.setLayout(new GridLayout(3,1));
		textPanel.setLayout(new GridLayout(2,2));
		//Font myFont = new Font("Arial", Font.TRUETYPE_FONT, 40);
		ButtonListener buttonListener = new ButtonListener();
		button.addActionListener(buttonListener);
		
		textPanel.add(row);
		textPanel.add(rowLabel);
		textPanel.add(column);
		textPanel.add(colLabel);
		button.setFont(new Font("Arial", Font.BOLD, 18));
		button.setEnabled(true);
		button.setBackground(Color.PINK);
		
		textState.setPreferredSize(new Dimension(250, 200));
		textState.setLineWrap(true);
	    textState.setWrapStyleWord(true);
		textState.setFont(new Font("Courier", Font.TRUETYPE_FONT, 36));
		textState.setEnabled(true);
		
		stateButton.setSize(200, 40);
		stateButton.setFont(new Font("Arial", Font.BOLD, 18));
		stateButton.setEnabled(false);
		stateButton.setBackground(Color.PINK);
		stateButton.setLocation(40, height - 100);
		this.add(textPanel);
		this.add(button);
		this.add(textState);
		
		
		this.add(stateButton);
		
	}

	// Mark each selected square with an X or an O and prevent
	// selection of the selected squares with seDisabled(true)
	public void updateButtons() {
		String str = "";
//		char[][] temp = theGame.getTicTacToeBoard();
//		for (int i = 0; i < temp.length; i++) {
//			for (int j = 0; j < temp[i].length; j++) {
//				String text = "" + temp[i][j];
//				if (text.equals("X") || text.equals("O")) {
//					str += text + " ";
//				}
//			}
//		}
		textState.setText(theGame.toString());
	}

	private void resetButtons(boolean enable) {
		
//		for (int i = 0; i < theGame.size(); i++) {
//			for (int j = 0; j < theGame.size(); j++) {
//				buttons[i][j].setText("");
//				buttons[i][j].setEnabled(enable);
//			}
//		}
		textState.setText("");
	}

	private class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			JButton buttonClicked = (JButton) arg0.getSource();
			if(button == buttonClicked){
				int r = Integer.parseInt(row.getText()), c = Integer.parseInt(column.getText());
				if(!((r >= 0 && r < 3 ) && (c >= 0 && c < 3)))
					throw new IllegalArgumentException();
				theGame.choose(r, c);
			}

			if (theGame.tied()) {
				stateButton.setText("Tied");
				updateButtons();
			} else if (theGame.didWin('X')) {
				stateButton.setText("X wins");
				updateButtons();
			} else {
				// If the game is not over, let the computer player choose
				// This algorithm assumes the computer player always
				// goes after the human player and is represented by 'O', not
				// 'X'
				Point play = computerPlayer.desiredMove(theGame);
				theGame.choose(play.x, play.y);
				if (theGame.didWin('O')) {
					stateButton.setText("O wins");
					updateButtons();
				}
			}
		}

		// private void setButtonsDisabled() {
		// for (int i = 0; i < buttons.length; i++)
		// for (int j = 0; j < buttons[i].length; j++)
		// buttons[i][j].setEnabled(false);
		// }
	}

}
