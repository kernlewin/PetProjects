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

	//Game class supports turn-based or simultaneous games
	//Simultaneous games will use a separate thread for each player
	public static enum PlayStyle {TURN_BASED, SIMULTANEOUS};

	//Current game state
	protected GameState mState;

	//List of active players
	protected Set<Player> mPlayers;

	//Play style
	protected PlayStyle mPlayStyle;

	//Current turn (where applicable)
	protected Player mTurn;


	//Constructor; call the implementation-specific "reset()" method to initialize
	public Game(PlayStyle style)
	{
		//Empty player list
		mPlayers = new HashSet<Player>();
		mState = null;
		mTurn = null;
		mPlayStyle = style;
		reset();
	}

	//Default playstyle is simultaneous
	public Game()
	{
		this(PlayStyle.SIMULTANEOUS);
	}

	//This method is used by Players attempting to make a move
	//If the Move is valid, then we will apply it, resulting in a new GameState.
	//Returns true if the move was legal, otherwise
	//false.
	public final boolean submitMove(Move m)
	{
		Player caller = m.getPlayer();

		if ((caller.equals(mTurn)) && (mState.isValidMove(m)) )
		{
			//Call implementation-specific method to actually process the move
			GameState nextState = mState.apply(m);
			
			return (nextState != null)?true:false;
		}
		
		return false;
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
	protected abstract GameState implementMove(Player caller, Move move);
}


