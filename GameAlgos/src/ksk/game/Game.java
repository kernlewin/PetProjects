package ksk.game;

import java.util.HashSet;
import java.util.Set;


/*
 * General game-controlling class
 * 
 * Presently a game consists of:
 * - A set of players
 * - A current game state
 * 
 */

public abstract class Game {

	//Current game state
	protected GameState mState;

	//List of active players
	protected Set<Player> mPlayers;

	//Current turn (where applicable)
	protected Player mTurn;

	//Constructor; call the implementation-specific "reset()" method to initialize
	public Game()
	{
		//Empty player list
		mPlayers = new HashSet<Player>();
		mState = null;
		mTurn = null;
		reset();
	}


	//A "move" will be implemented by providing an index to the list of possible next
	//states.  If the index is valid, then the GameState object will do whatever needs to be
	//done to accomplish this state transition.  Returns true if the move was legal, otherwise
	//false.  Note that optionally this method can also take a parameter indicating who's
	//trying to make the move.  This allows child classes to make sure that people don't move
	//out of turn.
	public final boolean move(Player caller, GameState nextState)
	{
		if ( ((caller == null)||(caller.equals(mTurn)))
				&& (mState.isValidNextState(nextState)) )
		{
			//Call implementation-specific method to actually process the move
			//It is responsible for any consequences of the move, including updating the
			//turn, eliminating inactive players, etc.
			if (implementMove(caller, nextState))
			{
				mState = nextState;
				return true;
			}
		}

		return false;

	}

	public final boolean move(GameState nextState)
	{
		return move(null, nextState);
	}

	//Method to add/remove players from the active list
	//Return true if the player list changed
	public final boolean registerPlayer(Player p, boolean active)
	{
		if (active)
		{
			//Attempt to add a player
			return mPlayers.add(p);
		}
		else if (!active)
		{
			//Attempt to remove a player
			return mPlayers.remove(p);
		}

		return false;
	}

	/********************
	 * Pure abstract methods
	 * *****************/

	//Reset the game to its starting position (start state, players, turn, etc.)
	protected abstract void reset();

	//Child classes need to override this if there's anything they need to do other than just
	//changing states.  Return true if the transition is approved, othewise, false
	protected abstract boolean implementMove(Player caller, GameState nextState);
}


