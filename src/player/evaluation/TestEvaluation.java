package player.evaluation;

import java.util.ArrayList;

import core.Board;
import core.Piece;
import core.Set;

public class TestEvaluation implements IEvaluation {

	@Override
	public int getScore(Board board, Set set, Piece piece) {
		int bestScore = Integer.MIN_VALUE;
		int current = Integer.MIN_VALUE;
		ArrayList<int[]> freePositions = board.getFreePositions();
		for (int[] pos : freePositions) {
			board.setPiece(piece, pos[0], pos[1]);
			boolean depthNotZero = set.remove(piece);
			
			if (board.gameOver()) {
				if (depthNotZero)
					set.add(0, piece);
				board.remove(pos[0], pos[1]);
				return Integer.MAX_VALUE - 1;
			} else if (set.isEmpty()) {
				current = 0;
			} else {
				current = 0;
			}
			if (current > bestScore) {
				bestScore = current;
			}
			if (depthNotZero)
				set.add(0, piece);
			board.remove(pos[0], pos[1]);
		}
		return bestScore;
	}

	@Override
	public int evaluateBoard(Board board, Set set) {
		return 0;
	}
}
