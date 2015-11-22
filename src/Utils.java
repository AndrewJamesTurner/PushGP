/**
 * <h1>Utils</h1>
 * This class Contains a range of utility functions
 *
 * @author  Andrew Turner
 * @version 1.1
 * @since   2015-10-05
 */


import java.util.Random;


public class Utils {

	static private Random randomGenerator = new Random();

	/**
	 * Pauses for the given number of milliseconds
	 *
	 * @param time number of milliseconds to pause for
	 * @return Nothing.
	 */
	public static void pause(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// Pass silently
		}
	}


	/**
	 * Generates a random number between zero and 1
	 *
	 * @return a random number between zero and one
	 */
	public static double randomDouble() {

		return randomGenerator.nextFloat();
	}


	/**
	 * Generates a random integer number between zero and the give maximum value
	 *
	 * @param Maximum integer value
	 * @return A random number between zero and the given maximum number
	 */
	public static int randomInt(int max) {

		return randomGenerator.nextInt(max);
	}

	/**
	 * Pads the end of the given string with whitespace up to the given length
	 *
	 * @param String to pad
	 * @param Length to pad to
	 * @return Padded string
	 */
	public static String padString(String str, int leng) {
		for (int i = str.length(); i <= leng; i++)
			str += " ";
		return str;
	}


	/**
	 * Waits until the user presses a key
	 */
	public static void waitForKeyPress() {

		try {
			System.in.read();
		} catch (Exception e) {}
	}


	public static boolean isNumeric(String str) {
		return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
	}

}
