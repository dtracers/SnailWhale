package math;

public class SummationShortCuts
{
	/**
	 * Computes the shortcut for ·2^x from 1 to n
	 * it uses bitshift because those are awesome (and possibly faster)
	 */
	public static double powerRule2(int n)
	{
		return (-1+2<<n)<<2;
	}

	/**
	 * Computes the shortcut for ·x^i from 1 to n
	 * if you are using 2 for x it is better to use powerRule2
	 * @param x x>1
	 */
	public static double powerRule(int n,int x)
	{
		return x/(x-1)*(StrictMath.pow(x,n )-1);
	}
}
