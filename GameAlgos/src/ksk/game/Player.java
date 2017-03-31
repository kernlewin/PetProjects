package ksk.game;

//Represents a player in a game
//May or may not be a human player.

public abstract class Player {
	//The Player class has methods to remember the last move it made, as well as the current
	//move it's working on (this working move will be used if time runs out while the program
	//is "thinking"
	Move lastMove, workingMove;
	
	//Constructor
	public Player()
	{
		lastMove = null;
		workingMove = null;
	}
	
	//Get a move from this player, timing out after timeOut milliseconds
	public final Move getMove(Gamestate, long timeOut)
	{
		long startTime = System.currentTimeMillis();
		
		while ()
	}
	
	//Get a move from this Player, unlimited time
	public final Move getMove(GameState state)
	{
		return getMove(state, -1);
	}

}
