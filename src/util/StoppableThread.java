package util;

public abstract class StoppableThread extends Thread
{
	private boolean running = true;
	private boolean toWait = false;
	private final void myWait()
	{
		synchronized(this)
		{
			repause();
			try
			{
				this.wait();
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * This is called right before the wait is called and the thread pauses
	 */
	public void repause()
	{
	}

	private final void myNotify()
	{
		synchronized(this)
		{
			toWait = false;
			unpause();
			this.notify();
		}
	}

	/**
	 * This is called right before the notify is called and the thread starts again
	 */
	public void unpause()
	{
	}

	/**
	 * Runs while the update Method returns true and while the running is true
	 */
	public final void run()
	{
		boolean myRunning = true;
		while(running&&myRunning)
		{
			if(toWait)
			{
				myWait();
			}
			try
			{
				myRunning = update();
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		postRunning();
	}

	/**
	 * Called after the thread is done running
	 */
	public abstract void postRunning();

	public final void end()
	{
		running = false;
	}


	public final void stopThread()
	{
		toWait = true;
	}

	public final void startThread()
	{
		myNotify();
	}

	/**
	 * this method might have thread sleeping in it so I am catching the method up here
	 * @return returns false if you want the method to stop running
	 * @throws InterruptedException
	 */
	public abstract boolean update() throws InterruptedException;


	public final boolean isPaused()
	{
		return toWait;
	}

}
