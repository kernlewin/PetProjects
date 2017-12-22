package ksk.ai.util;

/**
 * Generic interface for classes that are able to respond to a specific type of Event.
 * Note that all Event-specific code is in the Event class, so the listener/producer interfaces
 * can be handled completely generically.
 * 
 * @author Kern Lewin
 * @version 0.5
 *
 * @param <E> The type of Event that this interface is listening for
 */
public interface KEventListener<E extends KEvent> {

	public void onEvent(E event);
}
