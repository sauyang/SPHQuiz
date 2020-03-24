package com.sauyang.webservices.webservices.common;

import java.math.BigInteger;
import java.security.MessageDigest;

public class Encoding 
{
	/**
     * Generates Sha256 Hash based on string input
     *
     * @param string to hash
     * @return SHA-256 hash
     */
    public static String getSha256(String string)
    {
        try
        {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            BigInteger hash = new BigInteger(1, md.digest(string.getBytes("UTF-8")));
            return hash.toString(16);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    
    public static byte[] decodeBase64(byte[] data)
    {
    	return Base64.decode(data);
    }
    
    public static byte[] decodeBase64(String str)
    {
    	return Base64.decode(str);
    }
    
    public static String encodeBase64(byte[] data)
    {
    	return Base64.encodeToString(data, false);
    }
    
    public static String encodeBase64(String str)
    {
    	return Base64.encodeToString(str.getBytes(), false);
    }
}
