package ksk.AI.geneticSolutions;

import ksk.AI.geneticOLD.Genome;
import ksk.AI.geneticOLD.GenomeFactory;


/*
 * As a test of my genetic algorithm framework, this class attempts to use a genetic algorithm
 * to solve a simple problem with a known solution; Square Roots
 * 
 * The class will use two 64-bit chromosomes, x and y, representing a "solution" of (x/y).
 * This allows us to search for rational approximations to roots.
 */

public class SqrtDNA extends Genome implements GenomeFactory {

	protected double m_square;  //Number we're trying to root

	//Public Constructor:  Create an organism with two 64-bit chromosomes, designed to solve the
	//square root of a specific value
	public SqrtDNA(double square)
	{
		super(2, 64);

		m_square = square;
	}

	//Private Copy Constructor (used by factory methods)
	private SqrtDNA(SqrtDNA source, double mutationRate)
	{
		super(source, mutationRate);

		m_square = source.getSquare();
	}

	//Private Crossover Constructor (used by factory methods)
	private SqrtDNA(SqrtDNA source1, SqrtDNA source2, int crossPoint, double mutationRate)
	{
		super(source1, source2, crossPoint, mutationRate);

		m_square = source1.getSquare();
	}

	@Override
	public Genome newGenome() {
		// TODO Auto-generated method stub

		return new SqrtDNA(m_square);
	}

	@Override
	public Genome newGenome(Genome source, double mutationRate) {
		return new SqrtDNA((SqrtDNA)source, mutationRate);
	}

	@Override
	public Genome newGenome(Genome source1, Genome source2, int crossPoint,
			double mutationRate) {
		return new SqrtDNA((SqrtDNA)source1, (SqrtDNA)source2, crossPoint, mutationRate);
	}

	@Override
	public double calcFitnessScore() {
		//First, figure out what the solution represented by this genome is
		double root = getRoot();

		//The fitness score is the reciprocal of the difference between root*root and the actual target
		//We'll add a 1 to the denominator, otherwise an exact solution would have infinite fitness.
		//This gives fitness scores in the range 0 -> 1
		double diff = Math.abs(root*root - m_square);
		return 1.0 / (diff + 1);
	}

	/*-------------------------
	 * Domain-specific methods
	 --------------------------*/
	public double getSquare()
	{
		return m_square;
	}

	public double getRoot()
	{
		double numerator = getChromosomeValue(0);
		double denominator = getChromosomeValue(1);

		if (denominator==0)
		{
			return Double.MAX_VALUE;
		}

		//Only use positive roots
		return Math.abs(numerator/denominator);
	}

	public double errorPercent()
	{
		double root = getRoot();
		double squareError = root*root - m_square;

		return Math.abs(squareError / m_square);
	}

	/*
	//Temporary; for debugging

	public long getNumerator()
	{
		return getChromosomeValue(0);
	}

	public long getDenominator()
	{
		return getChromosomeValue(1);
	}
	 */

	//Static method for easily computing a square root
	//Creates an appropriate ecosystem and runs it until a "solution" is reached
	public static double sqrt(double value)
	{
		//Create an ecosystem of 100 organisms, default crossover and mutation rates
		ksk.AI.geneticOLD.EcoSystem eco = new ksk.AI.geneticOLD.EcoSystem(100, new ksk.AI.geneticSolutions.SqrtDNA(value));

		//Now run until the the error percentage is sufficiently low
		ksk.AI.geneticSolutions.SqrtDNA solution;
		do 
		{
			//Advance 10 generations
			for (int i=0; i<10; i++)
			{
				eco.nextGeneration();
			}
			
			//Get the fittest genome
			solution = (ksk.AI.geneticSolutions.SqrtDNA)eco.getFittestGenome();
		}
		while (solution.errorPercent() > 0.00001);

		//Return the result
		return solution.getRoot();
	}
}
