package math;


import java.util.ArrayList;


public class BeatDetector
{
	//this is the instantaneous VEdata
	public ArrayList<float[]> VEdata = new ArrayList<float[]>();
	//this is the average VEdata (over 43 Energy histories
	//public ArrayList<Double> AveragedEnergydata = new ArrayList<Double>();
	double[] AveragedEnergydata;

	private double[] EnergyHistory = new double[StaticNumbers.HistoryLength];
	int currentHistoryIndex = 0;


	ArrayList<SignificantItem> importantItems = new ArrayList<SignificantItem>();

	double maxEnergy = 0;

	int slowDown = 0;


	int shiftAvg = StaticNumbers.HistoryLength/2;//this is used so that some of the future values are computed in the average of the current value

	//values used for the actual beat detection
	private boolean aboveAverage = false;
	private long highestIndex = 0;//the index of the highest point when it is above the beat
	private double timeIndex = 0;
	private int highestShiftIndex = 0;//the index of the highest point when it is above the beat (shifted for the average value
	private float highestPoint;
	//the senstitivity of the beat detector:  smaller numbers remove more beats and is more strict
	double senstitivity = 0.8;

	private short[] longArray = new short[StaticNumbers.LargeFrameSize];

	//location for VEdata
	/**
	 * this number always counts up every time calculateVE is called
	 */
	private int counterIndex = 0;
	/**
	 * currentIndex = counterIndex%historyLength*2;
	 */
	private int currentIndex = 0;
	/**
	 * shiftIndex = (counterIndex-avgShift)%historyLength*2;
	 */
	private int shiftIndex = 0;


	public BeatDetector()
	{
		for(int k = 0;k<=StaticNumbers.HistoryArraySize;k++)
		{
			VEdata.add(new float[2]);
		}
		AveragedEnergydata = new double[StaticNumbers.HistoryArraySize];
	}

	/**
	 * Takes data from the array at the given index
	 * It is also given the index in time it is
	 * @param timeData
	 * @param startIndex
	 * @param timeIndex
	 */
	public void combineArray(ArrayList<short[]> timeData,int startIndex,double timeIndex)
	{
		for(int k = 0 ;k<4;k++)
		{
			short[] tempArray = timeData.get((k+startIndex)%StaticNumbers.arraySampleLength);
			int skipIndex = k*StaticNumbers.frameSize;
			for(int q = 0;q<StaticNumbers.frameSize;q++)
			{
				longArray[q+skipIndex] = tempArray[q];
			}
		}
		calculateVE(longArray,timeIndex);
	}

	public void calculateVE(short[] timeData,double timeReference)
   	{
   		//the size of bits that the array is taken over
   		int averageSize = StaticNumbers.LargeFrameSize;
   		//number of values

   		float[] result = VEdata.get(currentIndex);
		float volume = 0;
		float energy = 0;
		for(int q = 0; q<averageSize;q++)
		{
			float data = timeData[q];
			volume+=data;
			energy+=data*data;
		}
		maxEnergy = Math.max(maxEnergy, energy);
		result[0] = volume;
		result[1] = energy;

		EnergyHistory[currentHistoryIndex] = energy;

		double value = 0;
		for(int q=0;q<StaticNumbers.HistoryLength;q++)
		{
			value+=EnergyHistory[q];
		}
		value/=StaticNumbers.HistoryLength;
		AveragedEnergydata[currentIndex] = value;

		//moves the index for the history which is a looping value
		currentHistoryIndex++;
		currentHistoryIndex%=StaticNumbers.HistoryLength;


		if(slowDown == 17)
		{
			slowDown = 0;
		//	RunningGame.levelAni.addKeyFrame(new float[]{(float)value},StaticNumbers.inputTimeReference);
		}
		slowDown++;

		if(counterIndex>=shiftAvg)
			beatDetectionAlgorithm(timeReference);
		//moves the index for the index in VEdata
		counterIndex++;
		currentIndex=counterIndex%StaticNumbers.HistoryArraySize;

   	}


	/**
	 * Goes through each point once and sees if it is large enough away from the average to be considered a beat
	 * Need to make this static and go through all beats to determine Major beats
	 * @param timeReference - this is the reference for finding
	 */
	public void beatDetectionAlgorithm(double timeReference)
	{
		shiftIndex = (counterIndex-shiftAvg)%StaticNumbers.HistoryArraySize;
		float instantEnergy = VEdata.get((currentIndex))[1];
		double averageEnergy = AveragedEnergydata[shiftIndex];
		if(instantEnergy>=averageEnergy)
		{
			if(instantEnergy>highestPoint)
			{
				highestPoint = instantEnergy;
				highestIndex = counterIndex;
				timeIndex = timeReference;
				highestShiftIndex = shiftIndex;
			}
			aboveAverage = true;
		}else if(aboveAverage)
		{
			double avgEnergy = AveragedEnergydata[highestShiftIndex];
			double division = avgEnergy/highestPoint;

			aboveAverage = false;
			if(division<senstitivity)
			{
				importantItems.add(new SignificantItem(highestIndex,highestPoint,importantItems.size(),timeIndex));
			}
			highestPoint = 0;
			highestIndex = -1;
		}
		/**
		 * Will look for spikes that are above the average...
		 * Every spike above the average is a minor beat
		 * one the energy level crosses the average energy level we only take one spike until it falls back below
		 * This one spike is the maximum spike
		 *
		 * (maybe take two averages?)
		 */
	}
}
