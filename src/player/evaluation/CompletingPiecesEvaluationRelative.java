package player.evaluation;

import java.util.ArrayList;
import java.util.Collections;

import core.Board;
import core.Piece;
import core.Set;

public class CompletingPiecesEvaluationRelative implements IEvaluation{

	@Override
	public int evaluateBoard(Board board, Set set){
		return - almostCompletedRows(board, set);
	}
	
	public int almostCompletedRows(Board board, Set set){
		ArrayList<Piece> row;
		int almostCompletedRows = 0;
		for (int i = 0; i < 4; i++) {
			row = new ArrayList<Piece>();
			for (int j = 0; j < 4; j++) {
				row.add(board.get(j, i)); // rows
			}
			almostCompletedRows += completingPieces(row, set);
		}

		for (int i = 0; i < 4; i++) {
			row = new ArrayList<Piece>();
			for (int j = 0; j < 4; j++) {
				row.add(board.get(i, j)); // columns
			}
			almostCompletedRows += completingPieces(row, set);
		}
		

		row = new ArrayList<Piece>();
		for (int i = 0; i < 4; i++) {
			row.add(board.get(i, i)); // 1. diagonal
		}
		almostCompletedRows += completingPieces(row, set);
		
		row = new ArrayList<Piece>();
		for (int i = 0; i < 4; i++) {
			row.add(board.get(3-i, i)); // 2. diagonal
		}
		almostCompletedRows += completingPieces(row, set);
		return almostCompletedRows;
	}
	
	private int completingPieces(ArrayList<Piece> row, Set set){
		row.removeAll(Collections.singleton(null)); // remove all null-entries
		if (row.size() != 3) {
			return 0;
		}
		int completingPieces = 0;
		for (int i = 0; i < 4; i++) {
			int count = 0;
			for (Piece piece : row) {
				if (piece.getAttributes()[i] == true) {
					count++;
				}
				else {
					count--;					
				}
			}
			if (Math.abs(count) == 3) {
				for (Piece piece : set.getPieces()) {
					if(piece.getAttributes()[i] == row.get(0).getAttributes()[i]) {
						completingPieces++;
					}
				}
			}
		}
		final int normalizationFactor = 1000;
		return completingPieces * normalizationFactor / set.size();
	}
	
	public static void main(String[] args) {

		Board board = new Board();
		board.setPiece(Piece.stringToPeace("B"), 1, 0);
		board.setPiece(Piece.stringToPeace("R*"), 2, 0);
		board.setPiece(Piece.stringToPeace("(B*)"), 3, 0);
		
		Set set = new Set();
		set.remove(Piece.stringToPeace("B"));
		set.remove(Piece.stringToPeace("R*"));
		set.remove(Piece.stringToPeace("(B*)"));

		Piece piece = Piece.stringToPeace("(b)");
		set.remove(piece);
		CompletingPiecesEvaluationRelative eval = new CompletingPiecesEvaluationRelative();
		System.out.println(board);
		System.out.println(set);
		System.out.println(eval.almostCompletedRows(board, set));
	}
}
