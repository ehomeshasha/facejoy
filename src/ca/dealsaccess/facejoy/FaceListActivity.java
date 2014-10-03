package ca.dealsaccess.facejoy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ca.dealsaccess.facejoy.common.AppConstants;
import ca.dealsaccess.facejoy.image.ImageAdapter;
import ca.dealsaccess.util.GsonUtils;

import com.example.facejoy.R;
import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.os.Build;

public class FaceListActivity extends ActionBarActivity {

	private String personId;
	private String personName;
	private HttpRequests httpRequests;
	private JSONObject rst;
	private ArrayList<String> faceIdList = new ArrayList<String>();
	private ArrayList<String> faceImgPathList = new ArrayList<String>();
	private GridView gridView;
	private ImageAdapter imageAdapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.face_grid);
		// setContentView(R.layout.activity_display_message);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		// If your minSdkVersion is 11 or higher, instead use:
		// getActionBar().setDisplayHomeAsUpEnabled(true);

		Intent intent = getIntent();
		String title = intent.getStringExtra(AppConstants.EXTRA_MESSAGE);
		personId = intent.getStringExtra(AppConstants.PERSON_ID);
		personName = intent.getStringExtra(AppConstants.PERSON_NAME);
		
		Toast.makeText(this,
				"Selected Item: " + title + ", personId=" + personId + ", personName=" + personName,
				Toast.LENGTH_SHORT).show();
		
		
		
		

		new Thread(new Runnable() {
			public void run() {
				httpRequests = new HttpRequests("3807aeb4a0b911495fdf0c946d006251",
						"DQOkhWeHweMMItMjaIGwqG0Nns8JNj1E", true, false);
				try {
					rst = httpRequests.personGetInfo(new PostParameters().setPersonId(personId));
					JSONArray faces = rst.getJSONArray("face");
					for (int i = 0; i < faces.length(); i++) {
						String faceId = faces.getJSONObject(i).getString("face_id");
						faceIdList.add(faceId);
					}
					JSONObject rst2 = httpRequests.infoGetFace(new PostParameters().setFaceId(faceIdList));
					JSONArray faceInfoList = rst2.getJSONArray("face_info");
					for (int j = 0; j < faceInfoList.length(); j++) {
						String imgPath = faceInfoList.getJSONObject(j).getString("tag");
						faceImgPathList.add(imgPath);
					}
				} catch (final FaceppParseException e) {
					FaceListActivity.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(FaceListActivity.this, e.getMessage(), Toast.LENGTH_SHORT)
									.show();
						}
					});
					return;
				} catch (final JSONException e) {
					FaceListActivity.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(FaceListActivity.this, e.getMessage(), Toast.LENGTH_SHORT)
									.show();
						}
					});
					return;
				}
				FaceListActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						gridView = (GridView) findViewById(R.id.face_gridview);
						imageAdapter = new ImageAdapter(FaceListActivity.this, faceImgPathList, 200, 200);
						gridView.setAdapter(imageAdapter);
						gridView.setOnItemClickListener(new OnItemClickListener() { 
				            public void onItemClick(AdapterView<?> parent, View v, int position, long id) { 
				            	Intent intent = new Intent(FaceListActivity.this, FaceDetailActivity.class);
				            	intent.putExtra(AppConstants.EXTRA_MESSAGE, "openDisplayPhotoActivity");
				            	intent.putExtra(AppConstants.FACE_ID, faceIdList.get(position))
				            		.putExtra(AppConstants.FACE_IMG_PATH, faceImgPathList.get(position));
				        		startActivity(intent);
				            }
						});

					}
				});

			}
		}).start();

	}

//	public final class ViewHolder {
//		public ImageView img;
//		public TextView name;
//	}
//	
//	public class FaceAdapter extends BaseAdapter {
//
//		private LayoutInflater mInflater;
//
//		public FaceAdapter(Context context) {
//			this.mInflater = LayoutInflater.from(context);
//		}
//
//		@Override
//		public int getCount() {
//			return personListItem.size();
//		}
//
//		@Override
//		public Object getItem(int i) {
//			return personListItem.get(i);
//		}
//
//		@Override
//		public long getItemId(int arg0) {
//			return 0;
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//
//			ViewHolder holder = null;
//			if (convertView == null) {
//
//				holder = new ViewHolder();
//
//				convertView = mInflater.inflate(R.layout.person_list_view, null);
//				// holder.img = (ImageView)
//				// convertView.findViewById(R.id.face_thumb);
//				holder.name = (TextView) convertView.findViewById(R.id.person_name);
//				// holder.addBtn =
//				// (Button)convertView.findViewById(R.id.add_button);
//				convertView.setTag(holder);
//
//			} else {
//
//				holder = (ViewHolder) convertView.getTag();
//			}
//
//			List<String> faces = (ArrayList<String>) persons.get(position).get("faces");
//
//			//
//			holder.img.setBackgroundResource((Integer) mData.get(position).get("img"));
//			holder.name.setText((String) persons.get(position).get("name"));
//
//			// holder.addBtn.setOnClickListener(new View.OnClickListener() {
//			//
//			// @Override
//			// public void onClick(View v) {
//			// showInfo();
//			// }
//			// });
//
//			return convertView;
//		}
//
//	}

}
