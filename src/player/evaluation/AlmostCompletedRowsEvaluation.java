package player.evaluation;

import java.util.ArrayList;
import java.util.Collections;

import core.Board;
import core.Piece;
import core.Set;

public class AlmostCompletedRowsEvaluation implements IEvaluation{

	@Override
	public int evaluateBoard(Board board, Set set){
		return - almostCompletedRows(board);
	}
	
	public int almostCompletedRows(Board board){
		ArrayList<Piece> row;
		int almostCompletedRows = 0;
		for (int i = 0; i < 4; i++) {
			row = new ArrayList<Piece>();
			for (int j = 0; j < 4; j++) {
				row.add(board.get(j, i)); // rows
			}
			almostCompletedRows += sameAttributes(row);
		}

		for (int i = 0; i < 4; i++) {
			row = new ArrayList<Piece>();
			for (int j = 0; j < 4; j++) {
				row.add(board.get(i, j)); // columns
			}
			almostCompletedRows += sameAttributes(row);
		}
		

		row = new ArrayList<Piece>();
		for (int i = 0; i < 4; i++) {
			row.add(board.get(i, i)); // 1. diagonal
		}
		almostCompletedRows += sameAttributes(row);
		
		row = new ArrayList<Piece>();
		for (int i = 0; i < 4; i++) {
			row.add(board.get(3-i, i)); // 2. diagonal
		}
		almostCompletedRows += sameAttributes(row);
		return almostCompletedRows;
	}
	
	private int sameAttributes(ArrayList<Piece> row){
		row.removeAll(Collections.singleton(null)); // remove all null-entries
		if (row.size() != 3) {
			return 0;
		}
		int sameAttributes = 0;
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
				sameAttributes++;
			}
		}
		return sameAttributes;
	}
}
