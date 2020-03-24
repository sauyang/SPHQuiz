//package com.sauyang.webservices.webservices;
//
//
//import android.annotation.TargetApi;
//import android.os.AsyncTask;
//import android.os.Build;
//
//import com.sauyang.webservices.BuildConfig;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.lang.ref.WeakReference;
//import java.util.HashMap;
//import java.util.Hashtable;
//
//import webservices.debug.LogDbug;
//
//
//public abstract class WebServiceHandler extends AsyncTask<Hashtable<String, Object>, Integer, Void> {
//
//    protected String DEBUG_TAG = WebService.DEBUG_TAG;
//    protected HashMap<String, Object> userInfo = null;
//    private WebService ws;
//
//    protected boolean dataSuccess = false;
//
//    protected Object resultDataObject;
//    private DataTag dataHandlerTag;
//    private WebServiceDataHandler dataListener;
//    protected String stubLocalResponse = null;
//
//    public HashMap<String, Object> getUserInfo()
//    {
//        return userInfo;
//    }
//
//    public interface OnWebServiceHandlerEventListener
//    {
//        void onWebServiceHandlerPostExecute(int hashCode);
//    }
//
//    private WeakReference<OnWebServiceHandlerEventListener> eventListener;
//
//    // Assign the listener implementing events interface that will receive the events
//    public void setObjectListener(OnWebServiceHandlerEventListener eventListener)
//    {
//        this.eventListener = new WeakReference<>(eventListener);
//    }
//
//    public void setUserInfo(HashMap<String, Object> userInfo) {
//        this.userInfo = userInfo;
//    }
//
//    public WebServiceHandler(DataTag dataHandlerTag) {
//        this.dataHandlerTag = dataHandlerTag;
//    }
//
//    private void cancelWebService(){
//        if (ws != null){
//            ws.cancelRequest();
//        }
//        ws = null;
//    }
//
//    protected S1Error errorFromJSONObject(JSONObject jsonObject ) throws JSONException {
//        String errorCode = "error.general";
//        if (jsonObject.has(WebServiceConstants.Response.errorCode))
//            errorCode = jsonObject.getString(WebServiceConstants.Response.errorCode);;
//        String errorDescription = "";
//        if (jsonObject.has(WebServiceConstants.Response.errorDescription))
//            errorDescription = jsonObject.getString(WebServiceConstants.Response.errorDescription);
//        return S1Error.errorFrom(errorCode, errorDescription);
//    }
//
//    @Override
//    protected void onCancelled() {
//        cancelWebService();
//        super.onCancelled();
//    }
//
//    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
//    @Override
//    protected void onCancelled(Void aVoid) {
//        cancelWebService();
//        super.onCancelled(aVoid);
//    }
//
//    @Override
//    protected Void doInBackground(Hashtable<String, Object>... args) {
//        Hashtable<String, Object> parameters = null;
//
//        if(!isCancelled()) {
//            if (args != null) {
//                parameters = args[0];
//
//                if(parameters != null) {
//                    DataTag tag = getDataHandlerTag();
//                    tag.setUserInfo(new HashMap<>(parameters));
//                }
//            }
//            else {
//                DataTag tag = getDataHandlerTag();
//                tag.setUserInfo(new HashMap<String, Object>());
//            }
//
//            try {
//                if(WebServiceObject.getInstance().isUseStubWebService()){
//                    LogDbug.log(WebService.DEBUG_TAG,
//                            "Webservice object: " + this.toString() +
//                                    "\nRequest parameters: " + (parameters != null ? parameters.toString() : "{}"));
//                }
//                return doInBackground(parameters);
//            } catch (Exception e) {
//                LogDbug.log(DEBUG_TAG, "HTTP request initialisation failed", e);
//                dataSuccess = false;
//                processResponseFailure(S1Error.generalOperationError());
//            }
//        }
//
//        return null;
//    }
//
//    protected Void doInBackground(Hashtable<String, Object> requestParameters)
//    {
//        if (requestParameters == null)return null;
//        HTTPRequest request = new HTTPRequest(WebServiceUtilities.getWebServiceUrlForDataTag(getDataHandlerTag()), requestParameters);
//        return executeHttpRequest(request);
//    }
//
//    protected Void executeHttpRequest(HTTPRequest request)
//    {
//        if(isCancelled())
//            return null;
//        try
//        {
//            if(BuildConfig.DEBUG) {
//                if (request.getPostParams() != null) {
//                    LogDbug.log(WebService.DEBUG_TAG,
//                            "Webservice object: " + this.toString() +
//                                    "\nRequest url: " + request.url +
//                                    "\nRequest parameters: " + request.getPostParams().toString());
//                } else {
//                    LogDbug.log(WebService.DEBUG_TAG,
//                            "Webservice object: " + this.toString() +
//                                    "\nRequest url: " + request.url +
//                                    "\nRequest parameters: none");
//                }
//            }
//
//            ws = new WebService(request);
//            dataSuccess = ws.run();
//            if (isCancelled())return null;
//
//            if (dataSuccess){
//                String responseData = (String)ws.getResponseObject();
//
//                if(BuildConfig.DEBUG) { //otherwise wasted processing on json pretty printing on production
//                    try {
//                        LogDbug.log(WebService.DEBUG_TAG,
//                                "Webservice object: " + this.toString() +
//                                        "\nResponse url: " + request.url +
//                                        "\nResponse code: " + ws.getResponseCode() +
//                                        "\nResponse data:\n" + new JSONObject(responseData).toString(2));
//                    } catch (Exception e) {
//                        LogDbug.log(WebService.DEBUG_TAG,
//                                "Webservice object: " + this.toString() +
//                                        "\nResponse url: " + request.url +
//                                        "\nResponse code: " + ws.getResponseCode() +
//                                        "\nResponse data: " + responseData);
//                    }
//                }
//
//                dataSuccess = processResponseData(responseData);
//            }else {
//
//                S1Error error = (S1Error)ws.getResponseObject();
//
//                if(BuildConfig.DEBUG) {
//
//                    String responseCode = "" + ws.getResponseCode();
//
//                    LogDbug.log(WebService.DEBUG_TAG,
//                            "Webservice object: " + this.toString() +
//                                    "\nResponse url: " + request.url +
//                                    "\nResponse code: " + responseCode +
//                                    "\nResponse error code: " + error.getErrorCode() +
//                                    "\nResponse error desc: " + error.getErrorDescritpion());
//                }
//
//                processResponseFailure(ws.getResponseObject());
//            }
//        }
//        catch (Exception e)
//        {
//            LogDbug.log(WebService.DEBUG_TAG,
//                    "Webservice object: " + this.toString() +
//                            "\nResponse url: " + request.url +
//                            "Request exception: " + e.getMessage(), e);
//            setResultDataObject(S1Error.generalOperationError());
//            processResponseFailure(S1Error.generalOperationError());
//            dataSuccess = false;
//        }
//        // possibly return the web service data from here?
//
//        return null;
//    }
//
//    @Override
//    protected void onPostExecute(Void result)
//    {
//        if (dataListener != null)
//        {
//            dataListener.onPostData(this);
//        }
//
//        if (eventListener != null && eventListener.get() != null)
//            eventListener.get().onWebServiceHandlerPostExecute(this.hashCode());
//    }
//
//    @Override
//    protected void onPreExecute()
//    {
//        // notify WS starting
//        if (dataListener != null)
//        {
//            dataListener.onPreDataExecute(this);
//        }
//    }
//
//    public String getErrorCode(){//
//        return (resultDataObject != null && (resultDataObject instanceof S1Error))?((S1Error)resultDataObject).getErrorCode():"";
//    }
//
//    public String getErrorMessageDescription(){//
//        return (resultDataObject != null && (resultDataObject instanceof S1Error))?((S1Error)resultDataObject).getErrorDescritpion():"";
//    }
//
//    protected void processResponseFailure(Object error)
//    {
//        setResultDataObject(error);
//    }
//
//    public boolean isSuccess()
//    {
//        return dataSuccess;
//    }
//
//    public Object getResultDataObject() {
//        return resultDataObject;
//    }
//
//    public void setResultDataObject(Object resultDataObject) {
//        this.resultDataObject = resultDataObject;
//    }
//
//    public void setDefaultErrorObjectFromJSON(JSONObject jsonObject) throws JSONException{
//        if (jsonObject.has(WebServiceConstants.Response.errorCode)){
//            String errorCode = jsonObject.getString(WebServiceConstants.Response.errorCode);
//            S1Error error = S1Error.errorFrom(errorCode, jsonObject.getString(WebServiceConstants.Response.errorDescription));
//            setResultDataObject(error);
//        }
//    }
//
//    public DataTag getDataHandlerTag() {
//        return dataHandlerTag;
//    }
//
//    public void setDataHandlerTag(DataTag dataHandlerTag) {
//        this.dataHandlerTag = dataHandlerTag;
//    }
//
//    public WebServiceDataHandler getDataListener() {
//        return dataListener;
//    }
//
//    public void setDataListener(WebServiceDataHandler dataListener) {
//        this.dataListener = dataListener;
//    }
//
//    protected abstract boolean processResponseData(String data);
//}
//
