package ksk.game;

/*
 * This abstract base class implements the minimax algorithm, for a two-player zero-sum game.
 * It is assumed that there are two players, one trying to maximize their score, one trying to minimize it.
 * 
 * Child classes need to implement a method to calculate a value for each game state (node), potentially
 * using a heuristic.
 */

public abstract class MinimaxAlgo {
	
	//Calculate a value for the current game state by looking ahead, assuming that each player will appropriately
	//maximize/minimize their score
	public double minimax(GameState state, int depth, boolean maximizing)
	{
		return Double.NaN;
	}
	
	//Abstract method for evaluating a node without minimax (either because it's a leaf, or because we've looked ahead
	//as far as we plan to
	public abstract double evaluate(GameState state);
	
}
