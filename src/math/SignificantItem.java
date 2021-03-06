package math;


public class SignificantItem implements Comparable
{
	static boolean SORT_BY_LOCATION = false;
	static boolean SORT_BY_INTENSITY = false;
	public SignificantItem(long highestIndex, float highestPoint,int indexInList, double timeIndex)
	{
	//	System.out.println("Creating a beat at "+highestIndex+" time index "+timeIndex);
		soundIntensity = highestPoint;
		sampleLocation = highestIndex;
		this.indexInList = indexInList;
		this.timeIndex = timeIndex;
	}
	double timeIndex = 0;

	double soundIntensity;
	public long sampleLocation;
	public boolean predictedBeat;
	public int indexInList;
	public String toString()
	{
		return ""+sampleLocation;//"b "+(double)(sampleLocation*1320.0/44100.0);
	}

	@Override
	public int compareTo(Object o)
	{
		if(SORT_BY_LOCATION)
		{
			return (int) Math.signum(this.sampleLocation-((SignificantItem)o).sampleLocation);
		}else if(SORT_BY_INTENSITY)
		{
			return (int) Math.signum(this.soundIntensity-((SignificantItem)o).soundIntensity);
		}else
			return 0;
	}
}
