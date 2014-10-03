package ca.dealsaccess.util;

import java.lang.reflect.Field;

import android.app.ProgressDialog;
import android.content.DialogInterface;

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

	public static void DialogClosePermission(DialogInterface dialog, boolean permission) {
		try {
			Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
			field.setAccessible(true);
			field.set(dialog, permission);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
