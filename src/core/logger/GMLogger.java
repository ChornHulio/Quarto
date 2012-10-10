package core.logger;

import player.HumanPlayer;
import player.IPlayer;
import core.Action;
import core.Board;
import core.Piece;
import core.Set;

public class GMLogger implements ILogger {

	@Override
	public void logBoard(IPlayer player, Board board, Set set) { }

	@Override
	public void logPiece(IPlayer player, Board board, Piece piece, Set set) {
		if (!(player instanceof HumanPlayer)) {
			System.out.println(piece);
		}
	}

	@Override
	public void logAction(IPlayer player, Action action) {
		if (!(player instanceof HumanPlayer)) {
			System.out.println(action.x + " " + action.y);
		}
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
