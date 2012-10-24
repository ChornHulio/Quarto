package core;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import player.HumanPlayer;
import player.IPlayer;
import player.NovicePlayer;
import core.logger.GMLogger;
import core.logger.ILogger;
import core.logger.QuietLogger;


public class Game {
	
	private IPlayer[] players;
	private Set set = new Set();
	private Board board = new Board();
	private int winner;
	private ILogger logger;

	public Game(IPlayer[] players, ILogger logger) {
		this.players = players;
		this.logger = logger;
	}

	/**
	 * If a game shouldn't be started from the initial but from an intermediate state.
	 * @param board state of the board to start the game with
	 * @param set state of the set to start the game with
	 */
	public Game(Board board, Set set) { // in order to simulate games
		this.board = board;
		this.set = set;
		players = new IPlayer[2];
		players[0] =  new NovicePlayer();
		players[1] = players[0];
		this.logger = new QuietLogger();
	}

	/**
	 * Contains the main game loop
	 * @return 0 = first player won, 1 = second player won, 2 = tie
	 * @throws Exception
	 */
	public int play() throws Exception {
		int turn = 0;
		Piece firstPiece = null;
		// The following if condition is needed to satisfy the GameMaster's protocol.
		// At the very beginning of a game there are two options:
		// 1) The game receives a piece from StdIn:
		//    That means, that the game must reply with an action (= "we" are the second player)
		// 2) The game receives "." from StdIn:
		//    That means, that the game must reply with a piece (= "we" are the first player)
		if (logger instanceof GMLogger) {
			String firstInput = new Scanner(System.in).nextLine();
			if (!firstInput.trim().equals(".")){
				firstPiece = Piece.stringToPeace(firstInput);
				if (!(players[turn] instanceof HumanPlayer)) {
					turn++;
				}
			} else {
				if (players[turn] instanceof HumanPlayer) {
					turn++;
				}
			}
		}
		while (true) {
			logger.logBoard(players[turn % 2], board, set);
			Piece piece;
			if (firstPiece != null) { // only used for GameMaster's mode
				piece = firstPiece;
				firstPiece = null;
			} else {
				piece = players[turn % 2].choosePiece(board.copy(), set.copy());
			}
			logger.logPiece(players[turn++ % 2], board, piece, set);
			checkMove(piece);
			Action action = players[turn % 2].makeMove(board.copy(), set.copy(), piece);
			logger.logAction(players[turn % 2], action);
			checkMove(action);
			if (gameOver(turn % 2)) {
				logger.logGameOver(players[turn % 2], winner, board);
				return winner; // winner is set inside gameOver()
			}
		}
	}

	private boolean gameOver(int playerTurn) {
		if (board.gameOver()) { // one player has won
			winner = playerTurn;
			return true;
		} else if (set.isEmpty()) {
			winner = 2; // tie
			return true;
		}
		return false;
	}

	private void checkMove(Action action) throws Exception {
		if (!board.isEmpty(action.x, action.y)) {
			throw new Exception("Cell already taken");
		}
		board.setPiece(action.piece, action.x, action.y);
	}

	private void checkMove(Piece piece) throws Exception {
		if (!set.contrains(piece)) {
			throw new Exception("Piece already on the board");
		}
		set.remove(piece);
	}
	
	public static void main(String[] args) throws Exception {
		Board board = new Board();
		Set set = new Set();
		Piece b = new Piece(false, false, true, true);
		Piece r = new Piece(false, true, true, true); 
		Piece r_stern = new Piece(false, true, true, false);
		Piece b_big = new Piece(true, false, true, true);
		Piece r_big = new Piece(true, true, true, true);
		board.setPiece(b, 0, 0);
		board.setPiece(r, 1, 0);
		board.setPiece(r_stern, 2, 0);
		board.setPiece(b_big, 1, 3);
		board.setPiece(r_big, 0, 3);
		set.remove(b);
		set.remove(r);
		set.remove(r_stern);
		set.remove(b_big);
		set.remove(r_big);
		int simulations = 100000;
		ArrayList<int[]> freePositions = board.getFreePositions();
		Random generator = new Random();
		double[] wins = new double[set.size()];
		double[] losses = new double[set.size()];
		for(int p = 0; p < set.size(); p++) {
			for (int i = 0; i < simulations; i++) {
				int index = generator.nextInt(freePositions.size());
				Set tmpSet = set.copy();
				Board newBoard = board.copy();
				tmpSet.remove(set.get(p));
				newBoard.setPiece(set.get(p), freePositions.get(index)[0], freePositions.get(index)[1]);
				if (newBoard.gameOver()) {
					wins[p]++;
					continue;
				}
				Game game = new Game(newBoard, tmpSet);
				int currentResult = game.play();
				if(currentResult == 0){
					wins[p]++;
				} else if (currentResult == 1) {
					losses[p]++;
				}
			}			
		}
		for(int p = 0; p < set.size(); p++) {
			System.out.println(set.get(p) + " :\twins: " + wins[p] / simulations + "\tlosses: " + losses[p] / simulations);
		}
		
	}
}
