package player;

import java.util.ArrayList;
import java.util.Collections;

import core.Action;
import core.Board;
import core.Piece;
import core.Set;



public class NovicePlayer implements IPlayer {

	@Override
	public Piece choosePiece(Board board, Set set) {
		ArrayList<int[]> freePositions =  board.getFreePositions();
		Collections.shuffle(set.getPieces());
		boolean losingRow = false;
		for (Piece piece : set.getPieces()) {
			for (int[] pos : freePositions) {
				board.setPiece(piece, pos[0], pos[1]);
				if (board.gameOver()) {
					board.remove(pos[0], pos[1]);
					losingRow = true;
					break;
				}
				board.remove(pos[0], pos[1]);
			}
			if (losingRow) {
				losingRow = false;
				continue;
			}
			return piece;
		}
		return set.getPieces().get(0);
	}

	@Override
	public Action makeMove(Board board, Set set, Piece piece) {
		ArrayList<int[]> freePositions =  board.getFreePositions();
		Collections.shuffle(freePositions);
		for (int[] pos : freePositions) {
			board.setPiece(piece, pos[0], pos[1]);
			if (board.gameOver()) {
				board.remove(pos[0], pos[1]);
				return new Action(piece, pos[0], pos[1]);
			}
			board.remove(pos[0], pos[1]);
		}
		return new Action(piece, freePositions.get(0)[0], freePositions.get(0)[1]);
	}
	
	@Override
	public String toString() {
		return "Novice Player";
	}

}
