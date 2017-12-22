package ksk.ai.util;

/**
 * Interface for classes that produce a particular type of event.  The only purpose of separating
 * this from EventBroadcaster, is to avoid having code in the interface.
 * 
 * A class that generates events can extend the EventBroadcaster class, or implement this interface
 * and USE an EventBroadcaster, or both.
 * 
 * @author Kern Lewin
 * @version 0.5
 *
 * @param <E>
 */
public interface KEventProducer<E extends KEvent> {

	//Just one method
	public KEventBroadcaster<E> getEventBroadcaster();
}
