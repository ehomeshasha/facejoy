package ca.dealsaccess.facejoy;

import org.json.JSONException;
import org.json.JSONObject;

import ca.dealsaccess.facejoy.common.AppConstants;
import ca.dealsaccess.util.DeviceUtils;

import com.example.facejoy.R;
import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreatePersonActivity extends ActionBarActivity {

	// private TextView titleView;
	private EditText faceid_input;
	private Button createPersonBtn;
	private String[] facecheckedList;
	private String groupName;
	private Intent intent;
	private HttpRequests httpRequests;
	private JSONObject rst;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_person);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		// If your minSdkVersion is 11 or higher, instead use:
		// getActionBar().setDisplayHomeAsUpEnabled(true);

		intent = getIntent();
		String title = intent.getStringExtra(AppConstants.EXTRA_MESSAGE);
		String checkedListStr = intent.getStringExtra(AppConstants.FACE_CHECKED_LIST);
		facecheckedList = checkedListStr.split(",");

		Toast.makeText(this, "Selected Item: " + title + ", checkedListStr=" + checkedListStr,
				Toast.LENGTH_SHORT).show();
		faceid_input = (EditText) this.findViewById(R.id.faceid_input);
		faceid_input.setText(checkedListStr);

		createPersonBtn = (Button) this.findViewById(R.id.create_person_btn);
		createPersonBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				final String personName = ((EditText) CreatePersonActivity.this.findViewById(R.id.username_input))
						.getText().toString();
				httpRequests = new HttpRequests("3807aeb4a0b911495fdf0c946d006251",
						"DQOkhWeHweMMItMjaIGwqG0Nns8JNj1E", true, false);
				groupName = DeviceUtils.getDeviceId(CreatePersonActivity.this);
				
				
				new Thread(new Runnable() {
					public void run() {
						//首先以设备ID为名称创建组
						try {
							httpRequests.groupCreate(new PostParameters().setGroupName(groupName));
						} catch (final FaceppParseException e) {
						}
						//创建人物并添加face到该人物
						try {
							rst = httpRequests.personCreate(new PostParameters().setPersonName(personName)
									.setFaceId(facecheckedList).setGroupName(groupName));
							final String personId = rst.getString("person_id");
							CreatePersonActivity.this.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									Toast.makeText(CreatePersonActivity.this, "您已成功创建人物", Toast.LENGTH_SHORT).show();
									//CreatePersonActivity.this.setResult(FaceMainActivity.CREATE_PERSON, intent);
									//CreatePersonActivity.this.finish();
									//打开人物详情页
									
									PersonListActivity.openPersonDetailActivity(CreatePersonActivity.this, personId, personName);
								}
							
							});
							
						} catch (final FaceppParseException e) {
							CreatePersonActivity.this.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									Toast.makeText(CreatePersonActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
								}
							});
							
							return;
						} catch (final JSONException e) {
							CreatePersonActivity.this.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									Toast.makeText(CreatePersonActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
								}
							});
						}
						CreatePersonActivity.this.runOnUiThread(new Runnable() {
							public void run() {
								
							}
						});
					}
				}).start();
			}
		});

	}
}
