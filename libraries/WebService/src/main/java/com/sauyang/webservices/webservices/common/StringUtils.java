package com.sauyang.webservices.webservices.common;

import android.text.Editable;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * Utility class with String helper routines.
 */
public class StringUtils
{

	/**
	 * Tests if a string is a non-null, non-empty string. This can be called to
	 * determine if the string should be displayed, or not.
	 * 
	 * @param text
	 *        String to test.
	 * @return
	 *         If <code>text</code> is <code>null</code>, returns
	 *         <code>false</code>. <br>
	 *         If <code>text</code> is an empty string (""), returns
	 *         <code>false</code>. <br>
	 *         Else returns <code>true</code>.
	 */
	public static boolean isNonBlankString(String text)
	{
		// null text -> false
		if (text == null)
			return false;

		// empty text -> false
		if ("".equals(text))
			return false;

		return true;
	}

	/**
	 * Tests if a string is a blank string, or is null. This can be called to
	 * determine if the string should be displayed, or not. </p>
	 * 
	 * This is exactly the opposite result to {@link #isNonBlankString(String)}.
	 * 
	 * @param text
	 *        String to test.
	 * @return
	 *         If <code>text</code> is <code>null</code>, returns
	 *         <code>true</code>. <br>
	 *         If <code>text</code> is an empty string (""), returns
	 *         <code>true</code>. <br>
	 *         Else returns <code>null</code>.
	 * @see #isNonBlankString(String)
	 */
	public static boolean isBlankOrNull(String text)
	{
		return !isNonBlankString(text);
	}

	/**
	 * Returns a name in a printable style, based on whether the first & last
	 * names are valid to display.
	 * 
	 * @param name
	 *        first name - can be <code>null</code> or blank ("")
	 * @param surname
	 *        surname - can be <code>null</code> or blank ("")
	 * @return a string that can be printed that represents the person's name,
	 *         as "name surname".
	 *         If either name given is <code>null</code> or blank or consists
	 *         entirely of whitespace, that name is ignored. If both names are
	 *         <code>null</code>, returns <code>null</code>.
	 */
	public static String printableName(String name, String surname)
	{
		if ((name == null) && (surname == null))
			return null;

		if (name == null)
		{
			return surname.trim();
		}

		if (surname == null)
		{
			return name.trim();
		}

		return (name + " " + surname).trim();
	}
	
	public static int[] getDelimitedInts(String src, int delim )
	 {
	  ArrayList<String> parts = new ArrayList<String>(Arrays.asList((src.split((char)(delim)+""))));
	  int[] partValues = new int[parts.size()];
	  for (int i=0; i<parts.size(); i++) {
	   partValues[i] = 0;
	   String p = (String) parts.get(i);
	   if (p != null ) {
	    try {
	     partValues[i] = Integer.parseInt(p.trim());
	    }
	    catch ( NumberFormatException nfe ) {
	    }
	   }   
	  }  
	  return partValues;
	 }

    public static String getNotNullString(Editable editText) {

		if(editText == null) return "";
		return editText.toString();
    }
}
