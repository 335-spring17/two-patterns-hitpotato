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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import controller.OurObserver;
import model.ComputerPlayer;
import model.TicTacToeGame;

public class TextFieldView extends JPanel implements OurObserver {

	private TicTacToeGame theGame;
	private JButton stateButton = new JButton("Enter your move");
	private JButton button = new JButton("Make the move");
	private JTextField row = new JTextField("", SwingConstants.CENTER);
	private JTextField column = new JTextField("", SwingConstants.CENTER);
	private JLabel rowLabel = new JLabel("row");
	private JLabel colLabel = new JLabel("column");

	private JTextArea textState = new JTextArea("");
	private ComputerPlayer computerPlayer;
	private int height, width;

	// Constructor
	public TextFieldView(TicTacToeGame TicTacToeGame, int width, int height) {
		theGame = TicTacToeGame;
		this.height = height;
		this.width = width;
		computerPlayer = theGame.getComputerPlayer();
		initializeTextPanel();
	}

	// This method is called by OurObservable's notifyObservers()
	public void update() {
		if (theGame.maxMovesRemaining() == theGame.size() * theGame.size())
			resetTextArea(true);

		if (!theGame.stillRunning())
			resetTextArea(false);
		else {
			updateTextArea();
			stateButton.setText("Click your move");
		}
	}

	// Initialization
	private void initializeTextPanel() {

		// Add row, column label as well as row column textfield to JPanel.
		JPanel textPanel = new JPanel();
		textPanel.setLayout(new GridLayout(2, 2));
		ButtonListener buttonListener = new ButtonListener();
		button.addActionListener(buttonListener);
		textPanel.add(row);
		textPanel.add(rowLabel);
		textPanel.add(column);
		textPanel.add(colLabel);
		button.setFont(new Font("Arial", Font.BOLD, 18));
		button.setEnabled(true);
		button.setBackground(Color.PINK);
		// Set up JTextArea
		textState.setPreferredSize(new Dimension(190, 150));
		textState.setLineWrap(true);
		textState.setWrapStyleWord(true);
		textState.setFont(new Font("Courier", Font.BOLD, 34));
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
	public void updateTextArea() {
		textState.setText(theGame.toString());
	}

	// Reset JTextArea
	private void resetTextArea(boolean enable) {
		textState.setText("");
	}

	// Listener
	private class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			JButton buttonClicked = (JButton) arg0.getSource();

			// Check if it is clicked
			if (button == buttonClicked) {
				int r = 0, c = 0;

				// Check if input valid, throw exception if needed
				try {
					r = Integer.parseInt(row.getText());
					c = Integer.parseInt(column.getText());
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(null, "NAN instead of 0, 1, or 2");
					throw new NumberFormatException();
				}

				if (!((r >= 0 && r < 3) && (c >= 0 && c < 3))) {
					JOptionPane.showMessageDialog(null, "Invalid move");
					throw new IllegalArgumentException();
				}
				if (!theGame.available(r, c)) {
					JOptionPane.showMessageDialog(null, "Move not available");
					throw new IllegalArgumentException();
				}

				// if valid make a move
				theGame.choose(r, c);
			}

			if (theGame.tied()) {
				stateButton.setText("Tied");
				button.setText("Tied");
				updateTextArea();
			} else if (theGame.didWin('X')) {
				stateButton.setText("X wins");
				button.setText("X winds");
				updateTextArea();
			} else {
				// If the game is not over, let the computer player choose
				// This algorithm assumes the computer player always
				// goes after the human player and is represented by 'O', not
				// 'X'
				Point play = computerPlayer.desiredMove(theGame);
				theGame.choose(play.x, play.y);
				if (theGame.didWin('O')) {
					stateButton.setText("O wins");
					button.setText("O wins");
					updateTextArea();
				}
			}
		}
	}

}
