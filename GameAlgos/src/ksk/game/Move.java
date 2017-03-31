package ksk.game;

//Abstract class representing a "Move" within a game.
//All Moves will be represented using a String, which will be decoded by the concrete subclass

public abstract class Move {
	protected String mMoveString;
	
	public Move (String str)
	{
		if (str==null)
		{
			mMoveString = "";
		}
		
		decode();
	}
	
	//Get a reference to the Player making this move
	public String toString()
	{
		return mMoveString;
	}
	
	//Abstract method to interpret the MoveString
	//This will presumably modify some fields of the abstract subclass, which can be
	//used in applying the move.
	protected abstract void decode();
	}
