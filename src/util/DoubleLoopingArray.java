package util;

import java.util.ArrayList;

/**
 * A basic readWrite looping array with no safety measures
 * @author gigemjt
 *
 * @param <E>
 */
public class DoubleLoopingArray<E>
{

	ArrayList<E> list;

	/**
	 * This will always just count up
	 * it might freak out with overflow so something to be aware of
	 */
	protected int totalReadIndex = 0;
	/**
	 * Is only in the size of the Array
	 */
	protected int loopingReadIndex = 0;

	/**
	 * This will always just count up
	 * it might freak out with overflow so something to be aware of
	 */
	protected int totalWriteIndex = 0;
	/**
	 * Is only in the size of the Array
	 */
	protected int loopingWriteIndex = 0;

	int size = 0;
	public DoubleLoopingArray(int size)
	{
		this(new ArrayList<E>(),size);
	}

	public DoubleLoopingArray(ArrayList<E> list, int size)
	{
		this.size = size;
		this.list = list;
	}

	/**
	 * This will add elements to the list until the list reaches the correct size
	 */
	public boolean initializeElements(E element)
	{
		if(list.size()<size)
		{
			list.add(element);
			return true;
		}
		return false;
	}

	/**
	 * Every time this method is called the totalReadingIndex is incremented
	 * This is called with the intension of reading
	 * @return
	 */
	public E readElement()
	{
		E ele = list.get(loopingReadIndex);
		totalReadIndex++;
		loopingReadIndex = totalReadIndex%size;
		return ele;
	}

	/**
	 * Every time this method is called the totalReadingIndex is incremented
	 * This is called with the intension of writing
	 * @return
	 */
	public E writeElement()
	{
		E ele = list.get(loopingWriteIndex);
		totalWriteIndex++;
		loopingWriteIndex = totalWriteIndex%size;
		return ele;
	}

	public int getCurrentReadingIndex()
	{
		return loopingReadIndex;
	}

	public int getCurrentWritingIndex()
	{
		return loopingWriteIndex;
	}

	public int getTotalReadingIndex()
	{
		return totalReadIndex;
	}

	public int getTotalWritingIndex()
	{
		return totalWriteIndex;
	}


	public void reset()
	{
		totalWriteIndex = 0;
		totalReadIndex = 0;
		loopingWriteIndex = 0;
		loopingReadIndex = 0;
	}
}
