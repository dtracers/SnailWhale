package io;

import java.io.File;
import java.util.ArrayList;

import math.StaticNumbers;


import static math.StaticNumbers.*;
import util.SafeReadWriteLoopingArray;
import util.StoppableThread;

public abstract class MusicHandler
{
	InputDevice decoder;
	OutputDevice device;
	SafeReadWriteLoopingArray<short[]> musicFile;

	short[] buf;

	public boolean buffering = true;
	public boolean doneReading = false;
	private boolean forceStop = false;
	public boolean slowingDownBuffer = false;

	public boolean songFinished = false;

	public double outputTimeReference = 0;
	public double inputTimeReference = 0;

	private boolean stopRunning = false;

	StoppableThread inputThread;
	StoppableThread outputThread;

	public MusicHandler()
	{
		buf = new short[frameSize*2];
		musicFile = new SafeReadWriteLoopingArray<short[]>(true,maxBufferDistance,true,minBufferDistance,arraySampleLength);
	}

	/**
	 * Calls  in this order
	 * loadSound
	 * loadOutput
	 * initializeInput
	 * initializeOutput
	 * input.start()
	 * output.start()
	 * @param input
	 * @param output
	 */
	public void loadAndStart(InputDevice input,OutputDevice output)
	{
		loadSound(input);
		loadOutput(output);
		initializeInput();
		initializeOutput();
		inputThread.start();
		outputThread.start();
	}

	public void loadSound(InputDevice input)
	{
		buffering = true;
		musicFile.reset();
		slowingDownBuffer = false;
		forceStop = false;

		stopRunning = false;

		decoder = input;
	}

	public void loadOutput(OutputDevice output)
	{
		songFinished = false;
		device = output;
	}

	public final void initializeInput()
	{
		inputThread = new StoppableThread()
		{
			int readSong = 1;
			short[] longerArray = new short[LargeFrameSize];

			@Override
			public boolean update() throws InterruptedException
			{
				if(slowingDownBuffer)
				{
					Thread.sleep(100);
					return true;
				}
				if(musicFile.canWrite())
				{
					short[] currentFrame = musicFile.writeElement();
					//has to convert it to mono
					if(decoder.getChannels() == 2)
					{
						readSong = decoder.readSamples(buf,0, frameSize*2);
						for(int k=0;k<frameSize;k++)
						{
							currentFrame[k] = (short) ((buf[k*2]+buf[k*2+1])/2.0);//gets half of the song (maybe because it is stereo?)
						}
					}else //we can put it straight in
					{
						readSong = decoder.readSamples(currentFrame,0, frameSize);
					}
					if(!stopRunning)
						inputTimeReference = (musicFile.getCurrentWritingIndex()*frameSize)/((double)sampleRate);
					if(musicFile.getCurrentWritingIndex()%4==0)
					{
						combineArray(musicFile, musicFile.getCurrentWritingIndex()-4);
						postWriteMethod(longerArray);
					}
				}else
				{
					if(buffering)
					{
						outputThread.startThread();
					}
					slowingDownBuffer = true;
					inputThread.stopThread();
				}
				return readSong>0;
			}
			private void combineArray(SafeReadWriteLoopingArray<short[]> musicFile, int startIndex)
			{
				ArrayList<short[]> list = musicFile.getList();
				for(int k = 0 ;k<4;k++)
				{
					short[] tempArray = list.get((k+startIndex)%arraySampleLength);
					int skipIndex = k*frameSize;
					for(int q = 0;q<frameSize;q++)
					{
						longerArray[q+skipIndex] = tempArray[q];
					}
				}
			}
			@Override
			public void postRunning()
			{
				doneReading = true;
				musicFile.setMinDistance(true, 0);
			}

		};
	}

	public final void initializeOutput()
	{
		outputThread = new StoppableThread()
		{

			@Override
			public boolean update() throws InterruptedException
			{

				if(!songFinished)
				{
					if(musicFile.canRead())
					{
						short[] currentFrame = musicFile.readElement();
						device.writeSamples(currentFrame, 0, frameSize);
						if(!musicFile.canWrite())
						{
							inputThread.stopThread();
						}else
						{
							inputThread.startThread();
						}
						//going to need to change the way this works!
						outputTimeReference = (musicFile.getCurrentReadingIndex()*frameSize)/((double)sampleRate);
						if(doneReading&&musicFile.currentDistance()<2)
						{
							System.out.println("DONE READING?");
							songFinished = true;
							return false;
						}

					}else
					{
						buffering = true;
						outputThread.stopThread();
					}

				}

				return true;
			}

			@Override
			public void postRunning()
			{
			}

		};
	}

	/**
	 * Allows people to do what they like with the array after post writing
	 * Note that the object that this is written to is overwritten every time this method is called
	 * so you have to copy the values in order to store them
	 * @param longerArray
	 */
	public abstract void postWriteMethod(short[] longerArray);

	/**
	 * This is called when the output needs to stop happening so that we can buffer
	 */
	public abstract void buffering();

	/**
	 * This is called when the input needs to stop happening so the output can catch up
	 */
	public abstract void slowingDownInput();
}