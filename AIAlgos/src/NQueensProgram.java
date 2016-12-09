
public class NQueensProgram {

	public static void main(String[] args)
	{
	NQueensSystem solver = new NQueensSystem(8);
	
	NQueensSolution solution = (NQueensSolution)solver.getBestSolution();
	
	System.out.println(solution);
	}
}
