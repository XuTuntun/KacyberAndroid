package com.kacyber.network.http;

import com.loopj.android.http.RequestParams;

public class AndroidRequestParams extends RequestParams{
	
	public String getString(String key) {
		String value = null;
		//value = getParamsList();
		value = urlParams.get(key);
		return value;
	}
	
}
