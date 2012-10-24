package core.logger;

import player.IPlayer;
import core.Action;
import core.Board;
import core.Piece;
import core.Set;

/**
 * Logger for evaluating purposes
 *
 */
public class QuietLogger implements ILogger {

	@Override
	public void logBoard(IPlayer player, Board board, Set set) { }

	@Override
	public void logPiece(IPlayer player, Board board, Piece piece, Set set) { }

	@Override
	public void logAction(IPlayer player, Action action) { }

	@Override
	public void logGameOver(IPlayer player, int winner, Board board) { }
	
	@Override
	public void logDebug(String str) { }

}
