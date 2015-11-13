package com.cn.xtouch.fodelforuser.utils;
import java.lang.reflect.Type;

import android.util.Log;

import com.cn.xtouch.fodelforuser.http.Response.ResponseBean;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class JsonHelper {

	private static final JsonHelper jsonHelper = new JsonHelper();
	private String TAG = "gson";
	private Gson gson;

	//构造方法
	public JsonHelper() {
		gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	}

	public static JsonHelper getInstance() {
		return jsonHelper;
	}
	
	
	public ResponseBean<?> jsonToBean(String json, Type type) {
		ResponseBean<?> result = null;
		if (json != null) {
			try {
				result = gson.fromJson(json, type);
			} catch (Exception e) {
				Log.e(TAG, e.getMessage() + "");
			}
		}
		return result;
	}

	public Object jsonToBean(String json, Class<?> type){
		Object result = null;
		if(json != null){
			try {
				result = gson.fromJson(json, type);
			}catch(Exception e){
				Log.e(TAG,e.getMessage()+"");
			}
		}
		return result;
	}
	
	public String toJsonString(Object o){
		String jsonStr = gson.toJson(o);
		return jsonStr;
	}
}
