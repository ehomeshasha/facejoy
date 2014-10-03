package ca.dealsaccess.facejoy;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import ca.dealsaccess.facejoy.common.AppConstants;
import ca.dealsaccess.facejoy.image.ImageManager;
import ca.dealsaccess.util.ImageUtils;
import ca.dealsaccess.util.StringUtils;

import com.example.facejoy.R;
import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import android.view.LayoutInflater;
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
	
	private View detectResultView = null; 
	
	//private AlertDialog.Builder builder = null;
	
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
				
				
				
				
				//textView.setText("Waiting ...");
				
				FaceppDetect faceppDetect = new FaceppDetect();
				faceppDetect.setDetectCallback(new DetectCallback() {
					
					StringBuilder sb = new StringBuilder();
					List<String> faceList = new ArrayList<String>();
					List<String> faceIdList = new ArrayList<String>();
					List<String> facecheckedList = new ArrayList<String>();
					
					public void detectResult(JSONObject rst) {
						
						
						//System.out.println("result="+rst.toString());
						
						
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
							//sb.append("Finished, "+ count + " faces.\n");
							
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
								JSONObject attribute = rst.getJSONArray("face").getJSONObject(i).getJSONObject("attribute");
								//int age = attribute.getJSONObject("age").getInt("value");
								//int minAge = age - attribute.getJSONObject("age").getInt("range");
								//int maxAge = age + attribute.getJSONObject("age").getInt("range");
								
								//sb.append("FaceID: ").append(rst.getJSONArray("face").getJSONObject(i).getString("face_id"))
								//sb.setLength(0);
								faceIdList.add(rst.getJSONArray("face").getJSONObject(i).getString("face_id"));
								sb.append("face"+i).append("\n")
									.append("race: ").append(attribute.getJSONObject("race").getString("value"))
									.append(", confidence: ").append(attribute.getJSONObject("race").getDouble("confidence")).append("\n")
									.append("gender: ").append(attribute.getJSONObject("gender").getString("value"))
									.append(", confidence: ").append(attribute.getJSONObject("gender").getDouble("confidence")).append("\n")
									.append("smiling: ").append(attribute.getJSONObject("smiling").getDouble("value")).append("\n")
									.append("age: ").append(attribute.getJSONObject("age").getInt("value"))
									.append(", range: ").append(attribute.getJSONObject("age").getInt("range")).append("\n\n");
									
								faceList.add("face"+i);
								
							}
							
							//save new image
							img = bitmap;

							DisplayPhotoActivity.this.runOnUiThread(new Runnable() {
								
								public void run() {
									//show the image
									imageView.setImageBitmap(img);
									textView.setText(sb.toString());
								}
							});
							
							
							
							DisplayPhotoActivity.this.runOnUiThread(new Runnable() {
								public void run() {
									
									Dialog dialog;
									AlertDialog.Builder builder = new AlertDialog.Builder(DisplayPhotoActivity.this);
									builder.setTitle("Finished, "+ count + " faces.\n")
										.setView(detectResultView)
										.setMultiChoiceItems(faceList.toArray(new String[0]), null, new DialogInterface.OnMultiChoiceClickListener() {
										     public void onClick(DialogInterface dialog, int whichButton,boolean isChecked) {
										    	 if(isChecked) {
										    		 facecheckedList.add(faceIdList.get(whichButton));
										    	 } else {
										    		 facecheckedList.remove(faceIdList.get(whichButton));
										    	 }
										     }
										})
										.setPositiveButton("创建人物", new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface dialog, int which) {
												dialog.dismiss();
												String checkedListStr = StringUtils.toStringList(facecheckedList);
												Intent intent = new Intent(DisplayPhotoActivity.this, CreatePersonActivity.class);
												intent.putExtra(AppConstants.EXTRA_MESSAGE, "openAddPersonActivity");
												intent.putExtra(AppConstants.FACE_CHECKED_LIST, checkedListStr);
												startActivityForResult(intent, RESULT_OK);
											}
										})
										.setNegativeButton("添加到已有人物", new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface dialog, int which) {
												dialog.dismiss();
												String checkedListStr = StringUtils.toStringList(facecheckedList);
												Intent intent = new Intent(DisplayPhotoActivity.this, UseExistPersonActivity.class);
												intent.putExtra(AppConstants.EXTRA_MESSAGE, "openUseExistPersonActivity");
												intent.putExtra(AppConstants.FACE_CHECKED_LIST, checkedListStr);
												startActivityForResult(intent, RESULT_OK);
											}
										})
										.setOnCancelListener(new DialogInterface.OnCancelListener() {
											public void onCancel(DialogInterface dialog) {
												dialog.dismiss();
											}
										});
									dialog = builder.create();
									dialog.show();
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
				
				

				
				detectResultView = LayoutInflater.from(DisplayPhotoActivity.this).inflate(R.layout.detect_result_dialog, null);
				textView = (TextView) detectResultView.findViewById(R.id.detect_result_text);
				
				
				faceppDetect.detect(img);
			}
		});
	}
	
	
	
	
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       // TODO Auto-generated method stub
       switch (resultCode) {
       case RESULT_OK:
           
           break;
       default:
           break;
       }
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
					HttpRequests httpRequests = new HttpRequests("3807aeb4a0b911495fdf0c946d006251", "DQOkhWeHweMMItMjaIGwqG0Nns8JNj1E", true, false);
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
								//textView.setText("Network error.");
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
