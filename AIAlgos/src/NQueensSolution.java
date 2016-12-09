
import ksk.AI.annealing.AnnealingSolution;
import java.util.Random;

public class NQueensSolution extends AnnealingSolution {

	//Given that the number of queens, files and ranks are all the same, we can store the
	//board as a linear array of integers.  The nth entry in the array will be the RANK of the queen in the
	//nth file (there must be exactly one queen per file

	int m_Rank[];

	//Construct a random "solution"; not guaranteed to be conflict-free, but at least
	//there will only be one queen on each rank/file
	public NQueensSolution(int N)
	{
		m_Rank = new int[Math.max(1,N)];

		for (int i=0; i<m_Rank.length; i++)
		{
			m_Rank[i] = i;
		}

		//Randomize
		for (int i=0; i<m_Rank.length; i++)
		{
			perturb();
		}
	}

	public NQueensSolution( NQueensSolution src )
	{
		m_Rank = new int[src.m_Rank.length];

		for (int i=0; i <m_Rank.length; i++ )
		{
			m_Rank[i] = src.m_Rank[i];
		}
	}

	@Override
	protected AnnealingSolution perturb()
	{
		NQueensSolution copy = new NQueensSolution(this);
		
		
		//Choose two random files
		Random r = new Random();
		int n = m_Rank.length;

		int file1 = r.nextInt(n);
		int file2 = r.nextInt(n);

		while (file1==file2)
		{
			file2=r.nextInt(n);
		}

		//Swap
		copy.m_Rank[file1] = m_Rank[file2];
		copy.m_Rank[file2] = m_Rank[file1];

		return copy;
	}

	@Override
	public double getEnergy()
	{
		//Energy of solution is equal to the number of placement conflicts found.
		//A perfect solution has an energy of zero.

		//Note that we only need to check for diagonal conflicts
		int energy = 0;
		for (int p1 = 0; p1 < m_Rank.length; p1++)
		{
			for (int p2 = p1+1; p2<m_Rank.length; p2++)
			{
				int filediff = p2-p1;
				int rankdiff = Math.abs(m_Rank[p2] - m_Rank[p1]);

				//If these differences are the same, then we have a conflict
				if (filediff==rankdiff)
				{
					energy++;
				}
			}
		}

		return energy;
	}


	@Override
	public String toString()
	{
		String result = "";
		for (int rank=0; rank<m_Rank.length; rank++)
		{
			for (int file = 0; file<m_Rank.length; file++)
			{
				if (m_Rank[file] == rank)
				{
					result += "Q";
				}
				else
				{
					result += "*";
				}
			}
			result += "\n";
		}
		
		result += "Conflicts: " + getEnergy();

		return result;
	}
}
