package player;

import java.util.ArrayList;
import java.util.Random;

import core.Action;
import core.Board;
import core.Piece;
import core.Set;



public class RandomPlayer implements IPlayer {
	
	Random generator = new Random();

	@Override
	public Piece choosePiece(Board board, Set set) {
		return set.get(generator.nextInt(set.size()));
	}

	@Override
	public Action makeMove(Board board, Set set, Piece piece) {
		ArrayList<int[]> freePositions = board.getFreePositions();
		int index = generator.nextInt(freePositions.size());
		return new Action(piece, freePositions.get(index)[0], freePositions.get(index)[1]);
	}
	
	@Override
	public String toString() {
		return "Random Player";
	}
}
