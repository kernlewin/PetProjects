package ksk.game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//Abstract base class representing the state of a game
//GameStates are nodes in a graph; this class makes no assumptions about how node transitions
//occur, or how many states you can transition to from each state, it just allows you to navigate the graph
public abstract class GameState {

	//Maintain a list of possible next states (this may change as the game goes on)
	private List<GameState> mNextStates;
	
	//Constructor; optionally populate the list of possible next states
	//If it's not done at construction time, it will be done "just in time"
	public GameState(int lookahead)
	{
		if (lookahead > 0)
		{
			mNextStates = new ArrayList<GameState>();
			populateNextStates(lookahead-1);
		}
		else
		{
			mNextStates = null;
		}
	}


	//Get a list of the possible state transitions
	public GameState[] getNextStates()
	{
		if (mNextStates==null)
		{
			populateNextStates();
		}
		
		GameState[] result = new GameState[mNextStates.size()];
		
		int i=0;
		for (Iterator<GameState> iter = mNextStates.iterator(); iter.hasNext();)
		{
			result[i++] = iter.next();
		}
		
		return result;
	}

	//Check if a particular next state is a valid transition from this one
	public boolean isValidNextState(GameState state)
	{
		return mNextStates.contains(state);
	}
	
	
	/****************************
	 * Abstract methods
	 ***************************/
	
	//Abstract method to populate the list of possible next states
	protected abstract void populateNextStates(int lookahead);
}
