package ksk.util;

import java.util.ArrayList;
import java.util.List;

//Class used to generate random permutations of objects.

//Created using a raw array of objects in any order
//Each call to "next" creates a new ordering of the objects (first call returns initial order
//When the last unique permutation has been returned, the last permutation will be returned
//until "reset" is called.

public class Permutation<T> {
	//Enum representing the possible directions in which a value can move
	private enum Direction {LEFT, RIGHT};

	//Store a list of the objects.  We will never modify this list (or the underlying objects)
	private List<T> mObjects;

	//Create a list of Elements that we will actually permute
	//Each element has an index and a direction associated with it
	private ElementList mElements;

	//Constructors:  Store a list of objects, initialize their order and direction
	public Permutation(List<T> objects)
	{
		this((T[])objects.toArray());
	}
	
	public Permutation(T[] objects)
	{
		//Copy the list of objects
		mObjects = new ArrayList<T>();

		for (int i=0; i<mObjects.size(); i++)
		{
			mObjects.add(objects[i]);
		}

		//Create a list of integers representing the indices of the objects
		//We will permute this list using Johnson-Trotter, rather than the objects
		mElements = new ElementList(mObjects.size());
	}

	//Get the current ordering of the objects
	public List<T> getOrder()
	{
		int[] order = mElements.getOrder();

		List<T> result = new ArrayList<T>(order.length);

		for (int i=0; i<order.length; i++)
		{
			result.add(mObjects.get( order[i] ));
		}
		
		return result;
	}
	
	//Permute the objects, if possible.  Return true if the order changed
	public boolean next()
	{
		return mElements.next();
	}

	//Check if we've reached the last permutation
	public boolean hasNext()
	{
		return mElements.hasNext();
	}
	
	//Inner class to represent the elements (index and direction) that we will manipulate
	//using the Johnson-Trotter algorithm
	private class ElementList
	{		
		//An element List contains an array of indices, and of directions
		int[] mIndices;
		Direction[] mDirection;

		//Create an ElementList; just need to specify how many Elements are in the list
		public ElementList(int size)
		{
			//Create the lists
			if (size <=0)
			{
				throw new RuntimeException("Element List size must be a positive integer");
			}
			else
			{
				mIndices = new int[size];
				mDirection = new Direction[size];

				//Initialize the lists
				for (int i=0; i<mIndices.length; i++)
				{
					mIndices[i] = i;
					mDirection[i] = Direction.LEFT;
				}
			}
		}


		//Getter
		public int[] getOrder()
		{
			//Copy the mIndices array
			int[] value = new int[mIndices.length];
			for (int i=0; i<value.length; i++)
			{
				value[i] = mIndices[i];
			}

			return value;
		}

		//Change to the next ordering
		//return false if the order didn't change
		public boolean next()
		{
			//Find the largest mobile element
			int largestMobile = getLargestMobileIndex();

			//If none, then done
			if (largestMobile<0)
			{
				return false;
			}

			//Move the largest mobile element
			move(largestMobile);

			//Change the direction of every element larger than the largest mobile element
			for (int i=0; i<mIndices.length; i++)
			{
				if (mIndices[i] > mIndices[largestMobile])
				{
					flip(i);
				}
			}

			//Done!
			return true;
		}

		//Check if this is the final ordering
		public boolean hasNext()
		{
			return (getLargestMobileIndex() >= 0);
		}

		//Check for mobility
		//An element is mobile if it is greater than the element it is pointing to
		private boolean isMobile(int index)
		{
			//Get the index of the element that this element is pointing to
			int target;

			if (mDirection[index] == Direction.LEFT)
			{
				target = index - 1;
			}
			else
			{
				target = index + 1;
			}

			//Check for an element on the boundary, pointing "out"
			if ((target<0)||(target >= mIndices.length))
			{
				return false;
			}
			else //Check if the target is smaller.  Note that equality is impossible
			{
				if (mIndices[target] < mIndices[index])
				{
					return true;
				}
				else
				{
					return false;
				}
			}
		}

		//Get the index of the largest mobile element; if none, returns a negative value
		private int getLargestMobileIndex()
		{
			int max = mIndices[0];
			int maxIndex = 0;

			for (int i=0; i<mIndices.length; i++)
			{
				if ((mIndices[i] > max)&&(isMobile(i)))
				{
					max = mIndices[i];
					maxIndex = i;
				}
			}

			return maxIndex;
		}

		//Swap two elements
		private void move(int index)
		{
			//Make sure this element is actually mobile
			if (!isMobile(index))
			{
				return;
			}

			//Figure out the indices of the elements to swap
			int n1 = index;
			int n2 = (mDirection[index]==Direction.LEFT)?(index-1):(index+1);

			//Swap index and direction
			int tempIndex;
			Direction tempDirection;

			tempIndex = mIndices[n1];
			tempDirection = mDirection[n1];

			mIndices[n1] = mIndices[n2];
			mDirection[n1] = mDirection[n2];

			mIndices[n2] = tempIndex;
			mDirection[n2] = tempDirection;
		}

		//Toggle the direction of an element
		private void flip(int index)
		{
			mDirection[index] = (mDirection[index]==Direction.LEFT)?(Direction.RIGHT):(Direction.LEFT);
		}
	}
}
