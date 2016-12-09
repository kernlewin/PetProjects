package ksk.AI.ART;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.Map;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.BitSet;

/*
 * Implementation of the ART1 unsupervised learning algorithm for
 * categorizing (grouping) binary vectors based on "similarity"
 * 
 * Rather than creating a specialized class for these vectors,
 * it uses standard Java BitSets
 */

public class ART1 {

	//Algorithm parameters
	static final double DEF_BETA = 1.0;
	static final double DEF_RO = 0.5;
	static final int MAX_ITERATIONS = 100;

	double m_Beta;
	double m_Ro;
	int m_Bits;

	/*
	 * Originally I was going to use a list of lists to represent the feature vectors
	 * in each cluster, but this makes it difficult to iterate through the vectors when
	 * they can move from cluster to cluster during iteration, and also to ensure that each vector belongs to a unique cluster
	 * Instead we will maintain a Set of feature vectors, a Set of prototypes, and a Map connecting the two.
	 */

	//Set of all feature vectors
	Set<BitSet> m_VecSet;

	//List of all cluster prototypes (using a list rather than a set to maintain consistent order)
	List<BitSet> m_ProtoList;

	//Map that uniquely assigns each feature vector to a cluster:
	//map entries are <vector, prototype> pairs
	Map<BitSet, BitSet> m_ClusterMap;

	//Create an empty ART1 system with default beta and ro
	public ART1(int bits)
	{
		this(bits, DEF_BETA, DEF_RO);
	}

	//Create an empty ART1 system
	public ART1(int bits, double beta, double ro)
	{
		m_Beta = Math.abs(beta);
		m_Ro = Math.abs(ro);
		m_Bits = Math.max(1, bits);	

		//Create empty collections of features and prototypes, and an empty cluster map
		m_VecSet = new HashSet<BitSet>();
		m_ProtoList = new ArrayList<BitSet>();
		m_ClusterMap = new HashMap<BitSet, BitSet>(); 
	}


	//Copy Constructor
	public ART1( ART1 src)
	{
		this(src.m_Bits, src.m_Beta, src.m_Ro);

		//Iterate through the cluster map
		Set<Map.Entry<BitSet, BitSet>> entrySet = src.m_ClusterMap.entrySet();
		for (Map.Entry<BitSet, BitSet> entry : entrySet)
		{
			//Add copies of this feature vector and prototype to the map (note that duplicates are impossible)
			BitSet vec = (BitSet)entry.getKey().clone();
			BitSet prototype = (BitSet)entry.getValue().clone();			
			m_VecSet.add(vec);
			m_ProtoList.add(prototype);
			m_ClusterMap.put(vec,  prototype);
		}
	}

	public int getClusterCount()
	{
		return m_ProtoList.size();
	}

	//Gets a copy of the underlying cluster; the feature vectors in the
	//cluster are also copied; no external class ever gets a reference
	//to the original feature vector, nor a copy of the prototypes
	public Set< BitSet> getCluster(int index)
	{
		//Check for index error
		if ((index<0)||(index>=m_ProtoList.size()))
		{
			throw new java.lang.ArrayIndexOutOfBoundsException("Cluster #" + index + " does not exist.");
		}

		//Get the prototype for the selected cluster
		BitSet prototype = m_ProtoList.get(index);

		Set<BitSet> cluster = new HashSet<BitSet>();

		//Iterate through all of the feature vectors
		for (BitSet vec : m_VecSet)
		{
			//If this feature is in the target cluster, add it to the cluster
			if (m_ClusterMap.get(vec).equals(prototype))
			{
				cluster.add(vec);
			}
		}

		//Done!
		return cluster;
	}


	//Okay here's the heart of ART1
	//Every time a new feature vector is added, the system either adds it to
	//an existing category or creates a new category for it
	public void addVector(BitSet v)
	{
		//Create a copy of the vector, clipped to eliminate any extra bits
		BitSet vec = (BitSet)v.clone();
		if (v.size() > m_Bits)
		{
			vec.clear(m_Bits, v.size());
		}

		//Add this vector to our set of features
		m_VecSet.add (vec);

		//Loop through all of the available prototypes
		for (Iterator<BitSet> iter = m_ProtoList.iterator();  iter.hasNext();)
		{
			//Get the next cluster
			BitSet prototype = iter.next();

			//Calculate the intersecton of the feature vector with the current prototype vector
			BitSet intersection = (BitSet) (prototype.clone());
			intersection.and(vec);

			//Proximity check
			double proxLS = intersection.cardinality()/(m_Beta+prototype.cardinality());
			double proxRS = vec.cardinality() / (m_Beta + m_Bits);

			//Vigilance check
			double vigLS = intersection.cardinality()/vec.cardinality();
			double vigRS = m_Ro;

			//Check if both passed
			if ((proxLS<proxRS)&&(vigLS<vigRS))
			{
				//Update the prototype
				prototype.and(vec);

				//Add this vector to this cluster
				m_ClusterMap.put(vec, prototype);

				//Done!
				return;
			}
		}

		//If we get here, then none of the prototypes matched; start a new cluster
		m_ProtoList.add(vec);
		m_ClusterMap.put(vec, vec);
	}

	//Re-group all of the feature vectors in the system
	//Return true if there were any changes
	public boolean reCluster()
	{
		//Keep track of whether or not anything changed
		boolean changed = false;

		//Iterate through all of the feature vectors
		for (BitSet vec : m_VecSet)
		{
			//Remember the prototype for this vector
			BitSet prototype = m_ClusterMap.get(vec);

			//Remove this feature from the map
			m_VecSet.remove(vec);

			//Add this vector again
			addVector(vec);

			//Check if the prototype changed
			if (!(m_ClusterMap.get(vec).equals(prototype)))
			{
				changed = true;
			}
		}

		//Remove any duplicate prototypes
		Set<BitSet> protoSet = new HashSet<BitSet>();
		protoSet.addAll(m_ProtoList);
		m_ProtoList.clear();
		m_ProtoList.addAll(protoSet);

		//Done!
		return changed;
	}
}
