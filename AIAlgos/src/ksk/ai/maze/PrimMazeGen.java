package ksk.ai.maze;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Generates a maze using Prim's algorithm:
 * 
 * - Make a list of visited locations, initially just the start location
 * - Make a list of walls, initially just the walls around the start location
 * - Choose a random wall from the list.  If the location it leads to has not been
 *   visited, open this wall, add the new location to the visited list, and add its walls
 *   to the wall list.
 * - Remove the wall from the wall list
 * - Repeat the previous two steps until no walls remain in the list
 * 
 * @author Kern Lewin
 * @version 0.5
 */


public class PrimMazeGen implements MazeGenerator {

	@Override
	public void generate(Grid g, GridNode start, GridNode goal) {
		// TODO Auto-generated method stub
		
		//Set of visited GridNodes
		Set<GridNode> visited =new HashSet<GridNode>();
		visited.add(start);
		
		//Set of walls to consider
		Set<Edge<GridNode>> walls = g.getWalls(start);
		
		//Continue until there are no walls left to consider
		Random r = new Random();
		while (!walls.isEmpty())
		{
			//Choose a random wall
			int index = r.nextInt(walls.size());
			
			//Iterate to the chosen wall
			Iterator<Edge<GridNode>> iter = walls.iterator();
			Edge<GridNode> wall = iter.next();
			for (int i=0; i<index; i++)
			{
				wall = iter.next();
			}
			
			//Check if there is an unvisited node
			List<GridNode> nodes = wall.getNodes();
			GridNode node1 = nodes.get(0);
			GridNode node2 = nodes.get(1);
			
			if ((!visited.contains(node1))||(!visited.contains(node2)))
			{
				//Mark these Nodes as visited and connect them
				g.connect(node1, node2);
				visited.add(node1);
				visited.add(node2);
				
				//Add the new walls to the list
				walls.addAll( g.getWalls(node1));
				walls.addAll( g.getWalls(node2));
			}
			
			//Remove the wall
			walls.remove(wall);
		}
	}

}
