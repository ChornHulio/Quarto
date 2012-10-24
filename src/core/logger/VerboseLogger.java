package core.logger;

import player.IPlayer;
import core.Action;
import core.Board;
import core.Piece;
import core.Set;

/**
 * Prints all the moves and pieces being chosen and the state of the game after each move.
 *
 */
public class VerboseLogger implements ILogger {

	@Override
	public void logBoard(IPlayer player, Board board, Set set) {
		System.out.println(board);
	}

	@Override
	public void logPiece(IPlayer player, Board board, Piece piece, Set set) {
		System.out.println(player + " has chosen: " + piece + " for his opponent. (Out of:) " + set);
	}

	@Override
	public void logAction(IPlayer player, Action action) {
		System.out.println(player + " makes move: " + action.piece + " to " + action.x + " " + action.y);
	}

	@Override
	public void logGameOver(IPlayer player, int winner, Board board) {
		if (winner == 2) {
			System.out.println("Draw.");
		} else {
			System.out.println(player + " ( " + winner + " ) " + " wins.");
		}
		System.out.println("Final board state:");
		System.out.println(board);
	}

	@Override
	public void logDebug(String str) {
		System.out.print(str);
	}

}
