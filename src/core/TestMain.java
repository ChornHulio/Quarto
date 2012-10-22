package core;

import player.IPlayer;
import player.MinMaxAlphaBetaPlayer;
import player.evaluation.CompletingPiecesEvaluationAbsolute;
import player.evaluation.SimpleEvaluation;
import player.evaluation.TestEvaluation;
import core.logger.GMLogger;
import core.logger.ILogger;
import core.logger.QuietLogger;

public class TestMain {

	private static int rounds = 1000; // default
	private static IPlayer[] players = new IPlayer[2];
	private static ILogger logger = new QuietLogger();
//	private static ILogger logger = new VerboseLogger();

	public static void main(String[] args) throws Exception {
		int count = 0;
//		players[count++] = new MinMaxPlayer(2, new CompletingPiecesEvaluationAbsolute());
//		players[count++] = new MinMaxPlayer(2, new CompletingPiecesEvaluationRelative());
		players[count++] = new MinMaxAlphaBetaPlayer(2, new SimpleEvaluation());
		players[count++] = new MinMaxAlphaBetaPlayer(2, new CompletingPiecesEvaluationAbsolute());
//		players[count++] = new NovicePlayer();

		// play
		int[] results = new int[3];
		long time = System.currentTimeMillis();
		for (int i = 0; i < rounds; i++) {
			if (!(logger instanceof GMLogger)) {
				System.out.print(i + "...");
				if ((i + 1) % 10 == 0) {
					System.out.println();
				}
			}
			Game game = new Game(players, logger);
			int result = game.play();
			results[result]++;
			// The first player who makes the move is preferred, so you have to switch the players after each round
			IPlayer tmpPlayer = players[0];
			players[0] = players[1];
			players[1] = tmpPlayer;
			int tmpResult = results[0];
			results[0] = results[1];
			results[1] = tmpResult;
		}

		// printing the results
		System.out.println("\nExecution time per game played: "
				+ (System.currentTimeMillis() - time)
				/ ((double) rounds * 1000) + "s");
		System.out.println("******* results *******");
		System.out.println(results[0] + "\t: " + players[0].toString());
		System.out.println(results[1] + "\t: " + players[1].toString());
		System.out.println(results[2] + "\t: " + "ties");

	}

}
