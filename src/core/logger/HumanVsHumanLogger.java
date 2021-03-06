package core.logger;

import player.HumanPlayer;
import player.IPlayer;
import core.Action;
import core.Board;
import core.Piece;
import core.Set;

public class HumanVsHumanLogger implements ILogger {

	@Override
	public void logBoard(IPlayer player, Board board, Set set) {
		if (player instanceof HumanPlayer) {
			System.out.println(board);
			System.out.println("Choose enemy's piece: ");	
			System.out.println(set);	
		}
	}

	@Override
	public void logPiece(IPlayer player, Board board, Piece piece, Set set) {
			System.out.println(board);
			System.out.println(piece + " is your piece. Choose x and y: ");
	}

	@Override
	public void logAction(IPlayer player, Action action) {
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
