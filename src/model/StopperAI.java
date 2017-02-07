package model;

import java.awt.Point;
import java.util.Random;

/**
 * This TTT strategy tries to prevent the opponent from winning by checking for
 * a space where the opponent is about to win. If none found, it randomly picks
 * a place to win, which an sometimes be a win even if not really trying.
 * 
 * @author mercer
 */
public class StopperAI implements TicTacToeStrategy {

	@Override
	public Point desiredMove(TicTacToeGame theGame) {

		// If the AI can not block, look for a win
		if (theGame.maxMovesRemaining() == 0)
			throw new IGotNowhereToGoException(null);
		if (aboutWinByRow(theGame, 'O') != null)
			return aboutWinByRow(theGame, 'O');

		if (aboutWinByCol(theGame, 'O') != null)
			return aboutWinByCol(theGame, 'O');

		if (aboutWinByDiagonal(theGame, 'O') != null)
			return aboutWinByDiagonal(theGame, 'O');
		// First look to block an opponent win

		if (aboutWinByRow(theGame, 'X') != null)
			return aboutWinByRow(theGame, 'X');

		if (aboutWinByCol(theGame, 'X') != null)
			return aboutWinByCol(theGame, 'X');

		if (aboutWinByDiagonal(theGame, 'X') != null)
			return aboutWinByDiagonal(theGame, 'X');

		// If no block or win is possible, pick a move from those still
		// available
		int r, c;
		do {
			r = (int) (Math.random() * (theGame.size()));
			c = (int) (Math.random() * theGame.size());
		} while (!theGame.available(r, c));

		return new Point(r, c);
	}

	// Find next move to win by row
	private Point aboutWinByRow(TicTacToeGame theGame, char nextChar) {
		char[][] board = theGame.getTicTacToeBoard();
		int r = 0, c = 0, rowSum = 0;
		for (r = 0; r < theGame.size(); r++) {
			rowSum = 0;
			for (c = 0; c < theGame.size(); c++) {

				if (board[r][c] == nextChar)
					rowSum++;
				if (rowSum == theGame.size() - 1) {
					for (int col = 0; col < theGame.size(); col++) {
						if (theGame.available(r, col))
							return new Point(r, col);
					}
				} else
					continue;
			}
		}
		return null;
	}

	// Find next move to win by column.
	private Point aboutWinByCol(TicTacToeGame theGame, char nextChar) {
		char[][] board = theGame.getTicTacToeBoard();
		int r = 0, c = 0, colSum = 0;
		for (c = 0; c < theGame.size(); c++) {
			colSum = 0;
			for (r = 0; r < theGame.size(); r++) {
				if (board[r][c] == nextChar)
					colSum++;
				if (colSum == theGame.size() - 1) {
					for (int row = 0; row < theGame.size(); row++) {
						if (theGame.available(row, c))
							return new Point(row, c);
					}
				} else
					continue;
			}
		}
		return null;
	}

	// Find next move to win by Diagonal
	private Point aboutWinByDiagonal(TicTacToeGame theGame, char nextChar) {
		// Check Diagonal from upper left to lower right
		char[][] board = theGame.getTicTacToeBoard();
		int sum = 0, r = 0;
		for (r = 0; r < theGame.size(); r++) {
			if (board[r][r] == nextChar)
				sum++;
			if (sum == theGame.size() - 1) {
				for (int dia = 0; dia < theGame.size(); dia++) {
					if (theGame.available(dia, dia))
						return new Point(dia, dia);
				}
			} else
				continue;
		}

		// Check Diagonal from upper right to lower left
		sum = 0;
		for (r = theGame.size() - 1; r >= 0; r--) {
			if (board[theGame.size() - r - 1][r] == nextChar)
				sum++;
			if (sum == theGame.size() - 1) {
				for (int dia = theGame.size() - 1; dia >= 0; dia--) {
					if (theGame.available(theGame.size() - dia - 1, dia))
						return new Point(theGame.size() - dia - 1, dia);
				}
			} else
				continue;

		}
		// No win on either diagonal
		return null;
	}

}