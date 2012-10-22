package player.evaluation;

import java.util.ArrayList;

import core.Board;
import core.Piece;
import core.Set;

public class SimpleEvaluation implements IEvaluation{

	@Override
	public int getScore(Board board, Set set, Piece piece) {
		int bestScore = Integer.MIN_VALUE;
		int current = Integer.MIN_VALUE;
		ArrayList<int[]> freePositions = board.getFreePositions();
		for (int[] pos : freePositions) {
			board.setPiece(piece, pos[0], pos[1]);
			set.remove(piece);
			
			if (board.gameOver()) {
				return Integer.MAX_VALUE;
			} else if (set.isEmpty()) {
				current = 0;
			} else{
				return - board.almostCompletedRows();
			}
			if (current > bestScore) {
				bestScore = current;
			}
		}
		return bestScore;
	
	}
}
