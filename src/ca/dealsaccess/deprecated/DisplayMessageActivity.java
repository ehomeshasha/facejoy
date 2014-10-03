package ca.dealsaccess.deprecated;

import ca.dealsaccess.facejoy.common.AppConstants;


import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class DisplayMessageActivity extends ActionBarActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);

	    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	    
	    Intent intent = getIntent();
	    String title = intent.getStringExtra(AppConstants.EXTRA_MESSAGE);
	    Toast.makeText(this, "Selected Item: " + title, Toast.LENGTH_SHORT).show();
	    
	}

}
