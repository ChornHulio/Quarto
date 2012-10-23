package core;
import player.HumanPlayer;
import player.IPlayer;
import player.PlayerFactory;
import core.logger.GMLogger;
import core.logger.HumanOpponentLogger;
import core.logger.HumanVsHumanLogger;
import core.logger.ILogger;
import core.logger.QuietLogger;
import core.logger.VerboseLogger;
import java.util.Scanner;

public class Main {
	
	private static int rounds = 1; // default
	private static IPlayer[] players = new IPlayer[2];
	private static ILogger logger = new VerboseLogger();

	public static void main(String[] args) throws Exception {
		
		//String[] args = {"-q", "-n", "10", "-p", "human", "3", "-p", "novice", "3"}; // for debugging porpuse
		// parse arguments or ask for it
		if(args.length > 0) {
			parseArguments(args);
		} else {
			askForArguments();
		}
		
		// play
		int[] results = new int[3];
		long time = System.currentTimeMillis();
		for (int i = 0; i < rounds; i++) {
			if (!(logger instanceof GMLogger) && !(logger instanceof HumanOpponentLogger) && !(logger instanceof VerboseLogger)) {
				System.out.print(i + "...");
				if ((i+1) % 10 == 0) {
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
		System.out.println("\nExecution time per game played: " + (System.currentTimeMillis() - time) / ((double)rounds * 1000) + "s");
		System.out.println("******* results *******");
		System.out.println(results[0] + "\t: " + players[0].toString());
		System.out.println(results[1] + "\t: " + players[1].toString());
		System.out.println(results[2] + "\t: " + "ties");
	}
	
	static void parseArguments(String[] args) {
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
					players[playerCount++] = PlayerFactory.createPlayer(args, ++i);
				} else if (args[i].trim().equals("-n")) {
					rounds = Integer.parseInt(args[++i]);
				} else if (args[i].trim().equals("-q") || args[i].trim().equals("--quiet")) {
					logger = new QuietLogger();
				} else if (args[i].trim().equals("-g")) {
					logger = new GMLogger();
				} 
			}
			if (playerCount != 2 || players[0] == null || players[1] == null) {
				throw new Exception();
			}
			for (IPlayer player : players) {
				if (player instanceof HumanPlayer && !(logger instanceof GMLogger)) {
					logger = new HumanOpponentLogger();
				}
			}
			if ((players[0] instanceof HumanPlayer) && (players[1] instanceof HumanPlayer)) {
				logger = new HumanVsHumanLogger();
			}
		} catch(Exception e){
			System.out.println(errorMessage);
			System.exit(1);
		}
	}
	
	private static void askForArguments() {
		Scanner scanner = new Scanner( System.in );
		
		// rounds
		System.out.println("How many rounds do you want to play?");
		rounds = scanner.nextInt();
		
		// logger
		System.out.println("Do you want debug outputs? (y/n)");
		String quiet = scanner.next();
		if(quiet.trim().equals("n") || quiet.trim().equals("no")) {
			logger = new QuietLogger();
 		}

		// choose players
		int playerCount = 0;
		while (true) {
			boolean wrongChoice = false;
			System.out.println("Choose a player: human, random, novice, mc, pmc, minmax");
			String player = scanner.next().trim();
			int secondArg = 0;
			if (player.equals("mc") || player.equals("pmc")) {
				System.out.println("How many Monte-Carlo simulations do you want?");
				secondArg = scanner.nextInt();
			} else if (player.trim().equals("minmax")) {
				System.out.println("What max depth of the MinMax-Algorithm do you want?");
				secondArg = scanner.nextInt();
			}
			String[] arg = {player, String.valueOf(secondArg)};
			players[playerCount] = PlayerFactory.createPlayer(arg, 0);

			if(players[playerCount] == null) {
				System.out.println("You choose a wrong option. Do it again...");
				wrongChoice = true;
			}
			if(!wrongChoice && playerCount == 1) {
				break;
			} else if (!wrongChoice) {
				playerCount++;
			}
		}

		for (IPlayer player : players) {
			if (player instanceof HumanPlayer && !(logger instanceof GMLogger)) {
				logger = new HumanOpponentLogger();
			}
		}
		if ((players[0] instanceof HumanPlayer) && (players[1] instanceof HumanPlayer)) {
			logger = new HumanVsHumanLogger();
		}
	}
}
