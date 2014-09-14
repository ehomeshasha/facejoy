package ca.dealsaccess.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

public class ImageUtils {
	public static Bitmap getBitmapFromUri(Uri uri, Activity activity) {
		try	{
			// 读取uri所在的图片
			Bitmap bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
			return bitmap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
	  }
	 }
}
