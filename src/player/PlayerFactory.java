package player;

import player.evaluation.CompletingPiecesEvaluationRelative;

public class PlayerFactory {

	public static IPlayer createPlayer(String[] args, int i) {
		if (args[i].trim().equals("human")) {
			return new HumanPlayer();
		}
		else if (args[i].trim().equals("random")) {
			return new RandomPlayer();
		}
		else if (args[i].trim().equals("novice")) {
			return new NovicePlayer();
		}
		else if (args[i].trim().equals("mc")) {
			int simulations = Integer.parseInt(args[++i]);
			return new MonteCarloPlayer(simulations);
		}
		else if (args[i].trim().equals("pmc")) {
			int simulations = Integer.parseInt(args[++i]);
			return new MonteCarloParallelPlayer(simulations);
		}
		else if (args[i].trim().equals("minmax")) {
			int maxDepth = Integer.parseInt(args[++i]);
			return new MinMaxAlphaBetaPlayer(maxDepth, new CompletingPiecesEvaluationRelative());
		}
		return null;
	}

}
