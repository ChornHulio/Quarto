package unittests;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import player.*;
import player.evaluation.*;
import core.*;

public class MinMaxAlphaBetaPlayerTest {

	@Test
	public void testEquality() throws Exception {
		final int maxDepth = 4;
		final int minmaxStartingFromPiece = 8;

		int runs = 100;

		long minmaxTime = 0;
		long minmaxAlphaBetaTime = 0;

		for (int j = 0; j < runs; j++) {

			Board board = new Board();
			Set set = new Set();
			RandomPlayer randomPlayer = new RandomPlayer();
			MinMaxPlayer minmaxPlayer = new MinMaxPlayer(maxDepth, new TestEvaluation());
			MinMaxAlphaBetaPlayer minmaxAlphaBetaPlayer = new MinMaxAlphaBetaPlayer(maxDepth, new TestEvaluation());
			Piece randomPiece;
			for (int i = 0; i < minmaxStartingFromPiece; i++) {
				randomPiece = randomPlayer.choosePiece(board, set);
				set.remove(randomPiece);
				Action randomAction = randomPlayer.makeMove(board, set, randomPiece);
				board.setPiece(randomPiece, randomAction.x, randomAction.y);
			}
			randomPiece = randomPlayer.choosePiece(board, set);
			while (!board.gameOver() && !set.isEmpty()) {

				long time = System.currentTimeMillis();
				Action minmaxAction = minmaxPlayer.makeMove(board, set, randomPiece);
				minmaxTime += System.currentTimeMillis() - time;
				time = System.currentTimeMillis();
				Action minmaxAlphaBetaAction = minmaxAlphaBetaPlayer.makeMove(board, set, randomPiece);
				minmaxAlphaBetaTime += System.currentTimeMillis() - time;

				assertEquals(minmaxAction.x, minmaxAlphaBetaAction.x);
				assertEquals(minmaxAction.y, minmaxAlphaBetaAction.y);

				Piece minmaxPiece = minmaxPlayer.choosePiece(board, set);
				Piece minaxAlphaBetaPiece = minmaxAlphaBetaPlayer.choosePiece(board, set);
				
				randomPiece = randomPlayer.choosePiece(board, set);
				set.remove(randomPiece);
				Action randomAction = randomPlayer.makeMove(board, set, randomPiece);
				board.setPiece(randomPiece, randomAction.x, randomAction.y);
			}

		}
		System.out.println("min/max alpha/beta: " + minmaxAlphaBetaTime);
		System.out.println("min/max: " + minmaxTime);
		// assertTrue(minmaxAlphaBetaTime < minmaxTime);
	}
}
