package ksk.ai.maze;

/**
 * 
 * @author Kern Lewin
 * 
 * @version 0.5
 *
 * A GridNode is a special type of Node that uses a row and column as its unique identifiers.
 * Two GridNodes cannot share the same coordinates, and they cannot be connected unless they are adjacent to
 * each other
 */
public class GridNode extends Node {

	protected int mRow;
	protected int mColumn;
	
	/**
	 * Constructor:  
	 * @param row
	 * @param column
	 */
	public GridNode(int row, int column)
	{
		//Need to pass an ID to parent class, but in this case it may not be unique
		//We will use the row and column numbers separatedly to determine equality
		super(row*column);
		
		mRow = row;
		mColumn = column;
		
	}
	
	/**
	 * Get the row number of this Node
	 */
	public int getRow()
	{
		return mRow;
	}
	
	/**
	 * Get the column number of this Node
	 */
	public int getColumn()
	{
		return mColumn;
	}
	
	public String toString()
	{
		return "(" + mRow + ", " + mColumn + ")";
	}
	
	public int hashCode()
	{
		return mRow*mColumn;  //Needn't be unique
	}
	
	public boolean equals(Object o)
	{
		if (!(o instanceof GridNode))
		{
			return false;
		}
		
		if ((((GridNode)o).mRow == mRow)&&(((GridNode)o).mColumn == mColumn))
		{
			return true;
		}
		
		return false;
	}
	
	
}
