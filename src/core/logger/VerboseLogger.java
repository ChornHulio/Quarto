package core.logger;

import player.IPlayer;
import core.Action;
import core.Board;
import core.Piece;
import core.Set;

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
		System.out.println(player + " makes move: " + action.piece + " to " + action);
	}

	@Override
	public void logGameOver(IPlayer player, int winner) {
		if (winner == 2) {
			System.out.println("Draw.");
		} else {
			System.out.println(player + " ( " + winner + " ) " + " wins.");
		}

	}

}
