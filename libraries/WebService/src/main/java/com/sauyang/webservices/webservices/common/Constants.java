package com.sauyang.webservices.webservices.common;

public class Constants {
	
	/**
	 * Status codes used for the result of a
	 * data operation (such as an http connection).
	 * 
	 * SDK codes [0 - 1000]
	 * Custom Codes [ > 1000]
	 */
	public static class StatusCodes
	{
		public static final int NONE = -1;
		public static final int OPERATION_IN_PROCESS = 0;
		public static final int SUCCESS = 1;
		public static final int GENERAL_OPERATION_ERROR = 2;
		public static final int TIME_OUT = 3;
		public static final int NO_CONNECTION = 4;
		public static final int HTTP_NON_OK_RESPONSE = 5;
	}
	
	public static class LocationProviderConfig
	{
		public static final int GPS_NETWORK_PROVIDER = 0;
		public static final int GPS_PROVIDER = 1;
		public static final int NETWORK_PROVIDER = 2;
		public static final int NO_PROVIDER = 3;
	}
	
	public static final int HTTP_OK_RESPONSE = 200;
}
