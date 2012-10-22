package player.evaluation;

import core.*;

public interface IEvaluation {

	int getScore(Board board, Set set, Piece piece);

	int evaluateBoard(Board board, Set set);

}
