package com.sauyang.webservices.webservices.common;

import java.util.ArrayList;

/**
 * Utility class to perform various functions.
 *  
 * @author Stephen Asherson
 */
public class Utils {
	
	final static String[] hex = {
	    "%00", "%01", "%02", "%03", "%04", "%05", "%06", "%07",
	    "%08", "%09", "%0a", "%0b", "%0c", "%0d", "%0e", "%0f",
	    "%10", "%11", "%12", "%13", "%14", "%15", "%16", "%17",
	    "%18", "%19", "%1a", "%1b", "%1c", "%1d", "%1e", "%1f",
	    "%20", "%21", "%22", "%23", "%24", "%25", "%26", "%27",
	    "%28", "%29", "%2a", "%2b", "%2c", "%2d", "%2e", "%2f",
	    "%30", "%31", "%32", "%33", "%34", "%35", "%36", "%37",
	    "%38", "%39", "%3a", "%3b", "%3c", "%3d", "%3e", "%3f",
	    "%40", "%41", "%42", "%43", "%44", "%45", "%46", "%47",
	    "%48", "%49", "%4a", "%4b", "%4c", "%4d", "%4e", "%4f",
	    "%50", "%51", "%52", "%53", "%54", "%55", "%56", "%57",
	    "%58", "%59", "%5a", "%5b", "%5c", "%5d", "%5e", "%5f",
	    "%60", "%61", "%62", "%63", "%64", "%65", "%66", "%67",
	    "%68", "%69", "%6a", "%6b", "%6c", "%6d", "%6e", "%6f",
	    "%70", "%71", "%72", "%73", "%74", "%75", "%76", "%77",
	    "%78", "%79", "%7a", "%7b", "%7c", "%7d", "%7e", "%7f",
	    "%80", "%81", "%82", "%83", "%84", "%85", "%86", "%87",
	    "%88", "%89", "%8a", "%8b", "%8c", "%8d", "%8e", "%8f",
	    "%90", "%91", "%92", "%93", "%94", "%95", "%96", "%97",
	    "%98", "%99", "%9a", "%9b", "%9c", "%9d", "%9e", "%9f",
	    "%a0", "%a1", "%a2", "%a3", "%a4", "%a5", "%a6", "%a7",
	    "%a8", "%a9", "%aa", "%ab", "%ac", "%ad", "%ae", "%af",
	    "%b0", "%b1", "%b2", "%b3", "%b4", "%b5", "%b6", "%b7",
	    "%b8", "%b9", "%ba", "%bb", "%bc", "%bd", "%be", "%bf",
	    "%c0", "%c1", "%c2", "%c3", "%c4", "%c5", "%c6", "%c7",
	    "%c8", "%c9", "%ca", "%cb", "%cc", "%cd", "%ce", "%cf",
	    "%d0", "%d1", "%d2", "%d3", "%d4", "%d5", "%d6", "%d7",
	    "%d8", "%d9", "%da", "%db", "%dc", "%dd", "%de", "%df",
	    "%e0", "%e1", "%e2", "%e3", "%e4", "%e5", "%e6", "%e7",
	    "%e8", "%e9", "%ea", "%eb", "%ec", "%ed", "%ee", "%ef",
	    "%f0", "%f1", "%f2", "%f3", "%f4", "%f5", "%f6", "%f7",
	    "%f8", "%f9", "%fa", "%fb", "%fc", "%fd", "%fe", "%ff"
	  };

	/**
	 * Encodes a string into URI format. </p>
	 * 
	 * This method encodes a space character (' ') into a plus '+'. In order to
	 * encode ' ' into "%20", use {@link #uriEncode(String, boolean)}.
	 * 
	 * @param s
	 *        text to encode
	 * @return string in URI encoded format
	 * @see #uriEncode(String, boolean)
	 * @see #uriEncode(String, boolean, boolean)
	 */
	// TODO pull this method into a more suitable package
	public static String URIEncode(String s)
	{
		return uriEncode(s, false, false);
	}

	/**
	 * Encodes a string into URI format. </p>
	 * 
	 * @param s
	 *        text to encode
	 * @param usePercentEncoding
	 *        determines how the space character (' ') will be encoded. If
	 *        <code>true</code>, ' ' is encoded as "%20"; if <code>false</code>,
	 *        ' ' is encoded as '+'.
	 * @return string in URI encoded format
	 * @see #URIEncode(String)
	 * @see #uriEncode(String, boolean, boolean)
	 */
	// TODO pull this method into a more suitable package
	public static String uriEncode(String s, boolean usePercentEncoding)
	{
		return uriEncode(s, usePercentEncoding, false);
	}

	/**
	 * Encodes a string into URI format. </p>
	 * 
	 * @param s
	 *        text to encode
	 * @param usePercentEncoding
	 *        determines how the space character (' ') will be encoded. If
	 *        <code>true</code>, ' ' is encoded as "%20"; if <code>false</code>,
	 *        ' ' is encoded as '+'.
	 * @param forceUppercaseHex
	 *        depending on the server implementation that is being matched, this
	 *        parameter can be used to force the hex representation of
	 *        characters to use uppercase letters; default is to use lowercase
	 *        letters for hex values
	 * @return string in URI encoded format
	 * @see #URIEncode(String)
	 * @see #uriEncode(String, boolean)
	 */
	// TODO pull this method into a more suitable package
	public static String uriEncode(String s, boolean usePercentEncoding,
                                   boolean forceUppercaseHex)
	{
		StringBuffer sbuf = new StringBuffer();
		int len = s.length();
		for (int i = 0; i < len; i++)
		{
			int ch = s.charAt(i);
			if ('A' <= ch && ch <= 'Z')
			{ // 'A'..'Z'
				sbuf.append((char) ch);
			}
			else if ('a' <= ch && ch <= 'z')
			{ // 'a'..'z'
				sbuf.append((char) ch);
			}
			else if ('0' <= ch && ch <= '9')
			{ // '0'..'9'
				sbuf.append((char) ch);
			}
			else if (ch == ' ')
			{ // space
				if (usePercentEncoding)
				{
					sbuf.append("%20");
				}
				else
				{
					sbuf.append('+');
				}
			}
			else if (ch == '-'
				|| ch == '_' // unreserved
				|| ch == '.' || ch == '!' || ch == '~' || ch == '*'
				|| ch == '\'' || ch == '(' || ch == ')')
			{
				sbuf.append((char) ch);
			}
			else if (ch <= 0x007f)
			{ // other ASCII
				String hexChar = hex[ch];
				sbuf.append((forceUppercaseHex ? hexChar.toUpperCase()
					: hexChar));
			}
			else if (ch <= 0x07FF)
			{ // non-ASCII <= 0x7FF
				StringBuffer hexChar = new StringBuffer(4);
				hexChar.append(hex[0xc0 | (ch >> 6)]);
				hexChar.append(hex[0x80 | (ch & 0x3F)]);
				sbuf.append((forceUppercaseHex ? hexChar.toString()
					.toUpperCase() : hexChar.toString()));
			}
			else
			{ // 0x7FF < ch <= 0xFFFF
				StringBuffer hexChar = new StringBuffer(6);
				hexChar.append(hex[0xe0 | (ch >> 12)]);
				hexChar.append(hex[0x80 | ((ch >> 6) & 0x3F)]);
				hexChar.append(hex[0x80 | (ch & 0x3F)]);
				sbuf.append((forceUppercaseHex ? hexChar.toString()
					.toUpperCase() : hexChar.toString()));
			}
		}
		return sbuf.toString();
	}

	
	public static String replaceAll(String source, String pattern, String replace) {
		if (source!=null) {
			final int len = pattern.length();
	        StringBuffer sb = new StringBuffer();
	        int found = -1;
	        int start = 0;

	        while( (found = source.indexOf(pattern, start) ) != -1) {
	            sb.append(source.substring(start, found));
	            sb.append(replace);
	            start = found + len;
	        }

	        sb.append(source.substring(start));

	        return sb.toString();
	   }
	   else return "";
	}
	
	public static String stripHTMLTags(String _text) {

		String newSequence = "";

		boolean addChar = true;
		for (int i = 0; i < _text.length(); i++) {

			char currentChar = _text.charAt(i);

			if (currentChar == '<')
				addChar = false;
			else if (currentChar == '>')
				addChar = true;

			if ((addChar) && (currentChar != '>'))
				newSequence = newSequence + currentChar;
		}

		return newSequence;
	}
	
	public static boolean hasImageCaptureBug() {

		// list of known devices that have the bug
		ArrayList<String> devices = new ArrayList<String>();

		devices.add("hero");
		devices.add("sapphire");
		devices.add("dream");

		return devices.contains(android.os.Build.DEVICE);
	}
}


