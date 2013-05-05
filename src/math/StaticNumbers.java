package math;

public class StaticNumbers
{
	public static int arraySampleLength = 6000;
	public static int minBufferDistance = 20;
	public static int maxBufferDistance = arraySampleLength/2;
	public static int frameSize = 256;//1024*8;//256;// 1024/4;
	public static int LargeFrameSize = frameSize*4;//1024*8;//256;// 1024/4;
	public static int sampleRate = 44100;

	public static int HistoryLength = 20;//43;
	public static int HistoryArraySize = HistoryLength*4;//43;
}
