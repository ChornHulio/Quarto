package player;

import java.util.ArrayList;
import java.util.Random;

import parallel.LoopBody;
import parallel.Parallel;
import core.Action;
import core.Board;
import core.Game;
import core.Piece;
import core.Set;

public class MonteCarloParallelPlayer implements IPlayer {
	
	int simulations;
	Random generator = new Random();

	public MonteCarloParallelPlayer(int simulations) {
		this.simulations = simulations;
	}

	@Override
	public Piece choosePiece(final Board board, final Set set) throws Exception {
		if (set.size() == 1) {
			return set.get(0);
		}
		final int subSimulations = simulations / set.size();
		final int[] wins = new int[set.size()];
		final ArrayList<int[]> freePositions = board.getFreePositions();
		
		Parallel.For(0, set.size(), new LoopBody<Integer>()
			    {
			        public void run(Integer i)
			        {
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
							try {
								if(game.play() == 1){
									wins[i]++;
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}        
			    });
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
	public Action makeMove(final Board board, final Set set, final Piece piece) throws Exception {
		final ArrayList<int[]> freePositions = board.getFreePositions();
		if (freePositions.size() == 1) {
			return new Action(piece, freePositions.get(0)[0], freePositions.get(0)[1]);
		}
		final int subSimulations = simulations / freePositions.size();
		final int[] wins = new int[freePositions.size()];
		Parallel.For(0, freePositions.size(), new LoopBody<Integer>()
			    {
			        public void run(Integer i)
			        {
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
							try {
								if(game.play() == 0){
									wins[i]++;
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}        
			    }); 
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
		return "Parallel Monte-Carlo Player";
	}
}
