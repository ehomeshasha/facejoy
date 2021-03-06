package ca.dealsaccess.facejoy;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import ca.dealsaccess.deprecated.DisplayMessageActivity;
import ca.dealsaccess.facejoy.common.AppConstants;
import ca.dealsaccess.util.DialogUtils;
import ca.dealsaccess.util.FacecppUtils;
import ca.dealsaccess.util.StringUtils;

import com.example.facejoy.R;
import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;

import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;

public class FaceMainActivity extends ActionBarActivity {

	private static final String TAG = "FaceMainActivity";

	static final int PICTURE_CHOOSE = 1;
	static final int TAKE_PICTURE = 2;
	static final int CREATE_PERSON = 3;
	static final int ADD_PERSON = 4;
	static final int PERSON_LIST = 5;

	private ImageView imageView = null;
	private Bitmap img = null;
	private Button detectButton = null;
	private TextView textView = null;
	private TextView resultText;
	private View detectResultView = null;
	private int screenWidth;
	private int screenHeight;
	private String fileSrc = null;
	private ProgressDialog processDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.face_main_activity);

		processDialog = new ProgressDialog(this);
		imageView = (ImageView) this.findViewById(R.id.photo_imageView);
		imageView.setVisibility(View.INVISIBLE);
		detectButton = (Button) this.findViewById(R.id.button_detect);
		detectButton.setVisibility(View.INVISIBLE);
		textView = (TextView) this.findViewById(R.id.textView);

		// 获取屏幕大小
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;
		
		// 设定dectectButton的点击事件
		detectButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				DialogUtils.startLoadingAnimation(processDialog, "人脸检测中，请稍侯...");
				FaceppDetect faceppDetect = new FaceppDetect();
				// 设置人脸检测完成后的回调事件
				faceppDetect.setDetectCallback(new DetectCallback() {
					StringBuilder sb = new StringBuilder();
					List<String> faceList = new ArrayList<String>();
					List<String> faceIdList = new ArrayList<String>();
					List<String> facecheckedList = new ArrayList<String>();

					public void detectResult(JSONObject rst) {
						DialogUtils.finishLoadingAnimation(processDialog);
						
						Paint paint = new Paint();
						paint.setColor(Color.RED);
						paint.setStrokeWidth(Math.max(img.getWidth(), img.getHeight()) / 100f);

						// create a new canvas
						Bitmap bitmap = Bitmap.createBitmap(img.getWidth(), img.getHeight(), img.getConfig());
						Canvas canvas = new Canvas(bitmap);
						canvas.drawBitmap(img, new Matrix(), null);

						try {
							// find out all faces
							final int count = rst.getJSONArray("face").length();
							// sb.append("Finished, "+ count + " faces.\n");

							for (int i = 0; i < count; ++i) {
								float x, y, w, h;
								// get the center point
								x = (float) rst.getJSONArray("face").getJSONObject(i)
										.getJSONObject("position").getJSONObject("center").getDouble("x");
								y = (float) rst.getJSONArray("face").getJSONObject(i)
										.getJSONObject("position").getJSONObject("center").getDouble("y");

								// get face size
								w = (float) rst.getJSONArray("face").getJSONObject(i)
										.getJSONObject("position").getDouble("width");
								h = (float) rst.getJSONArray("face").getJSONObject(i)
										.getJSONObject("position").getDouble("height");

								// change percent value to the real size
								x = x / 100 * img.getWidth();
								w = w / 100 * img.getWidth() * 0.7f;
								y = y / 100 * img.getHeight();
								h = h / 100 * img.getHeight() * 0.7f;

								// draw the box to mark it out
								canvas.drawLine(x - w, y - h, x - w, y + h, paint);
								canvas.drawLine(x - w, y - h, x + w, y - h, paint);
								canvas.drawLine(x + w, y + h, x - w, y + h, paint);
								canvas.drawLine(x + w, y + h, x + w, y - h, paint);
								JSONObject attribute = rst.getJSONArray("face").getJSONObject(i)
										.getJSONObject("attribute");
								// sb.append("FaceID: ").append(rst.getJSONArray("face").getJSONObject(i).getString("face_id"))
								String faceId = rst.getJSONArray("face").getJSONObject(i).getString("face_id");
								String shortFaceId = faceId.substring(0,6);
								faceIdList.add(faceId);
								sb.append("faceId: " + shortFaceId).append("\n").append("race: ")
										.append(attribute.getJSONObject("race").getString("value"))
										.append(", confidence: ")
										.append(attribute.getJSONObject("race").getDouble("confidence"))
										.append("\n").append("gender: ")
										.append(attribute.getJSONObject("gender").getString("value"))
										.append(", confidence: ")
										.append(attribute.getJSONObject("gender").getDouble("confidence"))
										.append("\n").append("smiling: ")
										.append(attribute.getJSONObject("smiling").getDouble("value"))
										.append("\n").append("age: ")
										.append(attribute.getJSONObject("age").getInt("value"))
										.append(", range: ")
										.append(attribute.getJSONObject("age").getInt("range"))
										.append("\n\n");
								faceList.add("faceId: "+shortFaceId);
							}

							// save new image
							img = bitmap;
							//添加脸谱信息
							resultText.setText(sb.toString());

							FaceMainActivity.this.runOnUiThread(new Runnable() {
								public void run() {
									imageView.setImageBitmap(img);
									
									//添加Dialog框体
									Dialog dialog;
									AlertDialog.Builder builder = new AlertDialog.Builder(
											FaceMainActivity.this);
									builder.setTitle("Finished, " + count + " faces.\n")
											.setView(detectResultView)
											.setMultiChoiceItems(faceList.toArray(new String[0]), null,
													new DialogInterface.OnMultiChoiceClickListener() {
														public void onClick(DialogInterface dialog,
																int whichButton, boolean isChecked) {
															if (isChecked) {
																facecheckedList.add(faceIdList
																		.get(whichButton));
															} else {
																facecheckedList.remove(faceIdList
																		.get(whichButton));
															}
														}
													})
											//添加创建人物Activity
											.setPositiveButton("创建人物", new DialogInterface.OnClickListener() {
												public void onClick(DialogInterface dialog, int which) {
													//如果没有选择face,那么进行提示用户进行选择
													if(facecheckedList.size() == 0) {
														DialogUtils.DialogClosePermission(dialog, false);
														Toast.makeText(FaceMainActivity.this, "请先选择人脸",	Toast.LENGTH_SHORT).show();
													} else {
														String checkedListStr = StringUtils
																.toStringList(facecheckedList);
														Intent intent = new Intent(FaceMainActivity.this,
																CreatePersonActivity.class);
														intent.putExtra(AppConstants.FACE_CHECKED_LIST,
																checkedListStr);
														startActivityForResult(intent, CREATE_PERSON);
													}
												}
											})
											//添加加入已有人物Activity
											.setNegativeButton("加入到已有人物",
													new DialogInterface.OnClickListener() {
														public void onClick(DialogInterface dialog, int which) {
															//如果没有选择face,那么进行提示用户进行选择
															if(facecheckedList.size() == 0) {
																DialogUtils.DialogClosePermission(dialog, false);
																Toast.makeText(FaceMainActivity.this, "请先选择人脸",	Toast.LENGTH_SHORT).show();
															} else {
																String checkedListStr = StringUtils
																		.toStringList(facecheckedList);
																Intent intent = new Intent(FaceMainActivity.this,
																		PersonListActivity.class);
																intent.putExtra(AppConstants.FACE_CHECKED_LIST,
																		checkedListStr);
																startActivityForResult(intent, ADD_PERSON);
															}
														}
													})
											.setOnCancelListener(new DialogInterface.OnCancelListener() {
												public void onCancel(DialogInterface dialog) {
													DialogUtils.DialogClosePermission(dialog, true);
													dialog.dismiss();
												}
											});
									dialog = builder.create();
									dialog.show();
								}
							});

						} catch (final JSONException e) {
							e.printStackTrace();
							FaceMainActivity.this.runOnUiThread(new Runnable() {
								public void run() {
									Toast.makeText(FaceMainActivity.this, e.getMessage(),
											Toast.LENGTH_SHORT).show();
								}
							});
						}

					}
				});

				detectResultView = LayoutInflater.from(FaceMainActivity.this).inflate(
						R.layout.detect_result_dialog, null);
				resultText = (TextView) detectResultView.findViewById(R.id.detect_result_text);
				//调用人脸检测接口
				faceppDetect.detect(img);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.face_main_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.album:
			openAlbum();
			return true;
		case R.id.camera:
			openCamera();
			return true;
		case R.id.person:
			openPerson();
			return true;
		case R.id.settings:
			openSettings();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	//打开人物列表
	private void openPerson() {
		Intent intent = new Intent(this, PersonListActivity.class);
		startActivityForResult(intent, PERSON_LIST);
		
	}
	//打开摄像头
	private void openCamera() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(intent, TAKE_PICTURE);
	}
	//打开相册
	private void openAlbum() {
		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
		photoPickerIntent.setType("image/*");
		startActivityForResult(photoPickerIntent, PICTURE_CHOOSE);
	}
	//活动结束回调函数
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		Toast.makeText(FaceMainActivity.this, "requestCode is "+requestCode, Toast.LENGTH_SHORT)
		.show();
		if (requestCode == PICTURE_CHOOSE || requestCode == TAKE_PICTURE) {
			if (intent != null) {
				
				try {
					Cursor cursor = getContentResolver().query(intent.getData(), null, null, null, null);
					cursor.moveToFirst();
					int idx = cursor.getColumnIndex(ImageColumns.DATA);
					fileSrc = cursor.getString(idx);
						Options options = new Options();
					options.inJustDecodeBounds = true;
					img = BitmapFactory.decodeFile(fileSrc, options);
	
					// scale size to read
					options.inSampleSize = Math.max(1, (int) Math.ceil(Math.max((double) options.outWidth
							/ screenWidth, (double) options.outHeight / screenHeight)));
					options.inJustDecodeBounds = false;
					img = BitmapFactory.decodeFile(fileSrc, options);
					// textView.setText("Clik Detect. ==>");
				} catch (Exception e) {
					if(requestCode == TAKE_PICTURE) {
						if(resultCode == RESULT_OK) {
							  Bundle extras = intent.getExtras();    
				              img = (Bitmap) extras.get("data");
						}
					}
				}
				imageView.setImageBitmap(img);
				imageView.setVisibility(View.VISIBLE);
				detectButton.setVisibility(View.VISIBLE);
				textView.setVisibility(View.INVISIBLE);
			} else {
				Log.d(TAG, "idButSelPic Photopicker canceled");
			}
		} else if (requestCode == CREATE_PERSON) {
		} else if (requestCode == ADD_PERSON) {
		}
	}
	//打开设置页面
	public void openSettings() {
		Intent intent = new Intent(this, DisplayMessageActivity.class);
		startActivity(intent);
	}

	private class FaceppDetect {
		DetectCallback callback = null;

		public void setDetectCallback(DetectCallback detectCallback) {
			callback = detectCallback;
		}
		//检测人脸程序
		public void detect(final Bitmap image) {

			new Thread(new Runnable() {

				public void run() {
					HttpRequests httpRequests = FacecppUtils.getRequests();

					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					float scale = Math.min(1, Math.min(600f / img.getWidth(), 600f / img.getHeight()));
					Matrix matrix = new Matrix();
					matrix.postScale(scale, scale);

					Bitmap imgSmall = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix,
							false);

					imgSmall.compress(Bitmap.CompressFormat.JPEG, 100, stream);
					byte[] array = stream.toByteArray();

					try {
						// detect
						JSONObject result = httpRequests.detectionDetect(new PostParameters().setImg(array).setTag(fileSrc));
						// finished , then call the callback function
						if (callback != null) {
							callback.detectResult(result);
						}
					} catch (final FaceppParseException e) {
						FaceMainActivity.this.runOnUiThread(new Runnable() {
							public void run() {
								Toast.makeText(FaceMainActivity.this, e.getMessage(), Toast.LENGTH_SHORT)
										.show();
								DialogUtils.finishLoadingAnimation(processDialog);
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
