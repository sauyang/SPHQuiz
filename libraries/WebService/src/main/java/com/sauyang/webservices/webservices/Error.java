package com.sauyang.webservices.webservices;

import android.content.Context;

import com.sauyang.webservices.R;
import com.sauyang.webservices.webservices.common.Constants;


public class Error
{
	public static String getGeneralErrorMessage(int errorCode,
                                                String description)
	{
		// If there is already a valid error description... just use that
		if (description != null && !description.trim().equals(""))
		{
			return description;
		}
		
		if (errorCode == Constants.StatusCodes.NO_CONNECTION || errorCode == Constants.StatusCodes.TIME_OUT)
		{
			return "Connection is not available";
		}
		
		return null;
	}
}
