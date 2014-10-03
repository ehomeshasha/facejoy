package ca.dealsaccess.deprecated;

import ca.dealsaccess.facejoy.common.AppConstants;


import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class UseExistPersonActivity extends ActionBarActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);

	    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

	    Intent intent = getIntent();
	    String title = intent.getStringExtra(AppConstants.EXTRA_MESSAGE);
	    String checkedListStr = intent.getStringExtra(AppConstants.FACE_CHECKED_LIST);
	    Toast.makeText(this, "Selected Item: " + title+", checkedListStr="+checkedListStr, Toast.LENGTH_SHORT).show();
	    
	}

}
