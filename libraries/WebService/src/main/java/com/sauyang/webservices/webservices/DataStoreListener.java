package com.sauyang.webservices.webservices;

public interface DataStoreListener
{
	public void onPostDataSuccess(DataTag dataTag, Object result);
	
	public void onPostDataFailure(DataTag dataTag, String resultCode, String resultMessage);

	public void onPreDataExecute(DataTag dataTag);

	public void onProgressUpdate(DataTag dataTag, Integer... values);
}
