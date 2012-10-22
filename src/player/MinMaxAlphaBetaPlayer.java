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

	final int minmaxStartingFromPiece = 8;
	
	final int monteCarloSimulations = 100;

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
			return new NovicePlayer().choosePiece(board, set); //TODO: mcp
//			return new RandomPlayer().choosePiece(board, set);
		}
	}
	
	@Override
	public Action makeMove(Board board, Set set, Piece piece) throws Exception {
		if (set.size() <= 16 - minmaxStartingFromPiece) {
			max(board, set, piece, 0, -999, 999);
			return bestAction;
		} else {
//			return new MonteCarloParallelPlayer(monteCarloSimulations).makeMove(board, set, piece);
			return new NovicePlayer().makeMove(board, set, piece); //TODO: mcp
//			return new RandomPlayer().makeMove(board, set, piece);
		}
	}

	private int max(Board board, Set set, Piece piece, int depth, int alpha, int beta) {
		int max = Integer.MIN_VALUE;
		int current = max;
		ArrayList<int[]> freePositions = board.getFreePositions();
		for (int[] pos : freePositions) {
			board.setPiece(piece, pos[0], pos[1]);
			if (board.gameOver()) {
				board.remove(pos[0], pos[1]);
				if (depth == 0) {
					bestAction = new Action(piece, pos[0], pos[1]);
				}
				return Integer.MAX_VALUE - 1;
			} else if (set.isEmpty()) {
				if (depth == 0) {
				}
				current = 0;
			} else if (depth == maxDepth) {
				current = evaluation.evaluateBoard(board, set);
			} else {
				int opponentMax = Integer.MIN_VALUE;
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
					return current;
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
		int min = Integer.MAX_VALUE;
		int current = min;
		ArrayList<int[]> freePositions = board.getFreePositions();
		for (int[] pos : freePositions) {
			board.setPiece(piece, pos[0], pos[1]);
			if (board.gameOver()) {
				board.remove(pos[0], pos[1]);
				return Integer.MIN_VALUE + 1;
			} else if (set.isEmpty()) {
				if (depth == 0) {
				}
				current = 0;
			} else if (depth == maxDepth) {
				current = evaluation.evaluateBoard(board, set);
			} else {
				int opponentMin = Integer.MAX_VALUE;
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
					return current;
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
	
	public static void main(String[] argss) throws Exception {
//		Board board = new Board();
//		board.setPiece(Piece.stringToPeace("(r*)"), 1, 0);
//		board.setPiece(Piece.stringToPeace("(B*)"), 2, 0);
//		board.setPiece(Piece.stringToPeace("(r)"), 3, 0);
//		
//		board.setPiece(Piece.stringToPeace("(R)"), 1, 1);
//		board.setPiece(Piece.stringToPeace("R"), 2, 1);
//		
//		board.setPiece(Piece.stringToPeace("R*"), 1, 2);
//		
//		Set set = new Set();
//		set.remove(Piece.stringToPeace("(r*)"));
//		set.remove(Piece.stringToPeace("(B*)"));
//		set.remove(Piece.stringToPeace("(r)"));
//		set.remove(Piece.stringToPeace("(R)"));
//		set.remove(Piece.stringToPeace("R"));
//		set.remove(Piece.stringToPeace("R*"));
//
//		Piece piece = Piece.stringToPeace("b*");
//		set.remove(piece);
//		MinmaxPlayer player = new MinmaxPlayer(4, new TestEvaluation());
//		
//		System.out.println(board);
//		Action action = player.makeMove(board, set, piece);
//		board.setPiece(piece, action.x, action.y);
//		set.remove(piece);
//		System.out.println(board);
		
		
		Board board = new Board();
		board.setPiece(Piece.stringToPeace("(b*)"), 0, 0);
		board.setPiece(Piece.stringToPeace("r"), 1, 0);
		board.setPiece(Piece.stringToPeace("B"), 2, 0);
		board.setPiece(Piece.stringToPeace("(r)"), 3, 0);
		
		board.setPiece(Piece.stringToPeace("(B*)"), 0, 1);
		board.setPiece(Piece.stringToPeace("R*"), 2, 1);
		board.setPiece(Piece.stringToPeace("R"), 3, 1);

		board.setPiece(Piece.stringToPeace("B*"), 0, 2);
		board.setPiece(Piece.stringToPeace("b*"), 2, 2);

		board.setPiece(Piece.stringToPeace("(R)"), 0, 3);
		board.setPiece(Piece.stringToPeace("(r*)"), 2, 3);
		
		Set set = new Set();
		set.remove(Piece.stringToPeace("(b*)"));
		set.remove(Piece.stringToPeace("r"));
		set.remove(Piece.stringToPeace("B"));
		set.remove(Piece.stringToPeace("(r)"));
		
		set.remove(Piece.stringToPeace("(B*)"));
		set.remove(Piece.stringToPeace("R*"));
		set.remove(Piece.stringToPeace("R"));
		
		set.remove(Piece.stringToPeace("B*"));
		set.remove(Piece.stringToPeace("b*"));
		
		set.remove(Piece.stringToPeace("(R)"));
		set.remove(Piece.stringToPeace("(r*)"));

		Piece piece = Piece.stringToPeace("(b)");
		set.remove(piece);
		MinMaxAlphaBetaPlayer player = new MinMaxAlphaBetaPlayer(2, new TestEvaluation());
		
		System.out.println(board);
		Action action = player.makeMove(board, set, piece);
		board.setPiece(piece, action.x, action.y);
		set.remove(piece);
		System.out.println(board);
		System.out.println(player + " chooses from: " + set);
		Piece piece2 = player.choosePiece(board, set);
		System.out.println(piece2);
		
	}

}