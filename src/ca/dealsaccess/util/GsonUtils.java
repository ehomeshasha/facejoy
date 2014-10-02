package ca.dealsaccess.util;

import com.google.gson.Gson;

public class GsonUtils {

	private final static Gson gson = new Gson();
	
	public static void print(Object obj) {
		System.out.println(gson.toJson(obj));
	}
	
	public static void print(String objName, Object obj) {
		System.out.printf("%s: %s", objName, gson.toJson(obj));
		System.out.println();
	}
	
	public static String toJson(Object obj) {
		return gson.toJson(obj);
	}
}
