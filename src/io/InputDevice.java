package io;

public abstract class InputDevice
{
	public abstract int readSamples(short[] samples, int offset, int numSamples);

	public abstract int getChannels();

	public abstract float getLength();

	public abstract int getRate();

	public abstract int skipSamples(int numSamples);

	public abstract short[] readAllSamples();
}
