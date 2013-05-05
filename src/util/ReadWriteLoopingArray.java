package util;

import java.util.ArrayList;

/**
 * A basic readWrite looping array with no safety measures
 * @author gigemjt
 *
 * @param <E>
 */
public class ReadWriteLoopingArray<E>
{

	ArrayList<E> list;

	/**
	 * This will always just count up
	 * it might freak out with overflow so something to be aware of
	 */
	int totalReadIndex = 0;
	/**
	 * Is only in the size of the Array
	 */
	int loopingReadIndex = 0;

	/**
	 * This will always just count up
	 * it might freak out with overflow so something to be aware of
	 */
	int totalWriteIndex = 0;
	/**
	 * Is only in the size of the Array
	 */
	int loopingWriteIndex = 0;

	int size = 0;
	public ReadWriteLoopingArray(int size)
	{
		this.size = size;
		list = new ArrayList<E>();
	}

	public ReadWriteLoopingArray(ArrayList<E> list)
	{
		this.size = list.size();
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

}
