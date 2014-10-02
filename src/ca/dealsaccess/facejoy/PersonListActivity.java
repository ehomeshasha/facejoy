package ca.dealsaccess.facejoy;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ca.dealsaccess.facejoy.common.AppConstants;
import ca.dealsaccess.facejoy.image.ImageAdapter;
import ca.dealsaccess.util.DeviceUtils;
import ca.dealsaccess.util.GsonUtils;

import com.example.facejoy.R;
import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.os.Build;

public class PersonListActivity extends ActionBarActivity {

	private ListView listView = null;
	private HttpRequests httpRequests;
	private String groupName;
	private JSONObject rst;
	private JSONArray personList;
	private List<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
	// private List<Map<String, String>> persons = new ArrayList<Map<String,
	// String>>();
	private List<String> personListItem = new ArrayList<String>();
	// private List<Map<String, Object>> persons = new ArrayList<Map<String,
	// Object>>();
	// private Map<String, JSONObject> faceInfo = new HashMap<String,
	// JSONObject>();
	private String checkedListStr = null;
	private String[] facecheckedList;

	// private List<String> faces = new ArrayList<String>();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.person_list);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		// If your minSdkVersion is 11 or higher, instead use:
		// getActionBar().setDisplayHomeAsUpEnabled(true);

		Intent intent = getIntent();
		String title = intent.getStringExtra(AppConstants.EXTRA_MESSAGE);
		checkedListStr = intent.getStringExtra(AppConstants.FACE_CHECKED_LIST);
		Toast.makeText(this, "Selected Item: " + title + ", checkedListStr=" + checkedListStr,
				Toast.LENGTH_SHORT).show();
		if (checkedListStr != null) {
			facecheckedList = checkedListStr.split(",");
		}

		initData();
		//listView = (ListView) this.findViewById(R.id.person_listView);
	}

	private void initData() {

		// List<Map<String, Object>> list = new ArrayList<Map<String,
		// Object>>();

		httpRequests = new HttpRequests("3807aeb4a0b911495fdf0c946d006251",
				"DQOkhWeHweMMItMjaIGwqG0Nns8JNj1E", true, false);
		groupName = DeviceUtils.getDeviceId(PersonListActivity.this);

		new Thread(new Runnable() {
			public void run() {
				// 首先以设备ID为名称创建组
				try {
					rst = httpRequests.groupGetInfo(new PostParameters().setGroupName(groupName));
					// GsonUtils.print(rst);
					try {
						personList = rst.getJSONArray("person");
					} catch (JSONException e) {
						return;
					}
					// String faceStringList = "";
					for (int i = 0; i < personList.length(); i++) {
						personListItem.add(personList.getJSONObject(i).getString("person_name"));
						// Map<String, Object> personInfo = new HashMap<String,
						// Object>();
						// Map<String, String> personInfo = new HashMap<String,
						// String>();
						// personInfo.put("name",
						// personList.getJSONObject(i).getString("person_name"));
						// String personId =
						// personList.getJSONObject(i).getString("person_id");
						// personInfo.put("person_id", personId);
						// JSONObject rst2 = httpRequests.personGetInfo(new
						// PostParameters()
						// .setPersonId(personId));
						// List<String> faces = new ArrayList<String>();
						// try {
						// JSONArray faceList = rst2.getJSONArray("face");
						// for (int j = 0; j < faceList.length(); j++) {
						// String faceId =
						// faceList.getJSONObject(i).getString("face_id");
						// faces.add(faceId);
						// if (j == 0) {
						// faceStringList += faceId;
						// } else {
						// faceStringList += "," + faceId;
						// }
						// }
						// } catch (JSONException e) {
						//
						// }
						// personInfo.put("faces", faces);
						// persons.add(personInfo);
					}

					// JSONObject rst3 = httpRequests.infoGetFace(new
					// PostParameters().setFaceId(faceStringList));
					// JSONArray faceInfoList = rst3.getJSONArray("face_info");
					// for (int k = 0; k < faceInfoList.length(); k++) {
					// String faceId =
					// faceInfoList.getJSONObject(k).getString("face_id");
					// faceInfo.put(faceId, faceInfoList.getJSONObject(k));
					// }

					// GsonUtils.print(persons);
					// GsonUtils.print(faceInfo);

				} catch (final FaceppParseException e) {
					PersonListActivity.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(PersonListActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
						}
					});
					return;
				} catch (final JSONException e) {
					PersonListActivity.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(PersonListActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
						}
					});
					return;
				}
				PersonListActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {

						// PersonAdapter personAdapter = new
						// PersonAdapter(PersonListActivity.this);
						// listView.setAdapter(personAdapter);
						//GsonUtils.print(personListItem);
						listView = new ListView(PersonListActivity.this);
						listView.setAdapter(new ArrayAdapter<String>(PersonListActivity.this,
								android.R.layout.simple_expandable_list_item_1, personListItem));
						setContentView(listView);
						
						listView.setOnItemClickListener(new OnItemClickListener() {

							private String personId;
							
							@Override
							public void onItemClick(AdapterView<?> parent, View view, final int position,
									long id) {
								try {
									personId = personList.getJSONObject(position).getString(
											"person_id");
								} catch (JSONException e) {
									Toast.makeText(PersonListActivity.this, e.getMessage(),
											Toast.LENGTH_SHORT).show();
									return;
								}
								if (checkedListStr != null) {
									// 添加face到这个person
									
									
									AlertDialog.Builder builder = new AlertDialog.Builder(
											PersonListActivity.this);
									builder.setMessage("确认添加人脸到该人物？");
									builder.setTitle("提示");
									builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
										@Override
										public void onClick(final DialogInterface dialog, int which) {
											
											new Thread(new Runnable() {
												public void run() {
													try {
														httpRequests.personAddFace(new PostParameters().setPersonId(
																personList.getJSONObject(position).getString(
																		"person_id")).setFaceId(facecheckedList));
														PersonListActivity.this.runOnUiThread(new Runnable() {
															@Override
															public void run() {
																Toast.makeText(PersonListActivity.this, "您已成功添加人脸",
																		Toast.LENGTH_SHORT).show();
																//打开人物的详情页
																openPersonDetailActivity(PersonListActivity.this, personId, personListItem.get(position));
															}
														});
													} catch (FaceppParseException | JSONException e) {
														PersonListActivity.this.runOnUiThread(new Runnable() {
															@Override
															public void run() {
																try {
																	Field field = dialog.getClass().getSuperclass()
																			.getDeclaredField("mShowing");
																	field.setAccessible(true);
																	field.set(dialog, false);// 禁止对话框关闭
																} catch (Exception e1) {
																	e1.printStackTrace();
																}
																Toast.makeText(PersonListActivity.this, e.getMessage(),
																		Toast.LENGTH_SHORT).show();
															}
														});
													}
												}
											}).start();
										}
									});
									builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {

										}
									});
									builder.create().show();

								} else {
									//打开人物的详情页
									openPersonDetailActivity(PersonListActivity.this, personId, personListItem.get(position));
								}

							}

						});

						// GsonUtils.print(persons);
						// GsonUtils.print(faceInfo);
					}
				});
			}
		}).start();
	}

	public static void openPersonDetailActivity(Activity activity, String personId,
			String personName) {
		Intent intent = new Intent(activity, PersonDetailActivity.class);
		intent.putExtra(AppConstants.EXTRA_MESSAGE, "openPersonDetailActivity");
		intent.putExtra(AppConstants.PERSON_ID, personId);
		intent.putExtra(AppConstants.PERSON_NAME, personName);
		activity.startActivity(intent);
	}

	// Map<String, Object> map = new HashMap<String, Object>();
	// map.put("title", "G1");
	// map.put("info", "google 1");
	// map.put("img", R.drawable.abc_ab_bottom_solid_dark_holo);
	// list.add(map);
	//
	// map = new HashMap<String, Object>();
	// map.put("title", "G2");
	// map.put("info", "google 2");
	// map.put("img", R.drawable.abc_ab_bottom_solid_light_holo);
	// list.add(map);
	//
	// map = new HashMap<String, Object>();
	// map.put("title", "G3");
	// map.put("info", "google 3");
	// map.put("img", R.drawable.abc_ab_bottom_transparent_dark_holo);
	// list.add(map);
	//
	// return list;
	// }

	// ListView 中某项被选中后的逻辑
	// @Override
	// protected void onListItemClick(ListView l, View v, int position, long id)
	// {
	//
	// Log.v("MyListView4-click", (String)mData.get(position).get("title"));
	// }

	// /**
	// * listview中点击按键弹出对话框
	// */
	// public void showInfo() {
	// new AlertDialog.Builder(this).setTitle("我的listview").setMessage("介绍...")
	// .setPositiveButton("确定", new DialogInterface.OnClickListener() {
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	// }
	// }).show();
	//
	// }
	//
	// public final class ViewHolder {
	// public ImageView img;
	// public TextView name;
	// // public Button addBtn;
	// }
	//
	// public class PersonAdapter extends BaseAdapter {
	//
	// private LayoutInflater mInflater;
	//
	// public PersonAdapter(Context context) {
	// this.mInflater = LayoutInflater.from(context);
	// }
	//
	// @Override
	// public int getCount() {
	// return persons.size();
	// }
	//
	// @Override
	// public Object getItem(int i) {
	// return persons.get(i);
	// }
	//
	// @Override
	// public long getItemId(int arg0) {
	// return 0;
	// }
	//
	// @Override
	// public View getView(int position, View convertView, ViewGroup parent) {
	//
	// ViewHolder holder = null;
	// if (convertView == null) {
	//
	// holder = new ViewHolder();
	//
	// convertView = mInflater.inflate(R.layout.person_list_view, null);
	// //holder.img = (ImageView) convertView.findViewById(R.id.face_thumb);
	// holder.name = (TextView) convertView.findViewById(R.id.person_name);
	// // holder.addBtn =
	// // (Button)convertView.findViewById(R.id.add_button);
	// convertView.setTag(holder);
	//
	// } else {
	//
	// holder = (ViewHolder) convertView.getTag();
	// }
	//
	// List<String> faces = (ArrayList<String>)
	// persons.get(position).get("faces");
	//
	// //
	// holder.img.setBackgroundResource((Integer)mData.get(position).get("img"));
	// holder.name.setText((String) persons.get(position).get("name"));
	//
	// // holder.addBtn.setOnClickListener(new View.OnClickListener() {
	// //
	// // @Override
	// // public void onClick(View v) {
	// // showInfo();
	// // }
	// // });
	//
	// return convertView;
	// }
	//
	// }

}
