package player;

import java.util.ArrayList;
import java.util.Random;

import core.Action;
import core.Board;
import core.Game;
import core.Piece;
import core.Set;

public class MonteCarloPlayer implements IPlayer {
	
	int simulations;
	Random generator = new Random();

	public MonteCarloPlayer(int simulations) {
		this.simulations = simulations;
	}

	@Override
	public Piece choosePiece(Board board, Set set) throws Exception {
		if (set.size() == 1) {
			return set.get(0);
		}
		int subSimulations = simulations / set.size();
		int[] wins = new int[set.size()];
		for (int i = 0; i < set.size(); i++) {
			ArrayList<int[]> freePositions = board.getFreePositions();
			for (int j = 0; j < subSimulations; j++) {
				Set tmpSet = set.copy();
				tmpSet.remove(set.get(i));
				Board newBoard = board.copy();
				int index = generator.nextInt(freePositions.size());
				newBoard.setPiece(set.get(i), freePositions.get(index)[0], freePositions.get(index)[1]); // opponent's first move
				if (newBoard.gameOver()) {
					continue;
				}
				Game game = new Game(newBoard, tmpSet);
				if(game.play() == 1){
					wins[i]++;
				}
			}
		}
		int max = -1;
		int maxIndex = -1;
		for (int i = 0; i < wins.length; i++) {
			if (wins[i] > max) {
				max = wins[i];
				maxIndex = i;
			}
		}
		return set.get(maxIndex);
	}

	@Override
	public Action makeMove(Board board, Set set, Piece piece) throws Exception {
		ArrayList<int[]> freePositions = board.getFreePositions();
		if (freePositions.size() == 1) {
			return new Action(piece, freePositions.get(0)[0], freePositions.get(0)[1]);
		}
		int subSimulations = simulations / freePositions.size();
		int[] wins = new int[freePositions.size()];
		for (int i = 0; i < freePositions.size(); i++) {
			for (int j = 0; j < subSimulations; j++) {
				Set tmpSet = set.copy();
				tmpSet.remove(piece);
				Board newBoard = board.copy();
				newBoard.setPiece(piece, freePositions.get(i)[0], freePositions.get(i)[1]);
				if (newBoard.gameOver()) {
					wins[i]++;
					continue;
				}
				Game game = new Game(newBoard, tmpSet);
				if(game.play() == 0){
					wins[i]++;
				}
			}
		}
		int max = -1;
		int maxIndex = -1;
		for (int i = 0; i < wins.length; i++) {
			if (wins[i] > max) {
				max = wins[i];
				maxIndex = i;
			}
		}
		return new Action(piece, freePositions.get(maxIndex)[0], freePositions.get(maxIndex)[1]);
	}
	
	@Override
	public String toString() {
		return "Monte-Carlo Player";
	}
}
