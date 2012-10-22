package player;

import java.util.ArrayList;

import player.evaluation.*;
import core.Action;
import core.Board;
import core.Piece;
import core.Set;

public class MinMaxPlayer implements IPlayer {

	private int maxDepth;

	final int minmaxStartingFromPiece = 7;

	final int monteCarloSimulations = 100;

	private Action bestAction;
	private IEvaluation evaluation;

	public MinMaxPlayer(int maxDepth, IEvaluation evaluation) {
		this.maxDepth = maxDepth;
		this.evaluation = evaluation;
	}

	@Override
	public Piece choosePiece(Board board, Set set) throws Exception {
		if (set.size() <= 16 - minmaxStartingFromPiece && bestAction.piece != null) {
			return bestAction.piece;
		} else {
//			return new MonteCarloParallelPlayer(monteCarloSimulations).choosePiece(board, set);
			return new NovicePlayer().choosePiece(board, set); //TODO: mcp
//			return new RandomPlayer().choosePiece(board, set);
		}
	}

	@Override
	public Action makeMove(Board board, Set set, Piece piece) throws Exception {
		if (set.size() <= 16 - minmaxStartingFromPiece) {
			bestAction = null;
			max(board, set, piece, 0);
			return new Action(piece, bestAction.x, bestAction.y);
		} else {
//			return new MonteCarloParallelPlayer(monteCarloSimulations).makeMove(board, set, piece);
			return new NovicePlayer().makeMove(board, set, piece); //TODO: mcp
//			return new RandomPlayer().makeMove(board, set, piece);
		}
	}
	
	private int max(Board board, Set set, Piece argPiece, int depth) {
		if (depth == maxDepth) {
			return evaluation.getScore(board, set, argPiece);
		}
		int max = Integer.MIN_VALUE;
		int current;
		ArrayList<Action> actions = followingActions(board, set);
		for (Action action : actions) {
			board.setPiece(argPiece, action.x, action.y);
			boolean depthNotZero = set.remove(argPiece);
			if (board.gameOver()) {
				if(depthNotZero) set.add(0, argPiece);
				board.remove(action.x, action.y);
				if (depth == 0) {
					bestAction = action;
				}
				return Integer.MAX_VALUE - 1;
			} else if (set.isEmpty()) {
				current = 0;
			} else {
				current = min(board, set, action.piece, depth + 1);
			}
			if(depthNotZero) set.add(0, argPiece);
			board.remove(action.x, action.y);
			if (current > max) {
				max = current;
				if (depth == 0) {
					bestAction = action;
				}
			}
		}
		return max;
	}


	private int min(Board board, Set set, Piece argPiece, int depth) {
		if (depth == maxDepth) {
			return - evaluation.getScore(board, set, argPiece);
		}
		int min = Integer.MAX_VALUE;
		int current;
		ArrayList<Action> actions = followingActions(board, set);
		for (Action action : actions) {
			board.setPiece(argPiece, action.x, action.y);
			set.remove(argPiece);
			if (board.gameOver()) {
				set.add(0, argPiece);
				board.remove(action.x, action.y);
				return Integer.MIN_VALUE + 1;
			} else if (set.isEmpty()) {
				current = 0;
			} else {
				current = max(board, set, action.piece, depth + 1);
			}
			set.add(0, argPiece);
			board.remove(action.x, action.y);
			if (current < min) {
				min = current;
			}
		}
		return min;
	}
	
	ArrayList<Action> followingActions(Board board, Set set) {
		ArrayList<Action> actions = new ArrayList<Action>();
		ArrayList<int[]> freePositions = board.getFreePositions();
		for (int[] pos : freePositions) {
			if (set.isEmpty()) {
				actions.add(new Action(null, pos[0], pos[1]));
				break;
			}
			for (Piece piece : set.getPieces()) {
				actions.add(new Action(piece, pos[0], pos[1]));
			}
		}
		return actions;
	}
	
	@Override
	public String toString() {
		return "Min/Max Player (depth: " + maxDepth + ")";
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
		MinMaxPlayer player = new MinMaxPlayer(2, new TestEvaluation());
		
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