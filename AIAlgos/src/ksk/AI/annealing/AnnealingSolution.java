package ksk.AI.annealing;

public abstract class AnnealingSolution {
	
	protected abstract AnnealingSolution perturb();
	public abstract double getEnergy();
}
