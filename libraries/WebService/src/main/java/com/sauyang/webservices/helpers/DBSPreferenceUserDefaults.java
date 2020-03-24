package com.sauyang.webservices.helpers;

//This file is from Survey5 project I created last time :p2ms

import android.content.Context;
import android.content.SharedPreferences;
import java.util.Set;

import static com.sauyang.webservices.helpers.AndroidApplication.getInstance;

public class DBSPreferenceUserDefaults {
	
	public static final String PREFERENCE_STORE_KEY = "s1user-defaults";
	
	public static String getPreference(Context context, String name){
		return getPreference(context, name,null);
	}
	public static String getPreference(Context context, String name, String defValue){
		SharedPreferences pref = context.getSharedPreferences(PREFERENCE_STORE_KEY, Context.MODE_PRIVATE);
		return pref.getString(name, defValue);
	}

    public static String getPreference(String name){
        return getPreference(AndroidApplication.getInstance().getApplicationContext(), name);
    }

    public static Set<String> getPreferenceStringSet(String name, Set defaultValue){
        SharedPreferences pref = AndroidApplication.getInstance().getApplicationContext().getSharedPreferences(PREFERENCE_STORE_KEY, Context.MODE_PRIVATE);
        return pref.getStringSet(name, defaultValue);
    }

    public static void writePreferenceStringSet(String name, Set<String> values){
        SharedPreferences pref = AndroidApplication.getInstance().getApplicationContext().getSharedPreferences(PREFERENCE_STORE_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(name);
        editor.commit();
        editor.putStringSet(name, values);
        editor.commit();
    }

    public static void writePreference(String name, String value){
        writePreference(AndroidApplication.getInstance().getApplicationContext(), name,value);
    }
	
	public static void writePreference(Context context, String name, String value) {
		SharedPreferences pref = context.getSharedPreferences(PREFERENCE_STORE_KEY, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(name, value);
		editor.commit();
	}
	
	public static void removePreference(Context context, String name) {
		SharedPreferences pref = context.getSharedPreferences(PREFERENCE_STORE_KEY, Context.MODE_PRIVATE);
		if (pref.contains(name)){
			SharedPreferences.Editor editor = pref.edit();
			editor.remove(name);
			editor.commit();
		}
	}

    public static void removePreference(String name){
        removePreference(AndroidApplication.getInstance().getApplicationContext(), name);
    }
	
	public static void removePreferences(Context context, String...keyNames){
		SharedPreferences pref = context.getSharedPreferences(PREFERENCE_STORE_KEY, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		for(String keyName : keyNames){
			if (pref.contains(keyName)){
				editor.remove(keyName);
			}
		}
		editor.commit();
	}

    public static void removePreferences(String...keyNames){
        removePreferences(getInstance().getApplicationContext(), keyNames);
    }

}
