package player.evaluation;

import core.*;

public class TestEvaluation implements IEvaluation{

	@Override
	public int getScore(Board board, Set set) {
		return - board.almostCompletedRows();
	}
}
