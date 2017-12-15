package ksk.ai.maze;

/**
 * Generates a maze using the Krukal algorithm:
 * 
 * - Create a collection of Sets of grid locations.  Initially, each location in the maze is
 *   in a separate set
 * - Choose a wall at random
 * - If the wall connects locations in separate sets, connect these two locations, and unite
 *   their sets
 * - Repeat until there are no more walls to consider / all cells are in one set
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class KruskalMazeGen implements MazeGenerator {

	@Override
	public void generate(Grid g, GridNode start, GridNode goal) {
		// TODO Auto-generated method stub

		//Close everything (clear the maze)
		g.disconnectAll();

		//Get a list of walls
		List<Edge<GridNode>> walls = new ArrayList<Edge<GridNode>>(g.getWalls());
		
		//Map each node to its own "set" number.  There is a path between cells iff
		//they have the same value in this map
		Map<GridNode, Integer> sets = new HashMap<GridNode,Integer>();

		int index = 0;
		for (GridNode n : g.getNodes())
		{
			sets.put(n, index++);
		}
		
		//While there are walls left to consider...
		Random r = new Random();
		while (!walls.isEmpty())
		{
			//Choose a wall at random
			index = r.nextInt( walls.size() );
			Edge<GridNode> wall = walls.get(index);

			//Remove the wall from the list
			walls.remove(index);

			//If the cells on either side of this wall are in different sets, connect them and
			//join their sets together
			List<GridNode> nodes = wall.getNodes();
			GridNode node1 = (GridNode)nodes.get(0);
			GridNode node2 = (GridNode)nodes.get(1);
			int set1 = sets.get(node1);
			int set2 = sets.get(node2);
			
			if (g.isAdjacent(node1,node2)&&(set1 != set2))
			{
				//Add all of the nodes in the second set to the first set
				Set<Map.Entry<GridNode, Integer>> mapEntries = sets.entrySet();
				for (Map.Entry<GridNode, Integer> e : mapEntries)
				{
					if (e.getValue()==set2)
					{
						e.setValue(set1);
					}
				}
				
				g.connect(node1, node2);
			}

		} //Repeat
	}

}
