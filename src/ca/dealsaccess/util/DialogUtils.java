package ca.dealsaccess.util;

import android.app.ProgressDialog;

public class DialogUtils {
	
	public static void startLoadingAnimation(ProgressDialog processDialog) {
		startLoadingAnimation(processDialog, "Loading...");
	}
	
	public static void finishLoadingAnimation(ProgressDialog processDialog) {
		processDialog.dismiss();
	}

	public static void startLoadingAnimation(ProgressDialog processDialog, String msg) {
		processDialog.setMessage(msg);
	    processDialog.setCancelable(false);
	    processDialog.show();
	}
	
}
