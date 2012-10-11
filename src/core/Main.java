package core;
import player.*;
import player.evaluation.TestEvaluation;
import core.logger.*;


public class Main {

	public static void main(String[] args) throws Exception {
		
		int rounds = 1; // default
		
//		String[] args = {"-g", "-n", "10", "-p", "random", "4", "-p", "novice", "10000"};
		IPlayer[] players = new IPlayer[2];
		ILogger logger = new VerboseLogger();
		int playerCount = 0;
		String errorMessage = "Usage: -p PLAYER [parameters...] -p PLAYER [parameters...] [-n N] [-q]\n";
		errorMessage += "\t-n N\n";
		errorMessage += "\t\tplay N games (default: 1)\n";
		errorMessage += "\t-q, --quiet\n";
		errorMessage += "\t\tquiet mode\n";
		errorMessage += "\t-g\n";
		errorMessage += "\t\tin case the game is announced by the game master\n";
		errorMessage += "PLAYER can be:\n";
		errorMessage += "\t human\t\t: for a human player\n";
		errorMessage += "\t random\t\t: for a player who acts randomly\n";
		errorMessage += "\t novice\t\t: for a player who will always win in one step if he can (and won't let you win in one step)\n";
//		errorMessage += "\t mc <N>\t\t: for a monte-carlo player with N simulations at every move\n";
//		errorMessage += "\t pmc <N>\t: for a monte-carlo player with N simulations at every move (parallel execution)\n";
		errorMessage += "\t minmax <N>\t: for a player who uses the min/max algorithm and searches until N-th level\n";
		try{
			for (int i = 0; i < args.length; i++) {
				if (args[i].trim().equals("-p")) {
					i++;
					if (args[i].trim().equals("human")) {
						players[playerCount++] = new HumanPlayer();
					}
					else if (args[i].trim().equals("random")) {
						players[playerCount++] = new RandomPlayer();
					}
					else if (args[i].trim().equals("novice")) {
						players[playerCount++] = new NovicePlayer();
					}
					else if (args[i].trim().equals("mc")) {
						int simulations = Integer.parseInt(args[++i]);
						players[playerCount++] = new MonteCarloPlayer(simulations);
					}
					else if (args[i].trim().equals("pmc")) {
						int simulations = Integer.parseInt(args[++i]);
						players[playerCount++] = new MonteCarloParallelPlayer(simulations);
					}
					else if (args[i].trim().equals("minmax")) {
						int maxDepth = Integer.parseInt(args[++i]);
//						players[playerCount++] = new MinMaxPlayer(maxDepth, new TestEvaluation());
						//TODO: delete
						players[playerCount++] = new MinMaxAlphaBetaPlayer(maxDepth, new TestEvaluation());
//						break;
					}
				} else if (args[i].trim().equals("-n")) {
					rounds = Integer.parseInt(args[++i]);
				} else if (args[i].trim().equals("-q") || args[i].trim().equals("--quiet")) {
					logger = new QuietLogger();
				} else if (args[i].trim().equals("-g")) {
					logger = new GMLogger();
				} 
			}
			if (playerCount != 2) {
				throw new Exception();
			}
			for (IPlayer player : players) {
				if (player instanceof HumanPlayer && !(logger instanceof GMLogger)) {
					logger = new HumanOpponentLogger();
				}
			}
		} catch(Exception e){
			System.out.println(errorMessage);
			System.exit(1);
		}
		int[] results = new int[3];
		long time = System.currentTimeMillis();
		for (int i = 0; i < rounds; i++) {
			if (!(logger instanceof GMLogger)) {
				System.out.print(i + "...");
				if ((i+1) % 10 == 0) {
					System.out.println();
				}				
			}
			Game game = new Game(players, logger);
			int result = game.play();
			results[result]++;
		}
		System.out.println("\nExecution time per game played: " + (System.currentTimeMillis() - time) / ((double)rounds * 1000) + "s");
		
		// printing the results
		System.out.println("******* results *******");
		System.out.println(results[0] + "\t: " + players[0].toString());
		System.out.println(results[1] + "\t: " + players[1].toString());
		System.out.println(results[2] + "\t: " + "ties");
	}

}
