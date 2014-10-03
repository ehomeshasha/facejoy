package ca.dealsaccess.facejoy;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ca.dealsaccess.facejoy.common.AppConstants;
import ca.dealsaccess.util.CollectionUtils;
import ca.dealsaccess.util.DeviceUtils;
import ca.dealsaccess.util.DialogUtils;

import com.example.facejoy.R;
import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
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
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class PersonListActivity extends ActionBarActivity {

	private ListView listView = null;
	private HttpRequests httpRequests;
	private String groupName;
	private JSONObject rst;
	private JSONArray personList;
	private List<String> personListItem = new ArrayList<String>();
	private String checkedListStr = null;
	private String[] facecheckedList;
	private ArrayList<Integer> positionDel = new ArrayList<Integer>();
	private ArrayList<String> personIdDel = new ArrayList<String>();
	private MenuItem gobackItem;
	private MenuItem deleteItem;
	private ProgressDialog processDialog;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		processDialog = new ProgressDialog(this);
		
		Intent intent = getIntent();
		String title = intent.getStringExtra(AppConstants.EXTRA_MESSAGE);
		checkedListStr = intent.getStringExtra(AppConstants.FACE_CHECKED_LIST);
		Toast.makeText(this, "Selected Item: " + title + ", checkedListStr=" + checkedListStr,
				Toast.LENGTH_SHORT).show();
		if (checkedListStr != null) {
			facecheckedList = checkedListStr.split(",");
		}

		initData();
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
			personDelete(item);
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
		initListView();
	}

	private void personDelete(MenuItem item) {
		if(item.getTitle().equals("删除")) {
			if(personListItem.size() == 0) {
				return;
			}
			item.setTitle("删除选中的人物");
			gobackItem.setVisible(true);
			initListViewForDelete();
		} else if(item.getTitle().equals("删除选中的人物")) {
			if(personIdDel.size() == 0) {
				Toast.makeText(PersonListActivity.this, "请先选择要删除的人物", Toast.LENGTH_SHORT).show();
				return;
			}
			AlertDialog.Builder builder = new AlertDialog.Builder(
					PersonListActivity.this);
			
			builder.setMessage("确认删除选中的人物？")
				.setTitle("提示")
				.setPositiveButton("确认", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(final DialogInterface dialog, int which) {
						if(personIdDel.size() == 0) return;
						DialogUtils.startLoadingAnimation(processDialog, "删除中，请稍侯...");
						new Thread(new Runnable() {
							public void run() {
								try {
									JSONObject rst = httpRequests.personDelete(new PostParameters().setPersonId(personIdDel));
									final boolean success = rst.getBoolean("success");
									PersonListActivity.this.runOnUiThread(new Runnable() {
										@Override
										public void run() {
											if(success) {
												Toast.makeText(PersonListActivity.this, "您已成功删除人物", Toast.LENGTH_SHORT).show();
												CollectionUtils.removeElementByPosition(personListItem, positionDel);
											} else {
												Toast.makeText(PersonListActivity.this, "删除人物失败", Toast.LENGTH_SHORT).show();
											}
											DialogUtils.finishLoadingAnimation(processDialog);
											goBack();
										}
									});
								} catch (final FaceppParseException e) {
									PersonListActivity.this.runOnUiThread(new Runnable() {
										@Override
										public void run() {
											Toast.makeText(PersonListActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
											DialogUtils.finishLoadingAnimation(processDialog);
										}
									});
								} catch (final JSONException e) {
									PersonListActivity.this.runOnUiThread(new Runnable() {
										@Override
										public void run() {
											Toast.makeText(PersonListActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
											DialogUtils.finishLoadingAnimation(processDialog);
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



	private void initData() {

		httpRequests = new HttpRequests("3807aeb4a0b911495fdf0c946d006251",
				"DQOkhWeHweMMItMjaIGwqG0Nns8JNj1E", true, false);
		groupName = DeviceUtils.getDeviceId(PersonListActivity.this);

		new Thread(new Runnable() {
			public void run() {
				try {
					rst = httpRequests.groupGetInfo(new PostParameters().setGroupName(groupName));
					try {
						personList = rst.getJSONArray("person");
					} catch (JSONException e) {
						return;
					}
					for (int i = 0; i < personList.length(); i++) {
						personListItem.add(personList.getJSONObject(i).getString("person_name"));
					}

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
						initListView();
					}
				});
			}
		}).start();
	}

	public static void openPersonDetailActivity(Activity activity, String personId,
			String personName) {
		Intent intent = new Intent(activity, FaceListActivity.class);
		intent.putExtra(AppConstants.EXTRA_MESSAGE, "openPersonDetailActivity");
		intent.putExtra(AppConstants.PERSON_ID, personId);
		intent.putExtra(AppConstants.PERSON_NAME, personName);
		activity.startActivity(intent);
	}
	
	public void initListView() {
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
					builder.setMessage("确认添加人脸到该人物？")
						.setTitle("提示")
						.setPositiveButton("确认", new DialogInterface.OnClickListener() {
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
													PersonListActivity.this.getIntent().removeExtra(AppConstants.FACE_CHECKED_LIST);
													checkedListStr = null;
												}
											});
										} catch (final FaceppParseException e) {
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
										} catch (final JSONException e) {
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
						})
						.setNegativeButton("取消", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {

							}
						})
						.setOnCancelListener(new DialogInterface.OnCancelListener() {
							@Override
							public void onCancel(DialogInterface dialog) {
								try {
									Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
									field.setAccessible(true);
									field.set(dialog, true);//允许对话框关闭
								} catch (Exception e) {e.printStackTrace();}
								dialog.dismiss();
							}
						});
					builder.create().show();

				} else {
					//打开人物的详情页
					openPersonDetailActivity(PersonListActivity.this, personId, personListItem.get(position));
				}

			}

		});
	}
	
	private void initListViewForDelete() {
		listView = new ListView(PersonListActivity.this);
		listView.setAdapter(new ArrayAdapter<String>(PersonListActivity.this,
				android.R.layout.simple_list_item_multiple_choice, personListItem));
		setContentView(listView);
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				CheckedTextView v = (CheckedTextView) view;
				try {
					if(v.isChecked()) {
						positionDel.add(Integer.valueOf(position));
						personIdDel.add(personList.getJSONObject(position).getString("person_id"));
					} else {
						positionDel.remove(Integer.valueOf(position));
						personIdDel.remove(personList.getJSONObject(position).getString("person_id"));
					}
				} catch (JSONException e) {
					Toast.makeText(PersonListActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
				}
				
			}
			
		});
	}
}
