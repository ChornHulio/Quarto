package player;
import core.Action;
import core.Board;
import core.Piece;
import core.Set;


public interface IPlayer {

	Piece choosePiece(Board board, Set set) throws Exception;

	Action makeMove(Board board, Set set, Piece piece) throws Exception;

}
