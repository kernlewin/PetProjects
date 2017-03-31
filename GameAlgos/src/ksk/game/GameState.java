package ksk.game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//Abstract base class representing the state of a game
//GameStates are nodes in a graph; this class makes no assumptions about how node transitions
//occur, or how many states you can transition to from each state, it just allows you to navigate the graph
public abstract class GameState {

	//Maintain a list of possible next states (this may change as the game goes on)
	private List<Move> mNextMoves;
	
	//Constructor; optionally populate the list of possible next states
	//If it's not done at construction time, it will be done "just in time"
	public GameState(int lookahead)
	{
		if (lookahead > 0)
		{
			mNextMoves = new ArrayList<Move>();
			populateNextMoves(lookahead-1);
		}
		else
		{
			mNextMoves = null;
		}
	}


	//Get a list of the possible state transitions
	public Move[] getNextMoves()
	{
		if (mNextMoves==null)
		{
			populateNextMoves(1);
		}
		
		Move[] result = new Move[mNextMoves.size()];
		
		int i=0;
		for (Iterator<Move> iter = mNextMoves.iterator(); iter.hasNext();)
		{
			result[i++] = iter.next();
		}
		
		return result;
	}

	//Check if a particular next state is a valid transition from this one
	public boolean isValidMove(Move move)
	{
		return mNextMoves.contains(move);
	}
	
	
	/****************************
	 * Abstract methods
	 ***************************/
	
	//Abstract method to populate the list of possible moves from this GameState
	protected abstract void populateNextMoves(int lookahead);
	
	//Apply this move to the current GameState.
	//Returns the resulting GameState, or null if the move is illegal
	//Note that this method must update the turn, if applicable
	public abstract GameState applyMove(GameState state);	

}
