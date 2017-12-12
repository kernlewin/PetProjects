package ksk.ai.maze;

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
		List<Edge> walls = new ArrayList<Edge>(g.getWalls());

		System.out.println(walls.size());
		
		//Map each node to its own "set" number.  There is a path between cells iff
		//they have the same value in this map
		Map<GridNode, Integer> sets = new HashMap<GridNode,Integer>();

		int index = 0;
		for (GridNode n : g.getNodes())
		{
			sets.put(n, index++);
		}
		
		//While there is more than one set...
		Random r = new Random();
		while (!walls.isEmpty())
		{
			//Choose a wall at random
			index = r.nextInt( walls.size() );
			Edge wall = walls.get(index);

			//Remove the wall from the list
			walls.remove(index);

			//If the cells on either side of this wall are in different sets, connect them and
			//join their sets together
			Node[] nodes = wall.getNodes();
			GridNode node1 = (GridNode)nodes[0];
			GridNode node2 = (GridNode)nodes[1];
			
			if (g.isAdjacent(node1,node2)&&(sets.get(node1) != sets.get(node2)))
			{
				sets.put(node2, sets.get(node1));
				g.connect(node1, node2);
			}

		} //Repeat
	}

}
