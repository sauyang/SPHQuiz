package webservices.debug;

import android.util.Log;


public class LogDbug
{
	private static final String TAG = "LogDBug";

	public static final boolean DEBUG_ON = true;
    private static final int CHUNK_SIZE = 2048;

	private static final boolean LOGGING_ON = DEBUG_ON && true;

	/**
	 * Convenient debug log method to make it easy to translate the code from
	 * iOS implementation with the DebugLog #define.
	 *
	 * @param text
	 *        debug message
	 */
	public static void log(String text)
	{
		if (LOGGING_ON)
		{
            if (text != null) {
                for (int i = 0; i < text.length(); i += CHUNK_SIZE) {
                    Log.v(TAG, text.substring(i, Math.min(text.length(), i + CHUNK_SIZE)));
                }
            }
		}
	}

	/**
	 * Convenient debug log method to make it easy to translate the code from
	 * iOS implementation with the DebugLog #define.
	 *
	 * @param text
	 *        debug message
	 */
	public static void log(String tag, String text)
	{
		if (LOGGING_ON)
		{
            if (text != null) {
                for (int i = 0; i < text.length(); i += CHUNK_SIZE) {
                    Log.v(tag, text.substring(i, Math.min(text.length(), i + CHUNK_SIZE)));
                }
            }
		}
	}


	/**
	 * Convenient debug log method to make it easy to translate the code from
	 * iOS implementation with the DebugLog #define.
	 *
	 * @param text
	 *        debug message
	 */
	public static void log(String text, Throwable t)
	{
		if (LOGGING_ON)
		{
			Log.e(TAG, text, t);
		}
	}


	/**
	 * Convenient debug log method to make it easy to translate the code from
	 * iOS implementation with the DebugLog #define.
	 *
	 * @param text
	 *        debug message
	 */
	public static void log(String tag, String text, Throwable t)
	{
		if (LOGGING_ON)
		{
			Log.e(tag, text, t);
		}
	}

	/**
	 * printStackTrace that can be turned off for production
	 *
	 * @param e
	 */
	public static void printStackTrace(Exception e)
	{
		if (LOGGING_ON)
		{
			Log.getStackTraceString(e);
		}
	}

//	public static void logToPhone(String tag, String text)
//	{
//		//This can return a full clean log without any TAG limitation issue.
//		File logFilePath = new File(Environment.getExternalStorageDirectory(), "Log666");
//		File logFile  =  new File(logFilePath, "log.file");
//		if (!logFilePath.exists())
//		{
//			logFilePath.mkdirs();
//		}
//
//		try
//		{
//			//BufferedWriter for performance, true to set append to file flag
//			BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
//			buf.append(text);
//			buf.newLine();
//			buf.close();
//		}
//		catch (IOException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}
}
