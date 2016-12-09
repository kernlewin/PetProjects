package ksk.AI.geneticOLD;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/*
 * Represents a generic organism for use in genetic algorithms
 * Base class contains code for creating random genomes, copying genomes, or creating
 * genomes through crossover (in all cases, with optional mutation)
 * 
 * Subclasses will be responsible for interpreting this genome for their specific problem domain,
 * and assigning a non-negative fitness score.
 * 
 * Note that genomes are never modified once they are created.  Genomes are combined through
 * crossover, and modified through mutation at creation time.
 * 
 */
public abstract class Genome {

	//Parameters for the size (in bits) and number of chromosomes
	protected int m_chromeCount;
	protected int m_chromeSize;

	//Storage for the actual chromosomes
	private ArrayList<Boolean> m_chromosomes;

	//"Cache" of the fitness score; calculating fitness may be expensive for some problems,
	//So we'll provide separate methods for calculating vs. retrieving the score.
	//The base class has a mechanism in place to ensure that the score gets calculated at least
	//once before it is retrieved
	protected double m_fitness;

	/*--------------------
	 * Fitness Methods
	 --------------------*/
	//Calculate the fitness of this genome
	//This is a domain-specific problem, which must be implemented by concrete subclasses
	//Note that this should ONLY be called if something in the environment has changed
	//that potentially affects the fitness score.  Otherwise, "getFitnessScore()" is preferred
	public abstract double calcFitnessScore();
	
	
	public double getFitnessScore()
	{
		//If the fitness score is invalid, try to recalculate it
		if ((Double.isNaN(m_fitness))||(m_fitness<0))
		{
			m_fitness = calcFitnessScore();
		}
		
		//If the fitness score is STILL invalid, throw an exception
		if ((Double.isNaN(m_fitness))||(m_fitness<0))
		{
			throw new RuntimeException("Fitness scores must be valid, non-negative values!  Fitness score: " + m_fitness);
		}
		
		//At this point, we have a valid fitness score
		return m_fitness;
	}
	
	/*--------------------
	 * Constructors 
	 --------------------*/

	//Construct a random genome of specified size.  By default, chromosomes are all 1-bit
	public Genome (int size)
	{
		this(size, 1);
	}

	//Construct a genome with specified chromosome count and size
	public Genome(int count, int size)
	{
		m_chromeCount = Math.max(count,1);  //Must be at least 1
		m_chromeSize = Math.max(size,1);    //Must be at least 1
		m_fitness = Double.NaN;

		//Create a random genome of the specified size
		m_chromosomes = new ArrayList<Boolean>();
		for (int i=0; i<m_chromeCount*m_chromeSize; i++)
		{
			if (Math.random() > 0.5)
			{
				m_chromosomes.add(Boolean.TRUE);
			}
			else
			{
				m_chromosomes.add(Boolean.FALSE);
			}
		}
	}

	//Copy Constructor
	//Copy the source genome, but mutate bits at a rate given by the mutationRate (set to zero for a perfect copy)
	public Genome(Genome source, double mutationRate)
	{
		m_chromeCount = source.getChromosomeCount();
		m_chromeSize = source.getChromosomeSize();
		m_fitness = Double.NaN;

		m_chromosomes = new ArrayList<Boolean>(source.m_chromosomes);

		//Apply mutation
		mutate(mutationRate);
	}

	//Crossover constructor
	//Crossover two genomes, starting with 1 then crossing to 2 at a random point,
	//with a specified rate of mutation
	//If the two Genomes are incompatible, an exception will be thrown
	public Genome(Genome source1, Genome source2, int crossoverPoint, double mutationRate)
	{		
		//First, check for compatibility
		if (!source1.isCompatible(source2))
		{
			throw new RuntimeException("Cannot cross incompatible genomes.");
		}

		//Create any empty genome
		m_chromeCount = source1.getChromosomeCount();
		m_chromeSize = source1.getChromosomeSize();
		m_fitness = Double.NaN;
		m_chromosomes = new ArrayList<Boolean>();

		//Up to the crossover point, add chromosomes from source1
		crossoverPoint = Math.min(crossoverPoint,  m_chromeCount-1);
		for (int i=0; i<=crossoverPoint; i++)
		{
			m_chromosomes.addAll(source1.getChromosome(i));
		}
		
		//After the crossover point, add chromosomes from source2
		for (int i=crossoverPoint+1; i<m_chromeCount; i++)
		{
			m_chromosomes.addAll(source2.getChromosome(i));
		}
		
		//Mutate
		mutate(mutationRate);
	}

	/*--------------------
	 * Get/Set Methods 
	 --------------------*/
	public int getChromosomeCount()
	{
		return m_chromeCount;
	}

	public int getChromosomeSize()
	{
		return m_chromeSize;
	}

	//Return all of the bits in the specified chromosome
	//If the index is out of range, returns null
	public List<Boolean> getChromosome(int index)
	{
		if ((index < 0)||(index>=m_chromeCount))
		{
			return null;
		}

		//Get a copy of the chromosome
		return new ArrayList<Boolean>(m_chromosomes.subList(m_chromeSize*index, (m_chromeSize*(index+1))));
	}

	//Get a specific chromosome, as an integer
	public long getChromosomeValue(int index)
	{
		long result = 0;

		List<Boolean> chromosome = getChromosome(index);

		//Make sure the chromosome number was valid!
		if (chromosome==null)
		{
			return -1;
		}

		//Iterate through the list of bits
		for (Iterator<Boolean> it = chromosome.iterator(); it.hasNext();  )
		{
			//If it's true, add one
			if (it.next().booleanValue())
			{
				result++;
			}

			//Left-shift
			result *= 2;
		}

		//Done!
		return result;

	}

	/*--------------------------------
	 * Other Methods
	 --------------------------------*/

	//Check if two genomes are compatible (same size and count of chromosomes)
	//Only compatible genomes can be crossed to create offspring
	public boolean isCompatible(Genome partner)
	{
		if ((partner.getChromosomeCount()==getChromosomeCount())&&
				(partner.getChromosomeSize()==getChromosomeSize()))
		{
			return true;
		}

		return false;
	}

	//This should only happen at creation; genomes shouldn't change once created!
	//That's why this is a private method, called only from the constructor
	private void mutate(double rate)
	{
		for (ListIterator<Boolean> it = m_chromosomes.listIterator(); it.hasNext(); )
		{
			//Flip this bit if our random number is less than the mutation rate
			//Mutation rate should be a value between 0 and 1
			Boolean bit = it.next();
			if (Math.random() < rate)
			{
				it.set(Boolean.valueOf( !bit.booleanValue() ));
			}
		}
	}
}
