package util;

import java.util.ArrayList;

/**
 * prevents overwriting also has the ability to limit the distance between the reading and writing
 * @author gigemjt
 *
 * @param <E>
 */
public class SafeReadWriteLoopingArray<E> extends ReadWriteLoopingArray<E>
{
	/**
	 * if true then the array can write over indexes that has not been read yet
	 */
	private boolean maxDistance;
	private boolean minDistance;

	private int minDistanceAllowed;
	private int maxDistanceAllowed;

	private boolean canRead;
	private boolean canWrite;
	public SafeReadWriteLoopingArray(ArrayList<E> list)
	{
		this(list,false);
	}
	public SafeReadWriteLoopingArray(ArrayList<E> list,boolean overWrite)
	{
		super(list);
		if(!overWrite)
		{
			maxDistance = true;
			maxDistanceAllowed = size-1;
		}
	}

	public SafeReadWriteLoopingArray(int size,boolean overWrite)
	{
		super(size);
		if(!overWrite)
		{
			maxDistance = true;
			maxDistanceAllowed = size-1;
		}
	}
	public SafeReadWriteLoopingArray(int size)
	{
		this(size,false);
	}

	/**
	 * Every time this method is called the totalReadingIndex is incremented
	 * This is called with the intension of reading
	 * @return
	 */
	@Override
	public E readElement()
	{
		E ele = null;
		if(canRead)
			ele = super.readElement();
		if(minDistance)
		{
			if(currentDistance()<minDistanceAllowed)
			{
				//then this means that it has read to many
				canRead = false;
			}else if(!canRead)
			{
				canRead = true;
			}
		}
		return ele;
	}

	/**
	 * Every time this method is called the totalReadingIndex is incremented
	 * This is called with the intension of writing
	 * @return
	 */
	@Override
	public E writeElement()
	{
		E ele = null;
		if(canWrite)
			ele = super.writeElement();
		if(maxDistance)
		{
			if(currentDistance()>maxDistanceAllowed)
			{
				//then this means that it has read to many
				canWrite = false;
			}else if(!canWrite)
			{
				canWrite = true;
			}
		}
		return ele;
	}

	/**
	 * The current distance is the totalWriteIndex - totalReadIndex
	 * @return
	 */
	public int currentDistance()
	{
		return totalWriteIndex - totalReadIndex;
	}

	public boolean maxDistanceExceeded()
	{
		return canWrite;
	}

	public boolean minDistanceExceeded()
	{
		return canRead;
	}

	/**
	 * If true then that means that a maxDistanceExists
	 * @param max
	 * @param dist
	 */
	public void setMaxDistance(boolean max,int dist)
	{
		this.maxDistance = max;
		this.maxDistanceAllowed = dist;
	}

	/**
	 * If true then that means that a maxDistanceExists
	 * @param max
	 * @param dist
	 */
	public void setMinDistance(boolean min,int dist)
	{
		this.minDistance = min;
		this.minDistanceAllowed = dist;
	}

}
