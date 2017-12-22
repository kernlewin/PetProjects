package ksk.ai.util;

public abstract class KEvent {
	Object mSource;
	
	public KEvent(Object src)
	{
		mSource = src;
	}
	
	public Object getSource()
	{
		return mSource;
	}
}
