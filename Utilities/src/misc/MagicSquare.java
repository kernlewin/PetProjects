package misc;

import java.util.Random;

public class MagicSquare {
	//The magic square
	int[][] mGrid;

	//Create an N x N Magic Square
	//A Magic Square must contain all of the values from 1 to N once each, and the total
	//of each row, column and diagonal must be the same

	//N must be > 2
	public MagicSquare (int N)
	{
		if (N<=2)
		{
			throw new RuntimeException("Magic Squares cannot be created for N <= 2  (" + N + ")");
		}

		//Create a Magic Square using one of the two standard algorithms
		if (N%2 == 0)
		{
			//Even
			mGrid = GenerateMagicSquareSinglyEven(N);
		}
		else
		{
			mGrid = GenerateMagicSquareOdd(N);
		}
	}


	public int getValue(int row, int column)
	{
		return mGrid[row][column];
	}

	public String toString()
	{
		String result = "";
		for (int r = 0; r<mGrid.length; r++)
		{
			for (int c = 0; c < mGrid[r].length; c++)
			{
				result += mGrid[r][c] + " ";
			}

			result += "\n";
		}

		return result;
	}

	public int getSize()
	{
		return mGrid.length;
	}

	private int[][] GenerateMagicSquareEven(int N)
	{
		return null;
	}

	//Private method to fill a magic square.
	//Uses the Siamese algorithm with a random rotation
	//We will assume that N is odd, and greater than 2
	private int [][] GenerateMagicSquareOdd(int N)
	{
		int[][] grid = new int[N][N];

		//Fill the grid with zeroes
		for (int r=0; r<N; r++)
		{
			for (int c=0; c<N; c++)
			{
				grid[r][c] = 0;
			}
		}

		int row = 0;
		int column = N/2;

		for (int i= 1; i <= N*N; i++)
		{
			grid[row][column] = i;

			//Update the coordinates; move up and right
			int newRow = (row + N - 1) % N;
			int newColumn = (column + N + 1) % N;

			//If that location is full, move down instead
			if (grid[newRow][newColumn] != 0)
			{
				row = (row + N + 1) % N;
			}
			else
			{
				row = newRow;
				column = newColumn;
			}
		}

		//Apply random rotation
		grid = rotate (grid, new Random().nextInt(4));

		return grid;
	}

	public void rotate()
	{
		rotate(1);
	}

	public void rotate(int count)
	{
		mGrid = rotate(mGrid, count);
	}

	//Rotate the magic square right by the specified number of quarter-turns
	private int[][] rotate(int[][] grid, int count)
	{
		int N = grid.length;

		//Every 4 rotations brings us back to the starting position 
		for (int i=0; i<count%4; i++)
		{
			//Create a new, empty grid
			int[][] newGrid = new int[N][N];

			//Row i becomes column i-1
			for (int r = 0; r<N; r++)
			{
				for (int c = 0; c<N; c++)
				{
					newGrid[c][N-1-r] = grid[r][c];
				}
			}

			grid = newGrid;
		}

		return grid;
	}

	//Use the "LUX" algorithm to generate singly even magic squares.  Requires that
	//N is a multiple of 2, but NOT a multiple of 4
	private int [][] GenerateMagicSquareSinglyEven(int N)
	{
		//Make sure N is a valid number
		if ((N<6)||(N%2==1)||(N%4==0))
		{
			return null;
		}

		//Find M such that N = 4M + 2
		int M = (N - 2)/4;

		//Create the LUX grid; size is 2M+1, and it contains M+1 rows of L, 1 row of U, and M-1 rows of X,
		//With the middle U swapped up
		char[][] lux = new char[2*M+1][2*M+1];

		for (int r=0; r<lux.length; r++)
		{
			for (int c=0; c<lux[r].length; c++)
			{
				if (r<M+1)
				{
					lux[r][c] = 'L';
				}
				else if (r==M+1)
				{
					lux[r][c] = 'U';
				}
				else
				{
					lux[r][c] = 'X';
				}
			}
		}

		//Swap the middle U up
		lux[M+1][M] = 'L';
		lux[M][M] = 'U';

		//Create a grid for the magic square
		int[][] result = new int[N][N];

		//Start a Siamese walk through the lux grid
		int r = 0;
		int c = M;
		int nextNumber = 1;

		//Note that for any (r,c) in the LUX array, the four values around it in the
		//result array are (2r,2c), (2r,2c+1), (2r+1,2c) and (2r+1, 2c+1)
		for (int i=0; i<lux.length*lux.length; i++)
		{
			//Check which character we're at 
			if (lux[r][c] == 'L')
			{
				//Fill the numbers here in an L-pattern
				result[2*r][2*c+1] = nextNumber++;
				result[2*r+1][2*c] = nextNumber++;
				result[2*r+1][2*c+1] = nextNumber++;
				result[2*r][2*c] = nextNumber++;
			}
			else if (lux[r][c] == 'L')
			{
				//Fill the numbers here in a U-pattern
				result[2*r][2*c] = nextNumber++;
				result[2*r+1][2*c] = nextNumber++;
				result[2*r+1][2*c+1] = nextNumber++;
				result[2*r][2*c+1] = nextNumber++;				
			}
			else
			{
				//Fill the numbers here in an X-pattern
				result[2*r][2*c] = nextNumber++;
				result[2*r+1][2*c+1] = nextNumber++;
				result[2*r+1][2*c] = nextNumber++;
				result[2*r][2*c+1] = nextNumber++;				
			}

			//Mark this box as done
			lux[r][c] = '*';
			
			//Siamese walk to the next square
			int nextRow = (r + lux.length - 1)%lux.length;
			int nextColumn = (c + lux.length + 1)%lux.length;
			
			if (lux[nextRow][nextColumn] == '*')
			{
				nextRow = (r + lux.length + 1)%lux.length;
				nextColumn = c;
			}
			r = nextRow;
			c = nextColumn;
		}

	return result;	
	}


	public static boolean isMagic(int[][] grid) {
		//Grid must exist
		if (grid == null)
		{
			return false;
		}
		
		//Grid must exist
		

		//Grid size must be at least 2
		
		//Grid must be square, containing every number exactly once, and all row/column/diagonal totals
		//must be equal
		return null;
	}
}