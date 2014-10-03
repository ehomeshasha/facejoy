package ca.dealsaccess.util;

import java.util.List;

public class StringUtils {
	public static String toStringList(String[] sa) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < sa.length; ++i) {
			if (i != 0) sb.append(',');
			sb.append(sa[i]);
		}
		
		return sb.toString();
	}
	
	public static String toStringList(List<String> sa) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < sa.size(); ++i) {
			if (i != 0) sb.append(',');
			sb.append(sa.get(i));
		}
		
		return sb.toString();
	}
}
