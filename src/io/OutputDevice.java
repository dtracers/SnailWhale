package io;

/**
 * Basically uses libgdx interfaces but does not require libgdx
 * @author gigemjt
 *
 */
public abstract class OutputDevice
{
	/** @return whether this AudioDevice is in mono or stereo mode. */
	public abstract boolean isMono();

	/** writes 16bit samples
	 *
	 * @param samples The samples.
	 * @param offset The offset into the samples array
	 * @param numSamples the number of samples to write to the device */
	public abstract void writeSamples (short[] samples, int offset, int numSamples);

	/** Writes float samples
	 *
	 * @param samples The samples.
	 * @param offset The offset into the samples array
	 * @param numSamples the number of samples to write to the device */
	public abstract void writeSamples (float[] samples, int offset, int numSamples);

	/** @return the latency in samples. */
	public abstract int getLatency ();

	/**
	 * a normal dispose method
	 */
	public abstract void dispose ();

	/**
	 * Sets the volume in the range [0,1].
	 */
	public  abstract void setVolume (float volume);
}
