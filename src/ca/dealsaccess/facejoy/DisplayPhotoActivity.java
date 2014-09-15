package ca.dealsaccess.facejoy;

import java.io.ByteArrayOutputStream;

import org.json.JSONException;
import org.json.JSONObject;

import ca.dealsaccess.facejoy.common.AppConstants;
import ca.dealsaccess.facejoy.image.ImageManager;
import ca.dealsaccess.util.ImageUtils;

import com.example.facejoy.R;
import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.BitmapFactory.Options;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
import android.provider.MediaStore.Images.Thumbnails;
import android.support.v7.app.ActionBarActivity;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayPhotoActivity extends ActionBarActivity {

	private ImageView imageView = null;
	
	private TextView textView = null;
	
	private Button buttonDetect = null;
	
	private Bitmap img = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_display_photo);

	    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	    // If your minSdkVersion is 11 or higher, instead use:
	    // getActionBar().setDisplayHomeAsUpEnabled(true);
	    
	    
	    Intent intent = getIntent();
	    String title = intent.getStringExtra(AppConstants.EXTRA_MESSAGE);
	    String path = intent.getStringExtra(AppConstants.CLICK_PATH);
	    Toast.makeText(this, "Selected Item: " + title+ ", path="+path, Toast.LENGTH_SHORT).show();
	    
	    
/*	    Cursor cursor = getContentResolver().query(intent.getData(), null, null, null, null);
		cursor.moveToFirst();
	    
	    int idx = cursor.getColumnIndex(ImageColumns.DATA);
		String fileSrc = cursor.getString(idx); 
		//Log.d(TAG, "Picture:" + fileSrc);
		
		//just read size
		Options options = new Options();
		options.inJustDecodeBounds = true;
		img = BitmapFactory.decodeFile(fileSrc, options);

		//scale size to read
		options.inSampleSize = Math.max(1, (int)Math.ceil(Math.max((double)options.outWidth / 1024f, (double)options.outHeight / 1024f)));
		options.inJustDecodeBounds = false;
		img = BitmapFactory.decodeFile(fileSrc, options);
*/	    
	    
	    
	    
	    //Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI.buildUpon().appendPath(Long.toString(id)).build();
	    //img = ImageUtils.getBitmapFromUri(uri, this);
	    
	      // First decode with inJustDecodeBounds=true to check dimensions
        /*final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        img = Thumbnails.getThumbnail(this.getContentResolver(), id, Thumbnails.MINI_KIND, options);
        // Calculate inSampleSize
        options.inSampleSize = Math.max(1, (int)Math.ceil(Math.max((double)options.outWidth / 1024f, (double)options.outHeight / 1024f)));
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        img = Thumbnails.getThumbnail(this.getContentResolver(), id, Thumbnails.MINI_KIND, options);
	    
	    
	    imageView = (ImageView)this.findViewById(R.id.photo_imageView);
	    //imageView.setImageURI(uri);
	    imageView.setImageBitmap(img);*/
	    
	    
	    
	    imageView = (ImageView)this.findViewById(R.id.photo_imageView);
	  
	    //just read size
	  	Options options = new Options();
	  	options.inJustDecodeBounds = true;
	  	img = BitmapFactory.decodeFile(path, options);

	  	Display display = getWindowManager().getDefaultDisplay();
	  	//Point size = new Point();
	  	//display.getSize(size);
	  	//int width = size.x;
	  	//int height = size.y;
	  	int width = display.getWidth();
	  	int height = display.getHeight();
	  	//System.out.println("width="+width);
	  	//System.out.println("height="+height);
	  	//System.out.println("image_width="+options.outWidth);
	  	//System.out.println("image_height="+options.outHeight);
	  	//System.out.println("inSampleSize="+(int)Math.ceil(Math.max((double)options.outWidth / width, (double)options.outHeight / width)));
  		//scale size to read
  		//options.inSampleSize = Math.max(1, (int)Math.ceil(Math.max((double)options.outWidth / 1024f, (double)options.outHeight / 1024f)));
	  	options.inSampleSize = Math.max(1, (int)Math.ceil(Math.max((double)options.outWidth / width, (double)options.outHeight / height)));
  		options.inJustDecodeBounds = false;
  		img = BitmapFactory.decodeFile(path, options);

	    
  		imageView.setImageBitmap(img);
	    

	    
	    
	    
	    textView = (TextView)this.findViewById(R.id.photo_textView);
	    
	    buttonDetect = (Button)this.findViewById(R.id.button_detect);
	    /*buttonDetect.setVisibility(View.INVISIBLE);
	    
	    
	    while(true) {
	    	if(!imageManager.ismImageLoaderIdle()) {
	    		try {
					Thread.sleep(100);
				} catch (InterruptedException e) {}
	    	} else {
	    		onPhotoLoadFinish();
	    		break;
	    	}
	    	
	    }*/
	    
	    
	    
	    
	    
	    
	    buttonDetect.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				textView.setText("Waiting ...");
				
				FaceppDetect faceppDetect = new FaceppDetect();
				faceppDetect.setDetectCallback(new DetectCallback() {
					
					public void detectResult(JSONObject rst) {
						//Log.v(TAG, rst.toString());
						
						//use the red paint
						Paint paint = new Paint();
						paint.setColor(Color.RED);
						paint.setStrokeWidth(Math.max(img.getWidth(), img.getHeight()) / 100f);

						//create a new canvas
						Bitmap bitmap = Bitmap.createBitmap(img.getWidth(), img.getHeight(), img.getConfig());
						Canvas canvas = new Canvas(bitmap);
						canvas.drawBitmap(img, new Matrix(), null);
						
						
						try {
							//find out all faces
							final int count = rst.getJSONArray("face").length();
							for (int i = 0; i < count; ++i) {
								float x, y, w, h;
								//get the center point
								x = (float)rst.getJSONArray("face").getJSONObject(i)
										.getJSONObject("position").getJSONObject("center").getDouble("x");
								y = (float)rst.getJSONArray("face").getJSONObject(i)
										.getJSONObject("position").getJSONObject("center").getDouble("y");

								//get face size
								w = (float)rst.getJSONArray("face").getJSONObject(i)
										.getJSONObject("position").getDouble("width");
								h = (float)rst.getJSONArray("face").getJSONObject(i)
										.getJSONObject("position").getDouble("height");
								
								//change percent value to the real size
								x = x / 100 * img.getWidth();
								w = w / 100 * img.getWidth() * 0.7f;
								y = y / 100 * img.getHeight();
								h = h / 100 * img.getHeight() * 0.7f;

								//draw the box to mark it out
								canvas.drawLine(x - w, y - h, x - w, y + h, paint);
								canvas.drawLine(x - w, y - h, x + w, y - h, paint);
								canvas.drawLine(x + w, y + h, x - w, y + h, paint);
								canvas.drawLine(x + w, y + h, x + w, y - h, paint);
							}
							
							//save new image
							img = bitmap;

							DisplayPhotoActivity.this.runOnUiThread(new Runnable() {
								
								public void run() {
									//show the image
									imageView.setImageBitmap(img);
									textView.setText("Finished, "+ count + " faces.");
								}
							});
							
						} catch (JSONException e) {
							e.printStackTrace();
							DisplayPhotoActivity.this.runOnUiThread(new Runnable() {
								public void run() {
									textView.setText("Error.");
								}
							});
						}
						
					}
				});
				faceppDetect.detect(img);
			}
		});
	}
	
	
	
	
	
	
	
	
	
	private void onPhotoLoadFinish() {
		buttonDetect.setVisibility(View.VISIBLE);
	
		BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
        //bitmapDrawable.setCallback(null);
        img = bitmapDrawable.getBitmap();
		
		
		
		
		
		
		
		
		
		
	}









	private class FaceppDetect {
    	DetectCallback callback = null;
    	
    	public void setDetectCallback(DetectCallback detectCallback) { 
    		callback = detectCallback;
    	}

    	public void detect(final Bitmap image) {
    		
    		new Thread(new Runnable() {
				
				public void run() {
					HttpRequests httpRequests = new HttpRequests("4480afa9b8b364e30ba03819f3e9eff5", "Pz9VFT8AP3g_Pz8_dz84cRY_bz8_Pz8M", true, false);
		    		//Log.v(TAG, "image size : " + img.getWidth() + " " + img.getHeight());
		    		
		    		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		    		float scale = Math.min(1, Math.min(600f / img.getWidth(), 600f / img.getHeight()));
		    		Matrix matrix = new Matrix();
		    		matrix.postScale(scale, scale);

		    		Bitmap imgSmall = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, false);
		    		//Log.v(TAG, "imgSmall size : " + imgSmall.getWidth() + " " + imgSmall.getHeight());
		    		
		    		imgSmall.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		    		byte[] array = stream.toByteArray();
		    		
		    		try {
		    			//detect
						JSONObject result = httpRequests.detectionDetect(new PostParameters().setImg(array));
						//finished , then call the callback function
						if (callback != null) {
							callback.detectResult(result);
						}
					} catch (FaceppParseException e) {
						e.printStackTrace();
						DisplayPhotoActivity.this.runOnUiThread(new Runnable() {
							public void run() {
								textView.setText("Network error.");
							}
						});
					}
					
				}
			}).start();
    	}
    }

    interface DetectCallback {
    	void detectResult(JSONObject rst);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
