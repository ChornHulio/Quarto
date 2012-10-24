package player;

import java.util.ArrayList;

import player.evaluation.*;
import core.Action;
import core.Board;
import core.Piece;
import core.Set;

public class MinMaxAlphaBetaPlayer implements IPlayer {

	private int maxDepth;
	Piece nextPieceToChoose;

	final int minmaxStartingFromPiece = 6;
	
	final int monteCarloSimulations = 1000;
	
	final int WIN = 1000;
	final int INFINITE = 9999;

	private Action bestAction;
	private IEvaluation evaluation;

	public MinMaxAlphaBetaPlayer(int maxDepth, IEvaluation evaluation) {
		this.maxDepth = maxDepth;
		this.evaluation = evaluation;
	}

	@Override
	public Piece choosePiece(Board board, Set set) throws Exception {
		if (set.size() <= 16 - minmaxStartingFromPiece && nextPieceToChoose != null) {
			return nextPieceToChoose;
		} else {
//			return new MonteCarloParallelPlayer(monteCarloSimulations).choosePiece(board, set);
			return new NovicePlayer().choosePiece(board, set); 
		}
	}
	
	@Override
	public Action makeMove(Board board, Set set, Piece piece) throws Exception {
		if (set.size() <= 16 - minmaxStartingFromPiece) {
			max(board, set, piece, 0, -INFINITE, INFINITE);
			return bestAction;
		} else {
//			return new MonteCarloParallelPlayer(monteCarloSimulations).makeMove(board, set, piece);
			return new NovicePlayer().makeMove(board, set, piece); 
		}
	}

	private int max(Board board, Set set, Piece piece, int depth, int alpha, int beta) {
		int max = -INFINITE;
		int current = max;
		ArrayList<int[]> freePositions = board.getFreePositions();
		// iterating through free positions on the board
		for (int[] pos : freePositions) {
			board.setPiece(piece, pos[0], pos[1]);
			if (board.gameOver()) { // win
				board.remove(pos[0], pos[1]);
				if (depth == 0) {
					bestAction = new Action(piece, pos[0], pos[1]);
				}
				max = WIN;
				return max;
			} else if (set.isEmpty()) { // tie
				if (depth == 0) {
				}
				current = 0; 
			} else if (depth == maxDepth) { // leaf
				current = evaluation.evaluateBoard(board, set);
			} else {
				int opponentMax = -INFINITE;
				// iterating through remaining pieces
				for (int i = 0; i < set.size(); i++) {
					Piece p = set.get(i);
					set.remove(p);
					current = min(board, set, p, depth + 1, alpha, beta);
					set.add(i, p);
					if (current > opponentMax) {
						opponentMax = current;
						if (depth == 0 && current > max) {
							nextPieceToChoose = set.get(i);
						}
					}
				}
				current = opponentMax;
			}
			board.remove(pos[0], pos[1]);
			if (current > max) {
				if (depth == 0) {
					bestAction = new Action(piece, pos[0], pos[1]);
				}
				if (current >= beta) {
					return current; // pruning
				}
				max = current;
				if (current > alpha) {
					alpha = current;
				}
			}
		}
		return max;
	}

	private int min(Board board, Set set, Piece piece, int depth, int alpha, int beta) {
		int min = INFINITE;
		int current = min;
		ArrayList<int[]> freePositions = board.getFreePositions();
		// iterating through free positions on the board
		for (int[] pos : freePositions) {
			board.setPiece(piece, pos[0], pos[1]);
			if (board.gameOver()) { // win
				board.remove(pos[0], pos[1]);
				min = -WIN;
				return min;
			} else if (set.isEmpty()) { // tie
				if (depth == 0) {
				}
				current = 0;
			} else if (depth == maxDepth) { // leaf
				current = - evaluation.evaluateBoard(board, set);
			} else {
				int opponentMin = INFINITE;
				// iterating through remaining pieces
				for (int i = 0; i < set.size(); i++) {
					Piece p = set.get(i);
					set.remove(p);
					current = max(board, set, p, depth + 1, alpha, beta);
					set.add(i, p);
					if (current < opponentMin) {
						opponentMin = current;
					}
				}
				current = opponentMin;
			}
			board.remove(pos[0], pos[1]);
			if (current < min) {
				if (current <= alpha) {
					return current; // pruning
				}
				min = current;
				if (current < beta) {
					beta = current;
				}
			}
		}
		return min;
	}
	
	@Override
	public String toString() {
		return "Min/Max Player with alpha/beta pruning (depth: " + maxDepth + ")";
	}
}