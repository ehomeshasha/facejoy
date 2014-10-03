package ca.dealsaccess.facejoy;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ca.dealsaccess.facejoy.common.AppConstants;
import ca.dealsaccess.facejoy.image.ImageAdapter;
import ca.dealsaccess.util.CollectionUtils;
import ca.dealsaccess.util.DialogUtils;
import ca.dealsaccess.util.GsonUtils;

import com.example.facejoy.R;
import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;

import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class FaceListActivity extends ActionBarActivity {

	private String personId;
	private String personName;
	private HttpRequests httpRequests;
	private JSONObject rst;
	private ArrayList<String> faceIdList = new ArrayList<String>();
	private ArrayList<String> faceImgPathList = new ArrayList<String>();
	private GridView gridView;
	private ImageAdapter imageAdapter;
	private ArrayList<Integer> positionDel = new ArrayList<Integer>();
	private ArrayList<String> faceIdDel = new ArrayList<String>();
	private MenuItem gobackItem;
	private MenuItem deleteItem;
	private ProgressDialog processDialog;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.face_grid);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		processDialog = new ProgressDialog(this);

		Intent intent = getIntent();
		String title = intent.getStringExtra(AppConstants.EXTRA_MESSAGE);
		personId = intent.getStringExtra(AppConstants.PERSON_ID);
		personName = intent.getStringExtra(AppConstants.PERSON_NAME);
		
		getSupportActionBar().setTitle(personName+"的脸谱");
		
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
						initGridView();
					}
				});
			}
		}).start();

	}
	
	private void initGridView() {
		faceIdDel.clear();
		positionDel.clear();
		gridView = (GridView) findViewById(R.id.face_gridview);
		imageAdapter = new ImageAdapter(FaceListActivity.this, faceImgPathList,R.layout.face_image, 200, 200);
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
	
	private void setCheckBoxVisiblity(int visibility) {
		for(int i=0;i<gridView.getChildCount();i++) {
			CheckBox checkBox = (CheckBox) gridView.getChildAt(i).findViewById(R.id.image_body_checkbox);
			checkBox.setVisibility(visibility);
			final int index = i;
			checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if(isChecked) {
						positionDel.add(Integer.valueOf(index));
						faceIdDel.add(faceIdList.get(index));
					} else {
						positionDel.remove(Integer.valueOf(index));
						faceIdDel.remove(faceIdList.get(index));
					}
					Toast.makeText(FaceListActivity.this, GsonUtils.toJson(positionDel), Toast.LENGTH_SHORT).show();
				}
			});
		}
	}

	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.delete_menu, menu);
		gobackItem = menu.findItem(R.id.goback);
		deleteItem = menu.findItem(R.id.delete);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.delete:
			faceDelete(item);
			return true;
		case R.id.goback:
			goBack();
			return true;
		default:
		return super.onOptionsItemSelected(item);
		}
	}
	
	private void goBack() {
		deleteItem.setTitle("删除");
		gobackItem.setVisible(false);
		initGridView();
		setCheckBoxVisiblity(View.INVISIBLE);
	}

	



	private void faceDelete(MenuItem item) {
		if(item.getTitle().equals("删除")) {
			if(faceImgPathList.size() == 0) {
				return;
			}
			item.setTitle("删除选中的人脸");
			gobackItem.setVisible(true);
			setCheckBoxVisiblity(View.VISIBLE);
		} else if(item.getTitle().equals("删除选中的人脸")) {
			if(faceIdDel.size() == 0) {
				Toast.makeText(FaceListActivity.this, "请先选择要删除的人脸", Toast.LENGTH_SHORT).show();
				return;
			}
			AlertDialog.Builder builder = new AlertDialog.Builder(
					FaceListActivity.this);
			
			builder.setMessage("确认删除选中的人脸？")
				.setTitle("提示")
				.setPositiveButton("确认", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(final DialogInterface dialog, int which) {
						if(faceIdDel.size() == 0) return;
						DialogUtils.startLoadingAnimation(processDialog, "删除中，请稍侯...");
						new Thread(new Runnable() {
							public void run() {
								try {
									JSONObject rst = httpRequests.personRemoveFace(new PostParameters().setPersonId(personId)
											.setFaceId(faceIdDel));
									final boolean success = rst.getBoolean("success");
									FaceListActivity.this.runOnUiThread(new Runnable() {
										@Override
										public void run() {
											if(success) {
												Toast.makeText(FaceListActivity.this, "您已成功删除人脸", Toast.LENGTH_SHORT).show();
												CollectionUtils.removeElementByPosition(faceImgPathList, positionDel);
												CollectionUtils.removeElementByPosition(faceIdList, positionDel);
											} else {
												Toast.makeText(FaceListActivity.this, "删除人脸失败", Toast.LENGTH_SHORT).show();
											}
											DialogUtils.finishLoadingAnimation(processDialog);
											goBack();
										}
									});
								} catch (final FaceppParseException e) {
									FaceListActivity.this.runOnUiThread(new Runnable() {
										@Override
										public void run() {
											Toast.makeText(FaceListActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
										}
									});
								} catch (final JSONException e) {
									FaceListActivity.this.runOnUiThread(new Runnable() {
										@Override
										public void run() {
											Toast.makeText(FaceListActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
										}
									});
								}
							}
						}).start();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
			builder.create().show();
		}
	}
}
