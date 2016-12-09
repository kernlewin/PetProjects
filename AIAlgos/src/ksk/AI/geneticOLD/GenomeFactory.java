package ksk.AI.geneticOLD;


/*
 * This interface is typically used by problem solution classes and the EcoSystem.
 * The EcoSystem uses this to create Genomes that are compatible with a specific problem.
 * 
 * The intention is that classes searching for solutions to problems will both extend the Genome class and implement the
 * GenomeFactory to get Genomes of the appropriate type
 */

public interface GenomeFactory {
	public Genome newGenome();  //Random Genome factory method
	public Genome newGenome(Genome source, double mutationRate); //Copy Genome Factory method
	public Genome newGenome(Genome source1, Genome source2, int crossPoint, double mutationRate); //Crossover Genome Factory method
}
