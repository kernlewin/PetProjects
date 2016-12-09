package ksk.AI.ant;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import ksk.AI.graph.Edge;
import ksk.AI.graph.Graph;
import ksk.AI.graph.Node;

/*
 * This Graph subclass represents a graph that will be traversed by a population of
 * virtual ants, leaving pheromone trails.
 * 
 * AntGraphs will support traversal by an ant population to deposit pheromones, as well as
 * gradual evaporation to reduce pheromones.
 * 
 * This graph needs to be able to track the amount of pheromone on each edge, as well as the
 * normal weight (which will typically represent the "length" or "cost" of the edge) 
 */

public class AntGraph<T> extends Graph<T> {

	//Maintain a separate Graph internally to track pheromone levels
	Graph<T> m_PheromoneLevels;

	//Ants!!!
	AntColony m_Ants;

	//Construct an empty AntGraph
	public AntGraph()
	{
		this(AntColony.DEF_PHEROMONE_WEIGHT, AntColony.DEF_LENGTH_WEIGHT, AntColony.DEF_PHEROMONE_INTENSITY, AntColony.DEF_ANT_POPULATION);
	}

	//Construct an empty AntGraph
	public AntGraph(int antCount)
	{
		this(AntColony.DEF_PHEROMONE_WEIGHT, AntColony.DEF_LENGTH_WEIGHT, AntColony.DEF_PHEROMONE_INTENSITY, antCount);
	}

	//Construct an empty AntGraph
	public AntGraph(double pheromoneWeight, double lengthWeight, double intensity)
	{
		this(pheromoneWeight, lengthWeight, intensity, AntColony.DEF_ANT_POPULATION);

	}

	//Construct an empty AntGraph, specifying all parameters
	public AntGraph(double pheromoneWeight, double lengthWeight, double intensity, int antCount)
	{
		//Create the ant colony
		m_Ants = new AntColony(pheromoneWeight, lengthWeight, intensity, antCount);

		//Create a set of edges representing pheromone levels
		m_PheromoneLevels = new Graph<T>();
	}

	//Copy Constructor
	public AntGraph(AntGraph<T> src)
	{

	}

	//Override methods for connecting and disconnecting nodes, so that the Pheromone graph can be
	//kept in sync

	@Override
	public void connect(Node<T> n1, Node<T> n2, boolean directional, double weight)
	{
		super.connect(n1,n2,directional,weight);

		//Connect these nodes in the pheromone graph, with weight zero
		m_PheromoneLevels.connect(n1,n2,directional,0);
	}

	@Override
	public int disconnect(Node<T> n1, Node<T> n2)
	{
		super.disconnect(n1,n2);

		//Disconnect these nodes in the pheromone graph
		return m_PheromoneLevels.disconnect(n1,n2);
	}

	//Reset Pheromone levels
	public void resetPheromones()
	{
		//Iterate through all of the edges in the pheromone graph
		//To do this, we need to get all of the Nodes in the graph, and check which
		//ones are connected
		List<Node<T>> nodes = m_PheromoneLevels.getNodes();

		//Loop through every combination of nodes (note that both orders must be checked),
		//Looking for edges
		for (Node<T> n1 : nodes)
		{
			for (Node<T> n2 : nodes)
			{
				//Check if these nodes are connected
				if (m_PheromoneLevels.isConnected(n1,n2))
				{
					//Reset the pheromone level to zero
					//Note that we will assume directed connections; the Graph class
					//will sort it out if we're wrong, once we reset both directions
					m_PheromoneLevels.connect(n1,n2, true, 0);
				}
			}
		}
	}

	//Perform a number of traversals of the graph between two selected nodes, leaving pheromone trails behind
	//Note that traversalCount is the number of times that the whole population will attempt to walk from start
	//to goal
	public void traverse(Node<T> start, Node<T> goal, int traversalCount)
	{
		//Reset the graph
		resetPheromones();

		//Perform the traversals
		for (int i=0; i<traversalCount; i++)
		{
			m_Ants.traverse(start, goal);
			m_Ants.evaporate();
		}
	}

	//Inner class:  AntColony represents a population of ants who will traverse the graph,
	//depositing pheromones on their chosen paths
	private class AntColony
	{
		//Class constants
		static private final int DEF_ANT_POPULATION = 1000;
		static private final double DEF_PHEROMONE_WEIGHT = 2.0d;
		static private final double DEF_LENGTH_WEIGHT = 2.0d;
		static private final double DEF_PHEROMONE_INTENSITY = 0.8d; //0 to 1

		//These parameters reflect the relative importance that ants attribute to pheromone level vs.
		//distance when choosing a path
		double m_PheromoneWeight;
		double m_LengthWeight;

		//This parameter reflects both the intensity of pheromone applied to each edge, and
		//the rate of evaporation (the greater the first, the faster the second)
		double m_PheromoneIntensity;

		int m_Population;

		private AntColony()
		{
			this(DEF_PHEROMONE_WEIGHT, DEF_LENGTH_WEIGHT, DEF_PHEROMONE_INTENSITY, DEF_ANT_POPULATION);
		}

		private AntColony (int population)
		{
			this(DEF_PHEROMONE_WEIGHT, DEF_LENGTH_WEIGHT, DEF_PHEROMONE_INTENSITY, population);
		}

		private AntColony(double pheromoneWeight, double lengthWeight, double intensity, int population)
		{
			//These two weighting factors are relative, but they must be positive
			m_PheromoneWeight = Math.max(0.0d, pheromoneWeight);
			m_LengthWeight = Math.max(0.0d, lengthWeight);

			//The intensity parameter affects the amount of pheromone left on edges, as well as the
			//Rate at which it evaporates (the more they deposit, the faster the evaporation)
			//MUST be between 0 and 1
			m_PheromoneIntensity = Math.max(0.0d, Math.min(1.0d, intensity));

			//Population must be at least 1
			m_Population = Math.max(1, population);
		}

		//This method causes all of the ants to traverse the map once from start to goal, depositing
		//pheromones along the path once they reach their destination.
		//Returns the length of the shortest path found
		private double traverse(Node<T> start, Node<T> goal)
		{
			Random r = new Random();

			//Create a variable to store the length of the shortest path
			double shortestPath = Double.MAX_VALUE;

			//Loop through all of the ants
			for (int i=0; i<m_Population; i++)
			{

				//Create a variable for the path taken by this ant (initially start node)
				List<Node<T>> path = new ArrayList<Node<T>>();
				path.add(start);

				//Keep track of every node the ant has visited (if they backtrack, this will be
				//different than the path
				List<Node<T>> visited = new ArrayList<Node<T>>();
				visited.add(start);
				
				//Keep track of the current location of the ant (intially start node)
				Node<T> currentNode = start;
				
				//Loop until goal reached or path is empty
				while ( (!currentNode.equals(goal))&&(path.size()>0))
				{
					//Create a list of the probability factors (numerator of the probability equation)
					//for each connected node
					double probFactorTotal = 0.0d;
					List<Double> probFactor = new ArrayList<Double>();
					probFactor.add(0.0d);

					List<Node<T>> connectedNodes = new ArrayList<Node<T>>();

					//Loop through all of the nodes in the graph
					for (Node<T> node : m_PheromoneLevels.getNodes())
					{
						//Build a list of connected nodes that are not already in the path
						if ( (m_PheromoneLevels.isConnected(currentNode, node))&&(!visited.contains(node)))
						{
							//Add it to the path
							connectedNodes.add(node);

							//Calculate the probability factor
							double pheromoneFactor = Math.pow(m_PheromoneLevels.getWeight(currentNode, node), m_PheromoneWeight) ;
							double lengthFactor = Math.pow(1.0d/getWeight(currentNode, node), m_LengthWeight);
							double newProbFactor = pheromoneFactor*lengthFactor; 

							probFactor.add(newProbFactor);

							//Update the total of all of the probability factors for this "move"
							probFactorTotal += newProbFactor;
						}
					} //End loop

					//If a valid node target node exists...
					if (connectedNodes.size()>0)
					{

						//Choose a random number
						double num = r.nextDouble();

						//Keep a cumulative probability sum as you go through the list
						double probSum = 0.0d;

						//Loop through connected nodes, choosing the next node with probability based on the
						//value of that path
						Iterator<Node<T>> nodeIter = connectedNodes.iterator();
						Iterator<Double> probIter = probFactor.iterator();
						Node<T> node = null;

						while ((nodeIter.hasNext())&&(probSum < num))
						{
							node = nodeIter.next();
							double prob = probIter.next() / probFactorTotal;

							probSum += prob;
						} //End loop
					}
					else //Otherwise, "pop" the last node of the list (backtrack); leave it in 
					{
						path.remove(currentNode);
						if (path.size() > 0)
						{
						 currentNode = path.get(path.size()-1);
						}
						else
						{
							currentNode = null;
						}
					}//End if

				} //End Loop

				//Update the "best path"
				//Calculate the path length
				double length = 0.0d;
				for (int n = 1; n<path.size(); n++)
				{
					length += getWeight(path.get(i-1), path.get(i));
				}

				//Check to see if this is better than the best path so far
				if ((Double.valueOf(shortestPath).isNaN())||(length < shortestPath))
				{
					shortestPath = length;
				}
			} //End Loop

			//Return the length of the best path
			return shortestPath;
		}

		//Causes pheromone to evaporate from the edges of the map
		private void evaporate()
		{
			//Loop through every combination of nodes (note that both orders must be checked),
			//Looking for edges
			for (Node<T> n1 : nodes)
			{
				for (Node<T> n2 : nodes)
				{
					//Check if these nodes are connected
					if (m_PheromoneLevels.isConnected(n1,n2))
					{
						//Evaporate
						double weight = m_PheromoneLevels.getWeight(n1, n2);
						m_PheromoneLevels.connect(n1,n2, true, weight*(1.0d - m_PheromoneIntensity));
					}
				}
			}
		}
	}
}
