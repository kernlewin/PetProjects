package ksk.game;

import java.util.ArrayList;

//Abstract base class representing the state of a game
//GameStates are nodes in a graph; this class makes no assumptions about how node transitions
//occur, or how many states you can transition to from each state, it just allows you to navigate the graph

public abstract class GameState {

	//Maintain a list of possible next states (this may change as the game goes on)
	private ArrayList<GameState> mNextStates;
	
	//Get a list of the possible state transitions
	public GameState[] nextStates()
	{
		
	}
	
	
}
