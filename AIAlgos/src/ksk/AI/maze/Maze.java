package ksk.AI.maze;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Map;

import ksk.AI.graph.Graph;

/*
 * Class represents a rectangular Maze.  Each cell in the maze is connected to up to four neighours via doors
 * Each door can be open or closed.
 * 
 * A cell is considered open if at least one of its doors is open
 * 
 * In this implementation, the Maze class is backed by a Graph under the hood, where every cell is a Node, and every
 * open door is represented by an undirected edge
 * 
 * Each cell of the maze optionally has a character and a colour associated with it
 * 
 */

public class Maze implements ksk.vis.Printable{
	//Parameters for the maze dimensions
	int mRows;
	int mCols;
	
	//This graph will internally represent all of the cells and walls in the maze
	//The value of each node will indicate its position within the maze
	Graph<Integer> mGraph;
	
	//Map Characters and Colours to nodes in the graph
	Map<Integer, Character> mCharMap;
	Map<Integer, Color> mColorMap;
	
	//Copy Constructor
	
	//Construct a maze with given dimensions; initially all doors are closed
	public Maze(Dimension size)
	{
		//Must have at least 1 row, 1 column
		mRows = Math.max(size.height,1);
		mCols = Math.max(size.width,1);
		
		mGraph = new Graph<Character>()
	}
	
	//Copy Constructor
	
	//Package-private method to toggle doors; nobody outside of the package should be able to modify
	//the structure of the maze after construction
	
	//Set the character for a cell (if the cell is closed, this will not be displayed)
	
	//Set the colour for a cell (again, only if it is open)
	
	//Check if a door is open;  false if the door is closed or doesn't exist
	
	//Check if a cell is open
	
	//Get the row and column for a particular neighbour; null if the neighbour doesn't exist
	
	//Printable method:  drawS
	
	//Printable method:  drawG

}
