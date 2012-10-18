package player;

import java.util.ArrayList;
import java.util.Scanner;

import core.Action;
import core.Board;
import core.Piece;
import core.Set;

public class HumanPlayer implements IPlayer {

	private Scanner scanner = new Scanner(System.in);

	@Override
	public Piece choosePiece(Board board, Set set) {
		String input;
		int pieceNumber;
		while(true) try {
			input = scanner.nextLine();
			Piece piece = Piece.stringToPeace(input);
			if (piece != null) {
				return piece;
			}
			pieceNumber = Integer.parseInt(input);
			if(pieceNumber >= set.size() || pieceNumber < 0) {
				throw new Exception();
			} else {
				break;
			}
		} catch (Exception e) {
			System.out.println("Please enter number from 0 to " + (set.size() - 1));
		}
		return set.get(pieceNumber);
	}

	@Override
	public Action makeMove(Board board, Set set, Piece piece) {
		String input;
		int x = -1, y = -1;
		try {
			input = scanner.nextLine().replace(" ", "");
			ArrayList<String> inputs = new ArrayList<String>();
			for (String string : input.split(" ")) {
				if (!string.trim().isEmpty()) {
					inputs.add(string);
				}
			} 
			if (inputs.size() == 1) {
				x = Integer.parseInt(inputs.get(0)) / 10;
				y = Integer.parseInt(inputs.get(0)) - (x * 10);
			} else if (inputs.size() == 2) {
				x = Integer.parseInt(inputs.get(0));
				y = Integer.parseInt(inputs.get(1));
			}
			if(x >= 4 || x < 0 || y >= 4 || y < 0) {
				throw new Exception();
			}
		} catch (Exception e) {
			System.out.println("Please enter number from 0 to 3");
		}
		return new Action(piece, x, y);
	}
	
	@Override
	public String toString() {
		return "Human Player";
	}
	
	protected void finalize() {
		scanner.close();
	}
}
