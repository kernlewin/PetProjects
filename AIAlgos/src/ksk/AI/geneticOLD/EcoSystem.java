package ksk.AI.geneticOLD;

import java.util.ArrayList;
import java.util.Iterator;

/*
 * Represents a population of organisms being used to solve a problem in a particular domain
 * 
 */
public class EcoSystem {

	//Population parameters
	int m_popSize; 
	long m_generation;
	ArrayList<Genome> m_population;
	
	//Need to store a GenomeFactory to be able to create new Genomes that are of a
	//domain-specific class, not just of type Genome
	GenomeFactory m_factory;

	//Fitness Scores
	double m_bestFitness;
	double m_totalFitness;
	Genome m_bestOrganism;

	//Mutation/Crossover parameters
	protected double m_crossRate;
	protected double m_muteRate;

	//Default values
	public static final double DEF_CROSSOVER = 0.7;
	public static final double DEF_MUTATION = 0.01;

	/*--------------------
	 * Constructors 
		 --------------------*/
	//Construct a system given population size and a "seed" genome (populate it with compatible genomes)
	public EcoSystem (int size, GenomeFactory factory)
	{
		this(size, factory, DEF_CROSSOVER, DEF_MUTATION);
	}


	//Construct a system given population, seed, crossover and mutation rates
	public EcoSystem (int size, GenomeFactory factory, double crossRate, double muteRate)
	{
		m_crossRate = crossRate;
		m_muteRate = muteRate;
		m_factory = factory;
		
		m_popSize = Math.max(0, size);
		m_generation = 0;
		
		//Initialize fitness parameters.  Note that fitness scores can't be negative
		m_bestFitness = -1;
		m_bestOrganism = null;
		m_totalFitness = -1;

		//Add random organisms
		m_population = new ArrayList<Genome>();
		for (int i=0; i<m_popSize; i++)
		{
			m_population.add(m_factory.newGenome());
		}
		
		//Update fitness scores for the first generation
		updateFitness();
	}

	/*--------------------
	 * Evolution Methods 
		 --------------------*/
	//Advance the EcoSystem one generation; replace the current genomes with the same number
	//of child genomes, through crossover and mutation of the fittest members
	public void nextGeneration()
	{
		//Create a variable to store the new generation
		ArrayList<Genome> newPopulation = new ArrayList<Genome>();
		
		//Update all fitness scores (will also update the total, and find the fittest organism)
		updateFitness();
		
		//Repeat until the new generation is as big as the old population
		while (newPopulation.size() < m_population.size())
		{
		//Choose two genomes via RouletteWheel selection
		Genome parent1 = rouletteSelection();
		Genome parent2 = rouletteSelection();
		
		//Check if we should cross these over
		if (Math.random() < m_crossRate)
		{
			//Choose a crossover point
			int crossPoint = (int)Math.round(Math.random()*parent1.getChromosomeCount());
			
			//Crossover, mutate, and add the resulting offspring
			newPopulation.add(m_factory.newGenome(parent1, parent2, crossPoint, m_muteRate));
			newPopulation.add(m_factory.newGenome(parent2, parent1, crossPoint, m_muteRate));
		}
		//Otherwise, just copy these genomes into the new generation (with mutation)
		else
		{
			newPopulation.add(m_factory.newGenome(parent1, m_muteRate));
			newPopulation.add(m_factory.newGenome(parent2, m_muteRate));
		}
		
		}//End loop
		
		//Advance generation
		m_population = newPopulation;
		m_generation++;
		
		//Update fitness scores again
		updateFitness();
	}

	//Update the total fitness score, and find the fittest genome
	private void updateFitness()
	{
		//Fitness scores can't be negative, so we'll start off with
		m_totalFitness = 0;
		
		for (Iterator<Genome> it = m_population.iterator(); it.hasNext();)
		{
			Genome next = it.next();
			double fitness = next.getFitnessScore();
			
			//Update the total
			m_totalFitness += fitness;
			
			//Check if this is the fittest organism
			if (fitness > m_bestFitness)
			{
				m_bestFitness = fitness;
				m_bestOrganism = next;
			}
		}
	}

	//Roulette Wheel method; used to choose good candidates for the next generation
	//Note that this will not work as expected if you don't call updateFitness() first.
	//We don't want to call it here, for performance reasons
	private Genome rouletteSelection()
	{
		//Choose a random fitness score to stop at
		double targetFitness = Math.random()*m_totalFitness;
	
		//System.out.println("Total = " + m_totalFitness);
		//System.out.println("Target = " + targetFitness);
		
		//Loop through the population until you reach this score
		double currFitness = 0.0;
		Iterator<Genome> it = m_population.iterator();
		
		while ((currFitness <= targetFitness)&&(it.hasNext()))
		{
			//Add the next Genome's fitness to our running total
			Genome candidate = it.next();
			currFitness += candidate.getFitnessScore();
			
			//If we've passed the target fitness level, then select this Genome
			if (currFitness > targetFitness)
			{
				return candidate;
			}
		}
		
		//If we get here, we've run out of candidates (which should only happen if the
		//population is empty)
		return m_population.get(0);
	}
	
	/*--------------------
	 * Get/Set Methods 
     --------------------*/

	//Get the fittest member of the EcoSystem
	public Genome getFittestGenome()
	{
		//Make sure it's not null
		if (m_bestOrganism == null)
		{
			return null;
		}
		
		//Return a copy of the best organism.  Use our factory to make a copy
		//of the appropriate type, with no mutation
		return m_factory.newGenome(m_bestOrganism, 0);
	}
	
	//Get the population of the ecosystem
	public int getPopulationSize()
	{
		return m_population.size();
	}
	
	//Get the generation number
	public long getGeneration()
	{
		return m_generation;
	}
	
	//Get the total fitness of the ecosystem
	public double getTotalFitness()
	{
		return m_totalFitness;
	}
}
