package threading;

public class ThreadTest {

	private static int syncedCounter;
	private static int unsyncedCounter;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//Initialize the counters
		syncedCounter = 0;
		unsyncedCounter = 0;
		
		//Decide how many threads, and how many reps
		int threadCount = 1000;
		int repCount = 1000;
		int expectedResult = threadCount*repCount;
		
		//Start the threads
		System.out.print("Starting threads...");
		ThreadTest instance = new ThreadTest();
		Thread[] myThreads = new Thread[threadCount];
		for (int i=0; i<myThreads.length; i++)
		{
			myThreads[i] = instance.new Incrementer(repCount);
			myThreads[i].start();
		}
		
		//Wait for all of the threads to finish
		for (int i=0; i<myThreads.length; i++)
		{
			try
			{
				myThreads[i].join();
			}
			catch (InterruptedException e)
			{
			}
			
		}
		
		System.out.println("All threads finished!");
		
		System.out.println("Synced Result: " + syncedCounter + " (expected " + expectedResult + ")");
		System.out.println("UnSynced Result: " + unsyncedCounter + " (expected " + expectedResult + ")");

	}

	//Method to increment the synchronized counter
	public synchronized void syncedInc()
	{
		syncedCounter++;
	}
	
	//Method to increment the unsynchronized counter
	public void unsyncedInc()
	{
		unsyncedCounter++;
	}
	
	//Create an inner class that tries to modify each of the counters a set number of times
	public class Incrementer extends Thread
	{
		int mCount;

		public Incrementer(int repeats)
		{
			mCount = repeats;
		}
		
		public void run()
		{
			for (int i=0; i<mCount; i++)
			{
				syncedInc();
				unsyncedInc();
			}
		}
	}

}
