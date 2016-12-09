/**
 * Represents a simulated annealing solution to the N queens problem;
 * Finding a state, given an NxN chessboard and N queens, where the goal
 * is to arrange the queens such that none are threatening each other
 * @author P0082082
 *
 */

import ksk.AI.annealing.AnnealingSystem;
import ksk.AI.annealing.AnnealingSolution;

public class NQueensSystem extends AnnealingSystem {

	int m_N;
	
	public NQueensSystem(int N)
	{
		super();
		
		m_N = Math.max(1,N); //Must be at least 1x1
	}
	
	@Override
	protected AnnealingSolution initSolution()
	{
		return new NQueensSolution(m_N);
	}
	
}
