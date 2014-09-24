package ca.dealsaccess.facejoy;

import ca.dealsaccess.facejoy.common.AppConstants;
import ca.dealsaccess.util.DeviceUtils;

import com.example.facejoy.R;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class AddPersonActivity extends ActionBarActivity {

	//private TextView titleView;
	private EditText faceid_input;
	
	private Button createPersonBtn;
	
	private String deviceId;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.add_person);

	    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	    
	    // If your minSdkVersion is 11 or higher, instead use:
	    // getActionBar().setDisplayHomeAsUpEnabled(true);
	    
	    
	    Intent intent = getIntent();
	    String title = intent.getStringExtra(AppConstants.EXTRA_MESSAGE);
	    String checkedListStr = intent.getStringExtra(AppConstants.FACE_CHECKED_LIST);
	    
	    Toast.makeText(this, "Selected Item: " + title+", checkedListStr="+checkedListStr, Toast.LENGTH_SHORT).show();
	    faceid_input = (EditText) this.findViewById(R.id.faceid_input);
	    faceid_input.setText(checkedListStr);
	    
	    deviceId = DeviceUtils.getDeviceId(this);
	    
	    createPersonBtn = (Button) this.findViewById(R.id.add_person_btn);
	    createPersonBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				
				
				
				
				
				
				
				
			}
				
		});
	    
	    
	    
	    
	    
	    
	    //getSupportActionBar().setTitle("xxxx");
	    //titleView = (TextView) this.findViewById(R.id.add_person_title);
	    
	    //titleView.setText("正在创建人物,请稍侯...");
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	}
	
	
	

}
