package util;

import java.util.ArrayList;

/**
 * It just loops through items
 * Great for managing memory
 * @author gigemjt
 *
 */
public class LoopingArray<E>
{

	ArrayList<E> list;

	/**
	 * This will always just count up
	 * it might freak out with overflow so something to be aware of
	 */
	int totalIndex = 0;
	/**
	 * Is only in the size of the Array
	 */
	int loopingIndex = 0;

	int size = 0;

	public LoopingArray(int size)
	{
		this.size = size;
		list = new ArrayList<E>();
	}

	public LoopingArray(ArrayList<E> list)
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
	 * Every time this method is called the totalIndex is incremented
	 * @return
	 */
	public E getElement()
	{
		E ele = list.get(loopingIndex);
		totalIndex++;
		loopingIndex = totalIndex%size;
		return ele;
	}

	public int getCurrentIndex()
	{
		return loopingIndex;
	}

	public int getTotalIndex()
	{
		return totalIndex;
	}
}
