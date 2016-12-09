package ksk.AI.annealing;

public abstract class AnnealingSystem	 {

	private static final double TEMP_MIN_DEF = 0;
	private static final double TEMP_MAX_DEF = 100;
	private static final double TEMP_STEP_DEF = 0.9;
	private static final int REPEAT_DEF = 1000;
	
	private static final double TOLERANCE = 0.001;
	
	AnnealingSolution m_BestSolution;
	
	TemperatureSchedule m_TempSched;
	int m_RepeatPerTemp;

	protected abstract AnnealingSolution initSolution();
	
	public AnnealingSystem()
	{
		//Default Constructor; use default values
		this(TEMP_MIN_DEF, TEMP_MAX_DEF, TEMP_STEP_DEF, REPEAT_DEF);
	}

	public AnnealingSystem(int repeats)
	{
		this(TEMP_MIN_DEF, TEMP_MAX_DEF, TEMP_STEP_DEF, repeats);
	}
	
	public AnnealingSystem(double min, double max, double step)
	{
		this(min, max, step, REPEAT_DEF);
	}
	
	public AnnealingSystem(double min, double max, double step, int repeats)
	{
		//Create temperature scheduler; exponential by default
		//The "step" parameter is actually a multiplier
		m_TempSched = new TemperatureSchedule(min, max, step);
		
		//Set number of repeats per temperature, must be at least 1
		m_RepeatPerTemp = Math.max(1,repeats);
		
		//No solution found yet
		m_BestSolution = null;
	}

	public final AnnealingSolution getBestSolution()
	{
		//NOTE:  This method is final; the logic of how a search is performed cannot be overridden; the annealing
		//algorithm is domain-independent.  Domain-specific details are overridden in the protected abstract method
		//initSolution() and the domain-specific subclass of AnnealingSolution
		if (m_BestSolution==null)
		{
			//Create a random, domain-specific solution
			m_BestSolution = initSolution();
			
			//Reset the temperature schedule
			m_TempSched.reset();
			
			//Calculate a target stopping temperature; this will be "close" to the minimum temperature
			//specifically, TOLERANCE% of the overall temperature range.  So if the range is 100 degrees, and
			//the tolerance is 0.001 then we'll stop when we get within 0.001 degrees of the final temperature.
			// This should be sufficient, as the closer we get to the min temperature, the less likely the
			// solution is to change.
			double targetTemp = m_TempSched.getMin() + (m_TempSched.getMax() - m_TempSched.getMin())/100*TOLERANCE;
			
			//Now start the perturbation loop
			while (m_TempSched.getTemp() > targetTemp)
			{
				//Repeat a number of times at the same temperature
				for (int i=0; i<m_RepeatPerTemp; i++)
				{
					//Perturb the solution
					AnnealingSolution workingSolution = m_BestSolution.perturb();
					
					//Check for acceptance
					if (accept(workingSolution))
					{
						m_BestSolution = workingSolution;
					}
				}
				
				//Lower the temperature
				m_TempSched.nextTemp();
			}
			
		}
		
		return m_BestSolution;
	}
	
	//Compare a proposed solution to the current best solution and decide whether or not to accept it
	private boolean accept(AnnealingSolution s)
	{	
		//Calculate the energy difference between the two solutions; remember, lower energy is better!
		double deltaEnergy = s.getEnergy() - m_BestSolution.getEnergy();
		
		//If the solution is better, then always accept it
		if (deltaEnergy <= 0)
		{
			return true;  //Definitely accept
		}
		
		
		//Determine the acceptance probability; exponentially decays as energy difference increases and temperature decreases
		double prob = Math.pow(10, -(deltaEnergy/m_TempSched.getTemp()));
		
		//Choose a random number
		if (Math.random() < prob)
		{
			return true;
		}
		else
		{
			return false;
		}
		
	}
	
	//Inner class; temperature schedule
	//Default temperature schedule is exponential decay
	private class TemperatureSchedule
	{
		double m_Min;
		double m_Max;
		double m_Multiplier;
		double m_Temp;
		
		
		public TemperatureSchedule(double min, double max, double step)
		{
			m_Min = Math.min(min, max);
			m_Max = Math.max(min,max);
			m_Multiplier = Math.max(0, Math.min(1.0 - Double.MIN_VALUE , step));  //Step must be less than 1, greater than or equal to 0
			m_Temp = m_Max;
		}
		
		public void reset()
		{
			m_Temp = m_Max;
		}
		
		public double nextTemp()
		{
			m_Temp *= m_Multiplier;
			
			return m_Temp;
		}
		
		public double getTemp()
		{
			return m_Temp;
		}
		
		public double getMin()
		{
			return m_Min;
		}
		
		public double getMax()
		{
			return m_Max;
		}
		
		public double getStep()
		{
			return m_Multiplier;
		}
	}
	
	//Inner class; Linear temperature 
}