package ksk.ai.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Generic class for generating a specific type of Event.
 * Any class that needs to fire events can either extend this class, or just use
 * an object of this class and implement the EventProducer interface.
 * 
 * @author Kern Lewin
 * @version 0.5
 *
 * @param <E> The type of Event that this interface generates
 */
public class KEventBroadcaster<E extends KEvent> {

	List<KEventListener<E>> mListeners;

	public KEventBroadcaster()
	{
		mListeners = new ArrayList<KEventListener<E>>();
	}


	public void addListener(KEventListener<E> listener)
	{
		if (listener!=null)
		{
			mListeners.add(listener);
		}
	}

	public void removeListener(KEventListener<E> listener)
	{
		mListeners.remove(listener);
	}

	public void broadcast(E event)
	{
		for (KEventListener<E> listener : mListeners)
		{
			listener.onEvent(event);
		}
	}
}
