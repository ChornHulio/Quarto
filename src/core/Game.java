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
		if (board.gameOver()) { // one of the  players has won
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
}
