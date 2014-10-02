package ca.dealsaccess.facejoy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ca.dealsaccess.facejoy.common.AppConstants;

import com.example.facejoy.R;
import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;
import android.provider.MediaStore.Images.ImageColumns;

public class FaceDetailActivity extends ActionBarActivity {

	private Bitmap img;
	private int screenWidth;
	private int screenHeight;
	private ImageView imageView;
	private HttpRequests httpRequests;
	private JSONObject rst;
	private String faceId;
	private String imgPath;
	private TextView genderTextView;
	private TextView ageTextView;
	private TextView raceTextView;
	private TextView smilingTextView;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.face_detail);

	    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	    // If your minSdkVersion is 11 or higher, instead use:
	    // getActionBar().setDisplayHomeAsUpEnabled(true);
	    
	    // 获取屏幕大小
 		DisplayMetrics dm = new DisplayMetrics();
 		// 取得窗口属性
 		getWindowManager().getDefaultDisplay().getMetrics(dm);
 		// 窗口的宽度
 		screenWidth = dm.widthPixels;
 		// 窗口高度
 		screenHeight = dm.heightPixels;
	    
	    
	    Intent intent = getIntent();
	    String title = intent.getStringExtra(AppConstants.EXTRA_MESSAGE);
	    faceId = intent.getStringExtra(AppConstants.FACE_ID);
	    imgPath = intent.getStringExtra(AppConstants.FACE_IMG_PATH);
	    Toast.makeText(this, "Selected Item: " + title+", faceId="+faceId+", imgPath="+imgPath, Toast.LENGTH_SHORT).show();
	    
	    
		// just read size
		Options options = new Options();
		options.inJustDecodeBounds = true;
		img = BitmapFactory.decodeFile(imgPath, options);

		// scale size to read
		options.inSampleSize = Math.max(1, (int) Math.ceil(Math.max((double) options.outWidth
				/ screenWidth, (double) options.outHeight / screenHeight)));
		options.inJustDecodeBounds = false;
		img = BitmapFactory.decodeFile(imgPath, options);
		imageView = (ImageView) this.findViewById(R.id.face_detail_image);
		imageView.setImageBitmap(img);
		
		genderTextView = ((TextView) FaceDetailActivity.this.findViewById(R.id.gender_text));
		ageTextView = ((TextView) FaceDetailActivity.this.findViewById(R.id.age_text));
		raceTextView = ((TextView) FaceDetailActivity.this.findViewById(R.id.race_text));
		smilingTextView = ((TextView) FaceDetailActivity.this.findViewById(R.id.smiling_text));
		
		new Thread(new Runnable() {
			public void run() {
				httpRequests = new HttpRequests("3807aeb4a0b911495fdf0c946d006251",
						"DQOkhWeHweMMItMjaIGwqG0Nns8JNj1E", true, false);
				try {
					rst = httpRequests.infoGetFace(new PostParameters().setFaceId(faceId));
					JSONObject attribute = rst.getJSONArray("face_info").getJSONObject(0).getJSONObject("attribute");
					final String gender = attribute.getJSONObject("gender").getString("value");
					final String genderPossibility = attribute.getJSONObject("gender").getString("confidence");
					final String age = attribute.getJSONObject("age").getString("value");
					final String ageRange = attribute.getJSONObject("age").getString("range");
					final String race = attribute.getJSONObject("race").getString("value");
					final String racePossibility = attribute.getJSONObject("race").getString("confidence");
					final String smiling = attribute.getJSONObject("smiling").getString("value");
					
					FaceDetailActivity.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							genderTextView.setText("性别: "+gender+"\t"+"可能性: "+genderPossibility);
							ageTextView.setText("年龄: "+age+"\t"+"误差区间: "+ageRange);
							raceTextView.setText("种族: "+race+"\t"+"可能性: "+racePossibility);
							smilingTextView.setText("微笑度: "+smiling);
						}
					});
					
					
					
					
					
				} catch (final FaceppParseException e) {
					FaceDetailActivity.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(FaceDetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT)
									.show();
						}
					});
					return;
				} catch (final JSONException e) {
					FaceDetailActivity.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(FaceDetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT)
									.show();
						}
					});
				}
			}
		}).start();
	    
	}

}
