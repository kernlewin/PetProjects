package ksk.ai.maze;

import ksk.ai.util.KEvent;


public class MazeEvent extends GridEvent {
	
	protected EventType mType;
	protected GridNode mNode;
	
	public static enum EventType {
		NODE_EVENT,
		WALL_EVENT,
		MAZE_EVENT
	}

	/**
	 * Generate a Maze Event.  If the event doesn't occur at a specific location,
	 * then the row or col (or both) should be negative.
	 * 
	 * @param source Object generating the event.
	 * @param type The type of event that has occurred.
	 * @param row The row on which the event occurred, or negative value if no location
	 * @param col The column on which the event occurred, or negative value if no location
	 */
	public MazeEvent(Maze source, EventType type, GridNode n)
	{
		super(source);
		
		mType = type;
		mNode = n;
	}
}
