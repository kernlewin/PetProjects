package ksk.math;

/**
 * Class representing a generic mathematical sequence
 * 
 * @author Kern Lewin
 * @version 0.5
 *
 */
public abstract class Sequence
{
	/**
	 * Return the nth term of the sequence
	 * 
	 * @param n The term number to retrieve
	 * @return The nth term of the sequence
	 */
	public abstract long getTerm(long n);
}
