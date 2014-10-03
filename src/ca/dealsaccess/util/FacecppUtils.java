package ca.dealsaccess.util;

import ca.dealsaccess.facejoy.common.AppConstants;

import com.facepp.http.HttpRequests;

public class FacecppUtils {

	public static HttpRequests getRequests() {
		return new HttpRequests(AppConstants.API_KEY,	AppConstants.API_SECRET, true, false);
	}
}
