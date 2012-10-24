package core.logger;

import player.IPlayer;
import core.Action;
import core.Board;
import core.Piece;
import core.Set;

public interface ILogger {

	void logBoard(IPlayer player, Board board, Set set);
	void logPiece(IPlayer player, Board board, Piece piece, Set set);
	void logAction(IPlayer player, Action action);
	void logGameOver(IPlayer player, int winner, Board board);
	void logDebug(String str);
	
}
