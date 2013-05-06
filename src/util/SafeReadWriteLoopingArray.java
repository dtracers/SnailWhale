package util;

import java.util.ArrayList;

/**
 * prevents overwriting also has the ability to limit the distance between the reading and writing
 * @author gigemjt
 *
 * @param <E>
 */
public class SafeReadWriteLoopingArray<E> extends DoubleLoopingArray<E>
{
	/**
	 * if true then the array can write over indexes that has not been read yet
	 */
	private boolean maxDistance;
	private boolean minDistance;

	private int minDistanceAllowed;
	private int maxDistanceAllowed;

	private boolean canRead = false;
	private boolean canWrite = true;

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
		if(maxDistance)
		{
			if(currentDistance()<=maxDistanceAllowed)
			{
				canWrite = true;
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
		if(minDistance)
		{
			if(currentDistance()>minDistanceAllowed)
			{
				canRead = true;
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
		if(!canRead&&!min)
		{
			canRead = true;
		}
	}
	public boolean canWrite()
	{
		if(maxDistance)
		{
			if(currentDistance()<=maxDistanceAllowed)
			{
				canWrite = true;
			}else
			{
				canWrite = false;
			}
		}
		return canWrite;
	}

	public boolean canRead()
	{
		if(minDistance)
		{
			if(currentDistance()>minDistanceAllowed)
			{
				canRead = true;
			}else
			{
				canRead = false;
			}
		}
		return canRead;
	}





	/*
	 *
	 * CONSTRUCTORS
	 *
	 */


	/**
	 * sets the list given that will not overwrite itself it also has a certain size
	 * @param list
	 * @param size
	 */
	public SafeReadWriteLoopingArray(ArrayList<E> list,int size)
	{
		this(list,false,size);
	}

	/**
	 * sets the list given and you can determine whether or not it can overwrite itself
	 * @param list
	 * @param size
	 */
	public SafeReadWriteLoopingArray(ArrayList<E> list,boolean overWrite,int size)
	{
		this(list,overWrite?false:true,overWrite?0:(size-1),overWrite?false:true,0,size);
	}


	/**
	 * sets the list given and you can determine how much of it overwrites itself
	 * @param list	the list object that is set
	 * @param maxDistance  whether a max distance is allowed
	 * @param max the value for that maxDistance
	 * @param minDistance whether a minDistance is allowed
	 * @param min the value for that minDistance
	 * @param size this size of the array
	 */
	public SafeReadWriteLoopingArray(ArrayList<E> list,boolean maxDistance,int max,boolean minDistance,int min,int size)
	{
		super(list,size);
		this.maxDistance = maxDistance;
		maxDistanceAllowed = max;
		this.minDistance = minDistance;
		minDistanceAllowed = min;
	}

	/**
	 * sets the list given that will not overwrite itself it also has a certain size
	 * @param list
	 * @param size
	 */
	public SafeReadWriteLoopingArray(int size)
	{
		this(false,size);
	}

	/**
	 * sets the list given and you can determine whether or not it can overwrite itself
	 * @param list
	 * @param size
	 */
	public SafeReadWriteLoopingArray(boolean overWrite,int size)
	{
		this(overWrite?false:true,overWrite?0:(size-1),overWrite?false:true,0,size);
	}


	/**
	 * creates a new list and you can determine how much of it overwrites itself
	 * @param list	the list object that is set
	 * @param maxDistance  whether a max distance is allowed
	 * @param max the value for that maxDistance
	 * @param minDistance whether a minDistance is allowed
	 * @param min the value for that minDistance
	 * @param size this size of the array
	 */
	public SafeReadWriteLoopingArray(boolean maxDistance,int max,boolean minDistance,int min,int size)
	{
		this(new ArrayList<E>(),maxDistance,max,minDistance,min,size);
	}

	public ArrayList<E> getList()
	{
		return list;
	}

}
