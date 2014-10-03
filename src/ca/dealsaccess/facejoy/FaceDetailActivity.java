package ca.dealsaccess.facejoy;

import org.json.JSONException;
import org.json.JSONObject;

import ca.dealsaccess.facejoy.common.AppConstants;
import ca.dealsaccess.util.FacecppUtils;

import com.example.facejoy.R;
import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
	    
	    // 获取屏幕大小
 		DisplayMetrics dm = new DisplayMetrics();
 		getWindowManager().getDefaultDisplay().getMetrics(dm);
 		screenWidth = dm.widthPixels;
 		screenHeight = dm.heightPixels;
	    
	    Intent intent = getIntent();
	    //获取face id以及face图像的路径
	    faceId = intent.getStringExtra(AppConstants.FACE_ID);
	    imgPath = intent.getStringExtra(AppConstants.FACE_IMG_PATH);

	    //设置ImageView的bitmap
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
		
		//读取脸谱信息
		new Thread(new Runnable() {
			public void run() {
				httpRequests = FacecppUtils.getRequests();
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
					return;
				}
			}
		}).start();
	}
}
