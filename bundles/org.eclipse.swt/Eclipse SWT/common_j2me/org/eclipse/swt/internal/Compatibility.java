package org.eclipse.swt.internal;

import org.eclipse.swt.SWT;
import java.io.*;

/**
 * This class is a placeholder for utility methods commonly
 * used on J2SE platforms but not supported on some J2ME
 * profiles.
 * <p>
 * It is part of our effort to provide support for both J2SE
 * and J2ME platforms.
 * </p>
 * <p>
 * IMPORTANT: some of the methods have been modified from their
 * J2SE parents. Refer to the description of each method for 
 * specific changes.
 * </p>
 * <ul>
 * <li>Exceptions thrown may differ since J2ME's set of 
 * exceptions is a subset of J2SE's one.
 * </li>
 * <li>The range of the mathematic functions is subject to
 * change.
 * </li>		
 * </ul>
 */
public final class Compatibility {

/** 
 * Sine table
 * <p>
 * Values represent sine for each degree between 0 and 90. Values have
 * been multiplied by 65536 and rounded.
 * </p>
 */
static int[] sineTable = {
	    0,  1143,  2287,  3429,  4571,  5711,  6850,  7986,  9120, 10252, //  0 to  9 degrees
	11380, 12504, 13625, 14742, 15854, 16961, 18064, 19160, 20251, 21336, // 10 to 19 degrees
	22414, 23486, 24550, 25606, 26655, 27696, 28729, 29752, 30767, 31772, // 20 to 29 degrees
	32767, 33753, 34728, 35693, 36647, 37589, 38521, 39440, 40347, 41243, // 30 to 39 degrees
	42125, 42995, 43852, 44695, 45525, 46340, 47142, 47929, 48702, 49460, // 40 to 49 degrees
	50203, 50931, 51643, 52339, 53019, 53683, 54331, 54963, 55577, 56175, // 50 to 59 degrees
	56755, 57319, 57864, 58393, 58903, 59395, 59870, 60326, 60763, 61183, // 60 to 69 degrees
	61583, 61965, 62328, 62672, 62997, 63302, 63589, 63856, 64103, 64331, // 70 to 79 degrees
	64540, 64729, 64898, 65047, 65176, 65286, 65376, 65446, 65496, 65526, // 80 to 89 degrees
	65536 };                                                              //       90 degrees

/**
 * Answers the length of the side adjacent to the given angle
 * of a right triangle. In other words, it returns the integer
 * conversion of length * cos (angle).
 * <p>
 * IMPORTANT: the j2me version has an additional restriction on
 * the argument. length must be between -32767 and 32767 (inclusive).
 * </p>
 * 
 * @param angle the angle in degrees
 * @param length the length of the triangle's hypotenuse
 * @return the integer conversion of length * cos (angle)
 */
public static int cos(int angle, int length) {
	return sin(90 - angle, length);
}

/**
 * Answers the length of the side opposite to the given angle
 * of a right triangle. In other words, it returns the integer
 * conversion of length * sin (angle).
 * <p>
 * IMPORTANT: the j2me version has an additional restriction on
 * the argument. length must be between -32767 and 32767 (inclusive).
 * </p>
 * 
 * @param angle the angle in degrees
 * @param length the length of the triangle's hypotenuse
 * @return the integer conversion of length * sin (angle)
 */
public static int sin(int angle, int length) {
	if (length < -32767 || length > 32767) {
		SWT.error(SWT.ERROR_INVALID_RANGE);
	}
	if (angle < 0 || angle >= 360) {
		angle = angle % 360;
		if (angle < 0) angle += 360;
	}
	
	int sineValue;
	if (angle >= 0 && angle < 90) sineValue = sineTable[angle];
	else if (angle >= 90 && angle < 180) sineValue = sineTable[180 - angle];
	else if (angle >= 180 && angle < 270) sineValue = 0 - sineTable[angle - 180];
	else {
		// angle >= 270 && angle < 360
		sineValue = 0 - sineTable[360 - angle];
	}

	return (sineValue * length) >> 16;
}

/**
 * Answers the most negative (i.e. closest to negative infinity)
 * integer value which is greater than the number obtained by dividing
 * the first argument p by the second argument q.
 * 
 * @param p numerator
 * @param q denominator (must be different from zero)
 * @return the ceiling of the rational number p / q.
 */
public static int ceil(int p, int q) {
	int res = p / q;
	if ((p % q == 0) || 
		(res < 0) ||
		((res == 0) && ((p < 0 && q > 0) || (p > 0 && q < 0))))
		return res;
	else
		return res + 1;
}

/**
 * Answers the most positive (i.e. closest to positive infinity)
 * integer value which is less than the number obtained by dividing
 * the first argument p by the second argument q.
 *
 * @param p numerator
 * @param q denominator (must be different from zero)
 * @return the floor of the rational number p / q.
 */
public static int floor(int p, int q) {
	int res = p / q;
	if ((p % q == 0) ||
		(res > 0) ||
		((res == 0) && ((p > 0 && q > 0) || (p < 0 && q < 0))))
		return res;
	else
		return res - 1;
}

/**
 * Answers the result of rounding to the closest integer the number obtained 
 * by dividing the first argument p by the second argument q.
 * <p>
 * IMPORTANT: the j2me version has an additional restriction on
 * the arguments. p must be within the range 0 - 32767 (inclusive).
 * q must be within the range 1 - 32767 (inclusive).
 * </p>
 * 
 * @param p numerator
 * @param q denominator (must be different from zero)
 * @return the closest integer to the rational number p / q
 */
public static int round(int p, int q) {
	if (p < 0 || p > 32767 || q < 1 || q > 32767) {
		SWT.error(SWT.ERROR_INVALID_RANGE);
	}
	return (2 * p + q) / (2 * q);
}

/**
 * Returns 2 raised to the power of the argument.
 *
 * @param n an int value between 0 and 30 (inclusive)
 * @return 2 raised to the power of the argument
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if the argument is not between 0 and 30 (inclusive)</li>
 * </ul>
 */
public static int pow2(int n) {
	if (n >= 1 && n <= 30)
		return 2 << (n - 1);
	else if (n != 0) {
		SWT.error(SWT.ERROR_INVALID_RANGE);
	}
	return 1;
}

/**
 * Open a file if such things are supported.
 * 
 * @param filename the name of the file to open
 * @return a stream on the file if it could be opened.
 */
public static InputStream newFileInputStream (String filename) throws IOException {
	throw new IOException();
}

/**
 * Open a file if such things are supported.
 * 
 * @param filename the name of the file to open
 * @return a stream on the file if it could be opened.
 */
public static OutputStream newFileOutputStream (String filename) throws IOException {
	throw new IOException();
}

/**
 * Answers whether the character is a letter.
 *
 * @param c the character
 * @return true when the character is a letter
 */
public static boolean isLetter(char c) {
	return (Character.isUpperCase(c)) || (Character.isLowerCase(c));
}

/**
 * Answers whether the character is a letter or a digit.
 *
 * @param c the character
 * @return true when the character is a letter or a digit
 */
public static boolean isLetterOrDigit(char c) {
	return isLetter(c) || Character.isDigit(c);
}

/**
 * Answers whether the character is a Unicode space character.
 *
 * @param c	 the character
 * @return true when the character is a Unicode space character
 */
public static boolean isSpaceChar(char c) {
	return c == ' ';
}

/**
 * Answers whether the character is whitespace character.
 *
 * @param c the character
 * @return true when the character is a whitespace character
 */
public static boolean isWhitespace(char c) {
	// Optimized case for ASCII
	if ((c >= 0x1c && c <= 0x20) || (c >= 0x9 && c <= 0xd)) 
		return true;
	return false;
}

/**
 * Execute a program in a separate platform process if the
 * underlying platform support this.
 * <p>
 * The new process inherits the environment of the caller.
 * </p>
 *
 * @param program the name of the program to execute
 *
 * @exception IOException
 *  if the program cannot be executed
 * @exception SecurityException
 *  if the current SecurityManager disallows program execution
 */
public static void exec(String prog) throws java.io.IOException {
	throw new IOException();
}

/**
 * Execute progArray[0] in a separate platform process if the
 * underlying platform support this.
 * <p>
 * The new process inherits the environment of the caller.
 * <p>
 *
 * @param progArray array containing the program to execute and its arguments
 *
 * @exception IOException
 *  if the program cannot be executed
 * @exception	SecurityException
 *  if the current SecurityManager disallows program execution
 */
public static void exec(String[] progArray) throws java.io.IOException{
	throw new IOException();
}

/**
 * Returns the NLS'ed message for the given argument. This is only being
 * called from SWT.
 * 
 * @param key the key to look up
 * @return the message for the given key
 * 
 * @see SWT#getMessage
 */
public static String getMessage(String key) {
	String answer = key;
	
	if (key == null)
		SWT.error (SWT.ERROR_NULL_ARGUMENT);	
	if (key.equals( "SWT_Yes"))
		return "Yes";
	if (key.equals("SWT_No"))
		return "No";
	if (key.equals("SWT_OK")) 
		return "OK";
	if (key.equals("SWT_Cancel"))
	 	return "Cancel";
	if (key.equals("SWT_Abort"))
	 	return "Abort";
	if (key.equals("SWT_Retry"))
	 	return "Retry";
	if (key.equals("SWT_Ignore"))
	 	return "Ignore";
	if (key.equals("SWT_Sample"))
	 	return "Sample";
	if (key.equals("SWT_A_Sample_Text"))
	 	return "A Sample Text";
	if (key.equals("SWT_Selection"))
	 	return "Selection";
	if (key.equals("SWT_Current_Selection"))
	 	return "Current Selection";
	if (key.equals("SWT_Character_set"))
	 	return "Character set";
	if (key.equals("SWT_Font"))
	 	return "Font";
	if (key.equals("SWT_Extended_style"))
	 	return "Extended style";
	if (key.equals("SWT_Size"))
	 	return "Size";
	if (key.equals("SWT_Style"))
	 	return "Style";
	 	
	return key;
}
	
/**
 * Interrupt the current thread. 
 * <p>
 * Note that this is not available on CLDC.
 * </p>
 */
public static void interrupt() {
}

/**
 * Compares two instances of class String ignoring the case of the
 * characters and answers if they are equal.
 *
 * @param s1 string
 * @param s2 string
 * @return true if the two instances of class String are equal
 */
public static boolean equalsIgnoreCase(String s1, String s2) {
	if (s1 == s2) return true;
	if (s2 == null || s1.length() != s2.length()) return false;

	char[] cArray1 = s1.toCharArray();
	char[] cArray2 = s2.toCharArray();
	int length = s1.length();
	char c1, c2;

	for (int index = 0; index < length; index++) {
		c1 = cArray1[index];
		c2 = cArray2[index];
		if (c1 != c2 && 
			Character.toUpperCase(c1) != Character.toUpperCase(c2) &&
			Character.toLowerCase(c1) != Character.toLowerCase(c2)) {
			return false;
		}
	}
	return true;
}

/**
 * Copies a range of characters into a new String.
 *
 * @param buffer the StringBuffer we want to copy from
 * @param start the offset of the first character
 * @param end the offset one past the last character
 * @return a new String containing the characters from start to end - 1
 *
 * @exception	IndexOutOfBoundsException 
 *   when <code>start < 0, start > end</code> or <code>end > length()</code>
 */
public static String substring(StringBuffer buffer, int start, int end) {
	return buffer.toString().substring(start, end);
}

}
