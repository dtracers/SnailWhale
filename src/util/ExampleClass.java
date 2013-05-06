package util;

public class ExampleClass
{
	public static void main(String args[])
	{
		//withMinDistance();
		withNoMinDistance();
	}

	public static void withMinDistance()
	{
		SafeReadWriteLoopingArray<int[]> array = new SafeReadWriteLoopingArray<int[]>(5);//creates an array with a max dist of size and a minDist of 0

		//this loop should print out true while k<5
		initializeArray(10,0,array);

		//this will read through the array twice without any numbers changing
		readFromArray(10,array);

		//this will write over itself twice because it has technically already read it (this is why you set a min distance)
		writeToArray(10,10,array);

		//this will read 5 times getting the values (15-19)
		readFromArray(5,array);

		//this will write over itself 2 but not a third time
		writeToArray(15,15,array);

		//prints out the final set of numbers
		readFromArray(5,array);
	}

	public static void withNoMinDistance()
	{
		SafeReadWriteLoopingArray<int[]> array = new SafeReadWriteLoopingArray<int[]>(5);//creates an array with a max dist of size and a minDist of 0
		array.setMinDistance(false, 0);

		//this loop should print out true while k<5
		initializeArray(10,0,array);

		//this will read through the array twice without any numbers changing
		readFromArray(10,array);

		//this will write over itself twice because it has technically already read it (this is why you set a min distance)
		writeToArray(10,10,array);

		//this will read 5 times getting the values (15-19)
		readFromArray(5,array);

		//this will write over itself 2 but not a third time
		writeToArray(15,10,array);

		//prints out the final set of numbers
		readFromArray(5,array);
	}


	/*
	 * HELPER METHODS BELOW THAT SHOW READING AND WRITING FROM THE ARRAY
	 */


	public static void readFromArray(int numTimes,SafeReadWriteLoopingArray<int[]> array)
	{
		System.out.println("Reading From Array");
		for(int k = 0;k<numTimes;k++)
		{
			if(array.canRead())
			{
				System.out.println("k "+k+" : "+array.readElement()[0]+" Distance "+array.currentDistance());
			}else
			{
				System.out.println("can not read anymore");
				break;
			}
		}
		System.out.println("done reading");
	}

	public static void writeToArray(int numTimes,int startingNumber,SafeReadWriteLoopingArray<int[]> array)
	{
		System.out.println("Writing To Array ");
		for(int k = startingNumber;k<startingNumber+numTimes;k++)
		{
			if(array.canWrite())
			{
				array.writeElement()[0] = k;
				System.out.println("k "+k+" Distance "+array.currentDistance());
			}else
			{
				System.out.println("can not write anymore");
				break;
			}
		}
		System.out.println("done writing");
	}

	public static void initializeArray(int numTimes,int startingNumber,SafeReadWriteLoopingArray<int[]> array)
	{
		System.out.println("Adding elements To Array ");
		for(int k = startingNumber;k<startingNumber+numTimes;k++)
		{
			System.out.println("k: "+k+" "+array.initializeElements(new int[]{k}));
		}
		System.out.println("done adding");
	}
}
