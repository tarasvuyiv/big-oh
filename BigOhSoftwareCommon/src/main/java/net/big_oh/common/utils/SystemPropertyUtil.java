package net.big_oh.common.utils;

/**
 * Defines convenience methods for accessing frequently used system properties.
 * @author davewingate
 *
 */
public class SystemPropertyUtil
{

	/**
	 * An alias for <code>System.getProperty("file.separator")</code>.
	 * @return The system file separator.  E.g. "/" on UNIX.
	 */
	public static String getSystemFileSeparator() {
		return System.getProperty("file.separator");
	}
	
	/**
	 * An alias for <code>System.getProperty("path.separator")</code>.
	 * @return The system file separator.  E.g. ":" on UNIX.
	 */
	public static String getSystemPathSeparator() {
		return System.getProperty("path.separator");
	}
	
	/**
	 * An alias for <code>System.getProperty("line.separator")</code>.
	 * @return The system file separator.  E.g. "\n" on UNIX.
	 */
	public static String getSystemLineSeparator() {
		return System.getProperty("line.separator");
	}
	
}
