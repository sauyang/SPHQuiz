package com.sauyang.webservices.webservices;

import java.util.Hashtable;

public class HTTPRequest {
    public String url = null;

    public int getRequestType() {
        return requestType;
    }

    /**
     * Result of the web service call.
     */
    private int requestType = HttpRequestTypes.GET;

    // Post request
    private Hashtable<String,Object> mPostParams = null;

    // basic header details
    private String host = null;

    /**
     * Creates a default {@link HttpRequestTypes#GET} request.
     *
     * @param url
     *        Full GET url
     */
    public HTTPRequest(String url)
    {
        this.requestType = HttpRequestTypes.GET;
        this.url = url;
    }

    /**
     * Creates a {@link HttpRequestTypes#POST_URL_ENCODED} request, used by most
     * simple POST calls.
     *
     * @param url
     *        POST url
     * @param postParams
     *        POST {@link Hashtable} parameters
     */
    public HTTPRequest(String url, Hashtable<String, Object> postParams)
    {
        this(url);
        this.requestType = HttpRequestTypes.POST_URL_ENCODED;
        this.mPostParams = postParams;

    }

    public HTTPRequest(String url, Hashtable<String,Object> postParams, int _httpRequestTypes){
        this(url);
        this.requestType = _httpRequestTypes;
        this.mPostParams = postParams;
    }

    public String getHost()
    {
        return host;
    }

    public void setHost(String host)
    {
        this.host = host;
    }

    public static class HttpRequestTypes
    {
        /** standard GET request */
        public static final int GET = 0;
        /** standard POST request */
        public static final int POST_URL_ENCODED = 1;
        /** POST normally used for uploading binary data */
        public static final int POST_MULTIPART_FORM_DATA = 2;
        /** custom POST data string - useful for JSON data */
        public static final int POST_STRING_BODY = 3;
    }

    public Hashtable<String, Object> getPostParams() {
        return mPostParams;
    }
}

